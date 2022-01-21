package com.mercadolivro.emuns

enum class Errors(
    val code: String,
    val message: String
) {
    ML101("ML-101", "Book [%s] does not exist"),
    ML102("ML-102", "Cannot update book with status [%s]"),
    ML201("ML-201", "Customer [%S] does not exist")
}