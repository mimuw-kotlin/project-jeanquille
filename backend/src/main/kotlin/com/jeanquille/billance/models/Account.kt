package com.jeanquille.billance.models

import jakarta.persistence.*
import java.util.UUID

@Entity
class Account (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    var username: String = "",
    var password: String = "",
    var phoneNumber: String = "",

    @OneToMany(mappedBy = "payer")
    var transactionsAsPayer: MutableList<Transaction> = mutableListOf(),

    @OneToMany(mappedBy = "receiver")
    var transactionsAsReceiver: MutableList<Transaction> = mutableListOf(),

    @ManyToMany
    var friends: MutableList<Account> = mutableListOf()
    ) {
    constructor(username: String, password: String, phoneNumber: String?) : this(
        username = username,
        password = password,
        phoneNumber = phoneNumber ?: "",
    )
}