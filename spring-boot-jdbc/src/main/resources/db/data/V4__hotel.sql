--liquibase formatted sql

--changeset yuweijun:4 date:2016-06-11
DROP TABLE IF EXISTS `hotel`;
CREATE TABLE `hotel` (
  `city` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `zip` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`city`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

LOCK TABLES `hotel` WRITE;
INSERT INTO `hotel` (city, name, address, zip) VALUES (1, 'Conrad Treasury Place', 'William & George Streets', '4001');
UNLOCK TABLES;
--rollback drop table hotel;