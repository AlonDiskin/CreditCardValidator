package com.diskin.alon.ccv.di

import com.diskin.alon.ccv.validation.presentation.controller.CardValidationActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [ValidationFeatureModule::class])
interface ValidationSubcomponent : AndroidInjector<CardValidationActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<CardValidationActivity>()
}