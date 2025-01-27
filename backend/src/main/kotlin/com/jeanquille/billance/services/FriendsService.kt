package com.jeanquille.billance.services

import com.jeanquille.billance.models.Account
import com.jeanquille.billance.repositories.AccountRepository
import com.jeanquille.billance.utils.addFriend
import com.jeanquille.billance.utils.removeFriend
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class FriendsService(private val accountRepository: AccountRepository) {
    fun getFriends(accountId: UUID): MutableList<Account> {
        val account: Account = accountRepository.findById(accountId).orElseThrow()
        return account.friends
    }

    fun unfriend(accountId: UUID, friendId: UUID) {
        if (accountId == friendId) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot unfriend self")
        }

        val account: Account = accountRepository.findById(accountId).orElseThrow()
        val friend: Account = accountRepository.findById(friendId).orElseThrow()

        if (!account.friends.contains(friend)) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Not a friend")
        }

        account.removeFriend(friend)
        friend.removeFriend(account)
        accountRepository.saveAll(mutableListOf(account, friend))
    }

    fun addFriend(accountId: UUID, friendUsername: String) {
        val account: Account = accountRepository.findById(accountId).orElseThrow()
        val friend: Account = accountRepository.findByUsername(friendUsername) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Friend not found")
        if (account.id == friend.id) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot add self as friend")
        }

        if (account.friends.contains(friend)) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Already a friend")
        }

        account.addFriend(friend)
        friend.addFriend(account)
        accountRepository.saveAll(mutableListOf(account, friend))
    }
}
