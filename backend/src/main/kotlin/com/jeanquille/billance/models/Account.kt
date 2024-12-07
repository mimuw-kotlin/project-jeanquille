package com.jeanquille.billance.models

import jakarta.persistence.*
import java.util.UUID

@Entity
class Account (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    var username: String,
    var password: String,
    var phoneNumber: String,

    @OneToMany(mappedBy = "account")
    var memberships: MutableList<Member>,

    @OneToMany(mappedBy = "payer")
    var transactionsAsPayer: MutableList<Transaction>,

    @OneToMany(mappedBy = "receiver")
    var transactionsAsReceiver: MutableList<Transaction>,

    @ManyToMany
    var friends: MutableList<Account>,
    ) {
    constructor(username: String) : this(
        username = username,
        password = "",
        phoneNumber = "",
        memberships = mutableListOf(),
        transactionsAsPayer = mutableListOf(),
        transactionsAsReceiver = mutableListOf(),
        friends = mutableListOf()
    )
}