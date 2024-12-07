package com.jeanquille.billance.services

import com.jeanquille.billance.models.Account
import com.jeanquille.billance.repositories.AccountRepository
import org.springframework.stereotype.Service
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
            throw a party
        }
        accountRepository.save(account)
    }
}