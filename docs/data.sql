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

-- =========================
-- MARCAS
-- =========================
INSERT INTO marcas (nombre) VALUES
('Toyota'),
('Volkswagen'),
('BMW'),
('Seat'),
('Renault'),
('Ford'),
('Mercedes-Benz'),
('Hyundai');

-- =========================
-- MODELOS
-- (id_marca: 1=Toyota, 2=VW, 3=BMW, 4=Seat, 5=Renault, 6=Ford, 7=Mercedes, 8=Hyundai)
-- =========================
INSERT INTO modelos (nombre, id_marca) VALUES
('Corolla Cross',  1),  -- id 1
('Yaris',          1),  -- id 2
('Golf',           2),  -- id 3
('Polo',           2),  -- id 4
('Serie 3',        3),  -- id 5
('X5',             3),  -- id 6
('Ibiza',          4),  -- id 7
('Ateca',          4),  -- id 8
('Clio',           5),  -- id 9
('Captur',         5),  -- id 10
('Focus',          6),  -- id 11
('Puma',           6),  -- id 12
('Clase C',        7),  -- id 13
('GLC',            7),  -- id 14
('Tucson',         8),  -- id 15
('i20',            8);  -- id 16

-- ====================================================
-- VEHICULOS
-- NOTA: Las imagenes se guardan en static/img/vehiculos/
--       Sube las fotos manualmente desde el panel admin
--       o mediante phpMyAdmin. ruta_foto = NULL si no hay foto.
-- Categorias: 1=Economico,2=Compacto,3=Berlina,4=SUV,
--             5=Familiar,6=Deportivo,7=Furgoneta,8=Premium
-- Sucursales: 1=Centro,2=Chamartin,3=Atocha,4=Barajas,5=PlCastilla
-- ====================================================
INSERT INTO vehiculos
    (matricula, anio, color, plazas, puertas, etiqueta, transmision, combustion, kilometraje, precio_dia, ruta_foto, disponibilidad, id_categoria_vehiculo, id_modelo, id_sucursal)
VALUES
-- Toyota Corolla Cross (SUV, hibrido)
('1234ABC', 2023, 'Negro',  5, 5, 'ECO',          'automatica', 'hibrido_gasolina',           12000, 65.00, NULL, 'disponible', 4, 1, 1),
-- Toyota Yaris (Economico, gasolina)
('2345BCD', 2022, 'Blanco', 5, 5, 'ECO',          'manual',     'gasolina',                   25000, 38.00, NULL, 'disponible', 1, 2, 2),
-- Volkswagen Golf (Compacto, gasolina)
('3456CDE', 2023, 'Gris',   5, 5, 'C',            'manual',     'gasolina',                   18000, 45.00, NULL, 'disponible', 2, 3, 1),
-- Volkswagen Polo (Economico, gasolina)
('4567DEF', 2021, 'Rojo',   5, 5, 'C',            'manual',     'gasolina',                   40000, 35.00, NULL, 'disponible', 1, 4, 3),
-- BMW Serie 3 (Premium, diesel)
('5678EFG', 2024, 'Azul',   5, 4, 'B',            'automatica', 'diesel',                      5000, 95.00, NULL, 'disponible', 8, 5, 5),
-- BMW X5 (SUV Premium, hibrido enchufable)
('6789FGH', 2023, 'Plata',  7, 5, 'CERO',         'automatica', 'hibrido_enchufable_gasolina', 8000,130.00, NULL, 'disponible', 8, 6, 5),
-- Seat Ibiza (Economico, gasolina)
('7890GHI', 2022, 'Blanco', 5, 5, 'C',            'manual',     'gasolina',                   30000, 32.00, NULL, 'disponible', 1, 7, 2),
-- Seat Ateca (SUV, gasolina)
('8901HIJ', 2023, 'Gris',   5, 5, 'C',            'manual',     'gasolina',                   22000, 58.00, NULL, 'disponible', 4, 8, 3),
-- Renault Clio (Economico, hibrido)
('9012IJK', 2023, 'Naranja',5, 5, 'ECO',          'automatica', 'hibrido_gasolina',            9000, 40.00, NULL, 'disponible', 1, 9, 4),
-- Renault Captur (Compacto SUV, hibrido enchufable)
('0123JKL', 2024, 'Verde',  5, 5, 'CERO',         'automatica', 'hibrido_enchufable_gasolina', 3000, 55.00, NULL, 'disponible', 2,10, 4),
-- Ford Focus (Compacto, diesel)
('1234KLM', 2021, 'Negro',  5, 5, 'B',            'manual',     'diesel',                     50000, 42.00, NULL, 'disponible', 2,11, 1),
-- Ford Puma (Compacto SUV, gasolina)
('2345LMN', 2022, 'Azul',   5, 5, 'ECO',          'automatica', 'gasolina',                   28000, 50.00, NULL, 'disponible', 2,12, 2),
-- Mercedes Clase C (Berlina, diesel)
('3456MNO', 2024, 'Blanco', 5, 4, 'B',            'automatica', 'diesel',                      6000,110.00, NULL, 'disponible', 8,13, 5),
-- Mercedes GLC (SUV Premium, electrico)
('4567NOP', 2024, 'Plata',  5, 5, 'CERO',         'automatica', 'electrico',                   4000,140.00, NULL, 'disponible', 8,14, 5),
-- Hyundai Tucson (SUV, hibrido)
('5678OPQ', 2023, 'Gris',   5, 5, 'ECO',          'automatica', 'hibrido_diesel',             15000, 62.00, NULL, 'disponible', 4,15, 3),
-- Hyundai i20 (Economico, gasolina)
('6789PQR', 2021, 'Rojo',   5, 5, 'C',            'manual',     'gasolina',                   45000, 30.00, NULL, 'disponible', 1,16, 4);
