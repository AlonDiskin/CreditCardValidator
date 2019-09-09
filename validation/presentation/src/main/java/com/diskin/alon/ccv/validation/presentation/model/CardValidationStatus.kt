package com.diskin.alon.ccv.validation.presentation.model

/**
 * Describing the validation status of a credit card card.
 *
 * @param isValid validation status.
 * @param errorMessage optional validation error message describing an invalid status.
 */
data class CardValidationStatus private constructor(val isValid: Boolean, val errorMessage: String = "") {

    companion object {

        /**
         * Factory method for creating an instance of a valid card.
         */
        fun valid(): CardValidationStatus
                = CardValidationStatus(true)

        /**
         * Factory method for creating an instance of a invalid card.
         */
        fun invalid(invalidErrorMessage: String): CardValidationStatus
                = CardValidationStatus(false,invalidErrorMessage)
    }
}