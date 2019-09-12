package com.diskin.alon.ccv.validation.presentation.model

import com.diskin.alon.ccv.validation.presentation.util.ServiceRequest
import io.reactivex.Observable

data class CardExpiryValidationRequest(val expiry: String) :
    ServiceRequest<String, Observable<CardDetailValidationStatus>>(expiry)