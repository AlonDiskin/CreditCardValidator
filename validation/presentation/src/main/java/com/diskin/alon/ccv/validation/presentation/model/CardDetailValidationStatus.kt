package com.diskin.alon.ccv.validation.presentation.model

/**
 * Describing the validation status for a credit card detail.
 *
 * @param isValid validation status.
 * @param errorMessage optional validation error message describing an invalid status.
 */
data class CardDetailValidationStatus private constructor(val isValid: Boolean, val errorMessage: String = "") {

    companion object {

        /**
         * Factory method for creating an instance of a valid card.
         */
        fun valid(): CardDetailValidationStatus
                = CardDetailValidationStatus(true)

        /**
         * Factory method for creating an instance of a invalid card.
         */
        fun invalid(invalidErrorMessage: String): CardDetailValidationStatus
                = CardDetailValidationStatus(false,invalidErrorMessage)
    }
}