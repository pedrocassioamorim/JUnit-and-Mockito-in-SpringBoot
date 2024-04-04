package org.bootcampspringboot.junitandmockito.domain.entites;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity(name = "tb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    public User(Integer id, String name, String email, String password) {
        Id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(Id, user.Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }
}
