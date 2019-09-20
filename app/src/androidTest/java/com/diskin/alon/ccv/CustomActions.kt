package com.diskin.alon.ccv

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tsongkha.spinnerdatepicker.DatePicker
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

fun setExpiry(month: Int, year: Int):ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return "set date"
        }

        override fun getConstraints(): Matcher<View> {
            return allOf<View>(isAssignableFrom(DatePicker::class.java), isDisplayed())
        }

        override fun perform(uiController: UiController?, view: View?) {
            val datePicker: DatePicker = view as DatePicker
            val method = DatePicker::class.java.getDeclaredMethod("updateDate",
                Int::class.java,Int::class.java,Int::class.java)

            method.isAccessible = true
            method.invoke(datePicker,year,month,1)
        }
    }
}