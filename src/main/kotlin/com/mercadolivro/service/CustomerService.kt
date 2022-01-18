package com.mercadolivro.service

import com.mercadolivro.model.Customer
import com.mercadolivro.repository.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(
    val customerRepository: CustomerRepository
) {
    fun getAll(name: String?): List<Customer> {
        name?.let {
            return customerRepository.findByName(name)
        }
        return customerRepository.findAll().toList()
    }

    fun getCustomerById(id: Int): Customer {
        return customerRepository.findById(id).orElseThrow()
    }

    fun create(customer: Customer) {
        customerRepository.save(customer)
    }

    fun update(customer: Customer) {
        if (!customerRepository.existsById(customer.id!!)) {
            throw Exception("Não existe cliente com o id informado")
        }
        customerRepository.save(customer)
    }

    fun delete(id: Int) {
        if (!customerRepository.existsById(id)) {
            throw Exception("Não existe cliente com o id informado")
        }
        customerRepository.deleteById(id)
    }

}