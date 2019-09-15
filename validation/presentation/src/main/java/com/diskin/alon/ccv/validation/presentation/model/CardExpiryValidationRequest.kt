package com.diskin.alon.ccv.validation.presentation.model

import com.diskin.alon.ccv.validation.presentation.util.ServiceRequest
import io.reactivex.Single

data class CardExpiryValidationRequest(val expiry: String) :
    ServiceRequest<String, Single<CardDetailValidationStatus>>(expiry)