package com.diskin.alon.ccv.di

import com.diskin.alon.ccv.CCVApp
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class,ValidationActivityInjectionModule::class])
interface AppComponent : AndroidInjector<CCVApp> {
}