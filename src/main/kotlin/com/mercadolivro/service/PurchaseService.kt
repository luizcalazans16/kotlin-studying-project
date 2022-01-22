package com.mercadolivro.service

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.model.Purchase
import com.mercadolivro.repository.PurchaseRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    fun create(purchase: Purchase) {
        purchaseRepository.save(purchase)

        applicationEventPublisher.publishEvent(PurchaseEvent(this, purchase))
    }

    fun update(purchase: Purchase) {
        purchaseRepository.save(purchase)
    }
}
