-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Servidor: mysql:3306:3308
-- Tiempo de generación: 22-05-2022 a las 22:38:47
-- Versión del servidor: 8.0.29
-- Versión de PHP: 8.0.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `db`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Categoria`
--

CREATE TABLE `Categoria` (
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(150) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Imagen`
--

CREATE TABLE `Imagen` (
  `id` int NOT NULL,
  `imagen` longblob,
  `longitud` float DEFAULT NULL,
  `latitud` float DEFAULT NULL,
  `altitud` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Lugar`
--

CREATE TABLE `Lugar` (
  `longitud` float NOT NULL,
  `latitud` float NOT NULL,
  `altitud` float NOT NULL,
  `enlace` varchar(300) DEFAULT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `email_usuario` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Lugar_Categoria`
--

CREATE TABLE `Lugar_Categoria` (
  `longitud` float NOT NULL,
  `latitud` float NOT NULL,
  `altitud` float NOT NULL,
  `nombre_categoria` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Lugar_Ruta`
--

CREATE TABLE `Lugar_Ruta` (
  `longitud` float NOT NULL,
  `latitud` float NOT NULL,
  `altitud` float NOT NULL,
  `nombre_ruta` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Ruta`
--

CREATE TABLE `Ruta` (
  `nombre` varchar(50) NOT NULL,
  `email_usuario` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Ruta_Categoria`
--

CREATE TABLE `Ruta_Categoria` (
  `nombre_ruta` varchar(50) NOT NULL,
  `nombre_categoria` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Usuario`
--

CREATE TABLE `Usuario` (
  `email` varchar(50) NOT NULL,
  `nombre` varchar(50) DEFAULT NULL,
  `contrasenia` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `Categoria`
--
ALTER TABLE `Categoria`
  ADD PRIMARY KEY (`nombre`);

--
-- Indices de la tabla `Imagen`
--
ALTER TABLE `Imagen`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_lugar_imagen` (`longitud`,`latitud`,`altitud`);

--
-- Indices de la tabla `Lugar`
--
ALTER TABLE `Lugar`
  ADD PRIMARY KEY (`longitud`,`latitud`,`altitud`),
  ADD KEY `email_usuario` (`email_usuario`);

--
-- Indices de la tabla `Lugar_Categoria`
--
ALTER TABLE `Lugar_Categoria`
  ADD PRIMARY KEY (`longitud`,`latitud`,`altitud`,`nombre_categoria`),
  ADD KEY `fk_categoria_lugar` (`nombre_categoria`);

--
-- Indices de la tabla `Lugar_Ruta`
--
ALTER TABLE `Lugar_Ruta`
  ADD PRIMARY KEY (`longitud`,`latitud`,`altitud`,`nombre_ruta`),
  ADD KEY `fk_ruta_lugar` (`nombre_ruta`);

--
-- Indices de la tabla `Ruta`
--
ALTER TABLE `Ruta`
  ADD PRIMARY KEY (`nombre`,`email_usuario`),
  ADD KEY `email_usuario` (`email_usuario`);

--
-- Indices de la tabla `Ruta_Categoria`
--
ALTER TABLE `Ruta_Categoria`
  ADD PRIMARY KEY (`nombre_ruta`,`nombre_categoria`),
  ADD KEY `fk_categoria_ruta` (`nombre_categoria`);

--
-- Indices de la tabla `Usuario`
--
ALTER TABLE `Usuario`
  ADD PRIMARY KEY (`email`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `Imagen`
--
ALTER TABLE `Imagen`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `Imagen`
--
ALTER TABLE `Imagen`
  ADD CONSTRAINT `fk_lugar_imagen` FOREIGN KEY (`longitud`,`latitud`,`altitud`) REFERENCES `Lugar` (`longitud`, `latitud`, `altitud`);

--
-- Filtros para la tabla `Lugar`
--
ALTER TABLE `Lugar`
  ADD CONSTRAINT `Lugar_ibfk_1` FOREIGN KEY (`email_usuario`) REFERENCES `Usuario` (`email`);

--
-- Filtros para la tabla `Lugar_Categoria`
--
ALTER TABLE `Lugar_Categoria`
  ADD CONSTRAINT `fk_categoria_lugar` FOREIGN KEY (`nombre_categoria`) REFERENCES `Categoria` (`nombre`),
  ADD CONSTRAINT `fk_lugar_categoria` FOREIGN KEY (`longitud`,`latitud`,`altitud`) REFERENCES `Lugar` (`longitud`, `latitud`, `altitud`);

--
-- Filtros para la tabla `Lugar_Ruta`
--
ALTER TABLE `Lugar_Ruta`
  ADD CONSTRAINT `fk_lugar_ruta` FOREIGN KEY (`longitud`,`latitud`,`altitud`) REFERENCES `Lugar` (`longitud`, `latitud`, `altitud`),
  ADD CONSTRAINT `fk_ruta_lugar` FOREIGN KEY (`nombre_ruta`) REFERENCES `Ruta` (`nombre`);

--
-- Filtros para la tabla `Ruta`
--
ALTER TABLE `Ruta`
  ADD CONSTRAINT `Ruta_ibfk_1` FOREIGN KEY (`email_usuario`) REFERENCES `Usuario` (`email`);

--
-- Filtros para la tabla `Ruta_Categoria`
--
ALTER TABLE `Ruta_Categoria`
  ADD CONSTRAINT `fk_categoria_ruta` FOREIGN KEY (`nombre_categoria`) REFERENCES `Categoria` (`nombre`),
  ADD CONSTRAINT `fk_ruta_categoria` FOREIGN KEY (`nombre_ruta`) REFERENCES `Ruta` (`nombre`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
