package com.example.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * <code>
 * CREATE TABLE `users` (
 * id int(11) NOT NULL AUTO_INCREMENT,
 * username varchar(50) NOT NULL,
 * password varchar(50) NOT NULL,
 * salt varchar(50) DEFAULT NULL,
 * PRIMARY KEY (`id`),
 * INDEX index_username(username)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 * <p>
 * 用户名密码: casuser/Mellon
 * insert users (username, password, salt) values ('casuser', '2221743b6930692378a076f2411b6762', 'test-salt');
 * </code>
 * </pre>
 */
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "salt", nullable = false)
    private String salt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }
}

