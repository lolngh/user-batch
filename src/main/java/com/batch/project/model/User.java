package com.batch.project.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="users")
@Data
public class User {

    @Id
    private int userid;

    @Column(name="name")
    private String name;

    @Column(name="active")
    private boolean active;

    @Column(name="address")
    private String address;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Account account;

}
