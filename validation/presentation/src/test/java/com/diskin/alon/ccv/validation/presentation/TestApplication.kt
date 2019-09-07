package com.diskin.alon.ccv.validation.presentation

import android.app.Activity
import android.app.Application
import com.diskin.alon.ccv.validation.presentation.di.DaggerTestAppComponent
import com.diskin.alon.ccv.validation.presentation.di.TestAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import org.robolectric.TestLifecycleApplication
import java.lang.reflect.Method
import javax.inject.Inject

class TestApplication : Application() , HasActivityInjector, TestLifecycleApplication {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    private val testAppComponent: TestAppComponent = DaggerTestAppComponent.create()

    override fun onCreate() {
        super.onCreate()
        testAppComponent.inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    override fun beforeTest(method: Method?) {
    }

    override fun prepareTest(test: Any?) {
        testAppComponent.inject(test as CardValidationActivityTest)
    }

    override fun afterTest(method: Method?) {

    }
}