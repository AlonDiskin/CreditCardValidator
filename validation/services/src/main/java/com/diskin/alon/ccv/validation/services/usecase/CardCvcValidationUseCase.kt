package com.diskin.alon.ccv.validation.services.usecase

import com.diskin.alon.ccv.validation.domain.validateCardCvcCode
import com.diskin.alon.ccv.validation.services.model.CardTypeDto
import com.diskin.alon.ccv.validation.services.util.mapToDomainType
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Performs credit card cvc code validation.
 */
class CardCvcValidationUseCase : UseCase<CardCvcValidationUseCase.Params, Single<Boolean>> {

    override fun execute(param: Params) = Single.create<Boolean> { emitter ->
            emitter.onSuccess(validateCardCvcCode(param.cvcCode, param.cardType.mapToDomainType()))

        }.subscribeOn(Schedulers.computation())

    data class Params(val cvcCode: String, val cardType: CardTypeDto)
}