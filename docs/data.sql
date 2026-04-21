-- ============================================================
-- DATA.SQL — RentGo
-- Ejecutar DESPUÉS de ini_db.sql (que ya crea tipo_usuarios y admin)
-- ============================================================

-- ============================================================
-- CATEGORÍAS DE VEHÍCULOS
-- ============================================================
INSERT INTO categorias_vehiculos (nombre, descripcion) VALUES
('Económico',  'Vehículos pequeños y eficientes, ideales para desplazamientos urbanos.'),
('Compacto',   'Vehículos versátiles para ciudad y carretera, cómodos y eficientes.'),
('Berlina',    'Sedanes espaciosos con gran maletero y alto nivel de confort.'),
('SUV',        'Todoterrenos urbanos con mayor capacidad y altura al suelo.'),
('Familiar',   'Vehículos espaciosos para familias con maletero de gran capacidad.'),
('Deportivo',  'Coches de alto rendimiento y diseño exclusivo.'),
('Furgoneta',  'Vehículos de carga o para transporte de grupos.'),
('Premium',    'Alta gama con acabados de lujo y la última tecnología.');

-- ============================================================
-- SUCURSALES (15)
-- ============================================================
INSERT INTO sucursales (nombre, direccion, ciudad, email, telefono) VALUES
('Madrid Centro',        'C/ Gran Vía, 45',                   'Madrid',    'madrid.centro@rentgo.es',      '910000001'),
('Madrid Chamartín',     'C/ Agustín de Foxá, 29',            'Madrid',    'madrid.chamartin@rentgo.es',   '910000002'),
('Madrid Atocha',        'Glorieta Emperador Carlos V, s/n',   'Madrid',    'madrid.atocha@rentgo.es',      '910000003'),
('Madrid Barajas T1',    'Aeropuerto Adolfo Suárez T1',        'Madrid',    'madrid.barajas.t1@rentgo.es',  '910000004'),
('Madrid Barajas T4',    'Aeropuerto Adolfo Suárez T4',        'Madrid',    'madrid.barajas.t4@rentgo.es',  '910000005'),
('Madrid Plaza Castilla','Paseo de la Castellana, 200',        'Madrid',    'madrid.castilla@rentgo.es',    '910000006'),
('Barcelona Centro',     'Passeig de Gràcia, 80',              'Barcelona', 'barcelona.centro@rentgo.es',   '930000001'),
('Barcelona El Prat T1', 'Aeropuerto Josep Tarradellas T1',    'Barcelona', 'barcelona.prat.t1@rentgo.es',  '930000002'),
('Barcelona El Prat T2', 'Aeropuerto Josep Tarradellas T2',    'Barcelona', 'barcelona.prat.t2@rentgo.es',  '930000003'),
('Valencia Centro',      'Av. del Regne de València, 15',      'Valencia',  'valencia.centro@rentgo.es',    '960000001'),
('Valencia Aeropuerto',  'Aeropuerto de Manises, Terminal 1',  'Valencia',  'valencia.aero@rentgo.es',      '960000002'),
('Sevilla Centro',       'Av. de la Constitución, 12',         'Sevilla',   'sevilla.centro@rentgo.es',     '950000001'),
('Sevilla Aeropuerto',   'Aeropuerto San Pablo, Terminal 1',   'Sevilla',   'sevilla.aero@rentgo.es',       '950000002'),
('Bilbao Aeropuerto',    'Aeropuerto de Loiu, N-634',          'Bilbao',    'bilbao.aero@rentgo.es',        '944000001'),
('Málaga Aeropuerto',    'Aeropuerto Costa del Sol, T3',       'Málaga',    'malaga.aero@rentgo.es',        '952000001');

-- ============================================================
-- EXTRAS
-- ============================================================
INSERT INTO extras (nombre, descripcion, precio_dia) VALUES
('GPS',             'Navegador GPS con mapas actualizados incluidos.',                         8.00),
('Silla infantil',  'Silla homologada para niños de 9 a 36 kg (grupo I-III).',               10.00),
('Seguro premium',  'Cobertura total sin franquicia para mayor tranquilidad.',                15.00),
('WiFi portátil',   'Router 4G portátil con datos ilimitados incluidos.',                     6.00),
('Portaequipajes',  'Portaequipajes de techo homologado para equipaje o bicis extra.',        9.00),
('Pack bebé',       'Kit completo: silla grupo 0+ (0-13 kg), parasol y organizador trasero.',13.00);

-- ============================================================
-- USUARIOS CLIENTES (id=1 admin ya creado en ini_db.sql)
-- Contraseña: cliente123
-- ============================================================
INSERT INTO usuarios (nombre, apellidos, email, telefono, dni, direccion, password, id_tipo) VALUES
('María',  'García López',     'maria.garcia@gmail.com',    '611000001', '11111111A', 'C/ Alcalá, 10, Madrid',             'cliente123', 1),
('Carlos', 'López Pérez',      'carlos.lopez@gmail.com',    '622000002', '22222222B', 'Av. Diagonal, 50, Barcelona',       'cliente123', 1),
('Ana',    'Martínez García',  'ana.martinez@gmail.com',    '633000003', '33333333C', 'C/ Colón, 8, Valencia',             'cliente123', 1),
('Pedro',  'Sánchez Ruiz',     'pedro.sanchez@gmail.com',   '644000004', '44444444D', 'Av. Constitución, 3, Sevilla',      'cliente123', 1),
('Laura',  'Fernández Díaz',   'laura.fernandez@gmail.com', '655000005', '55555555E', 'Gran Vía, 20, Bilbao',              'cliente123', 1),
('Jorge',  'Rodríguez Moreno', 'jorge.rodriguez@gmail.com', '666000006', '66666666F', 'Paseo del Parque, 1, Málaga',       'cliente123', 1),
('Elena',  'González Jiménez', 'elena.gonzalez@gmail.com',  '677000007', '77777777G', 'Rambla Catalunya, 30, Barcelona',  'cliente123', 1),
('Miguel', 'Torres Herrero',   'miguel.torres@gmail.com',   '688000008', '88888888H', 'C/ Serrano, 15, Madrid',            'cliente123', 1);

-- ============================================================
-- MARCAS BASE (IDs 1-8)
-- ============================================================
INSERT INTO marcas (nombre) VALUES
('Toyota'),         -- 1
('Volkswagen'),     -- 2
('BMW'),            -- 3
('Mercedes-Benz'),  -- 4
('Seat'),           -- 5
('Hyundai'),        -- 6
('Renault'),        -- 7
('Ford');           -- 8

-- ============================================================
-- MODELOS BASE (IDs 1-16)
-- ============================================================
INSERT INTO modelos (nombre, id_marca) VALUES
('Corolla Cross', 1),   -- 1  Toyota
('RAV4',          1),   -- 2  Toyota
('Golf',          2),   -- 3  Volkswagen
('Polo',          2),   -- 4  Volkswagen
('Serie 3',       3),   -- 5  BMW
('X3',            3),   -- 6  BMW
('Clase E',       4),   -- 7  Mercedes-Benz
('Ateca',         5),   -- 8  Seat
('Clio',          7),   -- 9  Renault
('Captur',        7),   -- 10 Renault
('Ibiza',         5),   -- 11 Seat
('Puma',          8),   -- 12 Ford
('Clase C',       4),   -- 13 Mercedes-Benz
('Kona',          6),   -- 14 Hyundai
('Tucson',        6),   -- 15 Hyundai
('Kuga',          8);   -- 16 Ford

-- ============================================================
-- VEHÍCULOS BASE (IDs 1-12)
-- ============================================================
INSERT INTO vehiculos
    (matricula, anio, color, plazas, puertas, etiqueta, transmision, combustion,
     kilometraje, precio_dia, fianza, ruta_foto, disponibilidad,
     id_categoria_vehiculo, id_modelo, id_sucursal)
VALUES
-- Veh 1  Toyota Corolla Cross · SUV · Madrid Centro · 65€/día
('1111AAA', 2022, 'Blanco Perla',    5, 5, 'ECO', 'automatica', 'hibrido_gasolina',  25000,  65.00, 350.00, NULL, 'disponible', 4,  1, 1),
-- Veh 2  VW Golf · Compacto · Madrid Chamartín · 38€/día
('2222BBB', 2021, 'Gris Platino',    5, 5, 'C',   'manual',     'gasolina',          42000,  38.00, 200.00, NULL, 'disponible', 2,  3, 2),
-- Veh 3  Renault Clio · Económico · Madrid Centro · 45€/día
('3333CCC', 2020, 'Blanco Glaciar',  5, 5, 'C',   'manual',     'gasolina',          55000,  45.00, 150.00, NULL, 'disponible', 1,  9, 1),
-- Veh 4  Seat Ibiza · Económico · Madrid Atocha · 28€/día
('4444DDD', 2021, 'Rojo Pasión',     5, 5, 'C',   'manual',     'gasolina',          38000,  28.00, 130.00, NULL, 'disponible', 1, 11, 3),
-- Veh 5  BMW Serie 3 · Premium · Madrid Plaza Castilla · 123€/día
('5555EEE', 2022, 'Negro Zafiro',    5, 4, 'B',   'automatica', 'gasolina',          18000, 123.00, 700.00, NULL, 'disponible', 8,  5, 6),
-- Veh 6  Toyota RAV4 · SUV · Madrid Plaza Castilla · 140€/día
('6666FFF', 2023, 'Plata Metálico',  5, 5, 'ECO', 'automatica', 'hibrido_gasolina',  12000, 140.00, 800.00, NULL, 'disponible', 4,  2, 6),
-- Veh 7  VW Polo · Económico · Madrid Chamartín · 52€/día
('7777GGG', 2022, 'Azul Reef',       5, 5, 'C',   'manual',     'gasolina',          29000,  52.00, 220.00, NULL, 'disponible', 1,  4, 2),
-- Veh 8  Renault Captur · Compacto · Madrid Atocha · 55€/día
('8888HHH', 2021, 'Naranja Energy',  5, 5, 'C',   'manual',     'gasolina',          34000,  55.00, 260.00, NULL, 'disponible', 2, 10, 3),
-- Veh 9  Hyundai Tucson · SUV · Madrid Barajas T4 · 38€/día
('9999JJJ', 2022, 'Azul Galáctico',  5, 5, 'B',   'automatica', 'diesel',            31000,  38.00, 280.00, NULL, 'disponible', 4, 15, 5),
-- Veh 10 Ford Kuga · Familiar · Madrid Barajas T1 · 60€/día
('1010KKK', 2022, 'Gris Moscú',      5, 5, 'B',   'automatica', 'diesel',            28000,  60.00, 300.00, NULL, 'disponible', 5, 16, 4),
-- Veh 11 Mercedes Clase E · Berlina · Barcelona Centro · 98€/día
('2020LLL', 2023, 'Negro Obsidiana', 5, 4, 'B',   'automatica', 'gasolina',          15000,  98.00, 600.00, NULL, 'disponible', 3,  7, 7),
-- Veh 12 Mercedes Clase C · Premium · Madrid Chamartín · 62€/día
('3030MMM', 2022, 'Blanco Diamante', 5, 4, 'B',   'automatica', 'gasolina',          22000,  62.00, 450.00, NULL, 'disponible', 8, 13, 2);

-- ============================================================
-- MARCAS ADICIONALES (IDs 9-18)
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
-- MODELOS ADICIONALES (IDs 17-38)
-- ============================================================
INSERT INTO modelos (nombre, id_marca) VALUES
('A3',           9),   -- 17
('Q5',           9),   -- 18
('Sportage',    10),   -- 19
('Ceed',        10),   -- 20
('208',         11),   -- 21
('3008',        11),   -- 22
('XC40',        12),   -- 23
('V60',         12),   -- 24
('Model 3',     13),   -- 25
('Model Y',     13),   -- 26
('500',         14),   -- 27
('Tipo',        14),   -- 28
('CX-5',        15),   -- 29
('3',           15),   -- 30
('MX-5',        15),   -- 31
('Qashqai',     16),   -- 32
('Leaf',        16),   -- 33
('Astra',       17),   -- 34
('Mokka',       17),   -- 35
('Octavia',     18),   -- 36
('Kamiq',       18),   -- 37
('Transporter',  2);   -- 38

-- ============================================================
-- VEHÍCULOS ADICIONALES (IDs 13-43)
-- ============================================================
INSERT INTO vehiculos
    (matricula, anio, color, plazas, puertas, etiqueta, transmision, combustion,
     kilometraje, precio_dia, fianza, ruta_foto, disponibilidad,
     id_categoria_vehiculo, id_modelo, id_sucursal)
VALUES
('7890RST', 2022, 'Gris Nardo',      5, 4, 'B',    'automatica', 'diesel',                     22000,  68.00, 350.00, NULL, 'disponible', 2, 17, 2),
('8901STU', 2023, 'Blanco',          5, 5, 'ECO',  'automatica', 'hibrido_gasolina',            11000, 115.00, 700.00, NULL, 'disponible', 8, 18, 6),
('9012TUV', 2023, 'Verde Foresta',   5, 5, 'ECO',  'automatica', 'hibrido_gasolina',             8500,  72.00, 350.00, NULL, 'disponible', 4, 19, 5),
('0123UVW', 2021, 'Azul Saphir',     5, 5, 'C',    'manual',     'gasolina',                   34000,  43.00, 200.00, NULL, 'disponible', 2, 20, 3),
('1234VWX', 2023, 'Rouge Elixir',    5, 5, 'CERO', 'automatica', 'electrico',                   6000,  48.00, 200.00, NULL, 'disponible', 1, 21, 1),
('2345WXY', 2022, 'Gris Artense',    5, 5, 'B',    'automatica', 'diesel',                     27000,  75.00, 400.00, NULL, 'disponible', 4, 22, 7),
('3456XYZ', 2024, 'Blanco Cristal',  5, 5, 'CERO', 'automatica', 'electrico',                   3500, 105.00, 600.00, NULL, 'disponible', 4, 23, 8),
('4567YZA', 2023, 'Azul Denim',      5, 5, 'CERO', 'automatica', 'hibrido_enchufable_gasolina',  9000,  88.00, 500.00, NULL, 'disponible', 5, 24, 10),
('5678ZAB', 2024, 'Rojo Multicoat',  5, 4, 'CERO', 'automatica', 'electrico',                   7000, 125.00, 700.00, NULL, 'disponible', 3, 25, 2),
('6789ABC', 2024, 'Negro Obsidiana', 7, 5, 'CERO', 'automatica', 'electrico',                   5000, 145.00, 850.00, NULL, 'disponible', 4, 26, 4),
('7890BCD', 2020, 'Azul Dolce',      4, 3, 'C',    'manual',     'gasolina',                   48000,  28.00, 100.00, NULL, 'disponible', 1, 27, 3),
('8901CDE', 2019, 'Blanco Gelato',   5, 5, 'B',    'manual',     'diesel',                     62000,  33.00, 150.00, NULL, 'disponible', 2, 28, 12),
('9012DEF', 2022, 'Rojo Soul',       5, 5, 'B',    'automatica', 'gasolina',                   19000,  70.00, 380.00, NULL, 'disponible', 4, 29, 13),
('0123EFG', 2023, 'Blanco Glaciar',  5, 4, 'C',    'automatica', 'gasolina',                   12000,  52.00, 250.00, NULL, 'disponible', 2, 30, 14),
('1234MXZ', 2023, 'Rojo Soul',       2, 2, 'B',    'manual',     'gasolina',                    8000,  95.00, 500.00, NULL, 'disponible', 6, 31, 15),
('1234FGH', 2023, 'Cobre Sunset',    5, 5, 'ECO',  'automatica', 'hibrido_gasolina',            10000,  67.00, 330.00, NULL, 'disponible', 4, 32, 15),
('2345GHI', 2022, 'Azul Topacio',    5, 5, 'CERO', 'automatica', 'electrico',                  18000,  50.00, 220.00, NULL, 'disponible', 1, 33, 11),
('3456HIJ', 2021, 'Gris Selenio',    5, 5, 'C',    'manual',     'gnc',                        37000,  38.00, 180.00, NULL, 'disponible', 2, 34, 1),
('4567IJK', 2023, 'Verde Mamba',     5, 5, 'CERO', 'automatica', 'electrico',                   8000,  62.00, 300.00, NULL, 'disponible', 4, 35, 2),
('5678JKL', 2022, 'Gris Platino',    5, 5, 'B',    'manual',     'diesel',                     28000,  47.00, 220.00, NULL, 'disponible', 3, 36, 9),
('6789KLM', 2023, 'Naranja Energy',  5, 5, 'C',    'manual',     'gasolina',                   14000,  49.00, 230.00, NULL, 'disponible', 4, 37, 10),
('7890LMN', 2023, 'Azul Saphir',     5, 4, 'B',    'automatica', 'gasolina',                    9500,  98.00, 600.00, NULL, 'disponible', 8,  5, 12),
('8901MNO', 2024, 'Negro Obsidiana', 5, 4, 'B',    'automatica', 'hibrido_gasolina',             4500, 115.00, 720.00, NULL, 'disponible', 8, 13, 7),
('9012NOP', 2022, 'Blanco Candy',    5, 5, 'C',    'manual',     'gnc',                        23000,  46.00, 210.00, NULL, 'disponible', 2,  3, 14),
('0123OPQ', 2024, 'Rojo Crimson',    5, 5, 'ECO',  'automatica', 'hibrido_gasolina',             4000,  68.00, 320.00, NULL, 'disponible', 4,  1, 15),
('1234PQR', 2024, 'Azul Iron',       5, 5, 'CERO', 'automatica', 'electrico',                   2500,  60.00, 270.00, NULL, 'disponible', 2, 10, 6),
('2345QRS', 2023, 'Verde Aktiv',     5, 5, 'ECO',  'automatica', 'hibrido_gasolina',            13000,  53.00, 210.00, NULL, 'disponible', 2, 12, 3),
('3456RST', 2024, 'Plata Polar',     5, 5, 'CERO', 'automatica', 'hibrido_enchufable_gasolina',  6000,  80.00, 400.00, NULL, 'disponible', 4, 15, 11),
('4567STU', 2023, 'Azul Reef',       5, 5, 'C',    'manual',     'gasolina',                   15000,  36.00, 160.00, NULL, 'disponible', 1,  4, 13),
('5678TUV', 2022, 'Blanco Lunar',    5, 5, 'ECO',  'manual',     'gnc',                        29000,  35.00, 140.00, NULL, 'disponible', 1,  9, 14),
('6789UVW', 2022, 'Gris Monsoon',    5, 5, 'B',    'automatica', 'diesel',                     32000,  61.00, 310.00, NULL, 'disponible', 4,  8, 9),
('7890VWT', 2022, 'Blanco Glacial',  9, 5, 'B',    'manual',     'diesel',                     45000,  65.00, 400.00, NULL, 'disponible', 7, 38, 2);

-- ============================================================
-- VEHÍCULOS EXTRA (IDs 44-49) — añadidos en segunda tanda
-- ============================================================
INSERT INTO vehiculos
    (matricula, anio, color, plazas, puertas, etiqueta, transmision, combustion,
     kilometraje, precio_dia, fianza, ruta_foto, disponibilidad,
     id_categoria_vehiculo, id_modelo, id_sucursal)
VALUES
-- Veh 44  Audi A3 · Compacto · Barcelona El Prat T2 · 72€/día
('4401AAA', 2023, 'Gris Nardo',      5, 4, 'B',    'automatica', 'gasolina',             15000,  72.00, 350.00, NULL, 'disponible', 2, 17, 9),
-- Veh 45  Kia Sportage híbrido · SUV · Sevilla Centro · 58€/día
('4502BBB', 2023, 'Blanco Snow',     5, 5, 'ECO',  'automatica', 'hibrido_gasolina',     12000,  58.00, 300.00, NULL, 'disponible', 4, 19, 12),
-- Veh 46  Peugeot 208e eléctrico · Económico · Valencia Centro · 32€/día
('4603CCC', 2022, 'Rojo Elixir',     5, 5, 'CERO', 'automatica', 'electrico',            18000,  32.00, 150.00, NULL, 'disponible', 1, 21, 10),
-- Veh 47  Volvo XC40 Recharge eléctrico · SUV · Bilbao Aeropuerto · 95€/día
('4704DDD', 2024, 'Azul Denim',      5, 5, 'CERO', 'automatica', 'electrico',             9000,  95.00, 550.00, NULL, 'disponible', 4, 23, 14),
-- Veh 48  Tesla Model Y eléctrico · SUV · Madrid Centro · 135€/día
('4805EEE', 2024, 'Blanco Perla',    7, 5, 'CERO', 'automatica', 'electrico',             5000, 135.00, 800.00, NULL, 'disponible', 4, 26, 1),
-- Veh 49  Mazda CX-5 · SUV · Málaga Aeropuerto · 78€/día
('4906FFF', 2022, 'Azul Polaris',    5, 5, 'B',    'manual',     'gasolina',             24000,  78.00, 400.00, NULL, 'disponible', 4, 29, 15);

-- ============================================================
-- RESERVAS DE EJEMPLO (todas finalizadas — fechas en el pasado)
-- ============================================================
INSERT INTO reservas
    (fecha_reserva, fecha_inicio, fecha_fin, hora_inicio, hora_fin,
     precio_total, estado, observaciones,
     id_usuario, id_vehiculo, id_sucursal_recogida, id_sucursal_devolucion)
VALUES
('2026-01-10','2026-01-15','2026-01-20','09:00','09:00', 325.00,'finalizada', NULL,              2,  1, 1, 3),
('2026-01-18','2026-01-22','2026-01-25','10:00','10:00', 114.00,'finalizada', NULL,              3,  2, 2, 3),
('2026-02-01','2026-02-05','2026-02-10','08:00','08:00', 225.00,'finalizada', NULL,              4,  3, 1, 2),
('2026-02-14','2026-02-18','2026-02-21','09:00','09:00', 369.00,'finalizada', NULL,              5,  5, 6, 6),
('2026-03-05','2026-03-10','2026-03-15','11:00','09:00', 192.00,'finalizada', 'Necesito GPS',   6,  9, 5, 4),
('2026-03-20','2026-03-25','2026-03-28','08:30','09:00', 156.00,'finalizada', NULL,              7,  7, 2, 1),
('2026-04-01','2026-04-05','2026-04-08','09:00','09:00', 420.00,'finalizada', NULL,              8,  6, 6, 6),
('2026-04-10','2026-04-14','2026-04-18','10:00','10:00', 248.00,'cancelada',  'Silla infantil', 9, 12, 2, 3);

-- ============================================================
-- PAGOS ASOCIADOS A LAS RESERVAS
-- ============================================================
INSERT INTO pagos
    (fecha_pago, importe, fianza, metodo_pago, estado_pago, id_reserva)
VALUES
('2026-01-10', 325.00, 350.00, 'tarjeta_credito', 'realizado', 1),
('2026-01-18', 114.00, 200.00, 'tarjeta_debito',  'realizado', 2),
('2026-02-01', 225.00, 150.00, 'paypal',          'realizado', 3),
('2026-02-14', 369.00, 700.00, 'tarjeta_credito', 'realizado', 4),
('2026-03-05', 192.00, 280.00, 'tarjeta_debito',  'realizado', 5),
('2026-03-20', 156.00, 220.00, 'paypal',          'realizado', 6),
('2026-04-01', 420.00, 800.00, 'tarjeta_credito', 'realizado', 7),
('2026-04-10', 248.00, 450.00, 'tarjeta_debito',  'reembolsado', 8);

-- ============================================================
-- DEVOLUCIONES (reembolsos aprobados por admin)
-- ============================================================
INSERT INTO devoluciones
    (fecha_devolucion, importe_reembolsado, motivo, id_pago)
VALUES
('2026-04-11', 248.00, 'Cancelación solicitada por el cliente antes del inicio del alquiler. Reembolso total del importe abonado.', 8);
