--liquibase formatted sql

--changeset yuweijun:2 date:2016-06-10
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `age` int(4) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `user` WRITE;
INSERT INTO `user` VALUES (1,'yuweijun',38),(2,'test.yu',38);
UNLOCK TABLES;
--rollback drop table user;
