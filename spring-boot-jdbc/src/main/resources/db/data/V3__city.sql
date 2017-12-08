--liquibase formatted sql

--changeset yuweijun:3 date:2016-06-11
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `state` varchar(255) DEFAULT NULL,
  `country` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

LOCK TABLES `city` WRITE;
INSERT INTO `city` (name, state, country) VALUES ('San Francisco', 'CA', 'US');
UNLOCK TABLES;
--rollback drop table city;