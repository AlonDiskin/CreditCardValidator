package com.diskin.alon.ccv.validation.presentation.model

import com.diskin.alon.ccv.validation.presentation.util.ServiceRequest
import com.diskin.alon.ccv.validation.services.model.CardType
import com.diskin.alon.ccv.validation.services.usecase.CardNumberValidationUseCase.Params
import io.reactivex.Single

data class CardNumberValidationRequest(val cardType: CardType, val number: String) :
    ServiceRequest<Params, Single<Boolean>>(Params(number, cardType))