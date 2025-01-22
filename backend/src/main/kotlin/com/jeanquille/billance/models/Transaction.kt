package com.jeanquille.billance.models

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

@Entity
class Transaction (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var amount: Long = 0,

    @JoinColumn(name = "party_id")
    @ManyToOne
    @JsonBackReference
    var party: Party,

    @JoinColumn(name = "payer_id")
    @ManyToOne
    @JsonManagedReference
    var payer: Account,

    @JoinColumn(name = "receiver_id")
    @ManyToOne
    @JsonManagedReference
    var receiver: Account
)