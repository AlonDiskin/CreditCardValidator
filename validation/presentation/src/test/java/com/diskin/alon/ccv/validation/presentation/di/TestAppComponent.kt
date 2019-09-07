package com.diskin.alon.ccv.validation.presentation.di

import com.diskin.alon.ccv.validation.presentation.TestApplication
import com.diskin.alon.ccv.validation.presentation.CardValidationActivityTest
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [TestValidationActivityInjectionModule::class,TestAppModule::class])
interface TestAppComponent : AndroidInjector<TestApplication>{

    fun inject(test: CardValidationActivityTest)
}