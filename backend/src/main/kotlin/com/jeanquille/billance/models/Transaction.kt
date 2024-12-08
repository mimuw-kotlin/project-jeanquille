package com.jeanquille.billance.models

import jakarta.persistence.*

@Entity
class Transaction (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var amount: Long = 0,

    @JoinColumn(name = "party_id")
    @ManyToOne
    var party: Party,

    @JoinColumn(name = "payer_id")
    @ManyToOne
    var payer: Account,

    @JoinColumn(name = "receiver_id")
    @ManyToOne
    var receiver: Account
)