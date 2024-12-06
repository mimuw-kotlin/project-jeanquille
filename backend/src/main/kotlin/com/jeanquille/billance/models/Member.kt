package com.jeanquille.billance.models

import jakarta.persistence.*

@Entity
class Member (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @JoinColumn(name = "account_id")
    @ManyToOne
    var account: Account,

    @JoinColumn(name = "party_id")
    @ManyToOne
    var party: Party,

    var balance: Long = 0
    )