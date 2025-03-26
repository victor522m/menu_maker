DROP DATABASE menu_maker; 
CREATE DATABASE IF NOT EXISTS menu_maker;
USE menu_maker;

-- Crear tabla menu
CREATE TABLE IF NOT EXISTS menu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME
);

-- Crear tabla plato
CREATE TABLE IF NOT EXISTS plato (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_plato ENUM('PRIMERO', 'SEGUNDO', 'POSTRE') NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    descripcion TEXT,
    es_vegetariano BOOLEAN,
    tiempo_preparacion INT,
    tipo_carne VARCHAR(50),
    guarnicion VARCHAR(100),
    tipo_postre VARCHAR(50),
    apto_celiaco BOOLEAN,
    dtype VARCHAR(50)
);

-- Tabla intermedia para la relación muchos a muchos
CREATE TABLE IF NOT EXISTS menu_plato (
    menu_id BIGINT NOT NULL,
    plato_id BIGINT NOT NULL,
    PRIMARY KEY (menu_id, plato_id),
    FOREIGN KEY (menu_id) REFERENCES menu(id) ON DELETE CASCADE,
    FOREIGN KEY (plato_id) REFERENCES plato(id) ON DELETE CASCADE
);

-- Crear índices para optimización
CREATE INDEX idx_tipo_plato ON plato(tipo_plato);

-- Insertar menús
INSERT INTO menu (nombre, fecha_creacion, fecha_modificacion)
VALUES 
('Menu del Día', NOW(), NOW()),
('Menu Vegetariano', NOW(), NOW()),
('Menu Gourmet', NOW(), NOW());

-- Insertar platos
INSERT INTO plato (tipo_plato, nombre, precio, descripcion, es_vegetariano, tiempo_preparacion, tipo_carne, guarnicion, tipo_postre, apto_celiaco, dtype)
VALUES
('PRIMERO', 'Sopa de Verduras', 5.50, 'Sopa ligera con verduras frescas', TRUE, 15, NULL, NULL, NULL, TRUE, 'PRIMERO'),
('SEGUNDO', 'Pollo Asado', 10.90, 'Pollo marinado asado a la perfección', FALSE, 35, 'Pollo', 'Papas al horno', NULL, FALSE, 'SEGUNDO'),
('POSTRE', 'Flan Casero', 3.50, 'Flan hecho en casa con caramelo', TRUE, 10, NULL, NULL, 'Flan', TRUE, 'POSTRE'),
('POSTRE', 'Fruta de Temporada', 2.90, 'Selección de frutas frescas', TRUE, 5, NULL, NULL, 'Fruta', TRUE, 'POSTRE'),
('PRIMERO', 'Ensalada Mediterránea', 6.50, 'Ensalada con tomate, pepino y aceitunas', TRUE, 10, NULL, NULL, NULL, TRUE, 'PRIMERO'),
('SEGUNDO', 'Lasagna de Vegetales', 9.80, 'Lasagna con capas de vegetales y queso', TRUE, 40, NULL, NULL, NULL, FALSE, 'SEGUNDO'),
('POSTRE', 'Brownie Vegano', 4.20, 'Brownie hecho sin ingredientes de origen animal', TRUE, 20, NULL, NULL, 'Brownie', TRUE, 'POSTRE'),
('POSTRE', 'Helado de Mango', 3.90, 'Helado artesanal de mango', TRUE, 5, NULL, NULL, 'Helado', TRUE, 'POSTRE'),
('PRIMERO', 'Crema de Langosta', 12.50, 'Crema suave y deliciosa de langosta', FALSE, 20, NULL, NULL, NULL, FALSE, 'PRIMERO'),
('SEGUNDO', 'Filete de Res', 25.00, 'Filete de res con salsa de vino tinto', FALSE, 50, 'Res', 'Verduras al vapor', NULL, FALSE, 'SEGUNDO'),
('POSTRE', 'Tiramisú', 6.50, 'Clásico italiano con mascarpone y café', TRUE, 15, NULL, NULL, 'Tiramisú', FALSE, 'POSTRE'),
('POSTRE', 'Cheesecake de Fresa', 5.50, 'Tarta de queso con fresas frescas', TRUE, 10, NULL, NULL, 'Tarta', FALSE, 'POSTRE');

-- Relacionar menús con platos (tabla intermedia)
INSERT INTO menu_plato (menu_id, plato_id)
VALUES
(1, 1), -- Sopa de Verduras en Menu del Día
(1, 2), -- Pollo Asado en Menu del Día
(1, 3), -- Flan Casero en Menu del Día
(1, 4), -- Fruta de Temporada en Menu del Día
(2, 1), -- Sopa de Verduras en Menu Vegetariano
(2, 5), -- Ensalada Mediterránea en Menu Vegetariano
(2, 6), -- Lasagna de Vegetales en Menu Vegetariano
(2, 7), -- Brownie Vegano en Menu Vegetariano
(3, 9), -- Crema de Langosta en Menu Gourmet
(3, 10), -- Filete de Res en Menu Gourmet
(3, 11), -- Tiramisú en Menu Gourmet
(3, 12); -- Cheesecake de Fresa en Menu Gourmet


-- Parte para seguridad

CREATE DATABASE IF NOT EXISTS menu_maker;
USE menu_maker;

CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL
);

CREATE TABLE authorities (
    username VARCHAR(50),
    authority VARCHAR(50),
    FOREIGN KEY (username) REFERENCES users(username)
);

INSERT INTO users (username, password, enabled) VALUES -- el password es 123  
('admin', '{noop}123', 1),
('victor', '{noop}123', 1),
('angel', '{noop}123', 1),
('henry', '{noop}123', 1),
('user', '{noop}123', 1);

INSERT INTO authorities (username, authority) VALUES 
('admin', 'ROLE_OWNER'),
('victor', 'ROLE_OWNER'),
('angel', 'ROLE_OWNER'),
('henry', 'ROLE_OWNER'),
('user', 'ROLE_EMPLOYED');
