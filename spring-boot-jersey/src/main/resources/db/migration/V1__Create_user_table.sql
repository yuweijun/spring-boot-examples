DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password_hash` varchar(100) NOT NULL,
  `role` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_of_username` (`username`),
  KEY `index_of_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

INSERT INTO `user` VALUES (1,'admin','admin@localhost','$2a$10$S0V2Di4zTeAChqliMUA1D.wN5iBqoyVuNWHjHIA9Om3Ksgu2S0y6e','ADMIN'),
(2,'david','david@gmail.com','$2a$10$kicqX9AZkvaHShJQ2HAJGepechnFyuXzaAY/k8u/KcZaPlk2BQwVC','USER'),
(3,'test','test@test','$2a$10$NS9NHDTS/FHOoqcRQ7Tgwuk.POCmL5gNBXIO7bXwyiJrFNVZwLpt6','USER');

DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `password` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

INSERT INTO `account` VALUES (1,'password','jhoeller'),(2,'password','dsyer'),(3,'password','pwebb'),(4,'password','ogierke'),(5,'password','rwinch'),(6,'password','mfisher'),(7,'password','mpollack'),(8,'password','jlong');

DROP TABLE IF EXISTS `bookmark`;
CREATE TABLE `bookmark` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `account_id` int(4) NOT NULL,
  `uri` varchar(50) DEFAULT '',
  `description` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `index_of_account` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

INSERT INTO `bookmark` VALUES (1,1,'http://www.google.com.hk/1/jhoeller','A description 1'),(2,1,'http://www.google.com.hk/2/jhoeller','A description 2'),(3,2,'http://www.google.com.hk/1/dsyer','A description 1'),(4,2,'http://www.google.com.hk/2/dsyer','A description 2'),(5,3,'http://www.google.com.hk/1/pwebb','A description 1'),(6,3,'http://www.google.com.hk/2/pwebb','A description 2'),(7,4,'http://www.google.com.hk/1/ogierke','A description 1'),(8,4,'http://www.google.com.hk/2/ogierke','A description 2'),(9,5,'http://www.google.com.hk/1/rwinch','A description 1'),(10,5,'http://www.google.com.hk/2/rwinch','A description 2'),(11,6,'http://www.google.com.hk/1/mfisher','A description 1'),(12,6,'http://www.google.com.hk/2/mfisher','A description 2'),(13,7,'http://www.google.com.hk/1/mpollack','A description 1'),(14,7,'http://www.google.com.hk/2/mpollack','A description 2'),(15,8,'http://www.google.com.hk/1/jlong','A description 1'),(16,8,'http://www.google.com.hk/2/jlong','A description 2');

