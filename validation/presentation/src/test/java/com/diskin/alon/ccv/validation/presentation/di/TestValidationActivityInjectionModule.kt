package com.diskin.alon.ccv.validation.presentation.di

import com.diskin.alon.ccv.validation.presentation.controller.CardValidationActivity
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector

@Module(includes = [AndroidInjectionModule::class])
abstract class TestValidationActivityInjectionModule {

    @ContributesAndroidInjector
    abstract fun contributeValidationActivityInjector(): CardValidationActivity
}