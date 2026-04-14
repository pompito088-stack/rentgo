# 📋 AGENT.md — Memoria del Proyecto RentGo

> **⚠️ REGLA PRINCIPAL:** Antes de ejecutar cualquier tarea, el agente DEBE leer este archivo completo para tener contexto del proyecto, su estado actual y las reglas de trabajo.

---

## 🔒 Reglas de trabajo del agente

### Regla de commits obligatorios

**Por cada clase o archivo creado/modificado, se debe hacer un commit individual y subirlo al repositorio de GitHub.**

Esto significa:
- Si se crean 12 clases, se hacen **12 commits separados**, uno por cada clase.
- Cada commit debe tener un mensaje claro y profesional (ej: `feat: add Usuario entity`, `feat: add VehiculoController`).
- Después de cada commit se ejecuta `git push` para subir al repositorio remoto.
- No se acumulan cambios en un solo commit masivo.

### Regla de lectura previa

Antes de actuar ante cualquier petición del usuario:
1. Leer `AGENT.md` completo.
2. Comprobar el estado actual del proyecto.
3. Actuar en consecuencia.

---

## 🚗 Descripción del Proyecto

**RentGo** — Sistema de gestión de alquiler de coches.

**Concepto:** Sistema de reservas de vehículos.

**Funcionalidades principales:**
- Flota de vehículos
- Reservas por fechas
- Disponibilidad
- Clientes
- 👉 Excelente para lógica de fechas

---

## 🧾 Proyecto Transversal Final

**Ciclo:** Desarrollo de Aplicaciones Web (DAW) / Desarrollo de Aplicaciones Multiplataforma (DAM)  
**Curso:** 2.º DAM / DAW

### 📚 Módulos implicados
- Programación
- Bases de Datos
- Lenguaje de Marcas y SEO
- Sistemas Informáticos
- Entornos de Desarrollo

### 📅 Fechas clave
- **Fecha límite de entrega:** Domingo 26 de abril
- **Defensa del proyecto:** 27, 28 y 29 de abril

---

## 🧩 1. Descripción General del Proyecto

El alumno deberá desarrollar una **aplicación web monolítica completa**, integrando los conocimientos adquiridos durante el ciclo formativo.

La aplicación deberá simular un **sistema real de gestión**, pudiendo ser de temática libre (ej: gestión de clientes, reservas, productos, consultas médicas, etc.), siempre que cumpla con todos los requisitos técnicos exigidos.

El objetivo es evaluar la capacidad del alumno para **diseñar, desarrollar, documentar y desplegar** una aplicación funcional completa, alineada con un entorno profesional.

---

## 🏗️ 2. Requisitos Técnicos

### 🔹 2.1 Arquitectura
- Aplicación monolítica
- Uso del patrón **MVC** (Modelo – Vista – Controlador)
- Separación en capas:
  - Controladores (`@Controller`)
  - Servicios (`@Service`)
  - Repositorios (`@Repository`)

### 🔹 2.2 Backend (Programación)
- **Lenguaje:** Java
- **Framework:** Spring Boot
- **Persistencia:** Spring Data JPA
- **Funcionalidad mínima:**
  - CRUD completo de al menos una entidad principal
  - Implementación de:
    - Validaciones de datos
    - Manejo básico de errores

### 🔹 2.3 Frontend (Lenguaje de Marcas + SEO)
- **Motor de plantillas:** Thymeleaf
- **Framework CSS:** Bootstrap 5
- **Requisitos:**
  - Diseño responsive
  - Uso de HTML semántico
  - Inclusión de: `<header>`, `<main>`, `<section>`, `<article>`, `<footer>`
  - **SEO básico:**
    - Etiqueta `<title>`
    - `<meta description>`
    - Jerarquía correcta de encabezados (`h1`-`h6`)

### 🔹 2.4 Base de Datos
- **Motor:** MySQL
- **Requisitos mínimos:**
  - Mínimo 3 tablas relacionadas
  - Uso de claves primarias y foráneas
  - Al menos una relación 1:N
- **Entregables:**
  - Script de creación de base de datos (`.sql`)
  - Modelo Entidad-Relación (ER)
  - Modelo Relacional

### 🔹 2.5 Sistemas Informáticos (Despliegue)
- **Entorno:** Máquina virtual Linux (Ubuntu recomendado) o subdominio en Hostinger
- **Requisitos:**
  - Aplicación ejecutándose en local
  - **Contenedores:**
    - Aplicación Spring Boot
    - Base de datos MySQL

### 🔹 2.6 Entornos de Desarrollo
- **Repositorio en GitHub**
- **Requisitos:**
  - Mínimo 5 commits
  - Mensajes de commit claros y profesionales
  - Código versionado correctamente
- **Diagramas:**
  - Diagrama de clases (obligatorio)
  - Diagrama de secuencia (opcional, valorable)

---

## 🧠 3. Documentación Obligatoria

El alumno deberá entregar un documento técnico que incluya:
- Descripción del proyecto
- Tecnologías utilizadas
- Explicación de la arquitectura
- Instrucciones de despliegue
- Diagramas:
  - Clases
  - ER
  - (Opcional) Secuencia

---

## 📦 4. Entregables

- 🔗 Repositorio GitHub
- 🧾 Documento técnico (PDF o Markdown)
- 🗄️ Script SQL de la base de datos
- 🧠 Diagramas (clases y ER)
- 🐳 Archivos Docker (`Dockerfile` + `docker-compose`)
- 🖥️ Aplicación funcional

---

## 🎤 5. Defensa del Proyecto

El alumno deberá realizar una presentación oral del proyecto, donde deberá:
- Explicar la arquitectura de la aplicación
- Justificar decisiones técnicas
- Demostrar el funcionamiento
- Responder preguntas del profesorado

---

## 📊 6. Rúbrica de Evaluación

### 🧾 Programación (10 puntos)
| Criterio | Puntos |
|---|---|
| Arquitectura MVC | 2 pts |
| CRUD funcional | 2 pts |
| Uso correcto de Spring Boot | 2 pts |
| Calidad del código | 2 pts |
| Validaciones y lógica | 2 pts |

### 🧾 Base de Datos (10 puntos)
| Criterio | Puntos |
|---|---|
| Modelo ER | 2 pts |
| Modelo relacional | 2 pts |
| Relaciones correctas | 2 pts |
| Script SQL | 2 pts |
| Integración con la app | 2 pts |

### 🧾 Lenguaje de Marcas + SEO (10 puntos)
| Criterio | Puntos |
|---|---|
| HTML semántico | 2 pts |
| Diseño responsive | 2 pts |
| SEO básico | 2 pts |
| Formularios funcionales | 2 pts |
| Usabilidad | 2 pts |

### 🧾 Sistemas Informáticos (10 puntos)
| Criterio | Puntos |
|---|---|
| Entorno Linux | 2 pts |
| Dockerfile | 2 pts |
| Docker Compose | 2 pts |
| Ejecución funcional | 2 pts |
| Red Docker | 2 pts |

### 🧾 Entornos de Desarrollo (10 puntos)
| Criterio | Puntos |
|---|---|
| Repositorio GitHub | 2 pts |
| Commits de calidad | 2 pts |
| Diagrama de clases | 2 pts |
| Documentación | 2 pts |
| Uso de Git | 2 pts |

### ⭐ BONUS (+1 punto)
- Diagrama de secuencia
- Funcionalidades adicionales relevantes

---

## 🚀 7. Consideraciones Finales

Este proyecto representa una **simulación real de desarrollo profesional**, donde se evaluará no solo el resultado final, sino también:
- La organización del trabajo
- La calidad técnica
- La claridad en la documentación
- La capacidad de defensa del proyecto

### ⚠️ IMPORTANTE
- Entregas fuera de plazo podrán ser penalizadas
- El proyecto debe ser original
- Se valorará especialmente la calidad global y coherencia del sistema

> 💡 **Consejo estratégico:** No esperes al último sprint para montar Docker… eso siempre explota el domingo a las 23:58 😄

---

## 🗄️ Script SQL de la Base de Datos

```sql
DROP DATABASE IF EXISTS rentgo;
CREATE DATABASE rentgo;
USE rentgo;

CREATE TABLE tipo_usuarios(
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE usuarios(
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(80) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(9) NOT NULL UNIQUE,
    dni VARCHAR(9) NOT NULL UNIQUE,
    direccion VARCHAR(200) NOT NULL,
    password VARCHAR(255) NOT NULL,
    id_tipo INT NOT NULL DEFAULT 1,
    FOREIGN KEY (id_tipo) REFERENCES tipo_usuarios(id)
);

CREATE TABLE marcas(
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE modelos(
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    id_marca INT NOT NULL,
    FOREIGN KEY (id_marca) REFERENCES marcas(id)
);

CREATE TABLE categorias_vehiculos(
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200) NOT NULL
);

CREATE TABLE sucursales(
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    ciudad VARCHAR(80) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(9) NOT NULL UNIQUE
);

CREATE TABLE vehiculos(
    id INT PRIMARY KEY AUTO_INCREMENT,
    matricula VARCHAR(10) NOT NULL UNIQUE,
    anio INT NOT NULL,
    color VARCHAR(30) NOT NULL,
    kilometraje INT NOT NULL,
    precio_dia DECIMAL(10,2) NOT NULL,
    ruta_foto VARCHAR(255),
    id_categoria_vehiculo INT NOT NULL,
    id_modelo INT NOT NULL,
    id_sucursal INT NOT NULL,
    FOREIGN KEY (id_categoria_vehiculo) REFERENCES categorias_vehiculos(id),
    FOREIGN KEY (id_modelo) REFERENCES modelos(id),
    FOREIGN KEY (id_sucursal) REFERENCES sucursales(id)
);

CREATE TABLE extras(
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    precio_dia DECIMAL(10,2) NOT NULL
);

CREATE TABLE vehiculos_extras(
    id_vehiculo INT NOT NULL,
    id_extra INT NOT NULL,
    PRIMARY KEY (id_vehiculo, id_extra),
    FOREIGN KEY (id_vehiculo) REFERENCES vehiculos(id),
    FOREIGN KEY (id_extra) REFERENCES extras(id)
);

CREATE TABLE reservas(
    id INT PRIMARY KEY AUTO_INCREMENT,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    fecha_reserva DATE NOT NULL,
    precio_total DECIMAL(10,2) NOT NULL,
    estado ENUM('pendiente', 'confirmada', 'cancelada', 'finalizada') NOT NULL,
    observaciones VARCHAR(300),
    id_usuario INT NOT NULL,
    id_vehiculo INT NOT NULL,
    id_sucursal_recogida INT NOT NULL,
    id_sucursal_devolucion INT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    FOREIGN KEY (id_vehiculo) REFERENCES vehiculos(id),
    FOREIGN KEY (id_sucursal_recogida) REFERENCES sucursales(id),
    FOREIGN KEY (id_sucursal_devolucion) REFERENCES sucursales(id)
);

CREATE TABLE pagos(
    id INT PRIMARY KEY AUTO_INCREMENT,
    fecha_pago DATE NOT NULL,
    importe DECIMAL(10,2) NOT NULL,
    metodo_pago ENUM('tarjeta', 'transferencia', 'bizum', 'efectivo') NOT NULL,
    estado_pago ENUM('pendiente', 'pagado', 'rechazado') NOT NULL,
    id_reserva INT NOT NULL UNIQUE,
    FOREIGN KEY (id_reserva) REFERENCES reservas(id)
);

CREATE TABLE mantenimientos(
    id INT PRIMARY KEY AUTO_INCREMENT,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    motivo VARCHAR(200) NOT NULL,
    coste DECIMAL(10,2) NOT NULL,
    estado ENUM('pendiente', 'en_proceso', 'finalizado') NOT NULL,
    id_vehiculo INT NOT NULL,
    FOREIGN KEY (id_vehiculo) REFERENCES vehiculos(id)
);

-- Datos iniciales de tipos de usuario
INSERT INTO tipo_usuarios (nombre) VALUES ('cliente');
INSERT INTO tipo_usuarios (nombre) VALUES ('admin');
```

---

## 📌 Estado actual del proyecto

### Tecnologías
- **Java** + **Spring Boot 4.0.4**
- **Spring Data JPA** con **MySQL**
- **Thymeleaf** + **Bootstrap 5**
- `spring.jpa.hibernate.ddl-auto=validate` (la BD debe existir previamente)

### Entidades del modelo (12 tablas)
| Entidad | Tabla | Estado |
|---|---|---|
| TipoUsuario | `tipo_usuarios` | ✅ Completo |
| Usuario | `usuarios` | ✅ Completo |
| Marca | `marcas` | ✅ Completo |
| Modelo | `modelos` | ✅ Completo |
| CategoriaVehiculo | `categorias_vehiculos` | ✅ Completo |
| Sucursal | `sucursales` | ✅ Completo |
| Vehiculo | `vehiculos` | ✅ Completo |
| Extra | `extras` | ✅ Completo |
| VehiculoExtra | `vehiculos_extras` | Tabla intermedia (M:N via JPA) |
| Reserva | `reservas` | ✅ Completo |
| Pago | `pagos` | ✅ Completo |
| Mantenimiento | `mantenimientos` | ✅ Completo |

### Relaciones
| Relación | Tipo |
|---|---|
| TipoUsuario → Usuario | 1:N |
| Marca → Modelo | 1:N |
| Modelo → Vehículo | 1:N |
| CategoríaVehículo → Vehículo | 1:N |
| Sucursal → Vehículo | 1:N |
| Vehículo ↔ Extra | M:N (tabla intermedia `vehiculos_extras`) |
| Usuario → Reserva | 1:N |
| Vehículo → Reserva | 1:N |
| Sucursal → Reserva (recogida) | 1:N |
| Sucursal → Reserva (devolución) | 1:N |
| Reserva → Pago | 1:1 |
| Vehículo → Mantenimiento | 1:N |

### Archivos existentes
- `src/main/resources/application.properties` ✅
- `src/main/resources/templates/index.html` ✅
- `src/main/resources/templates/fragments/layout.html` ✅
- `src/main/resources/templates/vehiculos/lista.html` ✅
- `src/main/resources/templates/vehiculos/detalle.html` ✅
- `src/main/resources/templates/vehiculos/formulario.html` ✅
- `pom.xml` ✅

### Archivos Java creados (todas las entidades)

**Model:**
- `model/TipoUsuario.java` ✅
- `model/Usuario.java` ✅
- `model/Marca.java` ✅
- `model/Modelo.java` ✅
- `model/CategoriaVehiculo.java` ✅
- `model/Sucursal.java` ✅
- `model/Vehiculo.java` ✅
- `model/Extra.java` ✅
- `model/Reserva.java` ✅
- `model/Pago.java` ✅
- `model/Mantenimiento.java` ✅

**Repository:**
- `repository/TipoUsuarioRepository.java` ✅
- `repository/UsuarioRepository.java` ✅
- `repository/MarcaRepository.java` ✅
- `repository/ModeloRepository.java` ✅
- `repository/CategoriaVehiculoRepository.java` ✅
- `repository/SucursalRepository.java` ✅
- `repository/VehiculoRepository.java` ✅
- `repository/ExtraRepository.java` ✅
- `repository/ReservaRepository.java` ✅
- `repository/PagoRepository.java` ✅
- `repository/MantenimientoRepository.java` ✅

**Service:**
- `service/TipoUsuarioService.java` ✅
- `service/UsuarioService.java` ✅
- `service/MarcaService.java` ✅
- `service/ModeloService.java` ✅
- `service/CategoriaVehiculoService.java` ✅
- `service/SucursalService.java` ✅
- `service/VehiculoService.java` ✅
- `service/ExtraService.java` ✅
- `service/ReservaService.java` ✅
- `service/PagoService.java` ✅
- `service/MantenimientoService.java` ✅

**Controller:**
- `controller/HomeController.java` ✅
- `controller/AuthController.java` ✅ (login + registro + logout)
- `controller/TipoUsuarioController.java` ✅
- `controller/UsuarioController.java` ✅
- `controller/MarcaController.java` ✅
- `controller/ModeloController.java` ✅
- `controller/CategoriaVehiculoController.java` ✅
- `controller/SucursalController.java` ✅
- `controller/VehiculoController.java` ✅
- `controller/ExtraController.java` ✅
- `controller/ReservaController.java` ✅
- `controller/PagoController.java` ✅
- `controller/MantenimientoController.java` ✅

### Templates creados
- `templates/auth/login.html` ✅
- `templates/auth/registro.html` ✅
- `templates/index.html` ✅
- `templates/tipo-usuarios/lista.html` ✅
- `templates/tipo-usuarios/formulario.html` ✅
- `templates/usuarios/lista.html` ✅
- `templates/usuarios/formulario.html` ✅
- `templates/marcas/lista.html` ✅
- `templates/marcas/formulario.html` ✅
- `templates/modelos/lista.html` ✅
- `templates/modelos/formulario.html` ✅
- `templates/categorias/lista.html` ✅
- `templates/categorias/formulario.html` ✅
- `templates/sucursales/lista.html` ✅
- `templates/sucursales/formulario.html` ✅
- `templates/vehiculos/lista.html` ✅
- `templates/vehiculos/detalle.html` ✅
- `templates/vehiculos/formulario.html` ✅
- `templates/extras/lista.html` ✅
- `templates/extras/formulario.html` ✅
- `templates/reservas/lista.html` ✅
- `templates/reservas/formulario.html` ✅
- `templates/pagos/lista.html` ✅
- `templates/pagos/formulario.html` ✅
- `templates/mantenimientos/lista.html` ✅
- `templates/mantenimientos/formulario.html` ✅

---

## 📝 Historial de acciones del agente

| Fecha | Acción | Commit |
|---|---|---|
| 2026-04-10 | Creación de AGENT.md | 512828e |
| 2026-04-13 | Update pom.xml (JPA, MySQL, devtools) | efa0862 |
| 2026-04-13 | Configure application.properties | 5538bbc |
| 2026-04-13 | Add InitDB.sql | f0c467d |
| 2026-04-13 | Add TipoUsuario entity | 3c4d47a |
| 2026-04-13 | Add Usuario entity | 69a3d99 |
| 2026-04-13 | Add TipoUsuarioRepository | ea1e97d |
| 2026-04-13 | Add UsuarioRepository | 36f35c6 |
| 2026-04-13 | Add TipoUsuarioService | a3103e0 |
| 2026-04-13 | Add UsuarioService | 642d8d8 |
| 2026-04-13 | Add HomeController | 1a90e67 |
| 2026-04-13 | Add TipoUsuarioController | cbb5e11 |
| 2026-04-13 | Add UsuarioController | 1509962 |
| 2026-04-13 | Add index.html | 1f54135 |
| 2026-04-13 | Add tipo-usuarios/lista.html | c254a37 |
| 2026-04-13 | Add tipo-usuarios/formulario.html | 092da51 |
| 2026-04-13 | Add usuarios/lista.html | e1b0f99 |
| 2026-04-13 | Add usuarios/formulario.html | 48a4c26 |

---

## 🔜 Próximos pasos sugeridos

1. ~~Crear las entidades JPA restantes en `model/`.~~ ✅
2. ~~Crear repositorios, servicios y controladores para las entidades restantes.~~ ✅
3. ~~Crear plantillas Thymeleaf para las entidades restantes.~~ ✅
4. Crear `Dockerfile` y `docker-compose.yml`.
5. Generar diagrama de clases y diagrama ER.
6. Preparar documento técnico final.

