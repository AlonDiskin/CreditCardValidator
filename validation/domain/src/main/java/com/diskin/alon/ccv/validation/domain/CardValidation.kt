package com.diskin.alon.ccv.validation.domain

import com.diskin.alon.ccv.validation.domain.ValidatedCardType.*
import java.util.*
import java.util.regex.Pattern

/**
 * Known credit card types.
 *
 * @param cvcPattern expected cvc code pattern for each type.
 * @param numberPattern expected card number pattern for each type.
 */
enum class ValidatedCardType(val cvcPattern: Pattern, val numberPattern: Pattern) {
    VISA(Pattern.compile("^[0-9]{3}$"),Pattern.compile("^4[0-9]{12}(?:[0-9]{3})?$")),
    MASTER_CARD(Pattern.compile("^[0-9]{3}$"), Pattern.compile("^5[1-5][0-9]{14}$")),
    AMERICAN_EXPRESS(Pattern.compile("^[0-9]{4}$"),Pattern.compile("^3[47][0-9]{13}$"))
}

/**
 * Validates card cvc code.
 *
 * @param code string containing the card cvc code.
 * @param type the card type.
 * @return true if cvc code is valid, false otherwise
 */
fun validateCardCvcCode(code: String, type: ValidatedCardType) =
    // validate by matching expected code pattern
    when(type) {
        VISA -> VISA.cvcPattern.matcher(code).matches()
        MASTER_CARD -> MASTER_CARD.cvcPattern.matcher(code).matches()
        AMERICAN_EXPRESS -> AMERICAN_EXPRESS.cvcPattern.matcher(code).matches()
    }

/**
 * Validates card number.
 *
 * @param number string containing the card number .
 * @param type the card type.
 * @return true if card number is valid, false otherwise
 */
fun validateCardNumber(number: String, type: ValidatedCardType) =
    // validate by matching expected number pattern and checksum verification
    when(type) {
        VISA -> VISA.numberPattern.matcher(number).matches()
                && checkCardChecksum(numberToDigits(number))

        MASTER_CARD -> MASTER_CARD.numberPattern.matcher(number).matches()
                && checkCardChecksum(numberToDigits(number))

        AMERICAN_EXPRESS -> AMERICAN_EXPRESS.numberPattern.matcher(number).matches()
                && checkCardChecksum(numberToDigits(number))
    }

/**
 * Validates card expiry date.
 *
 * @param calendar calendar representation of card expiry. Given instance must set its day of month
 * to '1'.
 * @return true if card is not expired, false otherwise
 */
fun validateCardExpiry(calendar: Calendar) =
    if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
        calendar >= Calendar.getInstance()

    } else {
        false
    }

private fun numberToDigits(number: String): IntArray {
    val charArray = number.toCharArray()
    val digits = IntArray(charArray.size) { i -> 0 }

    for ((index, value) in charArray.withIndex()) {
        digits[index] = value.toString().toInt()
    }

    return digits
}

/**
 * Luhn Algorithm Check for credit card number validation.
 */
fun checkCardChecksum(digits: IntArray): Boolean {
    var sum = 0
    val length = digits.size

    for (i in 0 until length) {
        var digit = digits[length - i - 1]

        if (i % 2 == 1) {
            digit *= 2
        }

        sum += if (digit > 9) digit - 9 else digit
    }

    return sum % 10 == 0
}


