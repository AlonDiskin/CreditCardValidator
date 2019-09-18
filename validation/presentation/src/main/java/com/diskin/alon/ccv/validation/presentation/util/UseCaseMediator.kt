package com.diskin.alon.ccv.validation.presentation.util

import com.diskin.alon.ccv.validation.services.usecase.UseCase
import java.lang.IllegalArgumentException

/**
 * Application services executor with mediator pattern implementation.
 *
 * @param dispatch a map that contains all known service requests and their
 * matching use cases, that are able to fulfill those requests.
 */
class UseCaseMediator(private val dispatch: Map<Class<out ServiceRequest<*,*>>,UseCase<*,*>>) : ServiceExecutor {

    override fun <R : Any, P : Any> execute(request: ServiceRequest<P, R>): R {
        val requestClass: Class<ServiceRequest<*,*>> = request.javaClass

        if (dispatch.containsKey(requestClass)) {
            try {
                val usaCase: UseCase<P,R> = dispatch.get(requestClass) as UseCase<P, R>

                return usaCase.execute(request.param)

            } catch (exception: Exception) {
                throw IllegalArgumentException("Unable to serve request, use case expected types don't match request")
            }

        } else {
            throw IllegalArgumentException("Unknown request!")
        }
    }
}