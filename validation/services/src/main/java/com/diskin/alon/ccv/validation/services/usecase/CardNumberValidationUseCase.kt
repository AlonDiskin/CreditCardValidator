package com.diskin.alon.ccv.validation.services.usecase

import com.diskin.alon.ccv.validation.domain.validateCardNumber
import com.diskin.alon.ccv.validation.services.util.mapToDomainType
import com.diskin.alon.ccv.validation.services.model.CardTypeDto
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Performs credit card number validation.
 */
class CardNumberValidationUseCase : UseCase<CardNumberValidationUseCase.Params, Single<Boolean>> {

    override fun execute(param: Params) = Single.create<Boolean> { emitter ->
            emitter.onSuccess(validateCardNumber(param.number, param.cardType.mapToDomainType()))

        }.subscribeOn(Schedulers.computation())

    data class Params(val number: String, val cardType: CardTypeDto)
}