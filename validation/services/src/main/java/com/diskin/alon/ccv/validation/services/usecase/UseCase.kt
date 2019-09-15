package com.diskin.alon.ccv.validation.services.usecase

/**
 * Application use case contract.
 *
 * @param P use case input type.
 * @param R use case result type.
 */
interface UseCase<P : Any,R : Any> {

    /**
     * Executes use case.
     *
     * @param param data required for use case execution.
     */
    fun execute(param: P): R
}