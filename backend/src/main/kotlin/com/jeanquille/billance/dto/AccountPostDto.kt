package com.jeanquille.billance.dto

import com.jeanquille.billance.models.Account
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

data class AccountPostDto(
    val username: String,
    val password: String,
    val phoneNumber: String?
) {
    fun toAccount(): Account = Account(
        username,
        BCryptPasswordEncoder().encode(password),
        phoneNumber
    )
}
