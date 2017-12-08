--liquibase formatted sql

--changeset yuweijun:1 date:2016-01-08
DROP TABLE IF EXISTS `people`;
CREATE TABLE `people` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) NOT NULL,
  `job_title` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

LOCK TABLES `people` WRITE;
INSERT INTO `people` VALUES (1,'yuweijun','programmer'),(2,'test.yu','programmer');
UNLOCK TABLES;
--rollback drop table people;
