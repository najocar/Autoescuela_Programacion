-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 22-05-2023 a las 13:31:24
-- Versión del servidor: 10.4.27-MariaDB
-- Versión de PHP: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `autoescuela`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clases_alumnoes`
--

CREATE TABLE `clases_alumnoes` (
  `clase_id` int(11) NOT NULL,
  `alumnoes_dni` varchar(200) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `clases_alumnoes`
--

INSERT INTO `clases_alumnoes` (`clase_id`, `alumnoes_dni`) VALUES
(2, '14233472Z'),
(2, '34925823R'),
(3, '51213694P'),
(2, '51213694P'),
(3, '14233472Z'),
(1, '14233472Z'),
(4, '98478347Y'),
(3, '34925823R'),
(4, '34925823R');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `clases_alumnoes`
--
ALTER TABLE `clases_alumnoes`
  ADD KEY `FKmsx3p9b3vjyd8kj6muvhtlqvq` (`alumnoes_dni`),
  ADD KEY `FK50ku41pqe4a0byory9sncwo9x` (`clase_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
