package com.jeanquille.billance.services

import com.jeanquille.billance.models.Account
import com.jeanquille.billance.repositories.AccountRepository
import com.jeanquille.billance.utils.addFriend
import com.jeanquille.billance.utils.removeFriend
import org.springframework.stereotype.Service
import java.util.*

@Service
class FriendsService(private val accountRepository: AccountRepository) {
    fun getFriends(accountId: UUID): MutableList<Account> {
        val account: Account = accountRepository.findById(accountId).orElseThrow()
        return account.friends
    }

    fun unfriend(accountId: UUID, friendId: UUID) {
        val account: Account = accountRepository.findById(accountId).orElseThrow()
        val friend: Account = accountRepository.findById(friendId).orElseThrow()

        account.removeFriend(friend)
        friend.removeFriend(account)
        accountRepository.saveAll(mutableListOf(account, friend))
    }

    fun addFriend(accountId: UUID, friendId: UUID) {
        val account: Account = accountRepository.findById(accountId).orElseThrow()
        val friend: Account = accountRepository.findById(friendId).orElseThrow()

        account.addFriend(friend)
        friend.addFriend(account)
        accountRepository.saveAll(mutableListOf(account, friend))
    }
}