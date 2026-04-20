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
-- SUCURSALES (MADRID + OTRAS CIUDADES Y AEROPUERTOS)
-- =========================
INSERT INTO sucursales (nombre, direccion, ciudad, email, telefono) VALUES
-- Madrid
('RentGo Madrid Centro',            'Calle Gran Via 45',                          'Madrid',    'madrid.centro@rentgo.com',            '600111111'),
('RentGo Madrid Chamartin',         'Calle Agustin de Foxa 28',                   'Madrid',    'madrid.chamartin@rentgo.com',         '600222222'),
('RentGo Madrid Atocha',            'Glorieta del Emperador Carlos V 8',          'Madrid',    'madrid.atocha@rentgo.com',            '600333333'),
('RentGo Madrid Barajas T1',        'Avenida de Logrono 120 - Terminal 1',        'Madrid',    'madrid.barajas.t1@rentgo.com',        '600444444'),
('RentGo Madrid Barajas T4',        'Avenida de Logrono 120 - Terminal 4',        'Madrid',    'madrid.barajas.t4@rentgo.com',        '600444445'),
('RentGo Madrid Plaza Castilla',    'Paseo de la Castellana 189',                 'Madrid',    'madrid.plazacastilla@rentgo.com',     '600555555'),
-- Barcelona
('RentGo Barcelona Centro',         'Paseo de Gracia 92',                         'Barcelona', 'barcelona.centro@rentgo.com',         '610111111'),
('RentGo Barcelona El Prat T1',     'Aeropuerto El Prat - Terminal 1',            'Barcelona', 'barcelona.elprat.t1@rentgo.com',      '610222222'),
('RentGo Barcelona El Prat T2',     'Aeropuerto El Prat - Terminal 2',            'Barcelona', 'barcelona.elprat.t2@rentgo.com',      '610333333'),
-- Valencia
('RentGo Valencia Centro',          'Calle Colon 50',                             'Valencia',  'valencia.centro@rentgo.com',          '620111111'),
('RentGo Valencia Aeropuerto',      'Aeropuerto de Valencia - Acceso Principal',  'Valencia',  'valencia.aeropuerto@rentgo.com',      '620222222'),
-- Sevilla
('RentGo Sevilla Centro',           'Avenida de la Constitucion 12',              'Sevilla',   'sevilla.centro@rentgo.com',           '630111111'),
('RentGo Sevilla Aeropuerto',       'Aeropuerto de Sevilla - Terminal',           'Sevilla',   'sevilla.aeropuerto@rentgo.com',       '630222222'),
-- Bilbao
('RentGo Bilbao Aeropuerto',        'Aeropuerto de Bilbao - Loiu',                'Bilbao',    'bilbao.aeropuerto@rentgo.com',        '640111111'),
-- Malaga
('RentGo Malaga Aeropuerto',        'Aeropuerto Costa del Sol - Terminal',        'Malaga',    'malaga.aeropuerto@rentgo.com',        '650111111');

-- =========================
-- CLIENTES DE EJEMPLO
-- (contrasena de todos: cliente123)
-- =========================
INSERT INTO usuarios (nombre, apellidos, email, telefono, dni, direccion, password, id_tipo) VALUES
('Carlos',    'Garcia Lopez',      'carlos.garcia@gmail.com',    '612345678', '12345678A', 'Calle Alcala 10, Madrid',           'cliente123', 1),
('Maria',     'Martinez Ruiz',     'maria.martinez@gmail.com',   '623456789', '23456789B', 'Calle Serrano 22, Madrid',          'cliente123', 1),
('Juan',      'Rodriguez Sanchez', 'juan.rodriguez@hotmail.com', '634567890', '34567890C', 'Avenida Diagonal 55, Barcelona',    'cliente123', 1),
('Laura',     'Fernandez Gil',     'laura.fernandez@gmail.com',  '645678901', '45678901D', 'Calle Colon 8, Valencia',           'cliente123', 1),
('Antonio',   'Lopez Moreno',      'antonio.lopez@outlook.com',  '656789012', '56789012E', 'Avenida Constitucion 3, Sevilla',   'cliente123', 1),
('Sofia',     'Perez Torres',      'sofia.perez@gmail.com',      '667890123', '67890123F', 'Gran Via 77, Bilbao',               'cliente123', 1),
('Pablo',     'Sanchez Jimenez',   'pablo.sanchez@gmail.com',    '678901234', '78901234G', 'Paseo Maritimo 14, Malaga',         'cliente123', 1),
('Elena',     'Gomez Vargas',      'elena.gomez@hotmail.com',    '689012345', '89012345H', 'Calle Mayor 31, Madrid',            'cliente123', 1);
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
--       Sube las fotos manualmente desde el panel admin.
--       ruta_foto = NULL si no hay foto.
-- fianza: deposito en euros que se devuelve al entregar el coche.
-- Categorias: 1=Economico,2=Compacto,3=Berlina,4=SUV,
--             5=Familiar,6=Deportivo,7=Furgoneta,8=Premium
-- Sucursales: 1=MadCentro,2=Chamartin,3=Atocha,4=BarajasT1,
--             5=BarajasT4,6=PlCastilla,7=BcrCentro,...
-- ====================================================
INSERT INTO vehiculos
    (matricula, anio, color, plazas, puertas, etiqueta, transmision, combustion, kilometraje, precio_dia, fianza, ruta_foto, disponibilidad, id_categoria_vehiculo, id_modelo, id_sucursal)
VALUES
-- Toyota Corolla Cross (SUV, hibrido)       fianza 300
('1234ABC', 2023, 'Negro',  5, 5, 'ECO',  'automatica', 'hibrido_gasolina',            12000,  65.00, 300.00, NULL, 'disponible', 4,  1, 1),
-- Toyota Yaris (Economico, gasolina)         fianza 150
('2345BCD', 2022, 'Blanco', 5, 5, 'ECO',  'manual',     'gasolina',                    25000,  38.00, 150.00, NULL, 'disponible', 1,  2, 2),
-- Volkswagen Golf (Compacto, gasolina)       fianza 200
('3456CDE', 2023, 'Gris',   5, 5, 'C',    'manual',     'gasolina',                    18000,  45.00, 200.00, NULL, 'disponible', 2,  3, 1),
-- Volkswagen Polo (Economico, gasolina)      fianza 150
('4567DEF', 2021, 'Rojo',   5, 5, 'C',    'manual',     'gasolina',                    40000,  35.00, 150.00, NULL, 'disponible', 1,  4, 3),
-- BMW Serie 3 (Premium, diesel)              fianza 600
('5678EFG', 2024, 'Azul',   5, 4, 'B',    'automatica', 'diesel',                       5000,  95.00, 600.00, NULL, 'disponible', 8,  5, 6),
-- BMW X5 (SUV Premium, hibrido enchufable)   fianza 800
('6789FGH', 2023, 'Plata',  7, 5, 'CERO', 'automatica', 'hibrido_enchufable_gasolina',  8000, 130.00, 800.00, NULL, 'disponible', 8,  6, 6),
-- Seat Ibiza (Economico, gasolina)           fianza 150
('7890GHI', 2022, 'Blanco', 5, 5, 'C',    'manual',     'gasolina',                    30000,  32.00, 150.00, NULL, 'disponible', 1,  7, 2),
-- Seat Ateca (SUV, gasolina)                 fianza 300
('8901HIJ', 2023, 'Gris',   5, 5, 'C',    'manual',     'gasolina',                    22000,  58.00, 300.00, NULL, 'disponible', 4,  8, 4),
-- Renault Clio (Economico, hibrido)          fianza 150
('9012IJK', 2023, 'Naranja',5, 5, 'ECO',  'automatica', 'hibrido_gasolina',             9000,  40.00, 150.00, NULL, 'disponible', 1,  9, 5),
-- Renault Captur (Compacto SUV, hibrido enchufable) fianza 250
('0123JKL', 2024, 'Verde',  5, 5, 'CERO', 'automatica', 'hibrido_enchufable_gasolina',  3000,  55.00, 250.00, NULL, 'disponible', 2, 10, 4),
-- Ford Focus (Compacto, diesel)              fianza 200
('1234KLM', 2021, 'Negro',  5, 5, 'B',    'manual',     'diesel',                      50000,  42.00, 200.00, NULL, 'disponible', 2, 11, 1),
-- Ford Puma (Compacto SUV, gasolina)         fianza 200
('2345LMN', 2022, 'Azul',   5, 5, 'ECO',  'automatica', 'gasolina',                    28000,  50.00, 200.00, NULL, 'disponible', 2, 12, 2),
-- Mercedes Clase C (Berlina, diesel)         fianza 700
('3456MNO', 2024, 'Blanco', 5, 4, 'B',    'automatica', 'diesel',                       6000, 110.00, 700.00, NULL, 'disponible', 8, 13, 6),
-- Mercedes GLC (SUV Premium, electrico)      fianza 900
('4567NOP', 2024, 'Plata',  5, 5, 'CERO', 'automatica', 'electrico',                    4000, 140.00, 900.00, NULL, 'disponible', 8, 14, 6),
-- Hyundai Tucson (SUV, hibrido)              fianza 300
('5678OPQ', 2023, 'Gris',   5, 5, 'ECO',  'automatica', 'hibrido_diesel',              15000,  62.00, 300.00, NULL, 'disponible', 4, 15, 4),
-- Hyundai i20 (Economico, gasolina)          fianza 120
('6789PQR', 2021, 'Rojo',   5, 5, 'C',    'manual',     'gasolina',                    45000,  30.00, 120.00, NULL, 'disponible', 1, 16, 5);

-- ============================================================
-- MARCAS ADICIONALES
-- IDs continuarán desde 9 en adelante
-- ============================================================
INSERT INTO marcas (nombre) VALUES
('Audi'),         -- 9
('Kia'),          -- 10
('Peugeot'),      -- 11
('Volvo'),        -- 12
('Tesla'),        -- 13
('Fiat'),         -- 14
('Mazda'),        -- 15
('Nissan'),       -- 16
('Opel'),         -- 17
('Skoda');        -- 18

-- ============================================================
-- MODELOS ADICIONALES
-- ============================================================
INSERT INTO modelos (nombre, id_marca) VALUES
-- Audi (id_marca=9)
('A3',           9),   -- 17
('Q5',           9),   -- 18
-- Kia (id_marca=10)
('Sportage',    10),   -- 19
('Ceed',        10),   -- 20
-- Peugeot (id_marca=11)
('208',         11),   -- 21
('3008',        11),   -- 22
-- Volvo (id_marca=12)
('XC40',        12),   -- 23
('V60',         12),   -- 24
-- Tesla (id_marca=13)
('Model 3',     13),   -- 25
('Model Y',     13),   -- 26
-- Fiat (id_marca=14)
('500',         14),   -- 27
('Tipo',        14),   -- 28
-- Mazda (id_marca=15)
('CX-5',        15),   -- 29
('3',           15),   -- 30
('MX-5',        15),   -- 31  ← Deportivo
-- Nissan (id_marca=16)
('Qashqai',     16),   -- 32
('Leaf',        16),   -- 33
-- Opel (id_marca=17)
('Astra',       17),   -- 34
('Mokka',       17),   -- 35
-- Skoda (id_marca=18)
('Octavia',     18),   -- 36
('Kamiq',       18),   -- 37
-- Volkswagen (id_marca=2) – modelo adicional para Furgoneta
('Transporter',  2);   -- 38  ← Furgoneta

-- ============================================================
-- VEHICULOS ADICIONALES
-- Comentario por vehículo:
--   [Categoría · Combustible · Sucursal de recogida · precio_dia+fianza]
-- Categorias: 1=Economico,2=Compacto,3=Berlina,4=SUV,
--             5=Familiar,6=Deportivo,7=Furgoneta,8=Premium
-- ============================================================
INSERT INTO vehiculos
    (matricula, anio, color, plazas, puertas, etiqueta, transmision, combustion,
     kilometraje, precio_dia, fianza, ruta_foto, disponibilidad,
     id_categoria_vehiculo, id_modelo, id_sucursal)
VALUES
-- ── AUDI A3  [Compacto · diesel · Madrid Chamartin · 418,00 € (68+350)] ──────────────
('7890RST', 2022, 'Gris Nardo',    5, 4, 'B',    'automatica', 'diesel',
  22000,  68.00, 350.00, NULL, 'disponible', 2, 17, 2),
-- ── AUDI Q5  [Premium · híbrido gasolina · Madrid Plaza Castilla · 815,00 € (115+700)] ─
('8901STU', 2023, 'Blanco',        5, 5, 'ECO',  'automatica', 'hibrido_gasolina',
  11000, 115.00, 700.00, NULL, 'disponible', 8, 18, 6),
-- ── KIA SPORTAGE  [SUV · híbrido gasolina · Madrid Barajas T4 · 422,00 € (72+350)] ────
('9012TUV', 2023, 'Verde Foresta', 5, 5, 'ECO',  'automatica', 'hibrido_gasolina',
   8500,  72.00, 350.00, NULL, 'disponible', 4, 19, 5),
-- ── KIA CEED  [Compacto · gasolina · Madrid Atocha · 243,00 € (43+200)] ─────────────
('0123UVW', 2021, 'Azul Saphir',   5, 5, 'C',    'manual',     'gasolina',
  34000,  43.00, 200.00, NULL, 'disponible', 2, 20, 3),
-- ── PEUGEOT 208  [Económico · eléctrico · Madrid Centro · 248,00 € (48+200)] ──────────
('1234VWX', 2023, 'Rouge Elixir',  5, 5, 'CERO', 'automatica', 'electrico',
   6000,  48.00, 200.00, NULL, 'disponible', 1, 21, 1),
-- ── PEUGEOT 3008  [SUV · diesel · Barcelona Centro · 475,00 € (75+400)] ─────────────
('2345WXY', 2022, 'Gris Artense',  5, 5, 'B',    'automatica', 'diesel',
  27000,  75.00, 400.00, NULL, 'disponible', 4, 22, 7),
-- ── VOLVO XC40  [SUV · eléctrico · Barcelona El Prat T1 · 705,00 € (105+600)] ─────────
('3456XYZ', 2024, 'Blanco Cristal',5, 5, 'CERO', 'automatica', 'electrico',
   3500, 105.00, 600.00, NULL, 'disponible', 4, 23, 8),
-- ── VOLVO V60  [Familiar · híbrido enchufable · Valencia Centro · 588,00 € (88+500)] ──
('4567YZA', 2023, 'Azul Denim',    5, 5, 'CERO', 'automatica', 'hibrido_enchufable_gasolina',
   9000,  88.00, 500.00, NULL, 'disponible', 5, 24, 10),
-- ── TESLA MODEL 3  [Berlina · eléctrico · Madrid Chamartin · 825,00 € (125+700)] ──────
('5678ZAB', 2024, 'Rojo Multicoat',5, 4, 'CERO', 'automatica', 'electrico',
   7000, 125.00, 700.00, NULL, 'disponible', 3, 25, 2),
-- ── TESLA MODEL Y  [SUV · eléctrico · Madrid Barajas T1 · 995,00 € (145+850)] ─────────
('6789ABC', 2024, 'Negro Obsidiana',7,5, 'CERO', 'automatica', 'electrico',
   5000, 145.00, 850.00, NULL, 'disponible', 4, 26, 4),
-- ── FIAT 500  [Económico · gasolina · Madrid Atocha · 128,00 € (28+100)] ────────────
('7890BCD', 2020, 'Azul Dolce',    4, 3, 'C',    'manual',     'gasolina',
  48000,  28.00, 100.00, NULL, 'disponible', 1, 27, 3),
-- ── FIAT TIPO  [Compacto · diesel · Sevilla Centro · 183,00 € (33+150)] ────────────
('8901CDE', 2019, 'Blanco Gelato', 5, 5, 'B',    'manual',     'diesel',
  62000,  33.00, 150.00, NULL, 'disponible', 2, 28, 12),
-- ── MAZDA CX-5  [SUV · gasolina · Sevilla Aeropuerto · 450,00 € (70+380)] ──────────
('9012DEF', 2022, 'Rojo Soul',     5, 5, 'B',    'automatica', 'gasolina',
  19000,  70.00, 380.00, NULL, 'disponible', 4, 29, 13),
-- ── MAZDA 3  [Compacto · gasolina · Bilbao Aeropuerto · 302,00 € (52+250)] ─────────
('0123EFG', 2023, 'Blanco Glaciar',5, 4, 'C',    'automatica', 'gasolina',
  12000,  52.00, 250.00, NULL, 'disponible', 2, 30, 14),
-- ── MAZDA MX-5  [Deportivo · gasolina · Málaga Aeropuerto · 595,00 € (95+500)] ─────
('1234MXZ', 2023, 'Rojo Soul',     2, 2, 'B',    'manual',     'gasolina',
   8000,  95.00, 500.00, NULL, 'disponible', 6, 31, 15),
-- ── NISSAN QASHQAI  [SUV · híbrido gasolina · Málaga Aeropuerto · 397,00 € (67+330)] ──
('1234FGH', 2023, 'Cobre Sunset',  5, 5, 'ECO',  'automatica', 'hibrido_gasolina',
  10000,  67.00, 330.00, NULL, 'disponible', 4, 32, 15),
-- ── NISSAN LEAF  [Económico · eléctrico · Valencia Aeropuerto · 270,00 € (50+220)] ─────
('2345GHI', 2022, 'Azul Topacio',  5, 5, 'CERO', 'automatica', 'electrico',
  18000,  50.00, 220.00, NULL, 'disponible', 1, 33, 11),
-- ── OPEL ASTRA  [Compacto · GNC · Madrid Centro · 218,00 € (38+180)] ───────────────
('3456HIJ', 2021, 'Gris Selenio',  5, 5, 'C',    'manual',     'gnc',
  37000,  38.00, 180.00, NULL, 'disponible', 2, 34, 1),
-- ── OPEL MOKKA  [SUV · eléctrico · Madrid Chamartin · 362,00 € (62+300)] ───────────
('4567IJK', 2023, 'Verde Mamba',   5, 5, 'CERO', 'automatica', 'electrico',
   8000,  62.00, 300.00, NULL, 'disponible', 4, 35, 2),
-- ── SKODA OCTAVIA  [Berlina · diesel · Barcelona El Prat T2 · 267,00 € (47+220)] ──────
('5678JKL', 2022, 'Gris Platino',  5, 5, 'B',    'manual',     'diesel',
  28000,  47.00, 220.00, NULL, 'disponible', 3, 36, 9),
-- ── SKODA KAMIQ  [SUV · gasolina · Valencia Centro · 279,00 € (49+230)] ────────────
('6789KLM', 2023, 'Naranja Energy',5, 5, 'C',    'manual',     'gasolina',
  14000,  49.00, 230.00, NULL, 'disponible', 4, 37, 10),
-- ── BMW SERIE 3  [Premium · gasolina · Sevilla Centro · 698,00 € (98+600)] ─────────
('7890LMN', 2023, 'Azul Saphir',   5, 4, 'B',    'automatica', 'gasolina',
   9500,  98.00, 600.00, NULL, 'disponible', 8, 5, 12),
-- ── MERCEDES CLASE C  [Premium · híbrido gasolina · Barcelona Centro · 835,00 € (115+720)]
('8901MNO', 2024, 'Negro Obsidiana',5,4, 'B',    'automatica', 'hibrido_gasolina',
   4500, 115.00, 720.00, NULL, 'disponible', 8, 13, 7),
-- ── VW GOLF  [Compacto · GNC · Bilbao Aeropuerto · 256,00 € (46+210)] ──────────────
('9012NOP', 2022, 'Blanco Candy',  5, 5, 'C',    'manual',     'gnc',
  23000,  46.00, 210.00, NULL, 'disponible', 2, 3, 14),
-- ── TOYOTA COROLLA CROSS  [SUV · híbrido gasolina · Málaga Aeropuerto · 388,00 € (68+320)]
('0123OPQ', 2024, 'Rojo Crimson',  5, 5, 'ECO',  'automatica', 'hibrido_gasolina',
   4000,  68.00, 320.00, NULL, 'disponible', 4, 1, 15),
-- ── RENAULT CAPTUR  [Compacto · eléctrico · Madrid Plaza Castilla · 330,00 € (60+270)] ─
('1234PQR', 2024, 'Azul Iron',     5, 5, 'CERO', 'automatica', 'electrico',
   2500,  60.00, 270.00, NULL, 'disponible', 2, 10, 6),
-- ── FORD PUMA  [Compacto · híbrido gasolina · Madrid Atocha · 263,00 € (53+210)] ──────
('2345QRS', 2023, 'Verde Aktiv',   5, 5, 'ECO',  'automatica', 'hibrido_gasolina',
  13000,  53.00, 210.00, NULL, 'disponible', 2, 12, 3),
-- ── HYUNDAI TUCSON  [SUV · PHEV · Valencia Aeropuerto · 480,00 € (80+400)] ─────────
('3456RST', 2024, 'Plata Polar',   5, 5, 'CERO', 'automatica', 'hibrido_enchufable_gasolina',
   6000,  80.00, 400.00, NULL, 'disponible', 4, 15, 11),
-- ── VW POLO  [Económico · gasolina · Sevilla Aeropuerto · 196,00 € (36+160)] ──────────
('4567STU', 2023, 'Azul Reef',     5, 5, 'C',    'manual',     'gasolina',
  15000,  36.00, 160.00, NULL, 'disponible', 1, 4, 13),
-- ── RENAULT CLIO  [Económico · GNC · Bilbao Aeropuerto · 175,00 € (35+140)] ───────────
('5678TUV', 2022, 'Blanco Lunar',  5, 5, 'ECO',  'manual',     'gnc',
  29000,  35.00, 140.00, NULL, 'disponible', 1, 9, 14),
-- ── SEAT ATECA  [SUV · diesel · Barcelona El Prat T2 · 371,00 € (61+310)] ──────────
('6789UVW', 2022, 'Gris Monsoon',  5, 5, 'B',    'automatica', 'diesel',
  32000,  61.00, 310.00, NULL, 'disponible', 4, 8, 9),
-- ── VW TRANSPORTER  [Furgoneta · diesel · Madrid Chamartin · 465,00 € (65+400)] ────────
('7890VWT', 2022, 'Blanco Glacial',9, 5, 'B',    'manual',     'diesel',
  45000,  65.00, 400.00, NULL, 'disponible', 7, 38, 2);

-- ============================================================
-- RESERVAS DE EJEMPLO
-- Estado: confirmada / pendiente
-- ============================================================
INSERT INTO reservas
    (fecha_reserva, fecha_inicio, fecha_fin, hora_inicio, hora_fin,
     precio_total, estado, observaciones,
     id_usuario, id_vehiculo, id_sucursal_recogida, id_sucursal_devolucion)
VALUES
('2026-01-10','2026-01-15','2026-01-20','09:00','09:00', 325.00,'confirmada', NULL, 2, 1, 1, 3),
('2026-01-18','2026-01-22','2026-01-25','10:00','10:00', 114.00,'confirmada', NULL, 3, 2, 2, 3),
('2026-02-01','2026-02-05','2026-02-10','08:00','08:00', 225.00,'confirmada', NULL, 4, 3, 1, 2),
('2026-02-14','2026-02-18','2026-02-21','09:00','09:00', 369.00,'confirmada', NULL, 5, 5, 6, 6),
('2026-03-05','2026-03-10','2026-03-15','11:00','09:00', 192.00,'confirmada', 'Necesito GPS', 6, 9, 5, 4),
('2026-03-20','2026-03-25','2026-03-28','08:30','09:00', 156.00,'pendiente',  NULL, 7, 7, 2, 1),
('2026-04-01','2026-04-05','2026-04-08','09:00','09:00', 420.00,'confirmada', NULL, 8, 6, 6, 6),
('2026-04-10','2026-04-14','2026-04-18','10:00','10:00', 248.00,'pendiente',  'Silla infantil', 9, 12, 2, 3);

