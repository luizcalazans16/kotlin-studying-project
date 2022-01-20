package com.mercadolivro.controller

import com.mercadolivro.controller.request.CreateBookRequest
import com.mercadolivro.controller.request.UpdateBookRequest
import com.mercadolivro.extension.toBookEntity
import com.mercadolivro.model.Book
import com.mercadolivro.service.BookService
import com.mercadolivro.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("books")
class BookController(
    val bookService: BookService,
    val customerService: CustomerService
) {

    @GetMapping
    fun findAll(): List<Book> {
        return bookService.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): Book {
        return bookService.findById(id)
    }

    @GetMapping("/active")
    fun findActives(): List<Book> {
        return bookService.findActives()

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateBookRequest) {
        val customer = customerService.findById(request.customerId)

        bookService.create(request.toBookEntity(customer))

    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable id: Int, @RequestBody request: UpdateBookRequest) {
        val savedBook = bookService.findById(id)
        bookService.updateBook(request.toBookEntity(savedBook))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Int) {
        bookService.delete(id)
    }
}