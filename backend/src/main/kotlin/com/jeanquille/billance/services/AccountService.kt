package com.jeanquille.billance.services

import com.jeanquille.billance.models.Account
import com.jeanquille.billance.repositories.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(val accountRepository: AccountRepository) {
    fun getAllAccounts(): MutableList<Account> {
        return accountRepository.findAll()
    }

    //TEST
    fun createAccount(username: String): Account {
        val account = Account(username = username)
        return accountRepository.save(account)
    }
}