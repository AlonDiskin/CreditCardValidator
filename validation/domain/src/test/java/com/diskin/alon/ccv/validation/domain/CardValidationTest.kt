package com.diskin.alon.ccv.validation.domain

import com.google.common.truth.Truth.assertThat
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(JUnitParamsRunner::class)
class CardValidationTest {

    @Test
    @Parameters(method = "cardCvcValidationParams")
    fun shouldValidateCardCvcCode(code: String, type: ValidatedCardType, isValid: Boolean) {
        assertThat(validateCardCvcCode(code,type)).isEqualTo(isValid)
    }

    @Test
    @Parameters(method = "cardNumberValidationParams")
    fun shouldValidateCardNumber(number: String, type: ValidatedCardType, isValid: Boolean) {
        assertThat(validateCardNumber(number,type)).isEqualTo(isValid)
    }

    @Test
    @Parameters(method = "cardExpiryValidationParams")
    fun shouldValidateCardExpiryDate(calendar: Calendar, isValid: Boolean) {
        assertThat(validateCardExpiry(calendar)).isEqualTo(isValid)
    }

    fun cardCvcValidationParams() = arrayOf(
        arrayOf("12A34er", ValidatedCardType.VISA, false),
        arrayOf("1234", ValidatedCardType.VISA, false),
        arrayOf("12", ValidatedCardType.VISA, false),
        arrayOf("0", ValidatedCardType.VISA, false),
        arrayOf("-1234596", ValidatedCardType.VISA, false),
        arrayOf("111", ValidatedCardType.VISA, true),
        arrayOf("000", ValidatedCardType.VISA, true),
        arrayOf("12A34er", ValidatedCardType.MASTER_CARD, false),
        arrayOf("3456", ValidatedCardType.MASTER_CARD, false),
        arrayOf("-8", ValidatedCardType.MASTER_CARD, false),
        arrayOf("0", ValidatedCardType.MASTER_CARD, false),
        arrayOf("-196", ValidatedCardType.MASTER_CARD, false),
        arrayOf("100", ValidatedCardType.MASTER_CARD, true),
        arrayOf("159", ValidatedCardType.MASTER_CARD, true),
        arrayOf("12A34er", ValidatedCardType.AMERICAN_EXPRESS, false),
        arrayOf("456", ValidatedCardType.AMERICAN_EXPRESS, false),
        arrayOf("-4", ValidatedCardType.AMERICAN_EXPRESS, false),
        arrayOf("0", ValidatedCardType.AMERICAN_EXPRESS, false),
        arrayOf("-1986", ValidatedCardType.AMERICAN_EXPRESS, false),
        arrayOf("1001", ValidatedCardType.AMERICAN_EXPRESS, true),
        arrayOf("1234", ValidatedCardType.AMERICAN_EXPRESS, true))

    fun cardNumberValidationParams() = arrayOf(
        arrayOf("4111111111111111", ValidatedCardType.VISA, true),
        arrayOf("1111111111111111", ValidatedCardType.VISA, false),
        arrayOf("5555555555554444", ValidatedCardType.MASTER_CARD, true),
        arrayOf("9555555555554444", ValidatedCardType.MASTER_CARD, false),
        arrayOf("371449635398431", ValidatedCardType.AMERICAN_EXPRESS, true),
        arrayOf("171449635398431", ValidatedCardType.AMERICAN_EXPRESS, false))

    fun cardExpiryValidationParams() = arrayOf(
        arrayOf(Calendar.getInstance(),Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 1),
        arrayOf(getUnExpired1(),true),
        arrayOf(getUnExpired2(),true),
        arrayOf(getExpired(),false),
        arrayOf(getExpired2(),false),
        arrayOf(getIncorrectDate(),false))

    private fun getUnExpired1(): Calendar {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_MONTH,1)
        calendar.add(Calendar.MONTH,1)
        return calendar
    }

    private fun getUnExpired2(): Calendar {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_MONTH,1)
        calendar.add(Calendar.YEAR,1)
        return calendar
    }

    private fun getExpired(): Calendar {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_MONTH,1)
        calendar.add(Calendar.MONTH,-2)
        return calendar
    }

    private fun getExpired2(): Calendar {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_MONTH,1)
        calendar.add(Calendar.YEAR,-2)
        return calendar
    }

    private fun getIncorrectDate(): Calendar {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_MONTH,5)
        return calendar
    }
}