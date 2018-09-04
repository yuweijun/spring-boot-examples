-- create database ds0 default character set utf8mb4;
-- create database ds1 default character set utf8mb4;
-- grant all on ds0.* to 'dbuser'@'%' identified by 'dbpass';
-- grant all on ds1.* to 'dbuser'@'%' identified by 'dbpass';

--DROP TABLE IF EXISTS `t_user`;
--DROP TABLE IF EXISTS `t_order`;
--DROP TABLE IF EXISTS `t_order_item`;

CREATE TABLE IF NOT EXISTS `t_user` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS ds0.t_order(
    order_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    status VARCHAR(50),
    PRIMARY KEY (order_id)
);

CREATE TABLE IF NOT EXISTS ds1.t_order (
    order_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    status VARCHAR(50),
    PRIMARY KEY (order_id)
);

CREATE TABLE IF NOT EXISTS ds0.t_order_item (
    order_item_id INT NOT NULL AUTO_INCREMENT,
    order_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (order_item_id)
);

CREATE TABLE IF NOT EXISTS ds1.t_order_item (
    order_item_id INT NOT NULL AUTO_INCREMENT,
    order_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (order_item_id)
);
