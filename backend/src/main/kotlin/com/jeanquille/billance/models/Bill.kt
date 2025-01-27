package com.jeanquille.billance.models

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Bill(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    var name: String,

    var amount: Long,

    var date: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JsonBackReference
    var party: Party = Party(),

    @ManyToOne
    var payer: Account = Account(),

    @ManyToMany
    var participants: MutableList<Account>
)
