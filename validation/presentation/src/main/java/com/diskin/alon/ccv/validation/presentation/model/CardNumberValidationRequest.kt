package com.diskin.alon.ccv.validation.presentation.model

import com.diskin.alon.ccv.validation.presentation.util.ServiceRequest
import io.reactivex.Observable

data class CardNumberValidationRequest(val cardType: CardType, val number: String) :
    ServiceRequest<Pair<CardType, String>, Observable<CardValidationStatus>>(Pair(cardType, String()))