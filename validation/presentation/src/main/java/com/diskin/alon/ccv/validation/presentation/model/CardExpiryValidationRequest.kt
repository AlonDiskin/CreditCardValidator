package com.diskin.alon.ccv.validation.presentation.model

import com.diskin.alon.ccv.validation.presentation.util.ServiceRequest
import io.reactivex.Single
import java.util.*

data class CardExpiryValidationRequest(val expiry: Calendar) :
    ServiceRequest<Calendar, Single<Boolean>>(expiry)