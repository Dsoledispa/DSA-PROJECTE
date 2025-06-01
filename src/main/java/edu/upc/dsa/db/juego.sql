-- Eliminar la base de datos si ya existe
DROP DATABASE IF EXISTS db_juego;

-- Crear base de datos
CREATE DATABASE db_juego;

USE db_juego;

-- Tabla de categorías de objetos
CREATE TABLE categoria_objeto (
    id_categoria VARCHAR(100) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

-- Tabla de objetos de la tienda
CREATE TABLE objeto (
    id_objeto VARCHAR(100) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio INT NOT NULL,
    imagen VARCHAR(255),
    descripcion TEXT,
    id_categoria VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categoria_objeto(id_categoria) ON DELETE CASCADE
);

-- Tabla de usuarios
CREATE TABLE usuario (
    id_usuario VARCHAR(100) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

-- Tabla de partidas
CREATE TABLE partida (
    id_partida VARCHAR(100) PRIMARY KEY,
    id_usuario VARCHAR(100) NOT NULL,
    vidas INT,
    monedas INT,
    puntuacion INT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

-- Tabla inventario (relaciona partida y objeto, permite objetos repetidos)
CREATE TABLE inventario (
    id_inventario VARCHAR(100) PRIMARY KEY,
    id_partida VARCHAR(100) NOT NULL,
    id_objeto VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_partida) REFERENCES partida(id_partida) ON DELETE CASCADE,
    FOREIGN KEY (id_objeto) REFERENCES objeto(id_objeto) ON DELETE CASCADE
);

-- Tabla carrito (objetos temporales por partida)
CREATE TABLE carrito (
    id_carrito VARCHAR(100) PRIMARY KEY,
    id_partida VARCHAR(100) NOT NULL,
    id_objeto VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_partida) REFERENCES partida(id_partida) ON DELETE CASCADE,
    FOREIGN KEY (id_objeto) REFERENCES objeto(id_objeto) ON DELETE CASCADE
);

-- Tabla de insignias
CREATE TABLE insignia (
    id_insignia VARCHAR(100) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    avatar VARCHAR(255)
);

-- Tabla intermedia para la relación muchos a muchos entre usuario e insignia (con ID)
CREATE TABLE usuario_insignia (
    id_usuario_insignia VARCHAR(100) PRIMARY KEY,
    id_usuario VARCHAR(100) NOT NULL,
    id_insignia VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_insignia) REFERENCES insignia(id_insignia) ON DELETE CASCADE,
    UNIQUE (id_usuario, id_insignia) -- evita duplicados
);


-- Insertar categorías iniciales
INSERT INTO categoria_objeto (id_categoria, nombre) VALUES
('1', 'ARMAS'),
('2', 'ARMADURAS'),
('3', 'POCIONES');

-- Insertar productos de prueba
INSERT INTO objeto (id_objeto, nombre, precio, imagen, descripcion, id_categoria) VALUES
('1', 'Espada', 30, '/img/espada.jpg', 'Una espada', '1'),
('2', 'Armadura', 50, '/img/armadura.png', 'Una armadura', '2'),
('3', 'Poción', 20, '/img/pocion.png', 'Una pocion', '3');

-- Insertar insignias iniciales
INSERT INTO insignia (id_insignia, nombre, avatar) VALUES
('1', 'Policia', '/img/policia.webp'),
('2', 'Tornillo', '/img/tornillo.jpg'),
('3', 'Valor', '/img/valor.png');

-- Nota: Los usuarios, partidas, inventario, carrito se insertan desde la aplicación
