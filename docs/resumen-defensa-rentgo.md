# Resumen para exponer y defender RentGo en clase o TFG

> Documento pensado para hablar 3-5 minutos con seguridad, orden y lenguaje tecnico de DAM.

---

## 1. Presentacion rapida del proyecto

`RentGo` es una aplicacion web desarrollada en Java con Spring Boot para la gestion de alquiler de coches.

El sistema permite:
- registrar clientes
- iniciar sesion
- consultar vehiculos
- hacer reservas
- generar pagos
- cancelar reservas con devolucion
- gestionar mantenimientos
- administrar marcas, modelos, categorias, extras y sucursales

---

## 2. Objetivo del proyecto

El objetivo principal era crear una aplicacion completa de alquiler de vehiculos que no fuera solo un CRUD de tablas, sino un sistema con **logica de negocio real**.

Por ejemplo:
- evitar reservas solapadas del mismo vehiculo
- impedir reservar en fechas pasadas
- calcular disponibilidad real segun reservas y mantenimientos
- generar pagos automaticamente al reservar
- aplicar una politica de devolucion al cancelar
- servir imagenes tanto seed como subidas en runtime

---

## 3. Arquitectura elegida

He utilizado una arquitectura por capas, tipica de DAM y muy adecuada para Spring Boot:

- **Controller**: gestiona rutas y peticiones HTTP
- **Service**: aplica la logica de negocio
- **Repository**: accede a la base de datos con Spring Data JPA
- **Entity**: representa las tablas de la base de datos
- **Thymeleaf**: genera las vistas HTML en servidor

### Por que la elegi
Porque separa responsabilidades y hace que el proyecto sea:
- mas limpio
- mas mantenible
- mas facil de escalar
- mas facil de explicar academica y profesionalmente

---

## 4. Tecnologias utilizadas

- **Java**
- **Spring Boot**
- **Spring MVC**
- **Spring Data JPA / Hibernate**
- **Jakarta Validation**
- **MySQL**
- **Thymeleaf**
- **Bootstrap 5**
- **Bootstrap Icons**
- **Maven**

### Por que estas tecnologias
Porque permiten construir una aplicacion web full stack Java sin necesidad de separar backend y frontend en dos proyectos diferentes.

---

## 5. Base de datos

La base de datos se crea con `docs/ini_db.sql` y se rellena con `docs/data.sql`.

### Tablas principales
- `usuarios`
- `tipo_usuarios`
- `vehiculos`
- `categorias_vehiculos`
- `marcas`
- `modelos`
- `sucursales`
- `reservas`
- `pagos`
- `devoluciones`
- `mantenimientos`
- `extras`

### Relaciones importantes
- un usuario tiene un tipo de usuario
- un vehiculo pertenece a una categoria, un modelo y una sucursal
- una reserva relaciona usuario, vehiculo y dos sucursales
- un pago pertenece a una reserva
- una devolucion pertenece a un pago
- extras se relacionan con vehiculos y reservas mediante tablas intermedias

---

## 6. Modulos mas importantes del sistema

## Autenticacion
Gestionada con `AuthController` y `HttpSession`.

### Que permite
- login
- registro
- logout

### Decision tecnica
No se ha usado Spring Security, sino sesion manual, porque para un proyecto academico hace mas visible la logica de autenticacion.

---

## Vehiculos
Se gestionan desde `VehiculoController`.

### Funciones
- listado publico
- detalle publico
- alta, edicion y borrado para admin
- subida de foto

### Punto fuerte
La disponibilidad visual no depende solo de un campo fijo, sino de un calculo real en `VehiculoService`.

---

## Reservas
Es el modulo central del negocio.

### Lo que hace
- permite reservar coches
- valida fechas y horas
- evita solapamientos
- calcula precio total
- asigna extras
- genera pago automaticamente

### Punto fuerte
Aqui es donde se ve que no es un CRUD simple, sino una aplicacion con reglas de negocio.

---

## Pagos y devoluciones
- al crear una reserva se crea un pago
- si el cliente cancela, se genera una devolucion
- la devolucion cambia el estado del pago a `reembolsado`

### Punto fuerte
Se separa `Pago` de `Devolucion` para tener trazabilidad real del reembolso.

---

## Mantenimientos
Permiten bloquear vehiculos cuando estan en revision o reparacion.

### Punto fuerte
Un mantenimiento puede impedir una reserva, asi que afecta directamente a la disponibilidad real del vehiculo.

---

## 7. Flujo funcional que mejor explica el proyecto

Si tuviera que explicar una sola operacion para demostrar el valor del sistema, explicaria esta:

### Flujo: cliente reserva un vehiculo
1. El cliente entra en el detalle del vehiculo.
2. Accede al formulario de nueva reserva.
3. Selecciona fechas, horas, sucursal de devolucion, extras y metodo de pago.
4. El backend valida:
   - que no reserve en el pasado
   - que la fecha fin sea correcta
   - que no se solape con otra reserva
   - que no coincida con un mantenimiento
5. Se calcula el precio total.
6. Se guarda la reserva.
7. Se genera el pago automaticamente.

### Por que este flujo es importante
Porque demuestra relacion entre:
- interfaz
- logica de negocio
- persistencia
- integridad funcional entre entidades

---

## 8. Decisiones tecnicas fuertes que puedes defender

### 1. Arquitectura por capas
Separar controladores, servicios, repositorios y entidades mejora mantenimiento y claridad.

### 2. Thymeleaf en lugar de SPA
Encaja muy bien con Spring MVC, reduce complejidad y facilita la integracion entre backend y vistas.

### 3. SQL manual + `ddl-auto=validate`
La base de datos no la crea Hibernate automaticamente; se controla con scripts SQL y Hibernate solo valida que todo coincida.

### 4. DTO para disponibilidad
`DisponibilidadVehiculo` evita mezclar en la entidad datos calculados temporales.

### 5. Devolucion como entidad separada
Mejora trazabilidad y modela mejor el negocio que un simple boolean en `Pago`.

### 6. Imagenes servidas desde disco y classpath
Gracias a `WebConfig`, el sistema puede trabajar bien tanto en local como en Docker/servidor.

---

## 9. Problemas reales que resolvi durante el desarrollo

### 1. Error de validacion en apellidos al subir carnet
Se corrigio ampliando la validacion para aceptar letras Unicode y apellidos reales.

### 2. Imagenes que funcionaban en local pero no en servidor
Se soluciono configurando `WebConfig` para servir imagenes desde el disco y desde el classpath.

### 3. Diferencias entre entorno local y Docker/servidor
Se ajusto la configuracion para que las rutas de imagenes funcionaran de forma consistente.

### 4. Datos seed con imagenes
Se actualizaron los scripts SQL para que usuarios y vehiculos incluyeran rutas de imagenes desde el principio.

---

## 10. Puntos fuertes del proyecto

- arquitectura clara
- base de datos bien normalizada
- reglas de negocio reales
- interfaz diferenciada para admin y cliente
- gestion de imagenes
- flujo completo de reserva -> pago -> cancelacion -> devolucion
- buena base para seguir ampliando

---

## 11. Aspectos mejorables en una version profesional

- usar Spring Security
- cifrar contrasenas
- aumentar cobertura de tests
- usar enums Java para algunos estados
- mover parte de la logica de controladores grandes a servicios aun mas especializados

Esto no significa que el proyecto este mal; significa que esta bien orientado como proyecto de DAM y deja margen de evolucion profesional.

---

## 12. Guion oral breve para exponer

### Version de 1 minuto
> RentGo es una aplicacion web de alquiler de coches desarrollada con Java, Spring Boot, Thymeleaf y MySQL. He utilizado una arquitectura por capas con controladores, servicios, repositorios y entidades JPA. El proyecto no se limita a un CRUD, porque incluye reglas de negocio reales, como evitar reservas solapadas, calcular disponibilidad dinamica, generar pagos automaticamente y gestionar cancelaciones con devolucion. Ademas, incorpora panel de administracion, gestion de imagenes y una base de datos diseñada con relaciones reales entre usuarios, vehiculos, reservas, pagos y devoluciones.

### Version de 3 minutos
> Mi proyecto se llama RentGo y consiste en una aplicacion web para alquiler de coches. El objetivo no era solo crear tablas y formularios, sino construir un sistema con logica de negocio real. Para ello he usado Spring Boot como framework principal, Thymeleaf para generar las vistas HTML en servidor y MySQL como base de datos relacional. La aplicacion esta organizada en capas: los controladores gestionan las rutas, los servicios contienen la logica de negocio, los repositorios acceden a la base de datos y las entidades representan las tablas.  
>
> Los modulos principales son autenticacion, gestion de vehiculos, reservas, pagos, devoluciones y mantenimientos. El flujo mas importante es el de reserva: el cliente selecciona un vehiculo, indica fechas y horas, puede anadir extras y el sistema valida que no haya solapamientos ni mantenimientos. Si todo es correcto, calcula el precio total y genera automaticamente un pago asociado.  
>
> Otra parte importante es la cancelacion: el sistema aplica una politica de devolucion segun la antelacion con la que se cancele y registra el reembolso mediante una entidad propia llamada `Devolucion`, lo que da trazabilidad al proceso.  
>
> Tambien destacaria la gestion de imagenes, porque el sistema puede servir tanto imagenes incluidas en el proyecto como imagenes subidas en runtime, algo que fue importante al desplegar en Docker y en servidor Linux. En resumen, el proyecto demuestra conocimientos de Java, Spring MVC, JPA, SQL, validacion, sesiones, interfaces web y modelado relacional.

---

## 13. Preguntas que te pueden hacer y respuesta corta

### ¿Por que no es solo un CRUD?
Porque hay validaciones temporales, solapamientos, disponibilidad dinamica, pagos automaticos y devoluciones con reglas de negocio.

### ¿Por que usaste Thymeleaf?
Porque se integra perfectamente con Spring MVC y permite trabajar con un modelo servidor muy claro y apropiado para DAM.

### ¿Por que usaste JPA?
Porque facilita mapear entidades a tablas y trabajar con relaciones sin escribir todo el SQL manualmente.

### ¿Que parte del proyecto consideras mas compleja?
La gestion de reservas, porque conecta fechas, horas, disponibilidad, pagos, extras y cancelaciones.

### ¿Que decision tecnica consideras mas fuerte?
Separar `Pago` y `Devolucion`, y calcular la disponibilidad real en servicio en lugar de depender solo de un campo persistido.

---

## 14. Cierre final para defensa

> Si tuviera que resumir el proyecto en una frase, diria que RentGo es una aplicacion de alquiler de coches construida con arquitectura limpia y con logica de negocio real, donde se integran backend Java, vistas Thymeleaf y base de datos relacional para ofrecer un flujo completo desde el registro del cliente hasta la reserva, el pago y la devolucion.

