package com.mercadolivro.repository

import com.mercadolivro.model.Purchase
import org.springframework.data.repository.CrudRepository

interface PurchaseRepository : CrudRepository<Purchase, Int>
