package com.diskin.alon.ccv.validation.presentation.model

import com.diskin.alon.ccv.validation.presentation.util.ServiceRequest
import io.reactivex.Single

data class CardCvcValidationRequest(val cardType: CardType, val cvc: String) :
    ServiceRequest<Pair<CardType, String>, Single<Boolean>>(Pair(cardType, String()))