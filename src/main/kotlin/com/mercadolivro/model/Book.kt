package com.mercadolivro.model

import com.mercadolivro.emuns.BookStatus
import java.math.BigDecimal
import javax.persistence.*

@Entity(name = "book")
@Table(name = "book")
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column
    var name: String,

    @Column
    var price: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    var customer: Customer? = null
) {

    @Enumerated(EnumType.STRING)
    var status: BookStatus? = null
    set(value) {
        if(field == BookStatus.CANCELADO || field == BookStatus.DELETADO )
            throw Exception("Não é possível alterar um livro com status $field")

        field = value
    }

    constructor(id: Int? = null,
    name: String,
    price: BigDecimal,
    customer: Customer? = null,
    status: BookStatus?): this(id, name, price, customer) {
        this.status = status
    }
}