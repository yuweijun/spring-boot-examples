package com.example.spring.jdbc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * http://code.flickr.net/2010/02/08/ticket-servers-distributed-unique-primary-keys-on-the-cheap/
 * <p>
 * CREATE TABLE `Tickets64` (
 * `id` bigint(20) unsigned NOT NULL auto_increment,
 * `stub` char(1) NOT NULL default '',
 * PRIMARY KEY  (`id`),
 * UNIQUE KEY `stub` (`stub`)
 * ) ENGINE=MyISAM
 * SELECT * from Tickets64 returns a single row that looks something like:
 * <p>
 * +-------------------+------+
 * | id                | stub |
 * +-------------------+------+
 * | 72157623227190423 |    a |
 * +-------------------+------+
 * When I need a new globally unique 64-bit ID I issue the following SQL:
 * <p>
 * REPLACE INTO Tickets64 (stub) VALUES ('a');
 * SELECT LAST_INSERT_ID();
 * <p>
 * SPOFs
 * You really really don’t know want provisioning your IDs to be a single point of failure.
 * We achieve “high availability” by running two ticket servers.
 * At this write/update volume replicating between the boxes would be problematic,
 * and locking would kill the performance of the site.
 * We divide responsibility between the two boxes by dividing the ID space down the middle,
 * evens and odds, using:
 * <p>
 * TicketServer1:
 * auto-increment-increment = 2
 * auto-increment-offset = 1
 * <p>
 * TicketServer2:
 * auto-increment-increment = 2
 * auto-increment-offset = 2
 *
 * @author yuweijun
 * @date 2018-09-06
 */
@Service
public class DistributedPrimaryKeyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedPrimaryKeyService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public long getDistributedUniquePrimaryKey() {
        jdbcTemplate.update("SET @@auto_increment_increment=2");
        jdbcTemplate.update("REPLACE INTO tickets64 (stub) VALUES ('a')");
        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        LOGGER.info("distributed unique primary key : {}", id);
        return id;
    }

}
