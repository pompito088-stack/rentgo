# Guia backend Java de RentGo, explicada como profesor de DAM

> Esta guia complementa `docs/explicacion-codigo-dam.md`, pero aqui me centro solo en el **backend Java**: arranque, MVC, servicios, repositorios, entidades y flujo de negocio.  
> La idea es que puedas estudiar el proyecto **como si tuvieras que defender cada clase en la pizarra**.

---

## Indice

1. [Objetivo de esta guia](#objetivo-de-esta-guia)
2. [Mapa mental del backend](#mapa-mental-del-backend)
3. [Arranque y configuracion base](#arranque-y-configuracion-base)
4. [Configuracion web y variables globales](#configuracion-web-y-variables-globales)
5. [Autenticacion y sesion](#autenticacion-y-sesion)
6. [Home y cambio de vista por rol](#home-y-cambio-de-vista-por-rol)
7. [Perfil del usuario](#perfil-del-usuario)
8. [Modulo de vehiculos](#modulo-de-vehiculos)
9. [Modulo de reservas: flujo principal del negocio](#modulo-de-reservas-flujo-principal-del-negocio)
10. [Modulo de pagos y devoluciones](#modulo-de-pagos-y-devoluciones)
11. [Modulo de mantenimientos](#modulo-de-mantenimientos)
12. [CRUD admin repetitivos](#crud-admin-repetitivos)
13. [Servicios: donde vive la logica](#servicios-donde-vive-la-logica)
14. [Repositorios: acceso a datos](#repositorios-acceso-a-datos)
15. [Entidades JPA y relaciones](#entidades-jpa-y-relaciones)
16. [DTO de disponibilidad](#dto-de-disponibilidad)
17. [Flujos funcionales que debes saber explicar](#flujos-funcionales-que-debes-saber-explicar)
18. [Decisiones de diseno que puedes defender en clase](#decisiones-de-diseno-que-puedes-defender-en-clase)
19. [Preguntas tipicas de examen o exposicion](#preguntas-tipicas-de-examen-o-exposicion)

---

## Objetivo de esta guia

Si el archivo `docs/explicacion-codigo-dam.md` era el manual general, este documento es la **radiografia del backend**.

Aqui no me interesa tanto la apariencia visual, sino responder preguntas como estas:

- ¿Como arranca la aplicacion?
- ¿Como se sabe si un usuario es admin o cliente?
- ¿Quien valida una reserva?
- ¿Quien crea el pago?
- ¿Donde se comprueba que dos reservas no se solapen?
- ¿Por que las imagenes se sirven desde disco y desde el classpath?

En DAM esto importa mucho, porque el backend es donde se ve si realmente entiendes:

- arquitectura por capas
- logica de negocio
- persistencia con JPA
- validacion
- sesiones
- integridad funcional entre entidades

---

## Mapa mental del backend

Piensa el backend de `RentGo` como una cadena de 5 piezas:

1. **Cliente/navegador** hace una peticion HTTP.
2. **Controller** recibe esa peticion y decide que hacer.
3. **Service** aplica reglas de negocio.
4. **Repository** consulta o guarda en BD.
5. **Entity** representa la tabla con la que trabaja JPA.

### Ejemplo mental rapido
Un cliente reserva un coche:

- el formulario llama a `POST /reservas/guardar`
- entra en `ReservaController`
- ese controlador usa `ReservaService`, `VehiculoService`, `ExtraService`, `PagoService`, etc.
- el servicio y los repositorios comprueban solapamientos y guardan datos
- JPA persiste `Reserva` y luego `Pago`

Eso es exactamente el tipo de recorrido que un profesor de DAM quiere que sepas seguir sin perderte.

---

## Arranque y configuracion base

## `src/main/java/com/ilerna/rentgo/RentgoApplication.java`

### Que representa
Es el punto de entrada del proyecto.

### Lineas clave
```java
@SpringBootApplication
public class RentgoApplication {
    public static void main(String[] args) {
        SpringApplication.run(RentgoApplication.class, args);
    }
}
```

### Explicacion de profesor
- `@SpringBootApplication` le dice a Spring: “esta es la clase raiz, busca componentes, configura cosas automaticamente y arranca la app”.
- `SpringApplication.run(...)` levanta el contexto de Spring, el servidor embebido y todos los beans.

### Para que sirve
Sin esta clase, el proyecto no pasa de ser un conjunto de ficheros Java.

### Por que esta bien elegida
Porque es la forma estandar y limpia de arrancar una app Spring Boot.

---

## `pom.xml`

Aunque no es Java, afecta al backend de forma directa.

### Dependencias esenciales del backend
- `spring-boot-starter-webmvc`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-validation`
- `mysql-connector-j`

### Lo que aportan
- **Web MVC**: controladores, rutas, vistas y manejo de peticiones.
- **Data JPA**: ORM con Hibernate.
- **Validation**: `@NotBlank`, `@Email`, `@Pattern`, etc.
- **MySQL driver**: comunicacion real con la base de datos.

### Idea de profesor
Si te preguntan por que esas dependencias y no otras, la respuesta correcta es:

> porque el proyecto es una aplicacion web MVC con renderizado servidor, persistencia relacional y validacion declarativa de formularios y entidades.

---

## `src/main/resources/application.properties`

### Configuracion que define el comportamiento del backend

#### Conexion a BD
```properties
spring.datasource.url=...
spring.datasource.username=root
spring.datasource.password=
```

#### JPA / Hibernate
```properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

#### Subida de archivos
```properties
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB
app.upload.path=src/main/resources/static/img/vehiculos
app.upload.carnet.path=src/main/resources/static/img/carnets
```

### Lo importante que debes saber defender
- `ddl-auto=validate` significa que **Hibernate no crea ni modifica la BD**; solo comprueba que las entidades Java encajan con el esquema SQL existente.
- Esto tiene sentido porque el proyecto usa `docs/ini_db.sql` para crear tablas y `docs/data.sql` para poblarlas.
- Las rutas `app.upload.*` permiten que los controladores no tengan las carpetas escritas “a fuego” en el codigo.

---

## Configuracion web y variables globales

## `config/WebConfig.java`

### Funcion real
Configura como servir recursos de imagen desde:
- el disco
- el classpath del proyecto/JAR

### Metodo clave
`addResourceHandlers(ResourceHandlerRegistry registry)`

### Que hace exactamente
1. Lee `app.upload.path` y `app.upload.carnet.path`.
2. Convierte esas rutas a `Path` absolutos.
3. Genera URIs correctas con `toUri()`.
4. Crea dos handlers:
   - `/img/vehiculos/**`
   - `/img/carnets/**`
5. Les da dos localizaciones posibles:
   - `file:///...` del disco
   - `classpath:/static/...` del JAR

### Para que sirve
Esto permite dos escenarios a la vez:

- **imagenes seed** ya incluidas dentro del proyecto
- **imagenes subidas en runtime** por admin o cliente

### Por que esta solucion es buena
Porque resuelve un problema tipico real de despliegue:

- en local puede existir la carpeta fisica
- en Docker puede interesar servir desde el JAR
- en servidor Linux una ruta de disco puede no coincidir con Windows

El fallback al classpath fue una decision especialmente acertada para este proyecto.

---

## `config/GlobalModelAttributes.java`

### Que hace
Añade variables comunes a todas las vistas:
- `requestUri`
- `requestUrl`
- `baseUrl`

### Como lo consigue
Con `@ControllerAdvice` + varios metodos `@ModelAttribute`.

### Para que sirve
- SEO
- enlaces canonicos
- Open Graph
- logica visual en fragmentos compartidos

### Por que esta bien elegido
Porque evita repetir en cada controlador el mismo codigo para obtener la URL actual.

---

## Autenticacion y sesion

## `controller/AuthController.java`

Este archivo gobierna tres flujos:
- login
- registro
- logout

### Decision de diseno importante
No usa Spring Security. Usa `HttpSession` manual.

### Ventajas de esa eleccion
- mas simple de entender en DAM
- menos configuracion
- mas directo para aprender sesion, request, redirect y permisos

### Inconvenientes
- menos seguro a nivel profesional
- las contrasenas se comparan en claro
- no hay filtros centralizados de autorizacion

### Aun asi, para proyecto academico, es defendible
Porque hace visible el mecanismo de sesion de forma clara.

### Flujo de login
1. `GET /login` devuelve `auth/login`.
2. `POST /login` busca usuario por email.
3. Compara la password.
4. Si coincide, guarda `usuarioLogueado` en sesion.
5. Redirige a `/`.

### Flujo de registro
1. `GET /registro` crea un objeto `Usuario` vacio.
2. `POST /registro` valida Bean Validation.
3. Bloquea emails sospechosos tipo admin.
4. Comprueba duplicados de email, telefono y DNI.
5. Asigna el tipo `cliente` automaticamente.
6. Si hay foto de carnet, la guarda y rellena `rutaFotoCarnet`.
7. Guarda el usuario.
8. Redirige a login.

### Flujo de logout
Invalidar sesion y volver al inicio.

### Lo que yo subrayaria en clase
- La sesion se gestiona con un atributo llamado `usuarioLogueado`.
- El rol no lo decide el formulario; lo decide el servidor.
- El carnet se trata como subida opcional.
- Las validaciones de negocio se suman a Bean Validation.

---

## Home y cambio de vista por rol

## `controller/HomeController.java`

### Que resuelve
La misma ruta `/` sirve para dos experiencias:
- admin -> dashboard
- cliente/visitante -> landing

### Como lo hace
Lee `usuarioLogueado` de sesion y calcula `esAdmin`.

### Si el usuario es admin
Carga:
- total de usuarios
- total de clientes
- total de vehiculos
- total de reservas
- total de sucursales
- ultimas reservas

Ademas llama a `reservaService.actualizarEstadosAutomaticamente()` antes de mostrar datos.

### Por que esta decision es buena
Porque la home no es una pagina estatica. Es un **punto de entrada contextual** segun el rol.

### Que debes saber decir
> La ruta es la misma, pero la vista renderiza distinto segun el modelo. Eso permite una experiencia mas sencilla para el usuario y evita duplicar navegacion principal.

---

## Perfil del usuario

## `controller/PerfilController.java`

### Responsabilidad
Gestionar “Mi perfil” del usuario autenticado.

### Ideas de diseno importantes
- no se pasa un ID en la URL para editarte a ti mismo
- el usuario se toma de la sesion
- el admin no usa el mismo flujo de edicion que el cliente
- los campos editables estan limitados

### Flujo de mostrar perfil
- comprueba login
- anade `usuario` y `esAdmin`
- devuelve `usuarios/perfil`

### Flujo de editar perfil
- solo clientes
- recupera el usuario real de la BD
- valida telefono duplicado excluyendo al propio usuario
- actualiza solo:
  - nombre
  - apellidos
  - telefono
  - direccion
- si sube nuevo carnet, reemplaza imagen
- guarda y actualiza sesion

### Por que es una buena implementacion
Porque no confia ciegamente en el objeto del formulario.  
Usa un enfoque de “lista blanca” de campos editables, mucho mas seguro y limpio.

---

## Modulo de vehiculos

## `controller/VehiculoController.java`

Este controlador es muy completo y se puede usar como ejemplo modelo de CRUD en Spring MVC.

### Responsabilidades
- listar vehiculos
- mostrar detalle
- crear
- editar
- eliminar
- gestionar foto de vehiculo
- cargar datos auxiliares del formulario

### Idea de seguridad
- el listado y el detalle son publicos
- crear/editar/eliminar es solo admin

### Metodos auxiliares
- `noEstaLogueado(session)`
- `noEsAdmin(session)`

Esto evita repetir demasiado codigo y mejora legibilidad.

### Listado publico
Carga:
- vehiculos
- disponibilidades calculadas
- sucursales
- categorias
- bandera `esAdmin`

### Detalle publico
Carga el vehiculo por ID y calcula disponibilidad real.

### Formulario admin
Usa `cargarDatosFormulario(model)` para meter:
- modelos
- marcas
- categorias
- sucursales
- extras
- listas de etiquetas, transmisiones y combustiones

### Guardado
El metodo `guardar(...)` hace varias cosas:
- valida Bean Validation
- comprueba matricula duplicada
- gestiona subida de foto
- en edicion, mantiene foto antigua si no se sube nueva
- guarda el vehiculo

### Eliminacion
Antes de borrar la entidad, intenta borrar la foto del disco.

### Lo mejor que puedes defender de este modulo
- combina negocio y gestion de archivos sin mezclarlo todo en la vista
- mantiene la logica de permisos clara
- reutiliza datos auxiliares del formulario
- contempla casos reales de alta, edicion y sustitucion de imagen

---

## Modulo de reservas: flujo principal del negocio

## `controller/ReservaController.java`

Este es el controlador mas importante del sistema.

### Por que
Porque la reserva es el centro funcional de un rent-a-car.  
No es solo un CRUD: es una operacion con reglas, fechas, solapamientos, pago y cancelacion.

### Dependencias que usa
- `ReservaService`
- `VehiculoService`
- `SucursalService`
- `UsuarioService`
- `PagoService`
- `ExtraService`
- `MantenimientoService`
- `DevolucionService`

Esto ya te dice que es un modulo transversal.

### Listado de reservas
- obliga login
- actualiza estados automaticamente
- admin ve todas
- cliente ve solo las suyas

### Nueva reserva
- solo clientes
- puede venir con `vehiculoId` preseleccionado desde el detalle
- inicializa `fechaReserva` y `estado`

### Guardar reserva: despiece docente

#### 1. Validacion de formulario
Si Bean Validation falla, vuelve al formulario.

#### 2. Validaciones temporales
- fecha fin no antes que inicio
- parseo de horas
- si es el mismo dia, la hora fin debe ser posterior
- no se permite reservar en el pasado

#### 3. Validacion de solapamientos
- comprueba otras reservas del mismo vehiculo
- comprueba mantenimientos que choquen en fechas

#### 4. Resolver entidades desde BD
Transforma IDs del formulario en entidades reales:
- vehiculo
- sucursal de devolucion

#### 5. Asignar usuario y estado
Si es cliente:
- asigna el usuario logueado
- pone estado `confirmada`

#### 6. Calcular precio
- dias de alquiler
- precio del vehiculo por dia
- extras por dia
- total

#### 7. Guardar extras
Convierte `extrasIds` en `Set<Extra>`.

#### 8. Guardar reserva
Llama a `reservaService.guardar(...)`.

#### 9. Actualizar sucursal del vehiculo
Mueve el vehiculo a la sucursal de devolucion.

#### 10. Crear pago automatico
Si la reserva es nueva y la hace un cliente:
- crea un `Pago`
- copia importe
- copia fianza del vehiculo
- pone metodo de pago
- estado `realizado`

### Cancelacion de reservas
Hay dos pasos:

#### `GET /reservas/cancelar/{id}`
- comprueba que la reserva pertenece al usuario
- impide cancelar si ya empezo
- calcula politica de reembolso
- calcula devolucion del alquiler y de la fianza
- muestra resumen

#### `POST /reservas/cancelar/{id}`
- vuelve a comprobar permisos y estado
- marca reserva como `cancelada`
- recalcula el importe a devolver
- crea `Devolucion` si no existe
- deja el pago como `reembolsado`

### Que debes destacar si te preguntan
- aqui hay logica de negocio real, no solo CRUD
- se usan varias entidades coherentemente
- se respetan reglas temporales y de integridad
- la cancelacion genera trazabilidad economica

---

## Modulo de pagos y devoluciones

## `controller/PagoController.java`

### Doble perspectiva
- `mis-pagos` para cliente
- CRUD/lista para admin

### Idea de diseno
El cliente no administra pagos arbitrarios; solo consulta los suyos.  
El admin si puede ver y editar gestion administrativa.

### Detalles importantes
- un pago nuevo se fuerza a `realizado`
- el formulario de pago carga reservas y metodos permitidos
- la vista del cliente filtra por su usuario

---

## `controller/DevolucionController.java`

### Que representa este modulo
La formalizacion de un reembolso.

### Decision fuerte del modelo
No basta con poner `estadoPago = reembolsado`.  
Se crea una entidad `Devolucion` con:
- fecha
- importe
- motivo
- enlace al pago

### Por que esa decision es buena
Porque aporta **trazabilidad**.  
En una defensa, decir “separo Pago y Devolucion para auditar reembolsos” suma mucho.

### Flujo funcional
- lista devoluciones para admin
- formulario puede venir con `pagoId` preseleccionado
- solo pagos `realizado` sin devolucion son reembolsables
- al guardar, se resuelve el `Pago` real desde BD
- al eliminar, se revierte el pago a `realizado`

---

## Modulo de mantenimientos

## `controller/MantenimientoController.java`

### Funcion
Gestionar indisponibilidades tecnicas.

### Reglas de negocio importantes
- fecha fin >= fecha inicio
- si es mismo dia, hora fin > hora inicio
- no puede solaparse con reservas existentes del vehiculo

### Por que este modulo es mas importante de lo que parece
Porque un mantenimiento es una causa real de indisponibilidad del coche.  
A nivel de negocio compite con las reservas por el mismo recurso: el vehiculo.

---

## CRUD admin repetitivos

Hay varios controladores con patron muy parecido:
- `CategoriaVehiculoController`
- `ExtraController`
- `MarcaController`
- `ModeloController`
- `SucursalController`
- `TipoUsuarioController`

### Estructura comun
1. validacion de admin
2. listar
3. nuevo
4. editar
5. guardar
6. eliminar

### Por que este patron es correcto
- coherencia arquitectonica
- mas facil de mantener
- todas las tablas maestras se gestionan igual
- ideal para un proyecto de DAM con CRUD administrativos

### Excepcion interesante
`UsuarioController` esta intencionadamente capado para no permitir CRUD completo de usuarios desde admin.

---

## Servicios: donde vive la logica

## `service/UsuarioService.java`

Servicio sencillo, con funciones tipicas:
- listar
- buscar por id
- buscar por email
- buscar por telefono
- buscar por dni
- guardar
- eliminar
- contar clientes

### Por que sigue siendo util aunque sea simple
Porque mantiene a los controladores separados del acceso directo a datos.

---

## `service/VehiculoService.java`

### Lo importante
Su metodo `calcularDisponibilidad(Integer vehiculoId)` es logica de negocio real.

### Como decide disponibilidad
- mira mantenimientos activos ahora mismo
- mira reservas activas ahora mismo
- decide si el coche esta disponible, alquilado o en mantenimiento
- si no esta libre, calcula cuando vuelve a estarlo

### Por que esto es mejor que confiar en un campo estatico
Porque la disponibilidad real depende del tiempo y de eventos.  
No se puede fiar todo a un string persistido en la tabla.

---

## `service/ReservaService.java`

### Lo destacable
- lista reservas
- busca por id
- lista por usuario
- detecta solapamientos
- devuelve ultimas reservas
- actualiza estados automaticamente

### Metodo estrella
`actualizarEstadosAutomaticamente()`

#### Regla 1
Si ya paso fecha/hora de fin -> `finalizada`

#### Regla 2
Si ya llego fecha/hora de inicio -> `en_proceso`

### Ventaja de esta solucion
No hace falta programar un cron para un proyecto academico.  
Cada vez que alguien consulta reservas, el sistema se pone al dia.

### Inconveniente
La actualizacion no es “en background”; depende de acceder a la funcionalidad.

Aun asi, para DAM es una solucion elegante y suficiente.

---

## Servicios restantes

### `PagoService`
Gestiona pagos y filtros por usuario/reserva.

### `DevolucionService`
Gestiona devoluciones y sincronizacion del estado del pago.

### `MantenimientoService`
Gestiona mantenimientos y, segun el patron del proyecto, probablemente validaciones temporales y consultas por vehiculo.

### `CategoriaVehiculoService`, `ExtraService`, `SucursalService`, `MarcaService`, `ModeloService`, `TipoUsuarioService`
Son servicios mas CRUD, pero siguen siendo utiles por coherencia de capas.

---

## Repositorios: acceso a datos

## Idea general
Todos heredan de `JpaRepository`, por lo que Spring Data genera automaticamente gran parte del comportamiento.

### Ejemplo: `UsuarioRepository`
Metodos como:
- `findByEmail`
- `findByTelefono`
- `findByDni`
- `countByTipoUsuarioNombre`

### Ejemplo: `ReservaRepository`
Metodos como:
- `findByUsuarioId`
- `findAllByOrderByFechaReservaDesc(Pageable pageable)`
- `findReservasSolapadas(...)` con `@Query`

### Lo que debes saber explicar
Spring Data JPA permite dos estilos:
1. consulta derivada por nombre de metodo
2. consulta explicita con `@Query`

### Cuándo se usa cada una
- si la consulta es sencilla -> nombre de metodo
- si es mas compleja, como el solapamiento -> `@Query`

---

## Entidades JPA y relaciones

## `Usuario`
Representa `usuarios`.

### Relaciones
`ManyToOne` con `TipoUsuario`.

### Validaciones destacables
- `@Email`
- `@Pattern` para telefono
- `@Pattern` para DNI
- regex avanzada en apellidos

### Metodo muy importante
`isAdmin()` encapsula la logica del rol.

---

## `Vehiculo`
Representa `vehiculos`.

### Relaciones
- `ManyToOne` con `CategoriaVehiculo`
- `ManyToOne` con `Modelo`
- `ManyToOne` con `Sucursal`
- `ManyToMany` con `Extra`

### Tipos adecuados
- `BigDecimal` para precios
- `Byte` para plazas y puertas

### Idea importante
Aunque existe el campo `disponibilidad`, la disponibilidad que el usuario ve puede calcularse en tiempo real usando `VehiculoService`.

---

## `Reserva`
Representa `reservas`.

### Relaciones
- `Usuario`
- `Vehiculo`
- `SucursalRecogida`
- `SucursalDevolucion`
- `Extra`

### Metodos propios de negocio
- `isEstaFinalizada()`
- `isYaIniciada()`

Esto es interesante porque la entidad no solo almacena datos: tambien expone comportamiento coherente con su dominio.

---

## `Pago`
Representa `pagos`.

### Relaciones
- `OneToOne` con `Reserva`
- `OneToOne` inversa con `Devolucion`

### Decision buena
No usar `orphanRemoval` ni cascadas peligrosas con `Devolucion`, para evitar borrados accidentales de reembolsos.

---

## `Devolucion`
Representa `devoluciones`.

### Que aporta al dominio
Trazabilidad economica y juridica basica del reembolso.

---

## Otras entidades sencillas
- `TipoUsuario`
- `CategoriaVehiculo`
- `Marca`
- `Modelo`
- `Extra`
- `Sucursal`
- `Mantenimiento`

Todas cumplen una funcion de tabla maestra o entidad operativa complementaria.

---

## DTO de disponibilidad

## `dto/DisponibilidadVehiculo.java`

### Que es
Un DTO, no una entidad.

### Que contiene
- `estado`
- `disponibleDesde`
- helpers para fecha y hora

### Por que se ha elegido DTO
Porque la disponibilidad calculada en tiempo real no pertenece a la persistencia directa del coche, sino a una **proyeccion de negocio para mostrar en pantalla**.

---

## Flujos funcionales que debes saber explicar

## Flujo 1: login
1. navegador -> `POST /login`
2. `AuthController.login(...)`
3. `UsuarioService.buscarPorEmail(...)`
4. `UsuarioRepository.findByEmail(...)`
5. si coincide password -> guardar `usuarioLogueado` en sesion
6. redirect a `/`

## Flujo 2: alta de cliente con carnet
1. formulario -> `POST /registro`
2. `AuthController.registrar(...)`
3. Bean Validation + duplicados
4. asigna `cliente`
5. guarda imagen si existe
6. `usuarioService.guardar(...)`
7. redirect a `/login`

## Flujo 3: crear reserva
1. cliente -> `GET /reservas/nuevo?vehiculoId=...`
2. formulario -> `POST /reservas/guardar`
3. `ReservaController` valida reglas temporales y solapamientos
4. resuelve entidades de BD
5. calcula total
6. guarda `Reserva`
7. crea `Pago`
8. redirect a `/reservas`

## Flujo 4: cancelar reserva
1. cliente -> `GET /reservas/cancelar/{id}`
2. se calcula devolucion potencial
3. cliente confirma con `POST`
4. reserva pasa a `cancelada`
5. se crea `Devolucion`
6. pago pasa a `reembolsado`

## Flujo 5: subir foto de vehiculo
1. admin envia formulario con `MultipartFile`
2. `VehiculoController.guardar(...)`
3. sanea nombre y crea carpeta si hace falta
4. guarda archivo en disco
5. persiste `rutaFoto` como URL web

---

## Decisiones de diseno que puedes defender en clase

### 1. Uso de MVC clasico
Muy apropiado para DAM porque separa responsabilidades con claridad.

### 2. Uso de servicios aunque algunos sean simples
Porque prepara el proyecto para crecer y mantiene el controlador limpio.

### 3. `HttpSession` en lugar de Spring Security
No es lo mas profesional, pero es muy pedagogico y suficiente para un proyecto academico.

### 4. SQL manual + `validate`
Da control del esquema y evita alteraciones automaticas peligrosas.

### 5. Reembolso como entidad separada
Mejora trazabilidad y modela mejor el negocio.

### 6. Disponibilidad calculada en servicio
Es mas fiel a la realidad del alquiler que confiar en un simple estado persistido.

### 7. Fallback de imagenes disco + classpath
Resuelve despliegue local, Docker y recursos seed con una unica configuracion.

---

## Preguntas tipicas de examen o exposicion

### ¿Por que `ReservaController` usa tantos servicios?
Porque la reserva es una operacion transversal: afecta a vehiculo, usuario, sucursales, extras, pagos y devoluciones.

### ¿Donde se impide reservar dos veces el mismo coche en el mismo rango?
En `ReservaController`, apoyandose en `ReservaService.existeSolapamiento(...)` y `ReservaRepository.findReservasSolapadas(...)`.

### ¿Quien decide si un usuario es admin?
El backend, usando `Usuario.isAdmin()` sobre el `TipoUsuario` asociado.

### ¿Quien crea el pago de una reserva?
`ReservaController` cuando la reserva es nueva y la hace un cliente.

### ¿Por que no guardas la ruta fisica completa de las imagenes en la BD?
Porque lo correcto es guardar una ruta web logica (`/img/...`) y resolver el recurso desde configuracion.

### ¿Por que `Devolucion` no es solo un boolean en `Pago`?
Porque se necesita registrar fecha, importe y motivo del reembolso.

### ¿Que papel tiene `@ControllerAdvice` en este proyecto?
Inyectar atributos globales utiles en todas las plantillas sin repetir codigo en controladores.

---

## Cierre de profesor

Si estudias este backend bien, no solo entenderas `RentGo`: estaras entendiendo un patron que se repite en muchisimos proyectos Java empresariales pequenos y medianos.

### Lo que debes quedarte
- el **controller** orquesta la peticion
- el **service** aplica negocio
- el **repository** consulta o persiste
- la **entity** modela la tabla y sus relaciones
- el **DTO** transporta datos calculados para la vista

Y, sobre todo:

> En este proyecto el backend no es decorativo. Es el sitio donde realmente se decide si la aplicacion se comporta como un rent-a-car de verdad.

