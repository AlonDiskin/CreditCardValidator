package com.diskin.alon.ccv.validation.services.usecase

import com.diskin.alon.ccv.validation.domain.validateCardExpiry
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Validates card expiry date.
 */
class CardExpiryValidationUseCase : UseCase<Calendar,Single<Boolean>> {

    override fun execute(param: Calendar) =
        Single.create<Boolean> { emitter -> emitter.onSuccess(validateCardExpiry(param)) }
            .subscribeOn(Schedulers.computation())
}