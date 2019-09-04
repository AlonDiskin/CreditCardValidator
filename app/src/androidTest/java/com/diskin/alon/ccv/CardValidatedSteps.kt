package com.diskin.alon.ccv

import com.mauriciotogneri.greencoffee.GreenCoffeeSteps
import com.mauriciotogneri.greencoffee.annotations.And
import com.mauriciotogneri.greencoffee.annotations.Given
import com.mauriciotogneri.greencoffee.annotations.Then

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

    @Given("^User enters a valid card \"([^\"]*)\" of type \"([^\"]*)\"\$")
    fun userEntersAValidCardNumberOfTypeType(arg0: String, arg1: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @And("^Enters \"([^\"]*)\" and \"([^\"]*)\" date\$")
    fun entersCvcAndExpiresDate(arg0: String, arg1: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Then("Card should be validated")
    fun cardShouldBeValidated() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
