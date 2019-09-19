package com.diskin.alon.ccv.di

import com.diskin.alon.ccv.validation.presentation.controller.CardValidationActivity
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [ValidationSubcomponent::class])
abstract class ValidationActivityInjectionModule {

    @Binds
    @IntoMap
    @ClassKey(CardValidationActivity::class)
    internal abstract fun bindCardValidationAndroidInjectorFactory(builder: ValidationSubcomponent.Builder): AndroidInjector.Factory<*>
}