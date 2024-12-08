package com.jeanquille.billance.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Party (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String,

    var date: LocalDateTime,

    @OneToMany(mappedBy = "party", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonManagedReference
    var members: MutableList<Member> = mutableListOf(),

    @OneToMany(mappedBy = "party")
    var transactions: MutableList<Transaction>,

    @OneToMany(mappedBy = "party")
    var bills: MutableList<Bill>
)