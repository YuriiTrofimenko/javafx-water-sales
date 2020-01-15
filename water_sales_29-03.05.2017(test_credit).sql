-- phpMyAdmin SQL Dump
-- version 4.0.10.10
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1:3306
-- Время создания: Июн 03 2017 г., 18:46
-- Версия сервера: 5.5.45
-- Версия PHP: 5.4.44

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `water_sales`
--

-- --------------------------------------------------------

--
-- Структура таблицы `Barrel`
--

CREATE TABLE IF NOT EXISTS `Barrel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `shop_id` int(11) NOT NULL,
  `last_c_date` date NOT NULL,
  `capacity_id` int(11) NOT NULL,
  `recently_replaced` tinyint(1) NOT NULL DEFAULT '0',
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `whater_t_id` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL COMMENT 'price per liter',
  `counter` int(11) NOT NULL,
  `positions` int(11) NOT NULL DEFAULT '7',
  `last_sale_id` int(11) DEFAULT NULL,
  `allowed_rest` int(11) NOT NULL,
  `period_calc_date` date DEFAULT NULL,
  `period` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `shop_id` (`shop_id`),
  KEY `capacity_id` (`capacity_id`),
  KEY `whater_t_id` (`whater_t_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=357 ;

-- --------------------------------------------------------

--
-- Структура таблицы `BarrelCapacity`
--

CREATE TABLE IF NOT EXISTS `BarrelCapacity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `capacity` int(11) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

--
-- Дамп данных таблицы `BarrelCapacity`
--

INSERT INTO `BarrelCapacity` (`id`, `capacity`, `active`) VALUES
(1, 300, 1),
(2, 400, 1),
(3, 500, 1),
(4, 750, 1),
(5, 1000, 1),
(6, 200, 1);

-- --------------------------------------------------------

--
-- Структура таблицы `Car`
--

CREATE TABLE IF NOT EXISTS `Car` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` int(11) NOT NULL,
  `truck` tinyint(1) NOT NULL DEFAULT '1',
  `tonnage` int(11) NOT NULL,
  `gov_num` varchar(16) NOT NULL COMMENT 'government number',
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=10 ;

--
-- Дамп данных таблицы `Car`
--

INSERT INTO `Car` (`id`, `number`, `truck`, `tonnage`, `gov_num`, `active`) VALUES
(1, 20, 0, 4, '1', 1),
(2, 21, 0, 4, '2', 1),
(3, 22, 0, 6, '3', 1),
(4, 23, 0, 4, '4', 1),
(5, 24, 0, 2, '5', 1),
(6, 25, 0, 4, '6', 1),
(7, 26, 0, 4, '7', 1),
(8, 27, 0, 4, '8', 1),
(9, 28, 0, 4, 'АН2752КВ', 1);

-- --------------------------------------------------------

--
-- Структура таблицы `DayReport`
--

CREATE TABLE IF NOT EXISTS `DayReport` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=108 ;

-- --------------------------------------------------------

--
-- Структура таблицы `DayReportItem`
--

CREATE TABLE IF NOT EXISTS `DayReportItem` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `report_id` int(11) NOT NULL,
  `car_id` int(11) NOT NULL,
  `driver_id` int(11) NOT NULL,
  `to_pay` decimal(10,2) NOT NULL,
  `to_pay_far` decimal(10,2) NOT NULL,
  `profit` decimal(10,2) NOT NULL,
  `profit_far` decimal(10,2) NOT NULL,
  `volume` int(11) NOT NULL,
  `volume_far` decimal(10,2) NOT NULL,
  `far_count` int(11) NOT NULL,
  `debt_amort` decimal(10,2) NOT NULL,
  `clean_count` int(11) NOT NULL,
  `replace_count` int(11) NOT NULL,
  `debt` decimal(10,2) NOT NULL,
  `count_old` int(11) NOT NULL,
  `count_new` int(11) NOT NULL,
  `km` int(11) NOT NULL,
  `km_tonn` int(11) NOT NULL COMMENT 'km/tonn of water',
  `notice` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=847 ;

-- --------------------------------------------------------

--
-- Структура таблицы `DebtChange`
--

CREATE TABLE IF NOT EXISTS `DebtChange` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `shop_id` int(11) NOT NULL,
  `is_debt` tinyint(1) NOT NULL,
  `value` decimal(10,2) NOT NULL,
  `date` date NOT NULL,
  `debt_id` int(11) NOT NULL,
  `balance` decimal(10,2) NOT NULL,
  `sale_id` int(11) NOT NULL,
  `not_req_amort` tinyint(4) NOT NULL DEFAULT '0',
  `is_credit` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2523 ;

-- --------------------------------------------------------

--
-- Структура таблицы `Driver`
--

CREATE TABLE IF NOT EXISTS `Driver` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `phone` varchar(16) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=13 ;

-- --------------------------------------------------------

--
-- Структура таблицы `Sale`
--

CREATE TABLE IF NOT EXISTS `Sale` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `shop_id` int(11) NOT NULL,
  `barrel_id` int(11) NOT NULL,
  `driver_id` int(11) NOT NULL,
  `car_id` int(11) NOT NULL,
  `counter_old` int(11) NOT NULL,
  `counter_new` int(11) NOT NULL,
  `volume` int(11) DEFAULT NULL,
  `cleaning` tinyint(1) NOT NULL,
  `repair` tinyint(1) NOT NULL,
  `notice` varchar(255) NOT NULL,
  `to_pay` decimal(10,2) NOT NULL,
  `profit` decimal(10,2) NOT NULL,
  `debt` decimal(10,2) NOT NULL,
  `createdAt` date NOT NULL,
  `updatedAt` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `shop_id` (`shop_id`),
  KEY `driver_id` (`driver_id`),
  KEY `car_id` (`car_id`),
  KEY `barrel_id` (`barrel_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=13662 ;

-- --------------------------------------------------------

--
-- Структура таблицы `Settings`
--

CREATE TABLE IF NOT EXISTS `Settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  `value` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Дамп данных таблицы `Settings`
--

INSERT INTO `Settings` (`id`, `name`, `value`) VALUES
(1, 'cleaning_period', '6480000000'),
(2, 'cleaning_period_over', '7776000000'),
(3, 'allowed_rest_percent', '0.4');

-- --------------------------------------------------------

--
-- Структура таблицы `Shop`
--

CREATE TABLE IF NOT EXISTS `Shop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `address` varchar(128) NOT NULL,
  `phone` varchar(100) NOT NULL,
  `b_c_date` date NOT NULL COMMENT ' the date of the beginning of cooperation',
  `c_terms` varchar(512) NOT NULL COMMENT 'terms of cooperation',
  `legal_name` varchar(128) NOT NULL,
  `far` tinyint(1) NOT NULL,
  `debt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=321 ;

-- --------------------------------------------------------

--
-- Структура таблицы `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(25) NOT NULL,
  `password` varchar(25) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Дамп данных таблицы `User`
--

INSERT INTO `User` (`id`, `login`, `password`) VALUES
(1, '1', '1'),
(2, 'user', 'aR21W5');

-- --------------------------------------------------------

--
-- Структура таблицы `WaterType`
--

CREATE TABLE IF NOT EXISTS `WaterType` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Дамп данных таблицы `WaterType`
--

INSERT INTO `WaterType` (`id`, `name`, `active`) VALUES
(1, 'Аква', 1),
(2, 'Лазурная', 1);

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `Barrel`
--
ALTER TABLE `Barrel`
  ADD CONSTRAINT `barrel_ibfk_1` FOREIGN KEY (`whater_t_id`) REFERENCES `WaterType` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
