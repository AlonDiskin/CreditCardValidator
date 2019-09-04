package com.diskin.alon.ccv

import com.mauriciotogneri.greencoffee.GreenCoffeeSteps
import com.mauriciotogneri.greencoffee.annotations.And
import com.mauriciotogneri.greencoffee.annotations.Given
import com.mauriciotogneri.greencoffee.annotations.Then

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

    @Given("User enters an invalid card \"([^\"]*)\" of type \"([^\"]*)\"\$")
    fun userEntersAnInvalidCardNumberOfTypeType(arg0: String, arg1: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @And("^Enters \"([^\"]*)\" and \"([^\"]*)\" date\$")
    fun entersCvcAndExpiresDate(arg0: String, arg1: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Then("Card should not be validated")
    fun cardShouldNotBeValidated() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}