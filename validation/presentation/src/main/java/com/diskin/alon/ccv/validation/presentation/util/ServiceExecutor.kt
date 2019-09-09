package com.diskin.alon.ccv.validation.presentation.util

interface ServiceExecutor {

    fun <R> execute(request: ServiceRequest<*, R>): R
}