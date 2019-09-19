package com.diskin.alon.ccv.validation.presentation.model

import com.diskin.alon.ccv.validation.presentation.util.ServiceRequest
import com.diskin.alon.ccv.validation.services.model.CardType
import com.diskin.alon.ccv.validation.services.usecase.CardCvcValidationUseCase.Params
import io.reactivex.Single

data class CardCvcValidationRequest(val cardType: CardType, val cvc: String) :
    ServiceRequest<Params, Single<Boolean>>(Params(cvc,cardType))