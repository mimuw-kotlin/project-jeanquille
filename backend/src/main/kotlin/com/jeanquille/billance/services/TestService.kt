package com.jeanquille.billance.services

import com.jeanquille.billance.models.TestModel
import com.jeanquille.billance.repositories.TestRepository
import org.springframework.stereotype.Service

@Service
class TestService(val testRepository: TestRepository) {
    fun xddd(): MutableList<TestModel> {
        return testRepository.findAll()
    }
}