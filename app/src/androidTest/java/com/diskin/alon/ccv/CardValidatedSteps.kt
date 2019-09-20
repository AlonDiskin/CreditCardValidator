package com.diskin.alon.ccv

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import com.mauriciotogneri.greencoffee.GreenCoffeeSteps
import com.mauriciotogneri.greencoffee.annotations.And
import com.mauriciotogneri.greencoffee.annotations.Given
import com.mauriciotogneri.greencoffee.annotations.Then
import com.mauriciotogneri.greencoffee.annotations.When
import com.tsongkha.spinnerdatepicker.DatePicker
import org.hamcrest.Matchers

/**
 * 'Card validated' feature rule step definitions.
 */
class CardValidatedSteps : GreenCoffeeSteps(){

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
            .inRoot(isDialog())
            .perform(setExpiry(expiryMonth.toInt() - 1,expiryYear.toInt()))

        onView(withText("OK"))
            .inRoot(isDialog())
            .perform(click())
    }

    @And("^User Rotates device to landscape$")
    fun userRotatesDeviceToLandscape() {
        rotateDeviceToLandsacpe()
    }

    @Then("Card should be validated")
    fun cardShouldBeValidated() {
        // verify card detail  have been validated
        val context = ApplicationProvider.getApplicationContext<Context>()

        onView(withText(context.getString(R.string.valid_card_number)))
            .check(matches(isDisplayed()))
        onView(withText(context.getString(R.string.valid_card_cvc)))
            .check(matches(isDisplayed()))
        onView(withText(context.getString(R.string.valid_card_expiry)))
            .check(matches(isDisplayed()))
        onView(withId(R.id.button_submit))
            .check(matches(isEnabled()))
    }

    @When("^User submits card detail$")
    fun userSubmitsCardDetail() {
        onView(withId(R.id.button_submit))
            .perform(click())
    }

    @Then("^Detail input fields should be cleared$")
    fun detailInputFieldsShouldBeCleared() {
        onView(withId(R.id.card_number_input_edit))
            .check(matches(withText("")))
        onView(withId(R.id.card_cvc_input_edit))
            .check(matches(withText("")))
        onView(withId(R.id.card_expiry_input_edit))
            .check(matches(withText("")))
    }
}
