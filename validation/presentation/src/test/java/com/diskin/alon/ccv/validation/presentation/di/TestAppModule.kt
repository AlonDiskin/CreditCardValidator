package com.diskin.alon.ccv.validation.presentation.di

import com.diskin.alon.ccv.validation.presentation.viewmodel.CardValidationViewModel
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import javax.inject.Singleton

@Module
class TestAppModule {

    @Singleton
    @Provides
    fun provideViewModel(): CardValidationViewModel = Mockito.mock(
        CardValidationViewModel::class.java)
}