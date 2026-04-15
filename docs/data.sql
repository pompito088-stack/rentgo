USE rentgo;

-- =========================
-- TIPOS DE USUARIO
-- =========================
INSERT INTO tipo_usuarios (nombre) VALUES ('cliente');
INSERT INTO tipo_usuarios (nombre) VALUES ('admin');

-- =========================
-- USUARIO ADMINISTRADOR
-- (patron email: %@admin-%)
-- Contrasena: admin123
-- =========================
INSERT INTO usuarios (nombre, apellidos, email, telefono, dni, direccion, password, id_tipo)
VALUES ('Admin', 'Sistema RentGo', 'admin@admin-rentgo.com', '600000000', '00000000A', 'Oficina Central RentGo', 'admin123', 2);

-- =========================
-- CATEGORIAS DE VEHICULOS
-- =========================
INSERT INTO categorias_vehiculos (nombre, descripcion) VALUES
('Economico',  'Vehiculos pequenos y de bajo consumo, ideales para ciudad.'),
('Compacto',   'Vehiculos versatiles con buen equilibrio entre espacio y consumo.'),
('Berlina',    'Vehiculos comodos y elegantes para viajes largos y uso familiar.'),
('SUV',        'Vehiculos altos y espaciosos, adecuados para carretera y ciudad.'),
('Familiar',   'Vehiculos con gran maletero y espacio interior para familias.'),
('Deportivo',  'Vehiculos con enfoque en potencia, diseno y prestaciones.'),
('Furgoneta',  'Vehiculos amplios pensados para carga o grupos numerosos.'),
('Premium',    'Vehiculos de gama alta con acabados y equipamiento superior.');

-- =========================
-- EXTRAS DISPONIBLES
-- =========================
INSERT INTO extras (nombre, descripcion, precio_dia) VALUES
('GPS',                 'Sistema de navegacion incorporado para facilitar los desplazamientos.',      5.00),
('Silla infantil',      'Silla homologada para ninos pequenos.',                                      4.50),
('Conductor adicional', 'Permite registrar un segundo conductor autorizado.',                         7.00),
('Seguro premium',      'Cobertura premium con mejores condiciones frente a danos y asistencia.',    12.00),
('Cadenas para nieve',  'Juego de cadenas para circular en zonas de nieve.',                          6.00),
('WiFi portatil',       'Dispositivo de conexion a internet movil durante el alquiler.',              8.00),
('Portabicicletas',     'Soporte para transportar bicicletas de forma segura.',                       6.50),
('Asistencia premium',  'Servicio prioritario de asistencia en carretera.',                           9.00);

-- =========================
-- SUCURSALES (MADRID)
-- =========================
INSERT INTO sucursales (nombre, direccion, ciudad, email, telefono) VALUES
('RentGo Madrid Centro',        'Calle Gran Via 45',                          'Madrid', 'madrid.centro@rentgo.com',        '600111111'),
('RentGo Madrid Chamartin',     'Calle Agustin de Foxa 28',                   'Madrid', 'madrid.chamartin@rentgo.com',     '600222222'),
('RentGo Madrid Atocha',        'Glorieta del Emperador Carlos V 8',          'Madrid', 'madrid.atocha@rentgo.com',        '600333333'),
('RentGo Madrid Barajas',       'Avenida de Logrono 120',                     'Madrid', 'madrid.barajas@rentgo.com',       '600444444'),
('RentGo Madrid Plaza Castilla','Paseo de la Castellana 189',                 'Madrid', 'madrid.plazacastilla@rentgo.com', '600555555');

