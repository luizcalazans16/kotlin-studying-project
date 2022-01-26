package com.mercadolivro.service

import com.mercadolivro.emuns.CustomerStatus
import com.mercadolivro.emuns.Errors
import com.mercadolivro.emuns.RolesEnum
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.helper.buildCustomer
import com.mercadolivro.model.Customer
import com.mercadolivro.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    @MockK
    private lateinit var customerRepository: CustomerRepository

    @MockK
    private lateinit var bookService: BookService

    @MockK
    private lateinit var bCrypt: BCryptPasswordEncoder

    @InjectMockKs
    @SpyK
    private lateinit var customerService: CustomerService

    @Test
    fun `should return all customers`() {
        val fakeCustomers = listOf(buildCustomer(), buildCustomer())

        every { customerRepository.findAll() } returns (fakeCustomers)
        val customers = customerService.getAll(null)

        assertEquals(fakeCustomers, customers)
        verify(exactly = 1) { customerRepository.findAll() }
        verify(exactly = 0) { customerRepository.findByNameContaining(any()) }
    }

    @Test
    fun `should return all customers when name is informed`() {
        val name = UUID.randomUUID().toString()
        val fakeCustomers = listOf(buildCustomer(), buildCustomer())

        every { customerRepository.findByNameContaining(name) } returns (fakeCustomers)
        val customers = customerService.getAll(name)

        assertEquals(fakeCustomers, customers)
        verify(exactly = 0) { customerRepository.findAll() }
        verify(exactly = 1) { customerRepository.findByNameContaining(name) }
    }

    @Test
    fun `should create customer and encrypt password`() {
        val initialPassword = Random().toString()
        val fakeCustomer = buildCustomer(password = initialPassword)
        val fakePassword = UUID.randomUUID().toString()
        val fakeCustomerCopy = fakeCustomer.copy(password = fakePassword)

        every { customerRepository.save(fakeCustomerCopy) } returns fakeCustomer
        every { bCrypt.encode(initialPassword) } returns fakePassword

        customerService.create(fakeCustomer)

        verify(exactly = 1) { customerRepository.save(fakeCustomerCopy) }
        verify(exactly = 1) { bCrypt.encode(initialPassword) }
    }

    @Test
    fun `should return customer by id`() {
        val id = Random().nextInt()
        val mockedCustomer = buildCustomer(id = id)

        every { customerRepository.findById(id) } returns Optional.of(mockedCustomer)

        val customer = customerService.findById(id)

        assertEquals(mockedCustomer, customer)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `should throw not found exception when customer not found`() {
        val id = Random().nextInt()

        every { customerRepository.findById(id) } returns Optional.empty()
        val error = assertThrows<NotFoundException> {
            customerService.findById(id)
        }

        assertEquals("Customer [$id] does not exist", error.message)
        assertEquals("ML-201", error.errorCode)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `should update customer`() {
        val id = Random().nextInt()
        val mockedCustomer = buildCustomer(id = id)

        every { customerRepository.existsById(id) } returns true
        every { customerRepository.save(mockedCustomer) } returns mockedCustomer

        customerService.update(mockedCustomer)

        verify(exactly = 1) { customerRepository.existsById(id) }
        verify(exactly = 1) { customerRepository.save(mockedCustomer) }
    }

    @Test
    fun `should throw not found exception when update customer`() {
        val id = Random().nextInt()
        val mockedCustomer = buildCustomer(id = id)

        every { customerRepository.existsById(id) } returns false
        every { customerRepository.save(mockedCustomer) } returns mockedCustomer
        val error = assertThrows<NotFoundException> {
            customerService.update(mockedCustomer)
        }

        assertEquals("Customer [$id] does not exist", error.message)
        assertEquals("ML-201", error.errorCode)
        verify(exactly = 1) { customerRepository.existsById(id) }
        verify(exactly = 0) { customerRepository.save(mockedCustomer) }

    }

    @Test
    fun `should delete customer`() {
        val id = Random().nextInt()
        val mockedCustomer = buildCustomer(id = id)
        val expectedCustomer = mockedCustomer.copy(status = CustomerStatus.INATIVO)

        every { customerService.findById(id) } returns mockedCustomer
        every { customerRepository.save(expectedCustomer) } returns expectedCustomer
        every { bookService.deleteByCustomer(mockedCustomer) } just runs

        customerService.delete(id)

        verify(exactly = 1) { bookService.deleteByCustomer(mockedCustomer) }
        verify(exactly = 1) { customerRepository.save(expectedCustomer) }
    }

    @Test
    fun `should throw not found exception when delete customer`() {
        val id = Random().nextInt()

        every { customerService.findById(id) } throws NotFoundException(
            Errors.ML201.message.format(id),
            Errors.ML201.code
        )
        val error = assertThrows<NotFoundException> {
            customerService.delete(id)
        }

        assertEquals("Customer [$id] does not exist", error.message)
        assertEquals("ML-201", error.errorCode)
        verify(exactly = 1) { customerService.findById(id) }
        verify(exactly = 0) { bookService.deleteByCustomer(any()) }
        verify(exactly = 0) { customerRepository.save(any()) }
    }

    @Test
    fun `should return true when email available`() {
        val email = "${UUID.randomUUID()}@email.com"

        every { customerRepository.existsByEmail(email) } returns false

        val emailAvailable = customerService.emailAvailable(email)

        assertTrue(emailAvailable)
        verify(exactly = 1) { customerRepository.existsByEmail(email) }

    }
    @Test
    fun `should return false when email available`() {
        val email = "${UUID.randomUUID()}@email.com"

        every { customerRepository.existsByEmail(email) } returns true

        val emailAvailable = customerService.emailAvailable(email)

        assertFalse(emailAvailable)
        verify(exactly = 1) { customerRepository.existsByEmail(email) }

    }
}