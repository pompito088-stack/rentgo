-- ============================================================
-- DATA.SQL - RentGo
-- Ejecutar DESPUES de ini_db.sql (solo crea las tablas vacias)
-- ============================================================
-- ============================================================
-- TIPOS DE USUARIO
-- ============================================================
INSERT INTO tipo_usuarios (nombre) VALUES ('cliente');
INSERT INTO tipo_usuarios (nombre) VALUES ('admin');
-- ============================================================
-- USUARIO ADMINISTRADOR DEL SISTEMA
-- Contrasena: admin123
-- ============================================================
INSERT INTO usuarios (nombre, apellidos, email, telefono, dni, direccion, password, id_tipo)
VALUES ('Admin', 'Sistema RentGo', 'admin@admin-rentgo.com', '600000000', '00000000A', 'Oficina Central RentGo', 'admin123', 2);
-- ============================================================
-- CATEGORIAS DE VEHICULOS
-- ============================================================
INSERT INTO categorias_vehiculos (nombre, descripcion) VALUES
('Economico',  'Vehiculos pequenos y eficientes, ideales para desplazamientos urbanos.'),
('Compacto',   'Vehiculos versatiles para ciudad y carretera, comodos y eficientes.'),
('Berlina',    'Sedanes espaciosos con gran maletero y alto nivel de confort.'),
('SUV',        'Todoterrenos urbanos con mayor capacidad y altura al suelo.'),
('Familiar',   'Vehiculos espaciosos para familias con maletero de gran capacidad.'),
('Deportivo',  'Coches de alto rendimiento y diseno exclusivo.'),
('Furgoneta',  'Vehiculos de carga o para transporte de grupos.'),
('Premium',    'Alta gama con acabados de lujo y la ultima tecnologia.');
-- ============================================================
-- SUCURSALES (15)
-- ============================================================
INSERT INTO sucursales (nombre, direccion, ciudad, email, telefono) VALUES
('Madrid Centro',        'C/ Gran Via, 45',                   'Madrid',    'madrid.centro@rentgo.es',      '910000001'),
('Madrid Chamartin',     'C/ Agustin de Foxa, 29',            'Madrid',    'madrid.chamartin@rentgo.es',   '910000002'),
('Madrid Atocha',        'Glorieta Emperador Carlos V, s/n',  'Madrid',    'madrid.atocha@rentgo.es',      '910000003'),
('Madrid Barajas T1',    'Aeropuerto Adolfo Suarez T1',       'Madrid',    'madrid.barajas.t1@rentgo.es',  '910000004'),
('Madrid Barajas T4',    'Aeropuerto Adolfo Suarez T4',       'Madrid',    'madrid.barajas.t4@rentgo.es',  '910000005'),
('Madrid Plaza Castilla','Paseo de la Castellana, 200',       'Madrid',    'madrid.castilla@rentgo.es',    '910000006'),
('Barcelona Centro',     'Passeig de Gracia, 80',             'Barcelona', 'barcelona.centro@rentgo.es',   '930000001'),
('Barcelona El Prat T1', 'Aeropuerto Josep Tarradellas T1',   'Barcelona', 'barcelona.prat.t1@rentgo.es',  '930000002'),
('Barcelona El Prat T2', 'Aeropuerto Josep Tarradellas T2',   'Barcelona', 'barcelona.prat.t2@rentgo.es',  '930000003'),
('Valencia Centro',      'Av. del Regne de Valencia, 15',     'Valencia',  'valencia.centro@rentgo.es',    '960000001'),
('Valencia Aeropuerto',  'Aeropuerto de Manises, Terminal 1', 'Valencia',  'valencia.aero@rentgo.es',      '960000002'),
('Sevilla Centro',       'Av. de la Constitucion, 12',        'Sevilla',   'sevilla.centro@rentgo.es',     '950000001'),
('Sevilla Aeropuerto',   'Aeropuerto San Pablo, Terminal 1',  'Sevilla',   'sevilla.aero@rentgo.es',       '950000002'),
('Bilbao Aeropuerto',    'Aeropuerto de Loiu, N-634',         'Bilbao',    'bilbao.aero@rentgo.es',        '944000001'),
('Malaga Aeropuerto',    'Aeropuerto Costa del Sol, T3',      'Malaga',    'malaga.aero@rentgo.es',        '952000001');
-- ============================================================
-- EXTRAS
-- ============================================================
INSERT INTO extras (nombre, descripcion, precio_dia) VALUES
('GPS',             'Navegador GPS con mapas actualizados incluidos.',                         8.00),
('Silla infantil',  'Silla homologada para ninos de 9 a 36 kg (grupo I-III).',               10.00),
('Seguro premium',  'Cobertura total sin franquicia para mayor tranquilidad.',                15.00),
('WiFi portatil',   'Router 4G portatil con datos ilimitados incluidos.',                     6.00),
('Portaequipajes',  'Portaequipajes de techo homologado para equipaje o bicis extra.',        9.00),
('Pack bebe',       'Kit completo: silla grupo 0+ (0-13 kg), parasol y organizador trasero.',13.00);
-- ============================================================
-- USUARIOS CLIENTES  |  Contrasena: cliente123
-- ============================================================
INSERT INTO usuarios (nombre, apellidos, email, telefono, dni, direccion, password, ruta_foto_carnet, id_tipo) VALUES
('Maria',  'Garcia Lopez',     'maria.garcia@gmail.com',    '611000001', '11111111A', 'C/ Alcala, 10, Madrid',           'cliente123', '/img/carnets/carnet_conducir_maria_garcia.png',    1),
('Carlos', 'Lopez Perez',      'carlos.lopez@gmail.com',    '622000002', '22222222B', 'Av. Diagonal, 50, Barcelona',     'cliente123', '/img/carnets/carnet_conducir_carlos_lopez.png',    1),
('Ana',    'Martinez Garcia',  'ana.martinez@gmail.com',    '633000003', '33333333C', 'C/ Colon, 8, Valencia',           'cliente123', '/img/carnets/carnet_conducir_ana_martinez.png',    1),
('Pedro',  'Sanchez Ruiz',     'pedro.sanchez@gmail.com',   '644000004', '44444444D', 'Av. Constitucion, 3, Sevilla',    'cliente123', '/img/carnets/carnet_conducir_pedro_sanchez.png',   1),
('Laura',  'Fernandez Diaz',   'laura.fernandez@gmail.com', '655000005', '55555555E', 'Gran Via, 20, Bilbao',            'cliente123', '/img/carnets/carnet_conducir_laura_fernandez.png', 1),
('Jorge',  'Rodriguez Moreno', 'jorge.rodriguez@gmail.com', '666000006', '66666666F', 'Paseo del Parque, 1, Malaga',     'cliente123', '/img/carnets/carnet_conducir_jorge_rodriguez.png', 1),
('Elena',  'Gonzalez Jimenez', 'elena.gonzalez@gmail.com',  '677000007', '77777777G', 'Rambla Catalunya, 30, Barcelona', 'cliente123', '/img/carnets/carnet_conducir_elena_gonzalez.png',  1),
('Miguel', 'Torres Herrero',   'miguel.torres@gmail.com',   '688000008', '88888888H', 'C/ Serrano, 15, Madrid',          'cliente123', '/img/carnets/carnet_conducir_miguel_torres.png',   1);
-- ============================================================
-- MARCAS BASE (IDs 1-8)
-- ============================================================
INSERT INTO marcas (nombre) VALUES
('Toyota'),('Volkswagen'),('BMW'),('Mercedes-Benz'),('Seat'),('Hyundai'),('Renault'),('Ford');
-- ============================================================
-- MODELOS BASE (IDs 1-16)
-- ============================================================
INSERT INTO modelos (nombre, id_marca) VALUES
('Corolla Cross',1),('RAV4',1),('Golf',2),('Polo',2),('Serie 3',3),('X3',3),
('Clase E',4),('Ateca',5),('Clio',7),('Captur',7),('Ibiza',5),('Puma',8),
('Clase C',4),('Kona',6),('Tucson',6),('Kuga',8);
-- ============================================================
-- VEHICULOS BASE (IDs 1-12)
-- ============================================================
INSERT INTO vehiculos (matricula,anio,color,plazas,puertas,etiqueta,transmision,combustion,
     kilometraje,precio_dia,fianza,ruta_foto,disponibilidad,id_categoria_vehiculo,id_modelo,id_sucursal) VALUES
('1111AAA',2022,'Blanco Perla',   5,5,'ECO','automatica','hibrido_gasolina', 25000, 65.00,350.00,'/img/vehiculos/toyota_corolla_cross_blanco.png',         'disponible',4, 1,1),
('2222BBB',2021,'Gris Platino',   5,5,'C',  'manual',    'gasolina',         42000, 38.00,200.00,'/img/vehiculos/volkswagen_golf_gris_platino.png',        'disponible',2, 3,2),
('3333CCC',2020,'Blanco Glaciar', 5,5,'C',  'manual',    'gasolina',         55000, 45.00,150.00,'/img/vehiculos/renault_clio_blanco_glaciar.png',         'disponible',1, 9,1),
('4444DDD',2021,'Rojo Pasion',    5,5,'C',  'manual',    'gasolina',         38000, 28.00,130.00,'/img/vehiculos/seat_ibiza_rojo_pasion.png',              'disponible',1,11,3),
('5555EEE',2022,'Negro Zafiro',   5,4,'B',  'automatica','gasolina',         18000,123.00,700.00,'/img/vehiculos/bmw_serie_3_negro_zafiro.png',            'disponible',8, 5,6),
('6666FFF',2023,'Plata Metalico', 5,5,'ECO','automatica','hibrido_gasolina', 12000,140.00,800.00,'/img/vehiculos/toyota_rav4_plata_metalico.png',          'disponible',4, 2,6),
('7777GGG',2022,'Azul Reef',      5,5,'C',  'manual',    'gasolina',         29000, 52.00,220.00,'/img/vehiculos/volkswagen_polo_azul_reef.png',           'disponible',1, 4,2),
('8888HHH',2021,'Naranja Energy', 5,5,'C',  'manual',    'gasolina',         34000, 55.00,260.00,'/img/vehiculos/renault_captur_naranja_energy.png',       'disponible',2,10,3),
('9999JJJ',2022,'Azul Galactico', 5,5,'B',  'automatica','diesel',           31000, 38.00,280.00,'/img/vehiculos/hyundai_tucson_azul_galactico.png',       'disponible',4,15,5),
('1010KKK',2022,'Gris Moscu',     5,5,'B',  'automatica','diesel',           28000, 60.00,300.00,'/img/vehiculos/ford_kuga_gris_moscu.png',                'disponible',5,16,4),
('2020LLL',2023,'Negro Obsidiana',5,4,'B',  'automatica','gasolina',         15000, 98.00,600.00,'/img/vehiculos/mercedes-benz_clase_e_negro_obsidiana.png','disponible',3, 7,7),
('3030MMM',2022,'Blanco Diamante',5,4,'B',  'automatica','gasolina',         22000, 62.00,450.00,'/img/vehiculos/mercedes-benz_clase_c_blanco_diamante.png','disponible',8,13,2);
-- ============================================================
-- MARCAS ADICIONALES (IDs 9-18)
-- ============================================================
INSERT INTO marcas (nombre) VALUES
('Audi'),('Kia'),('Peugeot'),('Volvo'),('Tesla'),('Fiat'),('Mazda'),('Nissan'),('Opel'),('Skoda');
-- ============================================================
-- MODELOS ADICIONALES (IDs 17-38)
-- ============================================================
INSERT INTO modelos (nombre, id_marca) VALUES
('A3',9),('Q5',9),('Sportage',10),('Ceed',10),('208',11),('3008',11),
('XC40',12),('V60',12),('Model 3',13),('Model Y',13),('500',14),('Tipo',14),
('CX-5',15),('3',15),('MX-5',15),('Qashqai',16),('Leaf',16),('Astra',17),
('Mokka',17),('Octavia',18),('Kamiq',18),('Transporter',2);
-- ============================================================
-- VEHICULOS ADICIONALES (IDs 13-44)
-- ============================================================
INSERT INTO vehiculos (matricula,anio,color,plazas,puertas,etiqueta,transmision,combustion,
     kilometraje,precio_dia,fianza,ruta_foto,disponibilidad,id_categoria_vehiculo,id_modelo,id_sucursal) VALUES
('7890RST',2022,'Gris Nardo',      5,4,'B',   'automatica','diesel',                       22000, 68.00,350.00,'/img/vehiculos/audio_a3_gris_nardo.png',                        'disponible',2,17, 2),
('8901STU',2023,'Blanco',          5,5,'ECO', 'automatica','hibrido_gasolina',              11000,115.00,700.00,'/img/vehiculos/audi_q5_blanco.png',                             'disponible',8,18, 6),
('9012TUV',2023,'Verde Foresta',   5,5,'ECO', 'automatica','hibrido_gasolina',               8500, 72.00,350.00,'/img/vehiculos/kia_sportage_verde_foresta.png',                 'disponible',4,19, 5),
('0123UVW',2021,'Azul Saphir',     5,5,'C',   'manual',    'gasolina',                      34000, 43.00,200.00,'/img/vehiculos/kia_ceed_azul_saphir.png',                       'disponible',2,20, 3),
('1234VWX',2023,'Rojo Elixir',     5,5,'CERO','automatica','electrico',                      6000, 48.00,200.00,'/img/vehiculos/peugeot_208_rojo_elixir_2023.png',               'disponible',1,21, 1),
('2345WXY',2022,'Gris Artense',    5,5,'B',   'automatica','diesel',                        27000, 75.00,400.00,'/img/vehiculos/peugeot_3008_gris_artense.png',                  'disponible',4,22, 7),
('3456XYZ',2024,'Blanco Cristal',  5,5,'CERO','automatica','electrico',                      3500,105.00,600.00,'/img/vehiculos/volvo_xc40_blanco_cristal_2024.png',             'disponible',4,23, 8),
('4567YZA',2023,'Azul Denim',      5,5,'CERO','automatica','hibrido_enchufable_gasolina',    9000, 88.00,500.00,'/img/vehiculos/volvo_v60_azul_denim.png',                       'disponible',5,24,10),
('5678ZAB',2024,'Rojo Multicoat',  5,4,'CERO','automatica','electrico',                      7000,125.00,700.00,'/img/vehiculos/tesla_model_3_rojo_multicoat.png',               'disponible',3,25, 2),
('6789ABC',2024,'Negro Obsidiana', 7,5,'CERO','automatica','electrico',                      5000,145.00,850.00,'/img/vehiculos/tesla_model_y_negro_obsidiana.png',              'disponible',4,26, 4),
('7890BCD',2020,'Azul Dolce',      4,3,'C',   'manual',    'gasolina',                      48000, 28.00,100.00,'/img/vehiculos/fiat_500_azul_dolce.png',                        'disponible',1,27, 3),
('8901CDE',2019,'Blanco Gelato',   5,5,'B',   'manual',    'diesel',                        62000, 33.00,150.00,'/img/vehiculos/fiat_tipo_blanco_gelato.png',                    'disponible',2,28,12),
('9012DEF',2022,'Rojo Soul',       5,5,'B',   'automatica','gasolina',                      19000, 70.00,380.00,'/img/vehiculos/mazda_cx-5_rojo_soul_2022.png',                  'disponible',4,29,13),
('0123EFG',2023,'Blanco Glaciar',  5,4,'C',   'automatica','gasolina',                      12000, 52.00,250.00,'/img/vehiculos/mazda_3_blanco_2023.png',                        'disponible',2,30,14),
('1234MXZ',2023,'Rojo Soul',       2,2,'B',   'manual',    'gasolina',                       8000, 95.00,500.00,'/img/vehiculos/mazda_mx-5_rojo_soul_2023.png',                  'disponible',6,31,15),
('1234FGH',2023,'Cobre Sunset',    5,5,'ECO', 'automatica','hibrido_gasolina',              10000, 67.00,330.00,'/img/vehiculos/nissan_qashqai_cobre_sunset_2023.png',           'disponible',4,32,15),
('2345GHI',2022,'Azul Topacio',    5,5,'CERO','automatica','electrico',                     18000, 50.00,220.00,'/img/vehiculos/nissan_leaf_azul_topacio_2022.png',              'disponible',1,33,11),
('3456HIJ',2021,'Gris Selenio',    5,5,'C',   'manual',    'gnc',                           37000, 38.00,180.00,'/img/vehiculos/opel_astra_gris_selenio_2021.png',               'disponible',2,34, 1),
('4567IJK',2023,'Verde Mamba',     5,5,'CERO','automatica','electrico',                      8000, 62.00,300.00,'/img/vehiculos/opel_mokka_verde_mamba_2023.png',                'disponible',4,35, 2),
('5678JKL',2022,'Gris Platino',    5,5,'B',   'manual',    'diesel',                        28000, 47.00,220.00,'/img/vehiculos/skoda_octavia_gris_platino_2022.png',            'disponible',3,36, 9),
('6789KLM',2023,'Naranja Energy',  5,5,'C',   'manual',    'gasolina',                      14000, 49.00,230.00,'/img/vehiculos/skoda_kamiq_naranja_energy_2023.png',            'disponible',4,37,10),
('7890LMN',2023,'Azul Saphir',     5,4,'B',   'automatica','gasolina',                       9500, 98.00,600.00,'/img/vehiculos/bmw_serie_3_azul_saphir_2023.png',               'disponible',8, 5,12),
('8901MNO',2024,'Negro Obsidiana', 5,4,'B',   'automatica','hibrido_gasolina',               4500,115.00,720.00,'/img/vehiculos/mercedes-benz_clase_c_negro_obsidiana_2024.png', 'disponible',8,13, 7),
('9012NOP',2022,'Blanco Candy',    5,5,'C',   'manual',    'gnc',                           23000, 46.00,210.00,'/img/vehiculos/volkswagen_golf_blanco_candy.png',               'disponible',2, 3,14),
('0123OPQ',2024,'Rojo Crimson',    5,5,'ECO', 'automatica','hibrido_gasolina',               4000, 68.00,320.00,'/img/vehiculos/toyota_corolla_cross_rojo_crimson_2024.png',     'disponible',4, 1,15),
('1234PQR',2024,'Azul Iron',       5,5,'CERO','automatica','electrico',                      2500, 60.00,270.00,'/img/vehiculos/renault_captur_azul_iron_2024.png',              'disponible',2,10, 6),
('2345QRS',2023,'Verde Aktiv',     5,5,'ECO', 'automatica','hibrido_gasolina',              13000, 53.00,210.00,'/img/vehiculos/ford_puma_verde_aktiv_2023.png',                 'disponible',2,12, 3),
('3456RST',2024,'Plata Polar',     5,5,'CERO','automatica','hibrido_enchufable_gasolina',    6000, 80.00,400.00,'/img/vehiculos/hyundai_tucson_plata_polar_2024.png',            'disponible',4,15,11),
('4567STU',2023,'Azul Reef',       5,5,'C',   'manual',    'gasolina',                      15000, 36.00,160.00,'/img/vehiculos/volkswagen_polo_azul_reef_2022.png',             'disponible',1, 4,13),
('5678TUV',2022,'Blanco Lunar',    5,5,'ECO', 'manual',    'gnc',                           29000, 35.00,140.00,'/img/vehiculos/renault_clio_blanco_lunar.png',                  'disponible',1, 9,14),
('6789UVW',2022,'Gris Monsoon',    5,5,'B',   'automatica','diesel',                        32000, 61.00,310.00,'/img/vehiculos/seat_ateca_gris_monsoon.png',                    'disponible',4, 8, 9),
('7890VWT',2022,'Blanco Glacial',  9,5,'B',   'manual',    'diesel',                        45000, 65.00,400.00,'/img/vehiculos/volkswagen_transporter_blanco_glacial.png',      'disponible',7,38, 2);
-- ============================================================
-- VEHICULOS EXTRA (IDs 45-50)
-- ============================================================
INSERT INTO vehiculos (matricula,anio,color,plazas,puertas,etiqueta,transmision,combustion,
     kilometraje,precio_dia,fianza,ruta_foto,disponibilidad,id_categoria_vehiculo,id_modelo,id_sucursal) VALUES
('4401AAA',2023,'Gris Nardo',   5,4,'B',   'automatica','gasolina',         15000, 72.00,350.00,'/img/vehiculos/audi_a3_gris.png',               'disponible',2,17, 9),
('4502BBB',2023,'Blanco Snow',  5,5,'ECO', 'automatica','hibrido_gasolina', 12000, 58.00,300.00,'/img/vehiculos/kia_sportage_blanco_snow.png',    'disponible',4,19,12),
('4603CCC',2022,'Rojo Elixir',  5,5,'CERO','automatica','electrico',        18000, 32.00,150.00,'/img/vehiculos/peugeot_208_rojo_elexir.png',     'disponible',1,21,10),
('4704DDD',2024,'Azul Denim',   5,5,'CERO','automatica','electrico',         9000, 95.00,550.00,'/img/vehiculos/volvo_xc40_azul_denim.png',       'disponible',4,23,14),
('4805EEE',2024,'Blanco Perla', 7,5,'CERO','automatica','electrico',         5000,135.00,800.00,'/img/vehiculos/tesla_model_y_blanco_perla.png',  'disponible',4,26, 1),
('4906FFF',2022,'Azul Polaris', 5,5,'B',   'manual',    'gasolina',         24000, 78.00,400.00,'/img/vehiculos/mazda_cx-5_azul_polaris.png',     'disponible',4,29,15);
-- ============================================================
-- RESERVAS DE EJEMPLO (todas finalizadas - fechas en el pasado)
-- ============================================================
INSERT INTO reservas (fecha_reserva,fecha_inicio,fecha_fin,hora_inicio,hora_fin,
     precio_total,estado,observaciones,id_usuario,id_vehiculo,id_sucursal_recogida,id_sucursal_devolucion) VALUES
('2026-01-10','2026-01-15','2026-01-20','09:00','09:00',325.00,'finalizada',NULL,            2, 1,1,3),
('2026-01-18','2026-01-22','2026-01-25','10:00','10:00',114.00,'finalizada',NULL,            3, 2,2,3),
('2026-02-01','2026-02-05','2026-02-10','08:00','08:00',225.00,'finalizada',NULL,            4, 3,1,2),
('2026-02-14','2026-02-18','2026-02-21','09:00','09:00',369.00,'finalizada',NULL,            5, 5,6,6),
('2026-03-05','2026-03-10','2026-03-15','11:00','09:00',192.00,'finalizada','Necesito GPS',  6, 9,5,4),
('2026-03-20','2026-03-25','2026-03-28','08:30','09:00',156.00,'finalizada',NULL,            7, 7,2,1),
('2026-04-01','2026-04-05','2026-04-08','09:00','09:00',420.00,'finalizada',NULL,            8, 6,6,6),
('2026-04-10','2026-04-14','2026-04-18','10:00','10:00',248.00,'cancelada', 'Silla infantil',9,12,2,3);
-- ============================================================
-- PAGOS
-- ============================================================
INSERT INTO pagos (fecha_pago,importe,fianza,metodo_pago,estado_pago,id_reserva) VALUES
('2026-01-10',325.00,350.00,'tarjeta_credito','realizado',  1),
('2026-01-18',114.00,200.00,'tarjeta_debito', 'realizado',  2),
('2026-02-01',225.00,150.00,'paypal',          'realizado',  3),
('2026-02-14',369.00,700.00,'tarjeta_credito','realizado',  4),
('2026-03-05',192.00,280.00,'tarjeta_debito', 'realizado',  5),
('2026-03-20',156.00,220.00,'paypal',          'realizado',  6),
('2026-04-01',420.00,800.00,'tarjeta_credito','realizado',  7),
('2026-04-10',248.00,450.00,'tarjeta_debito', 'reembolsado',8);
-- ============================================================
-- DEVOLUCIONES
-- ============================================================
INSERT INTO devoluciones (fecha_devolucion,importe_reembolsado,motivo,id_pago) VALUES
('2026-04-11',248.00,'Cancelacion solicitada por el cliente antes del inicio del alquiler. Reembolso total del importe abonado.',8);