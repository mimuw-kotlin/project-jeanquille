package com.jeanquille.billance.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Bill (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    var name: String,

    var amount: Long,

    var date: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    var party: Party,

    @ManyToOne
    var payer: Account,

    @ManyToMany
    var participants: MutableList<Account>
)