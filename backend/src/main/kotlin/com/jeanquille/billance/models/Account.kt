package com.jeanquille.billance.models

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.util.*

@Entity
class Account (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    var username: String = "",
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var password: String = "",
    var phoneNumber: String = "",

    @OneToMany(mappedBy = "payer")
    @JsonBackReference
    var transactionsAsPayer: MutableList<Transaction> = mutableListOf(),

    @OneToMany(mappedBy = "receiver")
    @JsonBackReference
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