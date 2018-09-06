--liquibase formatted sql

--changeset yuweijun:5 date:2018-09-06
DROP TABLE IF EXISTS `tickets64`;
CREATE TABLE `tickets64` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `stub` char(1) NOT NULL default '',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `stub` (`stub`)
) ENGINE=MyISAM;
--rollback drop table tickets64;