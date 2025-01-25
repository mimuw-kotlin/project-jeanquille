package com.jeanquille.billance.services

import com.jeanquille.billance.models.Account
import com.jeanquille.billance.repositories.AccountRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class AccountService(val accountRepository: AccountRepository) {
    fun getAllAccounts(): MutableList<Account> {
        return accountRepository.findAll()
    }

    fun getAccount(accountId: UUID): Account {
        return accountRepository.findById(accountId).orElseThrow()
    }

    fun createAccount(account: Account) {
        if (accountRepository.existsByUsername(account.username)) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Username already taken")
        }
        accountRepository.save(account)
    }

    fun login(username: String, password: String): UUID {
        val account = accountRepository.findByUsername(username) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials")
        if (BCryptPasswordEncoder().matches(password, account.password)) {
            return account.id!!
        }
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials")
    }
}