package com.jeanquille.billance.repositories

import com.jeanquille.billance.models.TestModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TestRepository: JpaRepository<TestModel, Long> {

}