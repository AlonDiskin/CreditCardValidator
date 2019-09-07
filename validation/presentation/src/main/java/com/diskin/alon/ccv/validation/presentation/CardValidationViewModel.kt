package com.diskin.alon.ccv.validation.presentation

import androidx.lifecycle.LiveData

/**
 * Card validation screen view model contract.
 */
interface CardValidationViewModel {

    /**
     * Returns the current [CardType].
     */
    fun getCardType(): CardType

    /**
     * Set the card type value.
     */
    fun setCardType(cardType: CardType)

    /**
     * Get card detail validation status.
     *
     * @return [LiveData] whether card is valid or not.
     */
    fun isCardValid(): LiveData<Boolean>

    /**
     * Set the card number
     */
    fun setCardNumber(number: String)

    /**
     * Get the current card number
     */
    fun getCardNumber(): String

    /**
     * Get card number validation state.
     *
     * @return [LiveData] whether card number is valid or not.
     */
    fun isCardNumberValid(): LiveData<Boolean>

    /**
     * Set the card cvc number.
     */
    fun setCardCvc(cvc: String)

    /**
     * Get current card cvc number.
     */
    fun getCardCvc(): String

    /**
     * Get card cvc validation state.
     *
     * @return [LiveData] whether card cvc is valid or not.
     */
    fun isCardCvcValid(): LiveData<Boolean>

    /**
     * Set the card expiry date.
     */
    fun setCardExpiry(expiry: String)

    /**
     * Get the current card expiry date.
     */
    fun getCardExpiry(): String

    /**
     * Get card expiry date validation state.
     *
     * @return [LiveData] whether card cvc is valid or not.
     */
    fun isCardExpiryValid(): LiveData<Boolean>
}