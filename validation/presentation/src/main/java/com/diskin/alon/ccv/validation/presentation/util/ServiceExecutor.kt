package com.diskin.alon.ccv.validation.presentation.util

/**
 * Application services executor contract.
 */
interface ServiceExecutor {

    /**
     * Executes a service request.
     *
     * @param request the request to execute.
     * @return [R] the contracted request result.
     * @throws IllegalArgumentException if request is unknown to executor.
     */
    fun <R : Any, P : Any> execute(request: ServiceRequest<P, R>): R
}