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
    FOREIGN KEY (id_categoria) REFERENCES categoria_objeto(id_categoria)
);

-- Tabla de usuarios
CREATE TABLE usuario (
    nombreUsu VARCHAR(100) PRIMARY KEY,
    password VARCHAR(100) NOT NULL
);

-- Tabla de partidas
CREATE TABLE partida (
    id_partida VARCHAR(100) PRIMARY KEY,
    id_usuario VARCHAR(100) NOT NULL,
    vidas INT,
    monedas INT,
    puntuacion INT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(nombreUsu)
);

-- Tabla inventario (relaciona partida y objeto, permite objetos repetidos)
CREATE TABLE inventario (
    id_inventario VARCHAR(100) PRIMARY KEY,
    id_partida VARCHAR(100) NOT NULL,
    id_objeto VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_partida) REFERENCES partida(id_partida),
    FOREIGN KEY (id_objeto) REFERENCES objeto(id_objeto)
);

-- Insertar categorías iniciales (con IDs definidos manualmente)
INSERT INTO categoria_objeto (id_categoria, nombre) VALUES
('1', 'ARMAS'),
('2', 'ARMADURAS'),
('3', 'POCIONES');

-- Insertar usuarios de prueba
INSERT INTO usuario (nombreUsu, password) VALUES
('Paco', '1234'),
('Ana', '1234'),
('Miguel', '1234'),
('Diego', '1234');

-- Insertar productos de prueba (con IDs simples para ejemplo)
INSERT INTO objeto (id_objeto, nombre, precio, imagen, descripcion, id_categoria) VALUES
('1', 'Espada', 30, '/img/espada.jpg', 'Una espada', '1'),
('2', 'Armadura', 50, '/img/armadura.png', 'Una armadura', '2'),
('3', 'Poción', 20, '/img/pocion.png', 'Una pocion', '3');

-- Nota: Las partidas e inventario se insertan desde la aplicación

