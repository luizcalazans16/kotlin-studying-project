package com.mercadolivro.service

import com.mercadolivro.emuns.BookStatus
import com.mercadolivro.model.Book
import com.mercadolivro.model.Customer
import com.mercadolivro.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService(
    val bookRepository: BookRepository
) {
    fun create(book: Book) {
        bookRepository.save(book)
    }

    fun findAll(pageable: Pageable): Page<Book> {
        return bookRepository.findAll(pageable)
    }

    fun findById(id: Int): Book {
        return bookRepository.findById(id).orElseThrow()
    }

    fun findActives(pageable: Pageable): Page<Book> {
        return bookRepository.findByStatus(BookStatus.ATIVO, pageable)
    }

    fun updateBook(book: Book) {
        bookRepository.save(book)
    }

    fun delete(id: Int) {
        val book = findById(id)

        book.status = BookStatus.CANCELADO

        updateBook(book)
    }

    fun deleteByCustomer(customer: Customer) {
        val books = bookRepository.findByCustomer(customer)
        books.forEach { b ->
            b.status = BookStatus.DELETADO
        }
        bookRepository.saveAll(books)
    }
}