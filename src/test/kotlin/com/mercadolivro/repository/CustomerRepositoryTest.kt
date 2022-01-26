package com.mercadolivro.repository

import com.mercadolivro.helper.buildCustomer
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerRepositoryTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @BeforeEach
    fun setUp() {
        customerRepository.deleteAll()
    }

    @Test
    fun `should return name containing`() {
        val joao = customerRepository.save(buildCustomer(name = "João"))
        val joaquim = customerRepository.save(buildCustomer(name = "Joaquim"))
        val daniel = customerRepository.save(buildCustomer(name = "Daniel"))

        val customers = customerRepository.findByNameContaining("Jo")

        assertEquals(listOf(joao, joaquim), customers)

    }

    @Nested
    inner class `Exists by email` {
        @Test
        fun `should return true when email exists`() {
            val email = "teste@email.com"
            customerRepository.save(buildCustomer(email = email))

            val exists = customerRepository.existsByEmail(email)

            assertTrue(exists)
        }

        @Test
        fun `should return false when email does not exists`() {
            val email = "nonexistingemail@email.com"
            val exists = customerRepository.existsByEmail(email)

            assertFalse(exists)
        }
    }

    @Nested
    inner class `find by email` {
        @Test
        fun `should return customer when email exists`() {
            val email = "teste@email.com"
            val customer = customerRepository.save(buildCustomer(email = email))

            val result = customerRepository.findByEmail(email)

            assertNotNull(result)
            assertEquals(customer, result)
        }

        @Test
        fun `should return null when email does not exists`() {
            val email = "nonexistingemail@email.com"
            val result = customerRepository.findByEmail(email)

            assertNull(result)
        }
    }
}