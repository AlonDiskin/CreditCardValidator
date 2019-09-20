package com.diskin.alon.ccv

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps
import com.mauriciotogneri.greencoffee.annotations.And
import com.mauriciotogneri.greencoffee.annotations.Given
import com.mauriciotogneri.greencoffee.annotations.Then
import com.tsongkha.spinnerdatepicker.DatePicker
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers

/**
 * 'Card not validated' feature rule step definitions.
 */
class CardNotValidatedSteps : GreenCoffeeSteps() {

    @Given("User is in device home screen")
    fun userIsInDeviceHomeScreen() {
        openDeviceHome()
    }

    @And("User launch application")
    fun userLaunchApplication() {
        launchApp()
    }

    @And("^User entered card number \"([^\"]*)\",type \"([^\"]*)\", cvc \"([^\"]*)\", expiry month \"([^\"]*)\" year \"([^\"]*)\"$")
    fun userEnteredCardNumberTypeCvcExpiryMonthYear(number: String, type: String, cvc: String, expiryMonth: String, expiryYear: String) {
        //enter card type
        when(type) {
            "Visa" -> onView(withText(R.string.radio_visa_label))
                .perform(click())

            "MasterCard" -> onView(withText(R.string.radio_mastercard_label))
                .perform(click())

            "American Express" -> onView(withText(R.string.radio_american_express_label))
                .perform(click())
        }

        //enter card number
        onView(withId(R.id.card_number_input_edit))
            .perform(typeText(number))

        //enter card cvc code
        onView(withId(R.id.card_cvc_input_edit))
            .perform(typeText(cvc))

        //enter card expiry month and year
        onView(withContentDescription(R.string.expiry_icon_description))
            .perform(click())

        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
            .inRoot(RootMatchers.isDialog())
            .perform(setExpiry(expiryMonth.toInt() - 1,expiryYear.toInt()))

        onView(withText("OK"))
            .inRoot(RootMatchers.isDialog())
            .perform(click())
    }

    @And("^User Rotates device to landscape$")
    fun userRotatesDeviceToLandscape() {
        rotateDeviceToLandsacpe()
    }

    @Then("^Card should not be validated by \"([^\"]*)\"$")
    fun cardShouldNotBeValidatedBy(wrongDetail: String) {
        val context = ApplicationProvider.getApplicationContext<Context>()

        when(wrongDetail) {
            "cvc" -> {
                onView(withText(context.getString(R.string.valid_card_number)))
                    .check(ViewAssertions.matches(isDisplayed()))
                onView(withText(context.getString(R.string.invalid_card_cvc)))
                    .check(ViewAssertions.matches(isDisplayed()))
                onView(withText(context.getString(R.string.valid_card_expiry)))
                    .check(ViewAssertions.matches(isDisplayed()))
            }

            "number" -> {
                onView(withText(context.getString(R.string.invalid_card_number)))
                    .check(ViewAssertions.matches(isDisplayed()))
                onView(withText(context.getString(R.string.valid_card_cvc)))
                    .check(ViewAssertions.matches(isDisplayed()))
                onView(withText(context.getString(R.string.valid_card_expiry)))
                    .check(ViewAssertions.matches(isDisplayed()))
            }

            "expiry" -> {
                onView(withText(context.getString(R.string.valid_card_number)))
                    .check(ViewAssertions.matches(isDisplayed()))
                onView(withText(context.getString(R.string.valid_card_cvc)))
                    .check(ViewAssertions.matches(isDisplayed()))
                onView(withText(context.getString(R.string.invalid_card_expiry)))
                    .check(ViewAssertions.matches(isDisplayed()))
            }

            "all" -> {
                onView(withText(context.getString(R.string.invalid_card_number)))
                    .check(ViewAssertions.matches(isDisplayed()))
                onView(withText(context.getString(R.string.invalid_card_cvc)))
                    .check(ViewAssertions.matches(isDisplayed()))
                onView(withText(context.getString(R.string.invalid_card_expiry)))
                    .check(ViewAssertions.matches(isDisplayed()))
            }
        }

        onView(withId(R.id.button_submit))
            .check(ViewAssertions.matches(not(isEnabled())))
    }
}