# Script para video demo de RentGo (2-3 minutos)

> Objetivo: que puedas grabar un video corto, claro y natural, explicando **BBDD + backend + frontend** mientras enseñas la aplicacion o el codigo en pantalla.

---

## Checklist antes de grabar

- [ ] Tener abierta la app funcionando
- [ ] Tener a mano `docs/modelo_relacional.png` o `docs/ini_db.sql`
- [ ] Tener abierto algun controlador o servicio del backend
- [ ] Tener abierta la home, la ficha de vehiculo y el formulario de reserva
- [ ] Preparar el zoom del editor para que se lea bien
- [ ] Cerrar pestañas innecesarias
- [ ] Si puedes, grabar con resolucion limpia y letra grande

---

## Estructura recomendada del video

Duracion total aproximada: **2 minutos y 20 segundos**  
Es una duracion muy buena porque no se hace largo y te deja respirar.

### Reparto de tiempos
- **0:00 - 0:20** → introduccion
- **0:20 - 0:50** → base de datos
- **0:50 - 1:30** → backend
- **1:30 - 2:10** → frontend
- **2:10 - 2:20** → cierre

Si luego hablas un poco mas despacio, te quedara cerca de **2:30**, que tambien esta perfecto.

---

# Guion principal: que digo + que muestro

## 0:00 - 0:20 · Introduccion

### Que digo
> Hola, este es mi proyecto `RentGo`, una aplicacion web de alquiler de coches desarrollada con Java, Spring Boot, Thymeleaf y MySQL.  
> El objetivo ha sido crear no solo un CRUD, sino una aplicacion con logica real de negocio, con reservas, pagos, devoluciones, mantenimientos y gestion de usuarios.

### Que muestro
- La home de la aplicacion o `index.html`
- O mejor: la app abierta en el navegador, para que desde el primer segundo se vea que funciona

### Consejo
No empieces enseñando codigo directamente. Queda mejor abrir con la app funcionando y luego bajar a las capas tecnicas.

---

## 0:20 - 0:50 · Base de datos (30 segundos)

### Que digo
> En la parte de base de datos he diseñado un modelo relacional donde las tablas principales son `usuarios`, `vehiculos`, `reservas`, `pagos`, `devoluciones`, `mantenimientos`, `sucursales`, `marcas`, `modelos` y `extras`.  
> Lo importante es que las relaciones reflejan el negocio real: por ejemplo, una reserva relaciona un usuario, un vehiculo y dos sucursales, y ademas un pago pertenece a una reserva y una devolucion pertenece a un pago.  
> Tambien he usado tablas intermedias para las relaciones muchos a muchos, como los extras de un vehiculo o de una reserva.

### Que muestro
Opcion 1, la mejor:
- `docs/modelo_relacional.png`

Opcion 2, si prefieres codigo:
- `docs/ini_db.sql`
- bajar un poco por:
  - `CREATE TABLE usuarios`
  - `CREATE TABLE vehiculos`
  - `CREATE TABLE reservas`
  - `CREATE TABLE pagos`
  - `CREATE TABLE devoluciones`

### Lo que debes senalar con el raton
- `id_tipo` en `usuarios`
- `id_vehiculo`, `id_usuario`, `id_sucursal_recogida`, `id_sucursal_devolucion` en `reservas`
- `id_reserva` en `pagos`
- `id_pago` en `devoluciones`

### Frase corta por si te atascas
> La base de datos esta pensada para que las relaciones no sean inventadas, sino coherentes con el flujo real de alquiler.

---

## 0:50 - 1:30 · Backend (40 segundos)

### Que digo
> En el backend he seguido una arquitectura por capas, separando controladores, servicios, repositorios y entidades JPA.  
> Los controladores gestionan las rutas, los servicios contienen la logica de negocio, y los repositorios acceden a la base de datos con Spring Data JPA.  
> La parte mas importante esta en las reservas, porque ahi valido que no se pueda reservar en el pasado, que no haya solapamientos con otras reservas, y que tampoco coincida con mantenimientos.  
> Ademas, cuando el cliente crea una reserva, el sistema calcula el precio total y genera automaticamente un pago asociado.

### Que muestro
Abre estos archivos en este orden:
1. `src/main/java/com/ilerna/rentgo/controller/ReservaController.java`
2. `src/main/java/com/ilerna/rentgo/service/ReservaService.java`
3. opcional: `src/main/java/com/ilerna/rentgo/service/VehiculoService.java`

### Donde parar el raton
En `ReservaController.java`:
- el metodo `guardar(...)`
- la parte donde valida fechas
- la parte donde comprueba solapamientos
- la parte donde crea el pago

En `ReservaService.java`:
- `existeSolapamiento(...)`
- `actualizarEstadosAutomaticamente()`

En `VehiculoService.java`:
- `calcularDisponibilidad(...)`

### Frases naturales para acompanar mientras señalas
> Aqui, por ejemplo, es donde valido las fechas.  
> Aqui compruebo que no se solape con otra reserva.  
> Y aqui es donde se crea el pago automaticamente cuando la reserva es nueva.

### Idea clave que debes transmitir
Tu backend **no solo guarda datos**, sino que aplica reglas de negocio.

---

## 1:30 - 2:10 · Frontend (40 segundos)

### Que digo
> En el frontend he utilizado Thymeleaf con Bootstrap para generar vistas dinamicas en servidor.  
> He diferenciado la experiencia de cliente y de administrador, por ejemplo en la pagina de inicio o en los listados.  
> Una parte que me parece importante es el formulario de reserva, porque no es solo visual: muestra fechas, extras, sucursal de devolucion, metodo de pago y un resumen del presupuesto.  
> Tambien he trabajado la parte visual de las imagenes y del perfil del usuario, incluyendo la foto del carnet y las imagenes de vehiculos.

### Que muestro
Haz este recorrido visual:
1. Home o dashboard
2. `Vehiculos` → listado o detalle
3. formulario de reserva
4. perfil de usuario con carnet

Si quieres enseñar algo de codigo, abre un segundo:
- `src/main/resources/templates/index.html`
- `src/main/resources/templates/vehiculos/lista.html`
- `src/main/resources/templates/reservas/formulario.html`
- `src/main/resources/templates/fragments/layout.html`

### Mejor demo visual
- Home
- Ficha de un coche
- Boton reservar
- Formulario de reserva
- Perfil del cliente con imagen de carnet

### Frases para decir mientras navegas
> Aqui se ve el catalogo de vehiculos.  
> Desde el detalle se puede pasar directamente a la reserva.  
> Y en el formulario ya aparece toda la informacion necesaria para simular el alquiler.

### Idea que debes remarcar
El frontend no esta aislado: esta conectado directamente con el backend mediante Thymeleaf y con datos reales de la base de datos.

---

## 2:10 - 2:20 · Cierre

### Que digo
> En resumen, RentGo es un proyecto full stack donde he trabajado base de datos, backend y frontend de forma conectada, aplicando arquitectura por capas y reglas reales de negocio.  
> Creo que lo mas importante es que no se queda en un CRUD simple, sino que refleja el funcionamiento real de un sistema de alquiler.

### Que muestro
- Vuelve a la home o a una vista general bonita de la app
- O deja la ficha del coche / dashboard mientras terminas

---

# Version todavia mas natural, para leer casi literal

Si quieres hablar sin pensar demasiado, puedes usar esto casi tal cual:

## Introduccion
> Hola, este es mi proyecto RentGo, una aplicacion web de alquiler de coches desarrollada con Java, Spring Boot, Thymeleaf y MySQL. El objetivo ha sido crear una aplicacion completa, no solo un CRUD, sino un sistema con reservas, pagos, devoluciones, mantenimientos y gestion de usuarios.

## BBDD
> En la base de datos he diseñado un modelo relacional con tablas como usuarios, vehiculos, reservas, pagos, devoluciones, mantenimientos y extras. Lo importante es que las relaciones reflejan el negocio real: una reserva une un usuario, un vehiculo y dos sucursales, y ademas cada pago pertenece a una reserva y cada devolucion pertenece a un pago.

## Backend
> En el backend he seguido una arquitectura por capas con controladores, servicios, repositorios y entidades. La parte mas importante esta en las reservas, porque ahi valido que no se pueda reservar en el pasado, que no haya solapamientos con otras reservas o mantenimientos, y tambien calculo el precio total. Ademas, cuando se guarda una reserva, se genera automaticamente un pago asociado.

## Frontend
> En el frontend he usado Thymeleaf con Bootstrap para generar vistas dinamicas. He diferenciado la parte de cliente y la de administrador, y he trabajado especialmente el catalogo de vehiculos, el formulario de reserva y el perfil del usuario, incluyendo imagenes de coches y foto del carnet.

## Cierre
> En resumen, es un proyecto donde he conectado base de datos, backend y frontend con logica de negocio real, y creo que eso es lo que le da mas valor frente a un CRUD basico.

---

# Version express de 2 minutos exactos

Si tu profesor quiere algo mas rapido, usa este reparto:

- **0:00 - 0:15** introduccion
- **0:15 - 0:40** BBDD
- **0:40 - 1:15** backend
- **1:15 - 1:50** frontend
- **1:50 - 2:00** cierre

## Texto express

### Intro
> Este es mi proyecto RentGo, una aplicacion web de alquiler de coches desarrollada con Java, Spring Boot, Thymeleaf y MySQL.

### BBDD
> La base de datos esta modelada con tablas como usuarios, vehiculos, reservas, pagos y devoluciones, con relaciones que reflejan el negocio real del alquiler.

### Backend
> En el backend he usado arquitectura por capas. La parte mas importante es la reserva, porque valida fechas, evita solapamientos, calcula el precio y genera un pago automaticamente.

### Frontend
> En el frontend he trabajado con Thymeleaf y Bootstrap, diferenciando la experiencia de cliente y admin, y creando vistas como el catalogo de vehiculos, el formulario de reserva y el perfil del usuario.

### Cierre
> En resumen, he integrado base de datos, backend y frontend en una aplicacion con logica real de negocio.

---

# Que ventanas te recomiendo abrir antes de grabar

## Opcion ideal
1. Navegador con la app
2. Imagen `docs/modelo_relacional.png`
3. `ReservaController.java`
4. `ReservaService.java`
5. `reservas/formulario.html`

## Orden de grabacion recomendado
1. Home
2. Modelo relacional
3. `ReservaController`
4. `ReservaService`
5. detalle vehiculo
6. formulario reserva
7. perfil con carnet
8. cierre en home

---

# Consejos para que quede mejor el video

## 1. Habla un poco mas despacio de lo normal
Si vas demasiado rapido, pareceras nervioso y ademas perderas claridad.

## 2. No leas como robot
Aunque tengas script, intenta sonar como si lo contaras.

## 3. No enseñes mucho codigo de golpe
Mejor enseñar:
- una vista bonita
- una parte concreta del controlador
- una parte concreta del SQL

## 4. Señala con el raton mientras hablas
Eso da sensacion de control y de que sabes donde esta cada cosa.

## 5. Si te equivocas en una frase, sigue
En un video corto no pasa nada si no suena perfecto. Lo importante es que suene claro y seguro.

---

# Frases salvavidas por si te bloqueas

- > Aqui lo importante no es solo guardar datos, sino aplicar reglas de negocio.
- > Esta parte conecta directamente la interfaz con la base de datos a traves del backend.
- > He intentado que el proyecto no se quede en un CRUD, sino que refleje un caso real.
- > En esta vista se ve como Thymeleaf renderiza datos reales del modelo enviado por Spring.
- > Esta relacion en la base de datos tiene sentido porque refleja el flujo real del alquiler.

---

# Cierre final recomendado

> Si quieres, el siguiente paso te lo puedo preparar tambien: un **guion literal palabra por palabra para leerlo mientras grabas**, o una **chuleta ultra corta de 30 segundos por bloque** para memorizarla mas facil.

