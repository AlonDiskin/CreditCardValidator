package com.diskin.alon.ccv.validation.presentation.model

import com.diskin.alon.ccv.validation.presentation.util.ServiceRequest
import io.reactivex.Observable

data class CardCvcValidationRequest(val cardType: CardType, val cvc: String) :
    ServiceRequest<Pair<CardType, String>, Observable<CardDetailValidationStatus>>(Pair(cardType, String()))