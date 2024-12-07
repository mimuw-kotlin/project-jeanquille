package com.jeanquille.billance.dto

import com.jeanquille.billance.models.Account

data class AccountPostDto(
    val username: String,
    val password: String,
    val phoneNumber: String?
) {
    fun toAccount(): Account = Account(username, password, phoneNumber)
}