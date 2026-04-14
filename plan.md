# Plan de base de datos y modelo ER - RentGo

## Problema y enfoque

En esta fase el objetivo no es implementar todavia la aplicacion MVC, sino cerrar el diseno de la base de datos para poder construir un modelo entidad-relacion coherente, defendible y facil de traducir despues a SQL y JPA.

El trabajo se centra en:

- definir las entidades del dominio
- cerrar las cardinalidades y relaciones
- decidir claves primarias y foraneas
- detectar restricciones de negocio
- dejar la base lista para pasar a modelo relacional

## Estado actual y decisiones ya tomadas

Entidades confirmadas para el modelo ER:

- `Cliente`
- `Vehiculo`
- `Reserva`
- `CategoriaVehiculo`
- `Pago`
- `Sucursal`
- `Mantenimiento`
- `Usuario`

Entidad descartada:

- `Tarifa`, porque su informacion pasa a integrarse en el modelo principal y no se mantiene como tabla independiente.

Relaciones ya cerradas:

- `Cliente` 1:N `Reserva`
- `Vehiculo` 1:N `Reserva`
- `CategoriaVehiculo` 1:N `Vehiculo`
- `Vehiculo` 1:N `Mantenimiento`
- `Sucursal` 1:N `Vehiculo`
- `Reserva` 1:1 `Pago`
- `Reserva` N:1 `Sucursal` como sucursal de recogida
- `Reserva` N:1 `Sucursal` como sucursal de devolucion
- `Usuario` 1:1 `Cliente` de forma opcional

## Propuesta de enfoque para el ER

### Nucleo minimo del modelo

El nucleo funcional sigue siendo:

- `Cliente`
- `Vehiculo`
- `Reserva`

Porque permite justificar el negocio principal de alquiler y cubrir el flujo esencial del sistema.

### Entidades de soporte

- `CategoriaVehiculo` para clasificar vehiculos
- `Sucursal` para ubicar vehiculos y aportar realismo
- `Pago` para registrar el abono de una reserva
- `Mantenimiento` para justificar indisponibilidades
- `Usuario` como entidad del sistema para distinguir perfiles `CLIENTE` y `ADMIN`

## Criterios de modelado

### Claves principales

Todas las tablas deberian usar una PK simple (`id`) para facilitar la traduccion posterior a JPA y las relaciones entre tablas.

### Restricciones esperables

- `Vehiculo.matricula` unica
- `Cliente.email` probablemente unico
- `Cliente.documentoIdentidad` unico
- fechas coherentes en `Reserva` y `Mantenimiento`
- importes no negativos en `Reserva` y `Pago`

### Regla de negocio clave

La restriccion mas importante del sistema es impedir reservas solapadas para un mismo vehiculo en un rango de fechas.

## Atributos finales propuestos para el modelo ER

### `Usuario`

- id
- nombreUsuario
- email
- password
- rol (`CLIENTE` o `ADMIN`)
- activo

### `Cliente`

- id
- nombre
- apellidos
- email
- telefono
- documentoIdentidad
- direccion
- fechaRegistro
- usuario_id opcional y unico

Nota de modelado: no todo `Usuario` tendra por que ser `Cliente`. Los usuarios con rol `ADMIN` existiran en `Usuario`, mientras que los usuarios con rol `CLIENTE` podran vincularse en relacion `1:1` con la tabla `Cliente`.

### `CategoriaVehiculo`

- id
- nombre
- descripcion
- precioPorDia

### `Sucursal`

- id
- nombre
- direccion
- ciudad
- telefono
- email

### `Vehiculo`

- id
- marca
- modelo
- matricula
- anio
- disponible
- color
- kilometraje
- categoria_vehiculo_id
- sucursal_id

### `Reserva`

- id
- fechaInicio
- fechaFin
- fechaReserva
- precioTotal
- estado
- observaciones
- cliente_id
- vehiculo_id
- sucursal_recogida_id
- sucursal_devolucion_id opcional

### `Pago`

- id
- fechaPago
- importe
- metodoPago
- estadoPago
- reserva_id
- referencia

### `Mantenimiento`

- id
- fechaInicio
- fechaFin
- motivo
- coste
- estado
- vehiculo_id

## Resumen de contenido por entidad

### `Usuario`

Representa las cuentas del sistema. Su funcion es diferenciar acceso y permisos entre perfiles `CLIENTE` y `ADMIN`.

### `Cliente`

Representa la informacion de negocio del cliente que realiza reservas. Puede estar vinculado a un `Usuario` si ese cliente accede al sistema.

### `CategoriaVehiculo`

Agrupa vehiculos por tipo o segmento para facilitar clasificacion y futuras busquedas.

### `Sucursal`

Representa los puntos fisicos donde se encuentran vehiculos y donde se recogen o devuelven reservas.

### `Vehiculo`

Representa cada coche disponible para alquiler con sus datos identificativos, comerciales y de estado.

### `Reserva`

Representa la operacion principal del negocio. Une cliente, vehiculo y sucursales con un rango de fechas y un estado.

### `Pago`

Representa el pago unico asociado a una reserva.

### `Mantenimiento`

Representa periodos de indisponibilidad tecnica o revisiones del vehiculo.

## Decisiones cerradas del ER

### `Reserva` - `Pago`

Se fija como `1:1`. Cada reserva tendra un unico pago asociado, lo que simplifica el modelo y encaja con la decision ya tomada para el ER.

### `Reserva` - `Sucursal`

Se fijan dos relaciones separadas con `Sucursal`:

- `sucursalRecogida`
- `sucursalDevolucion`

Esto hace el modelo mas realista y evita ambiguedades en la reserva.

### `Usuario` - `Cliente`

Se fija como `1:1` opcional desde `Cliente` hacia `Usuario`. Esto permite que:

- un `Usuario` con rol `CLIENTE` pueda estar asociado a un `Cliente`
- un `Usuario` con rol `ADMIN` no necesite registro en `Cliente`

Es una solucion coherente para diferenciar cuentas del sistema y datos de negocio.

## Claves y restricciones propuestas

### Claves primarias

Todas las entidades utilizaran una clave primaria simple:

- `Usuario(id)`
- `Cliente(id)`
- `CategoriaVehiculo(id)`
- `Sucursal(id)`
- `Vehiculo(id)`
- `Reserva(id)`
- `Pago(id)`
- `Mantenimiento(id)`

### Claves unicas

- `Usuario.nombreUsuario` unico
- `Usuario.email` unico
- `Cliente.email` unico
- `Cliente.documentoIdentidad` unico
- `Cliente.usuario_id` unico por la relacion `1:1` opcional
- `Vehiculo.matricula` unica
- `CategoriaVehiculo.nombre` unico
- `Pago.reserva_id` unico para garantizar la relacion `1:1` con `Reserva`
- `Pago.referencia` unica si se usa como identificador externo de pago

### Claves foraneas

- `Cliente.usuario_id` -> `Usuario.id`
- `Vehiculo.categoria_vehiculo_id` -> `CategoriaVehiculo.id`
- `Vehiculo.sucursal_id` -> `Sucursal.id`
- `Reserva.cliente_id` -> `Cliente.id`
- `Reserva.vehiculo_id` -> `Vehiculo.id`
- `Reserva.sucursal_recogida_id` -> `Sucursal.id`
- `Reserva.sucursal_devolucion_id` -> `Sucursal.id`
- `Pago.reserva_id` -> `Reserva.id`
- `Mantenimiento.vehiculo_id` -> `Vehiculo.id`

### Nulabilidad recomendada

- `Cliente.usuario_id` puede ser nulo
- `Reserva.sucursal_devolucion_id` puede ser nulo si inicialmente se quiere permitir misma sucursal o dejarlo pendiente de asignacion
- el resto de FK principales deberian ser obligatorias

### Restricciones de integridad recomendadas

- `Reserva.fechaInicio <= Reserva.fechaFin`
- `Mantenimiento.fechaInicio <= Mantenimiento.fechaFin`
- `Reserva.precioTotal >= 0`
- `Pago.importe >= 0`
- `Mantenimiento.coste >= 0`
- `Vehiculo.kilometraje >= 0`
- `CategoriaVehiculo.precioPorDia >= 0`
- `Usuario.rol` solo admite `CLIENTE` o `ADMIN`
- `Reserva.estado` debe limitarse a un conjunto controlado de valores
- `Pago.estadoPago` debe limitarse a un conjunto controlado de valores
- `Mantenimiento.estado` debe limitarse a un conjunto controlado de valores

### Politica recomendada de borrado

Para que el modelo sea seguro y coherente:

- no conviene borrar un `Cliente` con `Reserva` asociada
- no conviene borrar un `Vehiculo` con `Reserva` o `Mantenimiento` asociado
- no conviene borrar una `Reserva` si ya tiene `Pago`

Por tanto, la recomendacion general es usar borrado restringido en la mayoria de relaciones de negocio.

## Restricciones funcionales del modelo ER

- un vehiculo no puede tener reservas solapadas en fechas compatibles
- un vehiculo no deberia estar disponible para reserva durante un periodo de mantenimiento
- un usuario con rol `ADMIN` no necesita fila asociada en `Cliente`
- un usuario con rol `CLIENTE` puede tener asociada una unica fila en `Cliente`
- una reserva debe tener un unico pago asociado

## Orden de trabajo propuesto

1. Revisar atributos definitivos por entidad.
2. Definir PK, FK y restricciones.
3. Dibujar el ER definitivo.
4. Traducir el ER a modelo relacional.
5. Preparar el script SQL inicial.

## Todos

1. Revisar atributos definitivos de cada entidad.
2. Decidir claves primarias, claves unicas y claves foraneas.
3. Fijar restricciones de negocio y validaciones de datos.
4. Traducir el modelo ER a tablas relacionales.
5. Preparar el borrador del script SQL inicial.
6. Revisar que el modelo sea coherente y defendible para la entrega.

## Notas

- El plan de implementacion MVC puede retomarse despues, una vez el ER este cerrado.
- `Usuario` deja de ser opcional en el diseno del ER porque se ha decidido incluir perfiles `CLIENTE` y `ADMIN`.
- `Sucursal` y `CategoriaVehiculo` aportan mucho valor al ER con poca complejidad extra.
- Las relaciones antes dudosas ya quedan cerradas para evitar cambios al pasar a SQL o JPA.
- `Mantenimiento` tambien queda dentro del modelo final porque ayuda a justificar indisponibilidades no causadas por reservas.
- `Tarifa` queda eliminada del modelo. El atributo economico principal se mueve a `CategoriaVehiculo` para evitar una tabla extra y mantener una tarifa comun por categoria.
