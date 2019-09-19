package com.diskin.alon.ccv.di

import androidx.lifecycle.ViewModelProviders
import com.diskin.alon.ccv.validation.presentation.controller.CardValidationActivity
import com.diskin.alon.ccv.validation.presentation.model.CardCvcValidationRequest
import com.diskin.alon.ccv.validation.presentation.model.CardExpiryValidationRequest
import com.diskin.alon.ccv.validation.presentation.model.CardNumberValidationRequest
import com.diskin.alon.ccv.validation.presentation.util.ServiceExecutor
import com.diskin.alon.ccv.validation.presentation.util.ServiceRequest
import com.diskin.alon.ccv.validation.presentation.util.UseCaseMediator
import com.diskin.alon.ccv.validation.presentation.viewmodel.CardValidationViewModel
import com.diskin.alon.ccv.validation.presentation.viewmodel.CardValidationViewModelImpl
import com.diskin.alon.ccv.validation.services.usecase.CardCvcValidationUseCase
import com.diskin.alon.ccv.validation.services.usecase.CardExpiryValidationUseCase
import com.diskin.alon.ccv.validation.services.usecase.CardNumberValidationUseCase
import com.diskin.alon.ccv.validation.services.usecase.UseCase
import dagger.Module
import dagger.Provides

@Module
class ValidationFeatureModule {

    @Provides
    fun providesServiceExecutor(): ServiceExecutor {
        val dispatch = HashMap<Class<out ServiceRequest<*, *>>, UseCase<*, *>>()

        dispatch.put(CardNumberValidationRequest::class.java,CardNumberValidationUseCase())
        dispatch.put(CardCvcValidationRequest::class.java,CardCvcValidationUseCase())
        dispatch.put(CardExpiryValidationRequest::class.java,CardExpiryValidationUseCase())

        return UseCaseMediator(dispatch)
    }

    @Provides
    fun providesCardValidationViewModel(activity: CardValidationActivity,
                                        factory: ValidationViewModelFactory)
            : CardValidationViewModel {
        return ViewModelProviders.of(activity,factory).get(CardValidationViewModelImpl::class.java)
    }
}