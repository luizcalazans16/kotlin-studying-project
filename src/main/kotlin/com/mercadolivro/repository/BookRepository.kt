package com.mercadolivro.repository

import com.mercadolivro.emuns.BookStatus
import com.mercadolivro.model.Book
import com.mercadolivro.model.Customer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<Book, Int> {

    fun findByStatus(status: BookStatus, pageable: Pageable): Page<Book>

    fun findByCustomer(customer: Customer): List<Book>
}