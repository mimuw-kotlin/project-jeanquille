package com.jeanquille.billance.repositories

import com.jeanquille.billance.models.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AccountRepository: JpaRepository<Account, UUID> {
    fun existsByUsername(username: String): Boolean
    fun findByUsername(username: String): Account
}