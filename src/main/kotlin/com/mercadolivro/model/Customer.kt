package com.mercadolivro.model

import javax.persistence.*

@Entity(name = "customer")
@Table(name = "customer")
data class Customer(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column
    var name: String,

    @Column
    var email: String
)