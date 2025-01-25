package com.jeanquille.billance.models

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.ObjectIdGenerators
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

    @ManyToMany
    @JsonIgnore
    var friends: MutableList<Account> = mutableListOf()
    ) {
    constructor(username: String, password: String, phoneNumber: String?) : this(
        username = username,
        password = password,
        phoneNumber = phoneNumber ?: "",
    )
}