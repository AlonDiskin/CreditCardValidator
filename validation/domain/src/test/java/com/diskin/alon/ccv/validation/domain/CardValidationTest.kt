package com.diskin.alon.ccv.validation.domain

import com.google.common.truth.Truth.assertThat
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.Month
import java.time.Year

@RunWith(JUnitParamsRunner::class)
class CardValidationTest {

    @Test
    @Parameters(method = "cardCvcValidationParams")
    fun shouldValidateCardCvcCode(code: String, type: CardType, isValid: Boolean) {
        assertThat(validateCardCvcCode(code,type)).isEqualTo(isValid)
    }

    @Test
    @Parameters(method = "cardNumberValidationParams")
    fun shouldValidateCardNumber(number: String, type: CardType, isValid: Boolean) {
        assertThat(validateCardNumber(number,type)).isEqualTo(isValid)
    }

    @Test
    @Parameters(method = "cardExpiryValidationParams")
    fun shouldValidateCardExpiryDate(month: Month, year: Year, isValid: Boolean) {
        assertThat(validateCardExpiry(month, year)).isEqualTo(isValid)
    }

    fun cardCvcValidationParams() = arrayOf(
        arrayOf("12A34er", CardType.VISA, false),
        arrayOf("1234", CardType.VISA, false),
        arrayOf("12", CardType.VISA, false),
        arrayOf("0", CardType.VISA, false),
        arrayOf("-1234596", CardType.VISA, false),
        arrayOf("111", CardType.VISA, true),
        arrayOf("000", CardType.VISA, true),
        arrayOf("12A34er", CardType.MASTER_CARD, false),
        arrayOf("3456", CardType.MASTER_CARD, false),
        arrayOf("-8", CardType.MASTER_CARD, false),
        arrayOf("0", CardType.MASTER_CARD, false),
        arrayOf("-196", CardType.MASTER_CARD, false),
        arrayOf("100", CardType.MASTER_CARD, true),
        arrayOf("159", CardType.MASTER_CARD, true),
        arrayOf("12A34er", CardType.AMERICAN_EXPRESS, false),
        arrayOf("456", CardType.AMERICAN_EXPRESS, false),
        arrayOf("-4", CardType.AMERICAN_EXPRESS, false),
        arrayOf("0", CardType.AMERICAN_EXPRESS, false),
        arrayOf("-1986", CardType.AMERICAN_EXPRESS, false),
        arrayOf("1001", CardType.AMERICAN_EXPRESS, true),
        arrayOf("1234", CardType.AMERICAN_EXPRESS, true))

    fun cardNumberValidationParams() = arrayOf(
        arrayOf("4111111111111111", CardType.VISA, true),
        arrayOf("1111111111111111", CardType.VISA, false),
        arrayOf("5555555555554444", CardType.MASTER_CARD, true),
        arrayOf("9555555555554444", CardType.MASTER_CARD, false),
        arrayOf("371449635398431", CardType.AMERICAN_EXPRESS, true),
        arrayOf("171449635398431", CardType.AMERICAN_EXPRESS, false))

    fun cardExpiryValidationParams() = arrayOf(
        arrayOf(LocalDate.now().month, Year.of(LocalDate.now().year), LocalDate.now().dayOfMonth == 1),
        arrayOf(LocalDate.now().month, Year.of(LocalDate.now().plusYears(1).year),true),
        arrayOf(LocalDate.now().plusMonths(5).month, Year.of(LocalDate.now().plusMonths(5).year),true),
        arrayOf(LocalDate.now().month, Year.of(LocalDate.now().minusYears(1).year),false),
        arrayOf(LocalDate.now().minusMonths(1).month, Year.of(LocalDate.now().year),false))
}