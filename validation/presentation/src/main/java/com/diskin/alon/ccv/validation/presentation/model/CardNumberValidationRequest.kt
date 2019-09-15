package com.diskin.alon.ccv.validation.presentation.model

import com.diskin.alon.ccv.validation.presentation.util.ServiceRequest
import io.reactivex.Single

data class CardNumberValidationRequest(val cardType: CardType, val number: String) :
    ServiceRequest<Pair<CardType, String>, Single<CardDetailValidationStatus>>(Pair(cardType, String()))