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

CREATE TABLE extras(
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    precio_dia DECIMAL(10,2) NOT NULL
);

CREATE TABLE vehiculos(
    id INT PRIMARY KEY AUTO_INCREMENT,
    matricula VARCHAR(10) NOT NULL UNIQUE,
    anio INT NOT NULL,
    color VARCHAR(30) NOT NULL,
    plazas TINYINT UNSIGNED NOT NULL,
    puertas TINYINT UNSIGNED NOT NULL,
    etiqueta ENUM('sin_etiqueta', 'B', 'C', 'ECO', 'CERO') NOT NULL,
    transmision ENUM('manual', 'automatica') NOT NULL,
    combustion ENUM('gasolina', 'diesel', 'hibrido_gasolina', 'hibrido_diesel',
                    'hibrido_enchufable_gasolina', 'hibrido_enchufable_diesel',
                    'electrico', 'glp', 'gnc', 'hidrogeno') NOT NULL,
    kilometraje INT NOT NULL,
    precio_dia DECIMAL(10,2) NOT NULL,
    fianza DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    ruta_foto VARCHAR(255),
    disponibilidad ENUM('disponible', 'alquilado', 'mantenimiento') NOT NULL DEFAULT 'disponible',
    id_categoria_vehiculo INT NOT NULL,
    id_modelo INT NOT NULL,
    id_sucursal INT NOT NULL,
    FOREIGN KEY (id_categoria_vehiculo) REFERENCES categorias_vehiculos(id),
    FOREIGN KEY (id_modelo) REFERENCES modelos(id),
    FOREIGN KEY (id_sucursal) REFERENCES sucursales(id)
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
    hora_inicio TIME NOT NULL DEFAULT '09:00:00',
    fecha_fin DATE NOT NULL,
    hora_fin TIME NOT NULL DEFAULT '09:00:00',
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

-- Tabla N:M entre reservas y extras elegidos al reservar
CREATE TABLE reservas_extras(
    id_reserva INT NOT NULL,
    id_extra INT NOT NULL,
    PRIMARY KEY (id_reserva, id_extra),
    FOREIGN KEY (id_reserva) REFERENCES reservas(id),
    FOREIGN KEY (id_extra) REFERENCES extras(id)
);

CREATE TABLE pagos(
    id INT PRIMARY KEY AUTO_INCREMENT,
    fecha_pago DATE NOT NULL,
    importe DECIMAL(10,2) NOT NULL,
    fianza DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    metodo_pago ENUM('tarjeta_credito', 'tarjeta_debito', 'paypal') NOT NULL,
    estado_pago VARCHAR(20) NOT NULL DEFAULT 'realizado',
    id_reserva INT NOT NULL UNIQUE,
    FOREIGN KEY (id_reserva) REFERENCES reservas(id)
);

CREATE TABLE mantenimientos(
    id INT PRIMARY KEY AUTO_INCREMENT,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    motivo VARCHAR(200) NOT NULL,
    coste DECIMAL(10,2) NOT NULL,
    estado ENUM('pendiente', 'en_proceso', 'finalizado') NOT NULL DEFAULT 'pendiente',
    id_vehiculo INT NOT NULL,
    FOREIGN KEY (id_vehiculo) REFERENCES vehiculos(id)
);

-- Datos iniciales de tipos de usuario
INSERT INTO tipo_usuarios (nombre) VALUES ('cliente');
INSERT INTO tipo_usuarios (nombre) VALUES ('admin');

-- Usuario administrador del sistema (email con patron %@admin-%)
-- Contrasena: admin123
INSERT INTO usuarios (nombre, apellidos, email, telefono, dni, direccion, password, id_tipo)
VALUES ('Admin', 'Sistema RentGo', 'admin@admin-rentgo.com', '600000000', '00000000A', 'Oficina Central RentGo', 'admin123', 2);
