package com.diskin.alon.ccv.validation.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.diskin.alon.ccv.validation.presentation.model.CardType
import com.diskin.alon.ccv.validation.presentation.model.CardValidationStatus

/**
 * Card validation screen view model contract.
 */
interface CardValidationViewModel {

    /**
     * Credit card type.
     */
    var cardType: CardType

    /**
     * Credit card number.
     */
    var cardNumber: String

    /**
     * Credit card cvc number.
     */
    var cardCvc: String

    /**
     * Credit card expiry date.
     */
    var cardExpiry: String

    /**
     * Credit card detail validation status.
     *
     * @return [LiveData] whether card is valid or not.
     */
    val isCardValid: LiveData<Boolean>

    /**
     * Credit card number validation status.
     *
     * @return [LiveData] containing number validation status data.
     */
    val isCardNumberValid: LiveData<CardValidationStatus>

    /**
     * Credit card cvc validation status.
     *
     * @return [LiveData] containing card cvc validation status data.
     */
    val isCardCvcValid: LiveData<CardValidationStatus>

    /**
     * Credit card expiry date validation status.
     *
     * @return [LiveData] containing card expiry validation status data.
     */
    val isCardExpiryValid: LiveData<CardValidationStatus>
}