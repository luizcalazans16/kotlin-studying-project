package com.mercadolivro.model

import com.mercadolivro.emuns.BookStatus
import com.mercadolivro.emuns.Errors
import com.mercadolivro.exception.BadRequestException
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
            throw BadRequestException(Errors.ML102.message.format(field), Errors.ML102.code)

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