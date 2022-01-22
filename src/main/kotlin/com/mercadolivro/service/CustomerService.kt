package com.mercadolivro.service

import com.mercadolivro.emuns.CustomerStatus
import com.mercadolivro.emuns.Errors
import com.mercadolivro.emuns.RolesEnum
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.model.Customer
import com.mercadolivro.repository.CustomerRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val bookService: BookService,
    private val bCrypt: BCryptPasswordEncoder
) {
    fun getAll(name: String?): List<Customer> {
        name?.let {
            return customerRepository.findByName(name)
        }
        return customerRepository.findAll().toList()
    }

    fun findById(id: Int): Customer {
        return customerRepository.findById(id).orElseThrow {
            NotFoundException(Errors.ML201.message.format(id), Errors.ML201.code)
        }
    }

    fun create(customer: Customer) {
        val customerToSave = customer.copy(
            roles = setOf(RolesEnum.CUSTOMER),
            password = bCrypt.encode(customer.password)
        )
        customerRepository.save(customerToSave)
    }

    fun update(customer: Customer) {
        if (!customerRepository.existsById(customer.id!!)) {
            throw Exception("Não existe cliente com o id informado")
        }
        customerRepository.save(customer)
    }

    fun delete(id: Int) {
        val customer = findById(id)
        bookService.deleteByCustomer(customer)

        customer.status = CustomerStatus.INATIVO
        customerRepository.save(customer)
    }

    fun emailAvailable(email: String): Boolean {
        return !customerRepository.existsByEmail(email)
    }

}