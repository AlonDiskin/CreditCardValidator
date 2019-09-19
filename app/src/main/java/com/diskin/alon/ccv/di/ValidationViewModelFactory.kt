package com.diskin.alon.ccv.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.diskin.alon.ccv.validation.presentation.util.ServiceExecutor
import com.diskin.alon.ccv.validation.presentation.viewmodel.CardValidationViewModelImpl
import javax.inject.Inject
import javax.inject.Provider

class ValidationViewModelFactory @Inject constructor(private val serviceExecutorProvider: Provider<ServiceExecutor>) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CardValidationViewModelImpl(serviceExecutorProvider.get()) as T
    }
}