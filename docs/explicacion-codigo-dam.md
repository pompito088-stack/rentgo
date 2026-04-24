# Explicacion del codigo de RentGo, como si te lo explicara tu profesor de DAM

> Objetivo de este documento: ayudarte a **entender el proyecto entero** sin limitarte a memorizarlo.  
> Lo explico con mentalidad de profesor: **que hace cada archivo, para que sirve, por que esta asi y que patron de programacion se esta aplicando**.

---

## Indice

1. [Como leer este documento](#como-leer-este-documento)
2. [Vision global del proyecto](#vision-global-del-proyecto)
3. [Estructura real del proyecto](#estructura-real-del-proyecto)
4. [Configuracion y arranque](#configuracion-y-arranque)
5. [Capa web y controladores](#capa-web-y-controladores)
6. [Capa de servicio](#capa-de-servicio)
7. [Capa de persistencia: repositorios](#capa-de-persistencia-repositorios)
8. [Modelo de dominio: entidades y DTO](#modelo-de-dominio-entidades-y-dto)
9. [Vistas Thymeleaf](#vistas-thymeleaf)
10. [Base de datos y scripts SQL](#base-de-datos-y-scripts-sql)
11. [Por que esta arquitectura tiene sentido en DAM](#por-que-esta-arquitectura-tiene-sentido-en-dam)
12. [Que te preguntaria yo en un examen de DAM](#que-te-preguntaria-yo-en-un-examen-de-dam)

---

## Como leer este documento

Cuando digo **linea por linea**, en un proyecto real no siempre tiene sentido comentar una a una las 200 lineas de getters, setters o CSS repetitivo. Por eso lo explico de esta forma:

- **Linea o bloque de lineas** cuando el codigo cambia de idea.
- **Que hace**: comportamiento tecnico.
- **Para que sirve**: objetivo funcional.
- **Por que lo he elegido**: decision de diseño.

Es decir: no solo te digo *que pone*, sino *por que un desarrollador lo haria asi*.

---

## Vision global del proyecto

`RentGo` es una aplicacion web Java con Spring Boot para alquiler de coches.

### Que permite hacer
- Registrar clientes.
- Iniciar sesion.
- Gestionar vehiculos, marcas, modelos, categorias, sucursales y extras.
- Hacer reservas.
- Generar pagos asociados a reservas.
- Gestionar cancelaciones y devoluciones.
- Registrar mantenimientos de vehiculos.
- Subir imagenes de coches y fotos de carnet.

### Arquitectura que usa
Es la arquitectura clasica que en DAM debes saber defender:

- **Controlador**: recibe la peticion HTTP.
- **Servicio**: aplica la logica de negocio.
- **Repositorio**: habla con la base de datos.
- **Entidad**: representa una tabla.
- **Vista Thymeleaf**: genera HTML dinamico.

Dicho de forma sencilla:

- El **controller** decide *que ruta responde*.
- El **service** decide *que reglas de negocio se aplican*.
- El **repository** decide *como se consulta o guarda*.
- La **entity** decide *como se mapea la tabla*.
- El **template HTML** decide *como se ve al usuario*.

---

## Estructura real del proyecto

### Backend Java principal
- `src/main/java/com/ilerna/rentgo/RentgoApplication.java`
- `src/main/java/com/ilerna/rentgo/config/*`
- `src/main/java/com/ilerna/rentgo/controller/*`
- `src/main/java/com/ilerna/rentgo/service/*`
- `src/main/java/com/ilerna/rentgo/repository/*`
- `src/main/java/com/ilerna/rentgo/model/*`
- `src/main/java/com/ilerna/rentgo/dto/*`

### Frontend Thymeleaf
- `src/main/resources/templates/index.html`
- `src/main/resources/templates/auth/*`
- `src/main/resources/templates/vehiculos/*`
- `src/main/resources/templates/reservas/*`
- `src/main/resources/templates/pagos/*`
- `src/main/resources/templates/usuarios/*`
- `src/main/resources/templates/fragments/layout.html`
- y el resto de carpetas CRUD (`categorias`, `marcas`, `modelos`, `extras`, etc.)

### Configuracion y datos
- `pom.xml`
- `src/main/resources/application.properties`
- `docs/ini_db.sql`
- `docs/data.sql`

### Test actual
- `src/test/java/com/ilerna/rentgo/RentgoApplicationTests.java`

---

# Configuracion y arranque

## 1. `pom.xml`

Este archivo define el proyecto Maven: dependencias, version de Java y plugin de build.

### Lineas 1-4
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project ...>
<modelVersion>4.0.0</modelVersion>
```
**Que hace**: declara que esto es un proyecto Maven y usa el modelo 4.0.0.  
**Para que sirve**: Maven necesita saber como interpretar el fichero.  
**Por que se elige**: es el estandar actual en proyectos Java con Maven.

### Lineas 5-10
```xml
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>4.0.4</version>
</parent>
```
**Que hace**: hereda configuracion del parent de Spring Boot.  
**Para que sirve**: evita declarar manualmente muchas versiones compatibles.  
**Por que se elige**: reduce errores de incompatibilidad entre librerias.

### Lineas 11-28
```xml
<groupId>com.ilerna</groupId>
<artifactId>rentgo</artifactId>
<version>0.0.1-SNAPSHOT</version>
```
**Que hace**: da identidad al proyecto.  
**Para que sirve**: Maven construye el artefacto con ese nombre y version.  
**Por que se elige**: es la forma canonica de nombrar un proyecto Java.

### Lineas 29-31
```xml
<properties>
  <java.version>25</java.version>
</properties>
```
**Que hace**: define la version de Java.  
**Para que sirve**: el compilador y Spring saben que nivel de lenguaje usar.  
**Por que se elige**: centralizar propiedades evita repetirlas.

### Lineas 32-77: dependencias

#### `spring-boot-starter-thymeleaf`
**Que hace**: integra Thymeleaf.  
**Para que sirve**: renderizar HTML dinamico en servidor.  
**Por que se elige**: en DAM encaja muy bien con MVC tradicional.

#### `spring-boot-starter-webmvc`
**Que hace**: aporta `@Controller`, rutas HTTP, vistas y MVC.  
**Para que sirve**: crear la parte web del proyecto.  
**Por que se elige**: es la base de una app Spring MVC clasica.

#### `spring-boot-starter-validation`
**Que hace**: activa Bean Validation (`@NotBlank`, `@Email`, etc.).  
**Para que sirve**: validar formularios y entidades.  
**Por que se elige**: la validacion declarativa es mas limpia que hacerlo todo a mano.

#### `spring-boot-starter-data-jpa`
**Que hace**: integra JPA/Hibernate.  
**Para que sirve**: mapear entidades a tablas y hacer persistencia ORM.  
**Por que se elige**: separa el modelo de dominio del SQL duro y repetitivo.

#### `mysql-connector-j`
**Que hace**: driver de MySQL.  
**Para que sirve**: que Java pueda conectar con la base de datos.  
**Por que se elige**: la base del proyecto esta en MySQL.

#### `spring-boot-devtools`
**Que hace**: reinicio rapido en desarrollo.  
**Para que sirve**: trabajar mas comodo en local.  
**Por que se elige**: mejora productividad; no va a produccion como dependencia principal.

#### Dependencias de test
- `spring-boot-starter-thymeleaf-test`
- `spring-boot-starter-webmvc-test`

**Que hacen**: ayudan a probar vistas y la capa web.  
**Para que sirven**: preparar el proyecto para testear.  
**Por que se eligen**: dejan abierta la puerta a hacer test de controladores y vistas.

### Lineas 79-86
```xml
<build>
  <plugins>
    <plugin>
      <artifactId>spring-boot-maven-plugin</artifactId>
    </plugin>
  </plugins>
</build>
```
**Que hace**: registra el plugin que empaqueta y ejecuta Spring Boot.  
**Para que sirve**: poder lanzar `mvn spring-boot:run` o construir el JAR ejecutable.  
**Por que se elige**: es el plugin oficial y necesario en casi todo proyecto Spring Boot.

---

## 2. `src/main/resources/application.properties`

Este fichero contiene la configuracion de la aplicacion.

### Linea 1
```properties
spring.application.name=rentgo
```
**Que hace**: da nombre logico a la aplicacion.  
**Para que sirve**: logs, contexto Spring y metadata.  
**Por que se elige**: ayuda a identificar el proyecto.

### Lineas 4-7
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rentgo?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
```
**Que hace**: configura la conexion a MySQL.  
**Para que sirve**: JPA y repositories necesitan esta conexion para trabajar.  
**Por que se elige**: separar configuracion de codigo es una buena practica.

### Lineas 9-12
```properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```
**Que hace**:
- `validate`: comprueba que las entidades encajan con la BD.
- `show-sql=true`: enseña consultas SQL en consola.
- `dialect`: usa sintaxis adecuada para MySQL.

**Para que sirve**: controlar el comportamiento de Hibernate.  
**Por que se elige**:
- `validate` es muy buena eleccion en un proyecto con scripts SQL manuales, porque evita que Hibernate te cambie la base por sorpresa.
- `show-sql` es muy util en clase y desarrollo para ver que esta haciendo el ORM.

### Lineas 14-16
```properties
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB
```
**Que hace**: limita el tamano maximo de las subidas.  
**Para que sirve**: evitar archivos exagerados y errores de memoria.  
**Por que se elige**: una foto de vehiculo o carnet cabe de sobra en ese rango.

### Lineas 18-22
```properties
app.upload.path=src/main/resources/static/img/vehiculos
app.upload.carnet.path=src/main/resources/static/img/carnets
```
**Que hace**: define carpetas para guardar imagenes.  
**Para que sirve**: `VehiculoController`, `AuthController` y `PerfilController` las usan al copiar archivos.  
**Por que se elige**: desacopla las rutas fisicas del codigo Java.

---

## 3. `src/main/java/com/ilerna/rentgo/RentgoApplication.java`

### Linea 1
`package com.ilerna.rentgo;`  
Define el paquete raiz.

### Lineas 3-4
Importan `SpringApplication` y `SpringBootApplication`.  
Son las clases clave para arrancar Spring Boot.

### Linea 6
```java
@SpringBootApplication
```
**Que hace**: combina varias anotaciones en una:
- `@Configuration`
- `@EnableAutoConfiguration`
- `@ComponentScan`

**Para que sirve**: activar el arranque automatico y buscar beans.  
**Por que se elige**: es la anotacion estandar de entrada en Spring Boot.

### Lineas 7-11
```java
public class RentgoApplication {
    public static void main(String[] args) {
        SpringApplication.run(RentgoApplication.class, args);
    }
}
```
**Que hace**: es el punto de entrada real de la aplicacion.  
**Para que sirve**: levanta el contexto Spring, Tomcat embebido y todos los beans.  
**Por que se elige**: sin esto no hay aplicacion ejecutandose.

---

## 4. `src/test/java/com/ilerna/rentgo/RentgoApplicationTests.java`

### Lineas 1-4
Importa JUnit y `@SpringBootTest`.

### Linea 6
```java
@SpringBootTest
```
**Que hace**: arranca todo el contexto Spring durante el test.  
**Para que sirve**: comprobar que el proyecto al menos levanta.  
**Por que se elige**: es el test minimo de sanidad.

### Lineas 7-11
```java
class RentgoApplicationTests {
    @Test
    void contextLoads() {}
}
```
**Que hace**: test vacio que solo verifica que Spring no rompe al iniciar.  
**Para que sirve**: detectar errores graves de configuracion.  
**Por que se elige**: es el tipico primer test de un proyecto Spring.

---

# Capa web y controladores

## Idea general de los controladores

En este proyecto los controladores hacen 4 cosas muy tipicas:

1. Reciben la ruta (`@GetMapping`, `@PostMapping`).
2. Comprueban permisos o sesion.
3. Piden datos a la capa servicio.
4. Devuelven una vista o una redireccion.

Esto esta muy bien en DAM porque evita mezclar HTML, SQL y reglas de negocio en el mismo sitio.

---

## 5. `config/GlobalModelAttributes.java`

Este archivo no es un controlador clasico, pero afecta a **todas las vistas**.

### Lineas 16-17
```java
@ControllerAdvice
public class GlobalModelAttributes {
```
**Que hace**: declara una clase global para MVC.  
**Para que sirve**: inyectar atributos comunes a todas las plantillas.  
**Por que se elige**: evita repetir el mismo codigo en cada controlador.

### Lineas 19-22
`requestUri(HttpServletRequest request)`  
Devuelve la URI actual.

**Para que sirve**: marcar menu activo, canonical o logica visual en Thymeleaf.

### Lineas 24-27
`requestUrl(...)` devuelve la URL completa.  
**Para que sirve**: SEO, metadatos Open Graph y canonical.

### Lineas 29-36
`baseUrl(...)` calcula la URL base (`http://host:puerto`).  
**Para que sirve**: construir rutas absolutas.  
**Por que se elige**: Thymeleaf 3 con Spring 6 ya no expone `#request` como antes, y esta clase compensa eso.

---

## 6. `config/WebConfig.java`

Este archivo ha sido clave para servir imagenes en local y en Docker/servidor.

### Lineas 16-17
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
```
**Que hace**: registra configuracion web personalizada.  
**Para que sirve**: anadir handlers de recursos estaticos.  
**Por que se elige**: Spring Boot sirve `/static` por defecto, pero aqui tambien se necesitan archivos del disco.

### Lineas 19-23
```java
@Value("${app.upload.path}")
private String uploadPath;
@Value("${app.upload.carnet.path}")
private String carnetUploadPath;
```
**Que hace**: inyecta propiedades desde `application.properties`.  
**Para que sirve**: no quemar rutas dentro del codigo.  
**Por que se elige**: flexibilidad y mantenimiento.

### Lineas 25-30
```java
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    Path uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
    Path carnetDir = Paths.get(carnetUploadPath).toAbsolutePath().normalize();
```
**Que hace**: convierte las rutas en `Path` absolutos y normalizados.  
**Para que sirve**: que funcionen bien en Windows y Linux.  
**Por que se elige**: evita errores con barras, rutas relativas y formatos `file:`.

### Lineas 32-37
```java
String uploadLocation = uploadDir.toUri().toString();
if (!uploadLocation.endsWith("/")) uploadLocation += "/";
```
Y lo mismo para carnet.

**Que hace**: genera una URI `file:///...` correcta.  
**Para que sirve**: Spring necesita una ubicacion valida de recurso.  
**Por que se elige**: `toUri()` es mas robusto que concatenar strings a mano.

### Lineas 39-47
```java
registry.addResourceHandler("/img/vehiculos/**")
        .addResourceLocations(uploadLocation, "classpath:/static/img/vehiculos/");

registry.addResourceHandler("/img/carnets/**")
        .addResourceLocations(carnetLocation, "classpath:/static/img/carnets/");
```
**Que hace**: crea dos rutas web y les asigna dos fuentes posibles:
1. disco
2. classpath del JAR

**Para que sirve**:
- mostrar imagenes subidas en runtime
- seguir viendo las imagenes seed incluidas dentro del proyecto

**Por que se elige**: el fallback al classpath resuelve el problema tipico de Docker: el archivo puede no existir en disco del contenedor, pero si dentro del JAR.

### Lineas 49-50
Aclara que no se redefine `/**` porque Spring Boot ya sirve los estaticos por defecto.  
**Esto es una buena decision**, porque duplicar handlers suele dar problemas sutiles.

---

## 7. `controller/AuthController.java`

Este controlador gestiona login, registro y logout.

### Lineas 27-39
Definen la clase, inyectan `UsuarioService`, `TipoUsuarioService` y la ruta de subida del carnet.

**Que hace**: prepara lo necesario para autenticar y registrar.  
**Para que sirve**: concentrar toda la autenticacion basica en un solo sitio.  
**Por que se elige**: es mejor que repartir login/registro entre muchos controladores.

### Lineas 43-47 - `mostrarLogin()`
```java
@GetMapping("/login")
public String mostrarLogin() {
    return "auth/login";
}
```
**Que hace**: muestra la vista del login.  
**Para que sirve**: responder a `GET /login`.  
**Por que se elige**: separar GET (mostrar) de POST (procesar) es la forma correcta en MVC.

### Lineas 49-65 - `login(...)`
#### Lineas 56-58
Busca usuario por email.

#### Lineas 58-60
```java
if (optUsuario.isPresent() && optUsuario.get().getPassword().equals(password)) {
    session.setAttribute("usuarioLogueado", optUsuario.get());
    return "redirect:/";
}
```
**Que hace**: autentica y guarda el usuario en sesion.  
**Para que sirve**: mantener al usuario logueado entre peticiones.  
**Por que se elige**: el proyecto no usa Spring Security; usa `HttpSession` manual.

> Como profesor te diria: **funciona**, pero en un proyecto profesional la contrasena deberia ir cifrada y validada con Spring Security.

#### Lineas 63-64
Mete un mensaje flash y redirige si falla.  
**Buena practica**: no renderizar la vista directamente tras un POST fallido cuando quieres seguir el patron PRG (Post/Redirect/Get).

### Lineas 69-74 - `mostrarRegistro(...)`
Crea un `new Usuario()` y lo envia a la vista.

**Para que sirve**: Thymeleaf necesita un objeto para el formulario.

### Lineas 77-140 - `registrar(...)`
Este es uno de los metodos mas importantes del proyecto.

#### Lineas 78-82
Recibe:
- el `Usuario` validado
- `BindingResult`
- la foto del carnet
- `Model`
- `RedirectAttributes`

**Que hace**: combina datos del formulario y fichero subido.  
**Por que se elige**: en registro real suelen convivir campos de texto y un archivo.

#### Lineas 84-87
Si Bean Validation detecta errores, vuelve al formulario.  
**Esto es correcto MVC puro**.

#### Lineas 89-94
Bloquea emails con patron `@admin-`.  
**Para que sirve**: evitar que un cliente se “autocree” con apariencia de admin.  
**Por que se elige**: defensa de negocio simple pero util.

#### Lineas 96-115
Comprueba duplicados de email, telefono y DNI.

**Que hace**: validacion de negocio adicional.  
**Para que sirve**: dar mensajes mas claros que un fallo SQL por constraint.  
**Por que se elige**: buena UX y mejor control de errores.

#### Lineas 117-118
```java
usuario.setTipoUsuario(tipoUsuarioService.buscarPorNombre("cliente").orElse(null));
```
**Que hace**: asigna el rol cliente automaticamente.  
**Para que sirve**: impedir que el formulario elija el rol.  
**Por que se elige**: seguridad y coherencia.

#### Lineas 120-134
Gestiona la subida del carnet.

Puntos clave:
- sanea el nombre del archivo
- crea directorio si no existe
- copia el archivo
- guarda la ruta web en BD

**Por que se elige este enfoque**:
- el usuario no guarda una ruta fisica, sino una ruta web (`/img/carnets/...`)
- eso desacopla almacenamiento y visualizacion

#### Linea 136
`usuarioService.guardar(usuario);`  
Guarda el usuario final.

#### Lineas 138-139
Mensaje flash de exito y redireccion a login.  
**Esto esta bien pensado** porque tras registrarte lo normal es iniciar sesion.

### Lineas 144-149 - `logout(...)`
Invalidar la sesion y volver a inicio.  
Es la forma basica correcta de cerrar sesion cuando trabajas con `HttpSession`.

---

## 8. `controller/HomeController.java`

Este controlador decide que ve un admin y que ve un cliente/visitante en la home.

### Lineas 17-30
Inyecta servicios de usuarios, vehiculos, reservas y sucursales.

### Lineas 32-53 - `index(...)`
#### Lineas 34-36
Recupera el usuario de sesion y calcula `esAdmin`.

#### Lineas 38-50
Si es admin:
- calcula estadisticas
- actualiza estados automáticos de reservas
- carga las ultimas 5 reservas

**Para que sirve**: convertir la home en dashboard administrativo.  
**Por que se elige**: reutiliza la misma ruta `/` para dos experiencias distintas.

**Decision de diseño interesante**:  
no crea una ruta `/admin/dashboard`; usa condicionales por rol.  
Eso simplifica la navegacion, aunque obliga a que `index.html` tenga dos caras.

---

## 9. `controller/PerfilController.java`

Gestiona “Mi perfil”.

### Lineas 37-46 - `mostrarPerfil(...)`
Comprueba sesion, mete `usuario` y `esAdmin` en modelo y devuelve `usuarios/perfil`.

**Para que sirve**: mostrar datos del usuario actual.  
**Por que se elige**: el perfil es contextual, no se pasa por ID en URL.

### Lineas 48-56 - `mostrarFormularioEditar(...)`
Solo deja editar perfil a clientes, no a admins.

**Por que se elige**: el admin en este proyecto no usa este flujo de edicion personal o se quiere limitar la superficie funcional.

### Lineas 58-110 - `guardarPerfil(...)`
Puntos importantes:

#### Lineas 64-70
Recupera el usuario real de sesion y luego el de BD.

**Para que sirve**: no confiar en el objeto del formulario como si fuera completo.  
**Por que se elige**: asi no pierdes datos sensibles ni relaciones no editables.

#### Lineas 74-80
Valida telefono duplicado excluyendo al propio usuario.  
**Esto es muy importante**: sin esa exclusion, el sistema pensaria que tu propio telefono esta duplicado.

#### Lineas 82-86
Actualiza solo campos editables:
- nombre
- apellidos
- telefono
- direccion

**Por que se elige**: principio de “lista blanca”. Es mejor indicar lo que SI puede cambiarse.

#### Lineas 88-102
Nueva subida de carnet con el mismo patron que en registro.

#### Lineas 104-107
Guarda y actualiza la sesion.  
**Esto es clave**: si no actualizas sesion, en pantalla seguirian apareciendo datos antiguos hasta volver a loguearse.

---

## 10. `controller/VehiculoController.java`

Es un CRUD completo y uno de los mejores ejemplos de controlador del proyecto.

### Lineas 28-54
Define ruta base `/vehiculos`, inyecta servicios relacionados y la ruta de subida.

### Lineas 56-63
Metodos auxiliares `noEstaLogueado()` y `noEsAdmin()`.

**Para que sirven**: no repetir la misma validacion 20 veces.  
**Por que se elige**: mejora legibilidad.

### Lineas 65-76 - listado publico
Muestra todos los vehiculos, sucursales, categorias y disponibilidades calculadas en tiempo real.

**Punto didactico importante**:  
la disponibilidad que se ve no depende solo del campo `disponibilidad` guardado, sino de un calculo real usando reservas y mantenimientos.

### Lineas 78-92 - detalle publico
Carga un vehiculo por ID y añade:
- si el usuario esta logueado
- si es admin
- la disponibilidad calculada

**Para que sirve**: la vista de detalle puede cambiar botones o mensajes segun sesion y rol.

### Lineas 94-114 - formularios nuevo/editar
Solo admin.  
Llaman a `cargarDatosFormulario(model)`.

**Por que se elige**: todos los combos (`modelos`, `sucursales`, `categorias`, etc.) se centralizan en un metodo reutilizable.

### Lineas 116-172 - `guardar(...)`
#### Lineas 125-128
Si hay errores de validacion, recarga datos del formulario y vuelve a la vista.

#### Lineas 130-136
Valida matricula duplicada excluyendo el propio ID en edicion.

#### Lineas 138-168
Gestion de foto:
- sanea nombre
- si editas y habia foto anterior, la borra
- guarda la nueva
- si no hay nueva foto en edicion, mantiene la anterior

**Esta parte esta muy bien resuelta** porque cubre los tres escenarios reales:
1. alta nueva con foto
2. edicion con nueva foto
3. edicion sin cambiar foto

#### Linea 170
Guarda el vehiculo.

### Lineas 174-185 - eliminar
Antes de borrar la entidad, elimina la foto del disco.

**Por que se elige**: evita “basura” de archivos huérfanos.

### Lineas 187-201 - `borrarFotoDelDisco(...)`
Convierte una ruta web en nombre de fichero y elimina en el directorio real.

**Que te preguntaria en clase**:  
¿Por que no se guarda la ruta fisica completa en la BD?  
Respuesta correcta: porque la BD debe guardar una ruta logica/web, no una dependencia del sistema operativo.

### Lineas 203-224 - `cargarDatosFormulario(...)`
Carga:
- modelos
- marcas
- categorias
- sucursales
- extras
- enums simulados como arrays de string

**Por que se elige**: rapido, simple y suficiente para un TFG/proyecto de DAM.  
En una app mas grande estas listas podrian venir de enums Java reales o tablas maestras.

---

## 11. `controller/ReservaController.java`

Es el corazon del negocio.

### Lineas 28-57
Inyecta muchos servicios porque reservar toca varias piezas:
- reserva
- vehiculo
- sucursal
- usuario
- pago
- extras
- mantenimiento
- devolucion

**Esto es normal**: una reserva es la operacion transversal principal.

### Lineas 63-77 - listar
- obliga login
- actualiza estados automaticamente
- admin ve todas
- cliente solo las suyas

**Por que se elige**: misma ruta, comportamiento distinto por rol.

### Lineas 79-104 - formulario nueva reserva
Solo clientes.  
Preselecciona vehiculo si vienes desde detalle.

**Muy buena UX**: desde la ficha del coche puedes reservar ese coche sin volver a elegirlo.

### Lineas 117-275 - guardar reserva
Este metodo es de los mas importantes de todo el proyecto.

#### Validaciones temporales
- Lineas 134-140: fecha fin no puede ser anterior.
- Lineas 142-148: parsea horas.
- Lineas 150-159: si es el mismo dia, hora fin > hora inicio.
- Lineas 161-169: no reservar en el pasado.

**Por que se elige**: las reservas mezclan fecha y hora, asi que no basta con validar solo fechas.

#### Solapamientos
- Lineas 172-180: no chocar con otras reservas.
- Lineas 182-189: no chocar con mantenimientos.

**Esto es logica de negocio pura**.  
Si esta parte fallara, la app permitiria doble alquiler del mismo coche.

#### Resolver entidades transitorias
- Lineas 192-204

**Que hace**: toma IDs que vienen del formulario y los convierte en entidades reales de BD.  
**Para que sirve**: JPA trabaja mejor con objetos bien resueltos.  
**Por que se elige**: evita guardar referencias incompletas.

#### Asignar usuario y estado
- Lineas 206-210

Si el que reserva es cliente:
- el usuario es el logueado
- el estado pasa a `confirmada`

**Por que se elige**: el cliente no decide su propio usuario ni el estado de negocio.

#### Calculo de precio
- Lineas 212-228

Calcula:
- dias
- precio base del vehiculo
- extras por dia
- total

**Esto esta muy bien planteado** porque los extras tambien se cobran por dia, no como importe fijo.

#### Extras seleccionados
- Lineas 230-237

Convierte lista de IDs en `Set<Extra>`.

#### Fecha de reserva
- Lineas 239-242

Si es nueva, pone fecha actual.

#### Guardado y efecto sobre el vehiculo
- Lineas 244-256

Una vez guardada, mueve la sucursal del vehiculo a la sucursal de devolucion.

**Ojo**: esto modela la ubicacion futura/real del coche tras la devolucion.  
Como profesor te diria que es una decision funcional interesante, aunque en sistemas mas complejos podria hacerse al cerrar efectivamente la reserva, no al crearla.

#### Creacion automatica del pago
- Lineas 258-272

Si la reserva es nueva y la hace un cliente, crea un `Pago` asociado.

**Por que se elige**: simplifica el flujo real de negocio. Reservar implica pagar.

### Lineas 283-336 - pantalla de cancelacion
Calcula la politica de reembolso:
- >= 24h: 100%
- < 24h: 50%
- la fianza se devuelve integra

**Esto introduce una regla de negocio realista**, no solo un CRUD academico.

### Lineas 343-404 - confirmar cancelacion
Hace varias cosas encadenadas:
1. pone reserva en `cancelada`
2. recalcula importe a devolver
3. crea `Devolucion` si no existia
4. pone pago en `reembolsado`

**Esto es muy buen ejemplo de consistencia de negocio**.  
Una operacion funcional cambia varias entidades relacionadas.

### Lineas 406-417 - `cargarDatosFormulario(...)`
Prepara combos del formulario y lista de metodos de pago.

---

## 12. `controller/PagoController.java`

### Idea general
Tiene doble cara:
- admin gestiona todos los pagos
- cliente ve `mis-pagos`

### Lineas 37-45 - `misPagos(...)`
Solo clientes.  
Muestra pagos del usuario logueado.

### Lineas 47-52 - `listar(...)`
Solo admin.  
Lista todos los pagos.

### Lineas 54-74 - formularios nuevo/editar
Carga reserva y metodos de pago.

### Lineas 76-94 - `guardar(...)`
Si el pago es nuevo, fuerza `estadoPago = realizado`.

**Por que se elige**: el estado inicial de un pago no deberia depender del formulario del usuario.

### Lineas 96-100 - eliminar
Borrado simple.

---

## 13. `controller/MantenimientoController.java`

Este controlador es muy parecido al de reservas, pero orientado a indisponibilidad tecnica.

### Lo importante de este archivo
- valida fechas y horas
- evita que un mantenimiento se solape con reservas
- solo admin puede gestionarlo

### Razon de diseño
Un mantenimiento no es solo “un registro mas”; es una causa de indisponibilidad operativa. Por eso tiene logica temporal, igual que las reservas.

---

## 14. `controller/DevolucionController.java`

Este archivo formaliza los reembolsos.

### Idea fuerte del diseño
No se cambia el pago “a pelo”, sino que se crea una entidad `Devolucion`.

**Por que es buena idea**:
- deja trazabilidad
- sabes cuanto se devolvio
- sabes cuando
- sabes el motivo

Eso en DAM vale mucho como argumento de diseño.

### Puntos clave
- lista devoluciones solo para admin
- permite preseleccionar pago
- solo deja elegir pagos `realizado` sin devolucion previa
- al guardar, resuelve el pago real desde BD
- al eliminar, revierte el pago

**Esto implementa integridad funcional entre Pago y Devolucion.**

---

## 15. `controller/UsuarioController.java`

Este controlador es especial porque esta **capado** adrede.

### Que permite
- solo listar usuarios

### Que bloquea
- nuevo
- editar
- guardar
- eliminar

**Por que se ha elegido asi**:
- los usuarios cliente se crean desde registro, no desde panel admin
- reduce riesgo de tocar cuentas a mano
- simplifica el modelo de seguridad

Como profesor, te diria que es una decision muy defendible: **no todo CRUD tiene por que estar abierto solo porque la tabla exista**.

---

## 16. Patron CRUD repetido en controladores admin

Hay varios controladores que siguen el mismo esquema de `CategoriaVehiculoController`:

- `controller/CategoriaVehiculoController.java`
- `controller/ExtraController.java`
- `controller/MarcaController.java`
- `controller/ModeloController.java`
- `controller/SucursalController.java`
- `controller/TipoUsuarioController.java`

### Patron comun
1. `@RequestMapping` con su ruta base.
2. Metodo `noEsAdmin(session)`.
3. `GET` listar.
4. `GET` nuevo.
5. `GET` editar/{id}.
6. `POST` guardar.
7. `GET` eliminar/{id}.

### Por que este patron esta bien elegido
- Es facil de mantener.
- Es consistente.
- Las vistas se parecen.
- Permite aprender MVC sin complejidad innecesaria.

### Diferencias entre ellos
- Cambia la entidad principal.
- Cambian los servicios usados.
- Algunos necesitan cargar relaciones extra (por ejemplo `ModeloController` necesita marcas).

---

## 17. `controller/SitemapController.java`

Este controlador no pertenece al negocio puro, sino al SEO.

### Lineas 34-69
Genera `sitemap.xml` dinamico.

### Que hace
- monta la URL base en runtime
- anade paginas publicas fijas
- anade ficha de cada vehiculo
- devuelve XML como string

### Para que sirve
Ayuda a buscadores a descubrir contenido indexable.

### Por que se elige
Es una solucion muy practica cuando las URLs dependen de la BD.  
En lugar de generar un XML manual y olvidarte de actualizarlo, se genera en tiempo real.

---

# Capa de servicio

## Idea general

La capa servicio esta entre controller y repository.

### Su funcion en este proyecto
- encapsular consultas
- aplicar reglas de negocio
- evitar que el controller hable “demasiado” con el repository

En algunos servicios la logica es muy simple, pero eso **no esta mal**.  
En arquitectura por capas, aunque un servicio hoy sea delgado, sigue siendo util porque:
- deja el diseño limpio
- permite crecer mañana
- evita acoplar el controlador a JPA

---

## 18. `service/UsuarioService.java`

### Lineas 11-17
Define `@Service` e inyecta `UsuarioRepository`.

### Lineas 18-49
Metodos tipicos:
- listar
- buscar por id
- buscar por email
- buscar por telefono
- buscar por dni
- guardar
- eliminar
- contar clientes

**Por que se elige**: separar consultas frecuentes en metodos con nombre claro.

---

## 19. `service/VehiculoService.java`

Este servicio tiene mas logica real.

### Lineas 22-32
Inyecta repositorio de vehiculos, reservas y mantenimientos.

### Lineas 33-52
CRUD basico.

### Lineas 54-99 - `calcularDisponibilidad(...)`
Esta es la parte importante.

#### Que hace
Calcula si un vehiculo esta:
- disponible
- alquilado
- en mantenimiento

y, si no esta disponible, **desde cuando volvera a estarlo**.

#### Como lo hace
1. Mira mantenimientos activos ahora.
2. Mira reservas activas ahora.
3. Decide cual bloquea mas tiempo.
4. Devuelve un `DisponibilidadVehiculo`.

#### Por que esta muy bien elegido
No confia ciegamente en un campo fijo en la tabla.  
La disponibilidad real se deduce de eventos temporales.

Eso es diseño de negocio correcto.

### Lineas 101-108 - `calcularDisponibilidades(...)`
Recorre lista de coches y devuelve un mapa por ID.

**Para que sirve**: la vista de listado puede pintar la disponibilidad de muchos coches de golpe.

---

## 20. `service/ReservaService.java`

### Lineas 21-54
CRUD y consultas tipicas.

### Lineas 33-42
Detecta solapamientos.

**Para que sirve**: impedir conflictos de agenda.

### Lineas 47-50
`listarUltimas(int n)` usa `PageRequest`.

**Por que se elige**: no tiene sentido traer todas las reservas para luego quedarte con 5.

### Lineas 56-93 - `actualizarEstadosAutomaticamente()`
Este metodo es muy didactico.

#### Regla 1
Si la fecha/hora de fin ya paso, la reserva pasa a `finalizada`.

#### Regla 2
Si la fecha/hora de inicio ya llego pero no ha acabado, pasa a `en_proceso`.

#### Por que se elige
Evita depender de tareas programadas o cron.  
Cada vez que listamos reservas, el sistema se autocorrige.

**Ventaja**: simple.  
**Inconveniente**: solo se actualiza cuando alguien accede a esa funcionalidad.

Como profesor, te diria que para un proyecto de DAM es una solucion muy razonable.

---

## 21. Servicios repetitivos por entidad

Estos servicios siguen un patron muy parecido a `UsuarioService`:

- `service/CategoriaVehiculoService.java`
- `service/ExtraService.java`
- `service/SucursalService.java`
- `service/MarcaService.java`
- `service/ModeloService.java`
- `service/TipoUsuarioService.java`
- `service/PagoService.java`
- `service/DevolucionService.java`
- `service/MantenimientoService.java`

### Patron comun
- `listarTodos()`
- `buscarPorId()`
- `guardar()`
- `eliminar()`
- algun metodo de consulta adicional segun necesidad

### Por que se elige este diseño
Porque cada entidad tiene su pequeña frontera de negocio. Aunque hoy sea simple, mañana puede crecer sin romper controladores.

### Casos con mas interes
- `PagoService`: filtra pagos por usuario/reserva.
- `DevolucionService`: probablemente sincroniza el estado del pago cuando hay devolucion.
- `MantenimientoService`: detecta solapamientos con ventanas temporales.

---

# Capa de persistencia: repositorios

## Idea general

Un `Repository` en Spring Data JPA es una interfaz que **no implementas manualmente** en la mayoria de casos.

### Por que esto es potente
Porque Spring genera la implementacion a partir de:
- heredar `JpaRepository`
- nombres de metodo (`findByEmail`, `findByDni`, etc.)
- consultas `@Query` si hace falta algo mas especifico

---

## 22. `repository/UsuarioRepository.java`

### Lineas 10-17
```java
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>
```
**Que hace**: hereda CRUD completo para `Usuario`.  
**Para que sirve**: no escribir SQL para operaciones basicas.  
**Por que se elige**: Spring Data JPA acelera mucho el desarrollo.

### Metodos
- `findByEmail`
- `findByTelefono`
- `findByDni`
- `countByTipoUsuarioNombre`

**Lo interesante** es que `countByTipoUsuarioNombre` navega una relacion.  
Eso demuestra el poder de Spring Data para derivar consultas a partir del nombre.

---

## 23. `repository/ReservaRepository.java`

### Linea 14
Extiende `JpaRepository<Reserva, Integer>`.

### Linea 15
`findByUsuarioId(Integer usuarioId)`  
Busca reservas de un usuario concreto.

### Linea 18
`findAllByOrderByFechaReservaDesc(Pageable pageable)`  
Trae las ultimas reservas.

### Lineas 20-26 - `@Query`
Consulta personalizada para reservas solapadas.

```java
AND r.estado <> 'cancelada'
AND r.fechaInicio <= :fechaFin AND r.fechaFin >= :fechaInicio
```

**Que hace**: aplica la logica clasica de solapamiento de rangos.  
**Por que se elige**: este caso ya no se resuelve bien solo con nombre de metodo.

---

## 24. Repositorios sencillos repetidos

Sigue el mismo patron el resto:

- `repository/CategoriaVehiculoRepository.java`
- `repository/MarcaRepository.java`
- `repository/ModeloRepository.java`
- `repository/VehiculoRepository.java`
- `repository/ExtraRepository.java`
- `repository/SucursalRepository.java`
- `repository/TipoUsuarioRepository.java`
- `repository/PagoRepository.java`
- `repository/DevolucionRepository.java`
- `repository/MantenimientoRepository.java`

### Lectura docente
Cuando veas una interfaz muy corta en Spring Data JPA, no pienses “esto es poco codigo”.  
Piensa: **estoy delegando trabajo repetitivo al framework**.

Eso es una buena practica, no una carencia.

---

# Modelo de dominio: entidades y DTO

## Idea general

Las entidades representan tablas y relaciones.

### Buenas decisiones que veo en este proyecto
- Validaciones Bean Validation cerca del dato.
- Relaciones JPA claras.
- `toString()` utiles para combos en Thymeleaf.
- Separacion entre entidades persistentes y DTO (`DisponibilidadVehiculo`).

---

## 25. `model/Usuario.java`

### Lineas 8-10
```java
@Entity
@Table(name = "usuarios")
public class Usuario {
```
**Que hace**: mapea la clase a la tabla `usuarios`.

### Lineas 11-13
ID autogenerado.

### Lineas 15-18
Campo `nombre` con validaciones.

**Por que se elige**: validacion declarativa y restriccion de longitud alineada con SQL.

### Lineas 20-26
Campo `apellidos`.

La regex:
```java
^[\p{L}'\-]+(?:\s+[\p{L}'\-]+)+$
```
permite letras Unicode, guiones, apostrofes y exige al menos dos apellidos.

**Esto esta muy bien hecho** porque acepta nombres reales del mundo hispano mejor que una regex simplona ASCII.

### Lineas 28-32
Email con `@Email`, `@NotBlank`, `@Size` y columna unica.

### Lineas 34-38
Telefono validado a 9 digitos empezando por 6 o 7.

### Lineas 40-44
DNI validado como 8 digitos + letra.

### Lineas 46-54
Direccion y password.

**Observacion de profesor**: la password valida longitud, pero no esta cifrada. Funciona para clase, pero en produccion se cifraria.

### Lineas 56-59
Ruta de foto del carnet.

**Buena eleccion**: el campo es opcional y largo suficiente para una ruta web.

### Lineas 61-68
Relacion `ManyToOne` con `TipoUsuario`.

**Interpretacion funcional**:
- muchos usuarios pueden ser `cliente`
- muchos usuarios pueden ser `admin`

### Lineas 69-85
Constructor vacio y constructor con parametros.

### Lineas 86-146
Getters y setters.

### Lineas 147-150
```java
public boolean isAdmin() {
    return tipoUsuario != null && "admin".equals(tipoUsuario.getNombre());
}
```
**Que hace**: encapsula la comprobacion de rol.  
**Para que sirve**: no repetir comparaciones de string por todo el proyecto.  
**Por que se elige**: mejora semantica y legibilidad.

### Lineas 151-154
`toString()` devuelve nombre y apellidos.  
**Util** en listas, combos y vistas.

---

## 26. `model/Vehiculo.java`

Esta entidad es rica porque un coche tiene muchas propiedades y relaciones.

### Lineas 19-22
`matricula` unica y obligatoria.

### Lineas 24-28
`anio` con rango 1990-2030.  
**Para que sirve**: evitar valores absurdos.

### Lineas 30-45
`color`, `plazas`, `puertas`.

### Lineas 47-65
`etiqueta`, `transmision`, `combustion` con regex.

**Por que se elige regex y no enum Java**:
- rapida de implementar
- encaja con valores string de la BD
- mas flexible en formularios Thymeleaf

Como profesor te diria: es valida, aunque con enum Java tendrias mas seguridad de tipos.

### Lineas 67-80
`kilometraje`, `precioDia`, `fianza`.

**Bien elegido** usar `BigDecimal` para dinero, nunca `double`.

### Lineas 82-83
`rutaFoto`.

### Lineas 85-92
Campo `disponibilidad` guardado en tabla.

**Ojo pedagogico**: luego ademas se calcula disponibilidad real.  
Esto quiere decir que hay una “disponibilidad administrativa” y una “disponibilidad operativa calculada”.

### Lineas 94-118
Relaciones con:
- `CategoriaVehiculo`
- `Modelo`
- `Sucursal`
- `Extra` (ManyToMany)

**Esto refleja bien el dominio**: un vehiculo pertenece a una categoria, a un modelo, a una sucursal y puede ofrecer varios extras.

### Lineas 121-232
Constructores, getters y setters.

### Lineas 233-236
`toString()` devuelve `modelo + " - " + matricula`.  
Muy util para combos administrativos.

---

## 27. `model/Reserva.java`

Es la entidad central del negocio.

### Campos temporales
- `fechaInicio`
- `horaInicio`
- `fechaFin`
- `horaFin`
- `fechaReserva`

**Muy buena decision** separar fecha y hora.  
Facilita formularios HTML5 y reglas de negocio detalladas.

### Campo `precioTotal`
Usa `BigDecimal` y no permite negativo.

### Campo `estado`
Es string, no enum Java.

### Relaciones
- `ManyToOne` con `Usuario`
- `ManyToOne` con `Vehiculo`
- `ManyToOne` con `SucursalRecogida`
- `ManyToOne` con `SucursalDevolucion`
- `ManyToMany` con `Extra`

**Esto modela muy bien una reserva real**.

### Metodos de negocio dentro de la entidad
#### `isEstaFinalizada()`
Calcula si ya deberia considerarse terminada por fecha/hora.

#### `isYaIniciada()`
Calcula si ya empezo.

**Por que se elige meter esto en la entidad**:  
porque son comportamientos naturales del propio objeto `Reserva`, no simples utilidades externas.

---

## 28. `model/Pago.java`

### Ideas fuertes
- Un pago pertenece a una unica reserva (`OneToOne`).
- Puede tener una devolucion asociada (`OneToOne` inversa).
- La fianza esta separada del importe del alquiler.

### Lineas 42-45
La relacion con `Reserva` es `OneToOne`.  
**Tiene sentido**: una reserva, un pago principal.

### Lineas 47-56
Relacion inversa con `Devolucion`.

El comentario es excelente desde el punto de vista docente:
- evita `cascade` y `orphanRemoval`
- explica el peligro de borrar la devolucion sin querer

Esto demuestra que el autor ya ha peleado con Hibernate de verdad.

---

## 29. `model/Devolucion.java`

### Que representa
El reembolso asociado a un pago.

### Por que no basta con un estado en `Pago`
Porque con una entidad propia puedes guardar:
- fecha
- importe reembolsado
- motivo
- relacion auditada con el pago

Eso es mejor modelado de dominio.

---

## 30. Entidades sencillas del dominio

### `model/TipoUsuario.java`
Tabla maestra de roles (`cliente`, `admin`).  
Simple a proposito.

### `model/CategoriaVehiculo.java`
Agrupa la flota por segmento (`SUV`, `Compacto`, etc.).  
Tiene `nombre` y `descripcion` porque no basta con una etiqueta visual.

### `model/Marca.java`
Tabla maestra de marcas.

### `model/Modelo.java`
Relaciona modelo con marca.  
Ejemplo perfecto de `ManyToOne`: muchos modelos pertenecen a una marca.

### `model/Extra.java`
Representa servicios opcionales de pago por dia.  
Tiene mucho sentido que sea entidad independiente porque se reutiliza en muchos coches y reservas.

### `model/Sucursal.java`
Representa ubicaciones fisicas.  
Valida nombre, direccion, ciudad, email y telefono.

### `model/Mantenimiento.java`
Modela periodos de indisponibilidad tecnica.  
Incluye el metodo `calcularEstadoPorFecha()` que clasifica:
- `pendiente`
- `en_proceso`
- `finalizado`

Eso esta muy bien porque el estado se deduce del tiempo real.

---

## 31. `dto/DisponibilidadVehiculo.java`

Esto no es entidad, es DTO.

### Para que sirve un DTO aqui
No quieres cargar en `Vehiculo` una logica/transporte que no pertenece directamente a su tabla.

### Que contiene
- `estado`
- `disponibleDesde`
- helpers para fecha y hora

### Por que se elige
Es un objeto de lectura ideal para pintar la UI sin contaminar la entidad con datos calculados temporales.

---

# Vistas Thymeleaf

## Idea general

Las plantillas Thymeleaf son la capa visual del proyecto.  
En lugar de hacer una SPA con React/Vue, se genera HTML desde servidor.

### Por que esta eleccion es buena en DAM
- Menos complejidad de frontend.
- MVC muy claro.
- Integracion directa con Spring.
- Ideal para practicar CRUD, sesiones, validacion y plantillas.

---

## 32. `templates/fragments/layout.html`

Este archivo es el mas grande del frontend y funciona como **sistema de diseño** del proyecto.

## Que contiene por bloques

### Lineas 5-54 aprox. - fragmento `head`
Incluye:
- metadatos SEO
- descripcion y keywords
- Open Graph
- Twitter Card
- favicon
- canonical
- JSON-LD
- imports de Bootstrap, Bootstrap Icons y Google Fonts

**Para que sirve**: centralizar el `<head>` de todas las paginas.  
**Por que se elige**: si cambias SEO o favicon, lo haces una sola vez.

### Lineas 72 en adelante - CSS comun
Define:
- variables de color
- layout cliente
- layout admin
- sidebar
- topbar
- cards
- tablas
- formularios
- responsive
- accesibilidad visual

**Para que sirve**: dar coherencia visual a toda la app.  
**Por que se elige**: mejor un layout compartido que 30 paginas con estilos sueltos.

### Fragmentos mas importantes del archivo
Aunque no te copie las 1200 lineas aqui, funcionalmente este archivo hace de:
- `head(...)`
- `navbar`
- `admin-sidebar`
- `footer`
- `scripts`

**Lectura de profesor**:  
esto es una aplicacion Thymeleaf bien planteada, porque usa fragmentos reutilizables y no duplica HTML estructural en cada vista.

---

## 33. `templates/index.html`

Es una plantilla dual.

### Si `esAdmin == true`
Muestra dashboard de admin:
- tarjetas de estadisticas
- accesos rapidos
- actividad reciente

### Si no
Muestra landing para cliente/visitante.

**Por que se elige**: una sola home para toda la app, con dos experiencias.  
**Ventaja**: navegacion sencilla.  
**Coste**: el HTML es mas grande y tiene condicionales.

---

## 34. Vistas de autenticacion

### `templates/auth/login.html`
Formulario de login.

### `templates/auth/registro.html`
Formulario de alta de cliente con subida opcional del carnet.

**Por que estan separadas**: login y registro son dos flujos distintos, aunque ambos pertenezcan a autenticacion.

---

## 35. Vistas de vehiculos

### `templates/vehiculos/lista.html`
Muestra la flota, disponibilidad y acciones segun rol.

### `templates/vehiculos/detalle.html`
Ficha publica de un vehiculo con opcion de reservar.

### `templates/vehiculos/formulario.html`
Formulario admin para alta/edicion con selects de categoria, marca, modelo, sucursal y extras.

**Por que este modulo es importante**: es el escaparate principal del negocio.

---

## 36. Vistas de reservas

### `templates/reservas/lista.html`
Admin ve todas; cliente solo las suyas.

### `templates/reservas/formulario.html`
Formulario con fechas, horas, extras y metodo de pago.

### `templates/reservas/cancelar.html`
Pantalla de confirmacion con politica de devolucion.

**Muy buena decision UX**: no cancelar “a ciegas”; primero se enseña resumen y dinero a devolver.

---

## 37. Vistas de pagos y devoluciones

### `templates/pagos/lista.html`
Backoffice de pagos.

### `templates/pagos/formulario.html`
Alta/edicion para admin.

### `templates/pagos/mis-pagos.html`
Vista del cliente con sus pagos.

### `templates/devoluciones/lista.html` y `formulario.html`
Gestion administrativa de reembolsos.

---

## 38. Vistas de usuario

### `templates/usuarios/perfil.html`
Muestra los datos del usuario autenticado y su foto de carnet si existe.

### `templates/usuarios/editar-perfil.html`
Permite a clientes actualizar ciertos campos y subir nuevo carnet.

### `templates/usuarios/lista.html`
Panel admin con todos los usuarios.

### `templates/usuarios/formulario.html`
Existe como plantilla, aunque el controlador de usuarios esta capado para alta/edicion manual.

---

## 39. Vistas CRUD repetitivas

Las carpetas:
- `categorias`
- `extras`
- `marcas`
- `modelos`
- `sucursales`
- `tipo-usuarios`
- `mantenimientos`

siguen un patron comun:
- `lista.html`
- `formulario.html`

**Por que se elige**:
- rapidez de desarrollo
- consistencia visual
- mantenimiento facil

---

# Base de datos y scripts SQL

## 40. `docs/ini_db.sql`

Este fichero crea toda la estructura.

### Que hace
- elimina y crea la BD `rentgo`
- crea tablas
- define PK
- define FK
- define relaciones N:M
- define enums SQL donde hace falta

### Lo importante didacticamente
- `usuarios` tiene `id_tipo` como FK a `tipo_usuarios`
- `vehiculos` depende de categoria, modelo y sucursal
- `reservas` une usuario, vehiculo y dos sucursales
- `pagos` tiene relacion 1:1 con `reservas`
- `devoluciones` tiene relacion 1:1 con `pagos`
- existen tablas intermedias `vehiculos_extras` y `reservas_extras`

### Por que se ha elegido `ddl-auto=validate`
Porque la estructura **no la crea Hibernate**: la crea este SQL.  
Hibernate solo verifica que el mapeo Java coincide.

Eso, en un proyecto academico serio, es una muy buena decision.

---

## 41. `docs/data.sql`

Este fichero mete datos de ejemplo.

### Que contiene por bloques
1. tipos de usuario
2. admin inicial
3. categorias
4. sucursales
5. extras
6. clientes con carnet
7. marcas
8. modelos
9. vehiculos con foto
10. reservas de ejemplo
11. pagos
12. devoluciones

### Por que es tan util
Permite tener una app lista para demo sin tener que crear datos a mano.

### Decisiones buenas que veo
- usuarios cliente ya tienen `ruta_foto_carnet`
- vehiculos tienen `ruta_foto`
- hay reservas, pagos y devoluciones de ejemplo para probar flujo completo

Esto hace que el proyecto sea demostrable desde el minuto 1.

---

# Por que esta arquitectura tiene sentido en DAM

## 42. Decisiones que yo defenderia en una exposicion

### 1. MVC clasico con Spring + Thymeleaf
Porque permite entender claramente separacion de responsabilidades.

### 2. JPA con entidades bien relacionadas
Porque el dominio del alquiler encaja muy bien con ORM y relaciones.

### 3. Servicios aunque algunos sean simples
Porque preparan el proyecto para crecer y mantienen limpio el controlador.

### 4. SQL manual + `ddl-auto=validate`
Porque da control real del esquema y evita cambios silenciosos.

### 5. DTO para disponibilidad
Porque no todo dato calculado debe vivir en la entidad persistente.

### 6. Sesion manual con `HttpSession`
Porque es simple y suficiente para un proyecto academico.  
No es lo mas profesional, pero es pedagogicamente muy claro.

### 7. Subida de imagenes al disco + fallback al classpath
Porque cubre tanto recursos seed como archivos subidos en runtime.  
Ademas ha demostrado ser util en despliegue Docker.

---

# Que te preguntaria yo en un examen de DAM

## 43. Preguntas tipicas con respuesta corta

### ¿Por que existe una capa `service` si algunos metodos solo llaman al repositorio?
Porque mantiene la arquitectura limpia y permite crecer sin acoplar controladores a persistencia.

### ¿Que diferencia hay entre `@Entity` y `@Repository`?
- `@Entity`: representa datos persistentes.
- `@Repository`: accede a la base de datos.

### ¿Por que `BigDecimal` para dinero?
Porque `double` introduce errores de precision.

### ¿Que ventaja tiene Thymeleaf frente a concatenar HTML en Java?
Separacion de responsabilidades y plantillas mas mantenibles.

### ¿Por que `Usuario.isAdmin()` es mejor que comparar el string en cada controlador?
Porque encapsula la logica y mejora legibilidad.

### ¿Por que `Devolucion` es una entidad y no solo un boolean en `Pago`?
Porque un reembolso necesita trazabilidad: fecha, importe y motivo.

### ¿Que problema resuelve `WebConfig`?
Servir imagenes desde disco y desde el classpath, en local y en servidor.

### ¿Que hace `@ControllerAdvice` en este proyecto?
Mete atributos globales (`requestUrl`, `requestUri`, `baseUrl`) en todas las vistas.

---

# Resumen final de profesor

Si yo tuviera que resumirte este proyecto en una sola idea, seria esta:

> `RentGo` no es solo un CRUD de tablas; es un proyecto donde ya hay **reglas de negocio reales**: disponibilidad temporal, solapamientos, pagos, reembolsos, mantenimientos, sesiones, roles, imagenes y SEO.

Eso es precisamente lo que hace que el codigo tenga valor academico:

- hay arquitectura
- hay negocio
- hay UI
- hay persistencia
- hay validacion
- hay despliegue

## Lo mejor del proyecto
- Separacion en capas.
- Buen uso de JPA y Thymeleaf.
- Reglas reales de reservas y cancelaciones.
- Manejo razonable de imagenes.
- SQL inicial bien estructurado.

## Lo que yo mejoraria en una version profesional
- Spring Security en lugar de `HttpSession` manual.
- Passwords cifradas.
- Mas tests.
- Algunos estados como enums Java.
- Posible separacion mayor entre logica de dominio y controladores grandes.

---

# Anexo: mapa rapido de archivos Java

## Config
- `RentgoApplication.java`
- `config/WebConfig.java`
- `config/GlobalModelAttributes.java`

## Controllers
- `controller/AuthController.java`
- `controller/HomeController.java`
- `controller/PerfilController.java`
- `controller/VehiculoController.java`
- `controller/ReservaController.java`
- `controller/PagoController.java`
- `controller/DevolucionController.java`
- `controller/MantenimientoController.java`
- `controller/UsuarioController.java`
- `controller/CategoriaVehiculoController.java`
- `controller/ExtraController.java`
- `controller/MarcaController.java`
- `controller/ModeloController.java`
- `controller/SucursalController.java`
- `controller/TipoUsuarioController.java`
- `controller/SitemapController.java`

## Services
- `service/UsuarioService.java`
- `service/VehiculoService.java`
- `service/ReservaService.java`
- `service/PagoService.java`
- `service/DevolucionService.java`
- `service/MantenimientoService.java`
- `service/CategoriaVehiculoService.java`
- `service/ExtraService.java`
- `service/MarcaService.java`
- `service/ModeloService.java`
- `service/SucursalService.java`
- `service/TipoUsuarioService.java`

## Repositories
- `repository/UsuarioRepository.java`
- `repository/VehiculoRepository.java`
- `repository/ReservaRepository.java`
- `repository/PagoRepository.java`
- `repository/DevolucionRepository.java`
- `repository/MantenimientoRepository.java`
- `repository/CategoriaVehiculoRepository.java`
- `repository/ExtraRepository.java`
- `repository/MarcaRepository.java`
- `repository/ModeloRepository.java`
- `repository/SucursalRepository.java`
- `repository/TipoUsuarioRepository.java`

## Model y DTO
- `model/Usuario.java`
- `model/Vehiculo.java`
- `model/Reserva.java`
- `model/Pago.java`
- `model/Devolucion.java`
- `model/Mantenimiento.java`
- `model/TipoUsuario.java`
- `model/CategoriaVehiculo.java`
- `model/Marca.java`
- `model/Modelo.java`
- `model/Extra.java`
- `model/Sucursal.java`
- `dto/DisponibilidadVehiculo.java`

---

Si quieres, en el siguiente paso te puedo preparar una **segunda parte todavia mas bestia**, archivo por archivo y vista por vista, incluyendo fragmentos HTML y explicacion de las plantillas Thymeleaf con el mismo estilo de profesor de DAM.

