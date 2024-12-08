package com.jeanquille.billance.models

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Party (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String = "New Party",

    var creationDate: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "party", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonManagedReference
    var members: MutableList<Member> = mutableListOf(),

    @OneToMany(mappedBy = "party", cascade = [CascadeType.ALL], orphanRemoval = true)
    var transactions: MutableList<Transaction> = mutableListOf(),

    @OneToMany(mappedBy = "party", cascade = [CascadeType.ALL], orphanRemoval = true)
    var bills: MutableList<Bill> = mutableListOf()
)