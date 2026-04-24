# Guia de Thymeleaf, HTML y SQL de RentGo, explicada como profesor de DAM

> Este documento se centra en la parte visual y de base de datos del proyecto:  
> **plantillas Thymeleaf, fragmentos HTML, experiencia de usuario, SEO, y scripts SQL**.

La idea es que entiendas no solo “como se ve”, sino **como se conecta la interfaz con el backend y con la base de datos**.

---

## Indice

1. [Objetivo de esta guia](#objetivo-de-esta-guia)
2. [Como se relacionan Thymeleaf, Java y SQL](#como-se-relacionan-thymeleaf-java-y-sql)
3. [Fragmentos y layout compartido](#fragmentos-y-layout-compartido)
4. [Pagina de inicio y cambio de vista por rol](#pagina-de-inicio-y-cambio-de-vista-por-rol)
5. [Pantallas de autenticacion](#pantallas-de-autenticacion)
6. [Pantallas de vehiculos](#pantallas-de-vehiculos)
7. [Pantallas de reservas](#pantallas-de-reservas)
8. [Pantallas de pagos, devoluciones y perfil](#pantallas-de-pagos-devoluciones-y-perfil)
9. [Patron CRUD visual del admin](#patron-crud-visual-del-admin)
10. [Que aporta Thymeleaf exactamente](#que-aporta-thymeleaf-exactamente)
11. [SQL estructural: `ini_db.sql`](#sql-estructural-inidbsql)
12. [SQL de datos: `data.sql`](#sql-de-datos-datasql)
13. [Relacion entre HTML, SQL y entidades JPA](#relacion-entre-html-sql-y-entidades-jpa)
14. [Decisiones de interfaz que puedes defender](#decisiones-de-interfaz-que-puedes-defender)
15. [Preguntas tipicas de examen o defensa](#preguntas-tipicas-de-examen-o-defensa)

---

## Objetivo de esta guia

En DAM no basta con saber Java. Tambien tienes que saber explicar:

- por que una vista esta hecha asi
- como se rellena el formulario
- como Thymeleaf enlaza un `th:field` con una propiedad Java
- como un `select` termina guardando una FK en la base de datos
- por que existe un fragmento comun de layout
- como el SQL y las relaciones JPA condicionan lo que ves en pantalla

Este documento te ayuda exactamente con eso.

---

## Como se relacionan Thymeleaf, Java y SQL

Vamos a verlo con un ejemplo muy simple.

### Caso: formulario de registro
- La plantilla `templates/auth/registro.html` tiene `th:object="${usuario}"`.
- Eso quiere decir que el backend envio al modelo un objeto `Usuario`.
- Los `th:field="*{nombre}"`, `*{email}`, `*{dni}`... se enlazan con los atributos de la entidad/modelo Java.
- Cuando haces submit, el controlador recibe un `@ModelAttribute Usuario usuario`.
- Ese objeto, despues de validarse, termina guardandose en la tabla `usuarios`.

Es decir:

**HTML -> Thymeleaf -> Controller -> Service -> Repository -> SQL**

Y luego al mostrar datos ocurre el camino inverso:

**SQL -> Repository -> Service -> Controller -> Thymeleaf -> HTML**

Ese ida y vuelta es la clave para entender todo el proyecto.

---

## Fragmentos y layout compartido

## `src/main/resources/templates/fragments/layout.html`

Este archivo es el centro del frontend.

### Funcion real
Actua como una mezcla de:
- sistema de diseño
- plantilla base
- libreria de fragmentos reutilizables
- contenedor SEO

### Por que es tan importante
Porque evita que cada vista repita:
- `<head>` completo
- navbar
- sidebar de admin
- footer
- imports de Bootstrap
- estilos comunes
- scripts comunes

---

## Fragmento `head(...)`

### Que contiene
- `<meta charset>`
- viewport responsive
- metadescripcion SEO
- keywords
- robots
- canonical
- Open Graph
- Twitter cards
- favicon SVG
- JSON-LD estructurado
- links a Bootstrap, Bootstrap Icons e Inter

### Que aporta en DAM
Esto demuestra que la vista no solo “se ve bien”, sino que esta pensada para:
- accesibilidad
- posicionamiento
- consistencia visual
- compartir enlaces con imagen

### Por que se eligio asi
Porque centralizar el `<head>` evita incoherencias entre paginas.  
Si cambias logo, descripcion SEO o favicon, lo haces una sola vez.

---

## CSS comun del layout

Aunque `layout.html` sea muy largo, conceptualmente el CSS se divide en bloques:

### 1. Variables de color
Define la paleta `teal` que luego se usa en toda la app.

### 2. Estilos base
- tipografia global
- reset ligero
- imagenes responsive
- foco accesible (`:focus-visible`)

### 3. Layout cliente
- navbar superior
- footer inferior
- contenido centrado

### 4. Layout admin
- sidebar fija a la izquierda
- topbar superior
- wrapper central
- tarjetas admin

### 5. Componentes visuales reutilizables
- badges
- botones
- tablas
- cards
- formularios
- avatar visual del usuario

### Por que esto esta bien planteado
Porque tienes una sola fuente de verdad para la apariencia.  
Es una solucion muy razonable en un proyecto MVC con Thymeleaf.

---

## Fragmentos funcionales importantes

Segun el uso del proyecto, `layout.html` actua como contenedor de fragmentos tipo:
- `head(...)`
- `navbar`
- `admin-sidebar`
- `footer`
- `scripts`

### Que ventaja tiene esto
Cada vista puede escribir algo como:
```html
<head th:replace="~{fragments/layout :: head('Titulo', null)}"></head>
<nav th:replace="~{fragments/layout :: navbar}"></nav>
```

Y con eso reutiliza estructura comun sin duplicar codigo.

---

## Pagina de inicio y cambio de vista por rol

## `templates/index.html`

Esta vista tiene una idea muy potente: **la misma pagina sirve como home publica o como dashboard admin**.

### Cuando `esAdmin` es verdadero
El `<body>` usa clase `admin-layout`.

#### Que se muestra
- sidebar admin
- topbar
- tarjetas de estadisticas
- accesos rapidos
- actividad reciente

#### Que datos vienen del backend
Desde `HomeController`:
- `totalUsuarios`
- `totalClientes`
- `totalVehiculos`
- `totalReservas`
- `totalSucursales`
- `ultimasReservas`

### Cuando `esAdmin` es falso
La plantilla renderiza la parte pensada para cliente/visitante.

### Por que esta decision es interesante
No crea dos rutas home distintas. La logica de rol condiciona la vista.  
Eso simplifica navegacion y hace que `/` sea siempre el punto de entrada natural.

### Que puedes decir en clase
> La misma plantilla Thymeleaf puede renderizar dos interfaces diferentes usando condicionales sobre el modelo que envia el controlador.

---

## Pantallas de autenticacion

## `templates/auth/login.html`

### Que es
Una pantalla de login sencilla, autocontenida y sin fragmento comun de layout.

### Por que no usa el layout global completo
Porque se comporta como una pantalla de acceso centrada en formulario, con un diseño mas aislado y simple.

### Elementos importantes
- formulario `POST` a `/login`
- inputs `email` y `password`
- alertas con `th:if="${error}"` y `th:if="${exito}"`
- boton para mostrar/ocultar contrasena con JS pequeno

### Que demuestra tecnicamente
- uso de mensajes flash
- integracion de Thymeleaf en mensajes de error/exito
- uso de Bootstrap sin necesidad de un frontend complejo

---

## `templates/auth/registro.html`

### Que es
El formulario de alta de cliente.

### Elementos Thymeleaf clave
```html
<form th:action="@{/registro}" th:object="${usuario}" method="post" enctype="multipart/form-data">
```

### Que significa cada cosa
- `th:action`: genera la URL correcta del formulario.
- `th:object="${usuario}"`: el formulario trabaja con el objeto `usuario` del modelo.
- `multipart/form-data`: necesario para subir la foto del carnet.

### Campos importantes
- `th:field="*{nombre}`
- `th:field="*{apellidos}`
- `th:field="*{email}`
- `th:field="*{telefono}`
- `th:field="*{dni}`
- `th:field="*{direccion}`
- `th:field="*{password}`

### Validacion visual
Usa:
- `th:classappend="${#fields.hasErrors('campo')} ? 'is-invalid'"`
- `th:errors="*{campo}"`

### Por que esto esta bien hecho
Porque enlaza validacion del backend con presentacion visual del error.  
No inventa una segunda capa de validacion separada; refleja la de Spring/Bean Validation.

### Subida de carnet
```html
<input type="file" id="carnetFoto" name="carnetFoto" accept="image/*">
```

El nombre `carnetFoto` coincide con el `@RequestParam` del controlador.  
Eso demuestra una conexion directa entre vista y backend.

---

## Pantallas de vehiculos

## `templates/vehiculos/lista.html`

Esta plantilla tiene dos caras, como `index.html`:
- vista admin
- vista cliente

### Vista admin
#### Que muestra
- grid de vehiculos en formato card
- foto o icono fallback
- datos clave del coche
- disponibilidad
- botones `Ver`, `Editar`, `Eliminar`
- buscador y filtros rapidos

### Thymeleaf interesante en admin
```html
<img th:if="${v.rutaFoto != null}" th:src="@{${v.rutaFoto}}">
```

### Por que es importante `@{${...}}`
Porque Thymeleaf resuelve correctamente la URL web de la imagen.  
Fue especialmente importante en despliegue con rutas del servidor.

### Vista cliente
#### Que muestra
- mini hero superior
- breadcrumbs
- barra de filtros
- pills de categorias
- filtros por sucursal, transmision, combustible, plazas, precio y disponibilidad
- contador de resultados

### Que demuestra esta vista
- Thymeleaf rellena opciones desde BD (`categorias`, `sucursales`)
- JS en cliente mejora la experiencia sin romper el modelo MVC
- la misma plantilla puede servir a dos tipos de usuario

---

## `templates/vehiculos/detalle.html`

Aunque aqui no he copiado todo el archivo, funcionalmente esta pantalla hace de:
- ficha publica del coche
- vista comercial del producto
- puerta de entrada a la reserva

### Que deberias saber explicar
- recibe un `vehiculo` desde el backend
- usa relaciones encadenadas como `vehiculo.modelo.marca.nombre`
- probablemente cambia botones segun `estaLogueado` y `esAdmin`
- sirve como punto de transicion a `/reservas/nuevo?vehiculoId=...`

---

## `templates/vehiculos/formulario.html`

### Que papel tiene
Es el formulario de alta/edicion para admin.

### Datos que necesita
Desde `VehiculoController.cargarDatosFormulario(...)`:
- `modelos`
- `marcas`
- `categorias`
- `sucursales`
- `extras`
- arrays de `etiquetas`, `transmisiones`, `combustiones`

### Que relacion tiene con SQL y JPA
Cada `select` representa una FK o una coleccion relacionada:
- categoria -> `id_categoria_vehiculo`
- modelo -> `id_modelo`
- sucursal -> `id_sucursal`
- extras -> tabla intermedia `vehiculos_extras`

---

## Pantallas de reservas

## `templates/reservas/formulario.html`

Esta es una de las vistas mas ricas del proyecto.

### Estructura por bloques visuales
1. vehiculo + fechas + horas
2. sucursales
3. extras opcionales
4. metodo de pago
5. politica de cancelacion
6. resumen de presupuesto
7. boton de confirmacion

### Que me gusta de su diseno
No es un formulario plano. Esta dividido por pasos visuales, lo que facilita la comprension del usuario.

### Conexiones clave con el backend

#### Objeto principal
```html
<form th:action="@{/reservas/guardar}" th:object="${reserva}">
```

#### Campos ocultos
- `id`
- `fechaReserva`
- `precioTotal`
- `estado`

Esto sirve para que el formulario pueda reenviar informacion necesaria aunque no sea visible.

#### Select del vehiculo
Cada opcion incluye atributos `data-*`:
- `data-precio`
- `data-fianza`
- `data-sucursal`
- `data-sucursal-nombre`

### Por que se eligio este enfoque
Porque el JavaScript del formulario puede recalcular el presupuesto sin tener que llamar al servidor cada vez.

### Extras
Los extras se renderizan con `th:each` y cada checkbox manda `extrasIds`.

Eso encaja con el controlador, que recibe `List<Integer> extrasIds`.

### Metodo de pago
El `select` usa `metodosPago`, que viene del backend.

### Resumen de precio
Aunque el calculo oficial y final lo hace el backend, la vista ofrece una **simulacion visual dinamica** muy util.

### Lectura de profesor
Esta vista es muy buen ejemplo de mezcla equilibrada entre:
- Thymeleaf para pintar datos del servidor
- JavaScript para mejorar UX
- backend como fuente de verdad del calculo final

---

## `templates/reservas/cancelar.html`

### Funcion
Mostrar al cliente un resumen claro antes de cancelar.

### Que enseña
- reserva concreta
- vehiculo
- recogida y devolucion
- extras
- observaciones
- importe del alquiler
- devolucion del alquiler
- devolucion de la fianza
- total a devolver

### Por que esta muy bien pensada
Porque la cancelacion no es “un clic ciego”.  
La interfaz deja claro el impacto economico antes de confirmar.

### Relacion con el backend
Los valores `devolucion`, `fianzaDevolucion`, `totalDevolucion` y `politica` los calcula `ReservaController`.

Eso es importante: la plantilla **no inventa la logica**, solo la representa.

---

## `templates/reservas/lista.html`

### Funcion visual
Muestra reservas segun rol:
- admin -> todas
- cliente -> solo las suyas

### Que deberias explicar
Es la traduccion visual directa de la logica de `ReservaController.listar(...)`.

---

## Pantallas de pagos, devoluciones y perfil

## `templates/pagos/mis-pagos.html`

### Funcion
Pantalla del cliente para ver solo sus pagos.

### Que demuestra
Que una misma entidad (`Pago`) puede tener una vista cliente y otra admin distintas.

---

## `templates/pagos/lista.html` y `formulario.html`

### Funcion
Backoffice administrativo de pagos.

### Relacion con el backend
- el admin edita/crea pagos
- el formulario usa lista de reservas
- los metodos de pago se rellenan desde el modelo

---

## `templates/devoluciones/lista.html` y `formulario.html`

### Funcion
Gestion del reembolso.

### Lo interesante
El formulario no permite elegir cualquier pago al azar; solo pagos validos para reembolso segun filtra el backend.

Eso es una buena combinacion de:
- control del backend
- interfaz guiada

---

## `templates/usuarios/perfil.html`

### Funcion
Mostrar el perfil del usuario autenticado.

### Elementos destacados
- datos personales
- rol
- direccion
- seccion de carnet para cliente

### Punto tecnico importante
```html
<img th:src="@{${usuario.rutaFotoCarnet}}">
```

Esto enlaza directamente con la ruta web guardada en BD y servida por `WebConfig`.

### Lo que conecta aqui
- BD (`ruta_foto_carnet`)
- entidad `Usuario.rutaFotoCarnet`
- controlador de perfil
- recurso servido por Spring
- plantilla Thymeleaf

Es decir: un recorrido completo de extremo a extremo.

---

## `templates/usuarios/editar-perfil.html`

### Funcion
Permitir al cliente cambiar datos concretos y actualizar la foto del carnet.

### Por que es util didacticamente
Demuestra el patron habitual de:
- ver perfil
- editar perfil
- reenviar datos al backend
- actualizar sesion y volver a vista de consulta

---

## Patron CRUD visual del admin

Las carpetas:
- `categorias`
- `extras`
- `marcas`
- `modelos`
- `sucursales`
- `tipo-usuarios`
- `mantenimientos`

siguen el patron:
- `lista.html`
- `formulario.html`

### Que aporta este patron
- consistencia visual
- menos curva de aprendizaje para el usuario admin
- mantenimiento mas facil
- misma filosofia para todas las tablas maestras

### Que puedes decir en clase
> El panel admin usa un patron visual repetible: cada modulo tiene una vista de listado y una vista de formulario, lo que da coherencia de uso y reduce complejidad.

---

## Que aporta Thymeleaf exactamente

## Ventajas reales dentro de RentGo

### 1. Binding de formularios
`th:object` y `th:field` conectan directamente con objetos Java.

### 2. Renderizado condicional
`th:if`, `th:unless` permiten cambiar vista segun rol o segun datos.

### 3. Bucles sobre colecciones
`th:each` pinta listas de vehiculos, reservas, extras, sucursales, etc.

### 4. URLs seguras
`@{/ruta}` y `@{${rutaDinamica}}` generan rutas correctas.

### 5. Integracion con mensajes de error
`#fields.hasErrors(...)` y `th:errors` pintan directamente Bean Validation.

### 6. Composicion con fragmentos
`th:replace` permite reusar layout y estructura.

### Por que esto es adecuado para DAM
Porque hace visible la relacion entre backend y frontend sin meter una SPA completa ni APIs REST separadas.

---

# SQL estructural: `ini_db.sql`

## Funcion general
Crear la base de datos y todas las tablas.

### Que hace en orden
1. `DROP DATABASE IF EXISTS rentgo;`
2. `CREATE DATABASE rentgo;`
3. `USE rentgo;`
4. crea tablas maestras
5. crea tablas operativas
6. crea relaciones N:M

---

## Tablas clave

## `tipo_usuarios`
Tabla maestra de roles.

### Por que existe
Para no guardar el rol como texto libre dentro de `usuarios`.

---

## `usuarios`
Campos principales:
- nombre
- apellidos
- email
- telefono
- dni
- direccion
- password
- `ruta_foto_carnet`
- `id_tipo`

### Que representa en diseno relacional
Una tabla de personas del sistema con una FK a `tipo_usuarios`.

---

## `vehiculos`
Campos principales:
- matricula
- anio
- color
- plazas
- puertas
- etiqueta
- transmision
- combustion
- kilometraje
- precio_dia
- fianza
- ruta_foto
- disponibilidad
- FK a categoria, modelo y sucursal

### Por que esta bien diseniada
Porque separa muy bien los datos propios del coche de los datos de clasificacion y ubicacion.

---

## `reservas`
Campos principales:
- fecha/hora de inicio
- fecha/hora de fin
- fecha de reserva
- precio total
- estado
- observaciones
- usuario
- vehiculo
- sucursal recogida
- sucursal devolucion

### Por que es la tabla central
Porque une casi todo el negocio: cliente, coche, ubicaciones y fechas.

---

## `pagos`
Relacion 1:1 con `reservas`.

### Por que tiene sentido
Cada reserva tiene un pago principal asociado.

---

## `devoluciones`
Relacion 1:1 con `pagos`.

### Por que no se mete todo en `pagos`
Porque la devolucion tiene vida propia: fecha, motivo e importe reembolsado.

---

## Tablas intermedias

### `vehiculos_extras`
Relacion N:M entre vehiculos y extras.

### `reservas_extras`
Relacion N:M entre reservas y extras.

### Lectura docente
Estas tablas existen porque en SQL relacional una N:M no se puede representar directamente sin tabla puente.

---

# SQL de datos: `data.sql`

## Funcion general
Cargar datos iniciales de demostracion.

### Orden logico de insercion
1. tipos de usuario
2. admin
3. categorias
4. sucursales
5. extras
6. clientes
7. marcas
8. modelos
9. vehiculos
10. reservas
11. pagos
12. devoluciones

### Por que ese orden importa
Porque hay claves foraneas.  
No puedes insertar una reserva si antes no existen usuario, vehiculo y sucursales.

---

## Datos especialmente utiles para demo

### Clientes con foto de carnet
Los clientes ya traen `ruta_foto_carnet`, lo que permite probar el perfil sin subir archivos manualmente.

### Vehiculos con imagen
La flota ya trae `ruta_foto`, lo que hace posible enseñar la web como catalogo visual desde el primer momento.

### Reservas pasadas
Permiten probar:
- listado de reservas
- estados
- pagos
- devoluciones

### Pagos y devoluciones seed
Demuestran el flujo economico completo aunque no hagas una reserva nueva.

---

## Relacion entre HTML, SQL y entidades JPA

Esta es la parte mas importante para entender el proyecto de verdad.

### Ejemplo 1: login
- HTML pide email y password
- controlador busca por email
- repositorio consulta `usuarios`
- JPA mapea un `Usuario`
- Thymeleaf luego usa `session.usuarioLogueado`

### Ejemplo 2: seleccionar vehiculo en una reserva
- HTML muestra `<select>` de vehiculos
- Thymeleaf recorre `${vehiculos}`
- el formulario manda `vehiculo.id`
- el controlador resuelve ese ID a la entidad `Vehiculo`
- SQL lo guarda como `id_vehiculo` en `reservas`

### Ejemplo 3: extras de una reserva
- HTML manda varios `extrasIds`
- controlador los convierte en `Set<Extra>`
- JPA persiste la relacion en `reservas_extras`

### Ejemplo 4: foto de carnet en perfil
- SQL almacena `/img/carnets/...`
- entidad `Usuario` mapea `rutaFotoCarnet`
- controlador mete `usuario` en modelo
- Thymeleaf pinta `th:src="@{${usuario.rutaFotoCarnet}}"`
- `WebConfig` resuelve el recurso real

Eso es integracion completa entre capas.

---

## Decisiones de interfaz que puedes defender

### 1. Uso de fragmentos compartidos
Reduce duplicacion y mejora consistencia.

### 2. Home dual por rol
Simplifica la navegacion del sistema.

### 3. Formularios por bloques
Especialmente en reservas, mejora UX y claridad.

### 4. Uso de Bootstrap
Permite una UI consistente y responsive sin reinventar componentes.

### 5. Validacion visual integrada con backend
Los errores de Bean Validation se reflejan directamente en la interfaz.

### 6. Uso de `data-*` en formularios
Permite enriquecer la experiencia de usuario con JS sin comprometer la logica oficial del backend.

### 7. Pantalla intermedia de cancelacion
Aporta transparencia y evita acciones destructivas ciegas.

---

## Preguntas tipicas de examen o defensa

### ¿Por que usar Thymeleaf y no una SPA con React?
Porque el proyecto esta orientado a MVC servidor, es mas simple para DAM y encaja muy bien con Spring Boot.

### ¿Que ventaja tiene `th:field`?
Que enlaza directamente inputs HTML con propiedades del objeto Java enviado por el controlador.

### ¿Por que hay dos layouts, cliente y admin?
Porque la experiencia de uso cambia mucho entre catalogo/cliente y backoffice/admin.

### ¿Por que usar tablas intermedias para extras?
Porque las relaciones N:M en SQL necesitan una tabla puente.

### ¿Por que `data.sql` es importante en una demo?
Porque permite mostrar la aplicacion completa sin tener que cargar datos manualmente.

### ¿Que conecta una foto de carnet con la UI?
La cadena es: SQL -> entidad `Usuario` -> controlador -> Thymeleaf -> `WebConfig` -> archivo fisico o classpath.

### ¿Por que el resumen de precio en reservas no puede ser solo JavaScript?
Porque el calculo definitivo debe validarlo el backend; el JS solo mejora la experiencia visual.

---

## Cierre de profesor

Si entiendes esta guia, entiendes algo muy importante en DAM:

> una interfaz web bien hecha no vive sola; esta completamente condicionada por el backend, por el modelo relacional y por como se mapean los datos.

En `RentGo` eso se ve muy bien porque:
- Thymeleaf enlaza directamente con objetos Java
- las entidades reflejan las tablas SQL
- las vistas muestran relaciones reales del dominio
- los formularios acaban convertidos en inserts/updates coherentes

En resumen:

- **HTML/Thymeleaf** decide como se presenta la informacion
- **Java/Spring** decide como se procesa
- **SQL** decide como se estructura y se conserva

Y las tres cosas juntas forman la aplicacion completa.

