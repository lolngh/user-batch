package com.batch.project.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="account")
@Data
public class Account {

    @Id
    private int accountid;

    @Column(name="account_name")
    private String accountname;

    @Column(name="type")
    private String type;

    @Column(name="balance")
    private Long balance;

    @OneToOne
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    private User user;

}
