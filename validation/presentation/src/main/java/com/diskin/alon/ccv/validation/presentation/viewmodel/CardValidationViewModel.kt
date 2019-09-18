package com.diskin.alon.ccv.validation.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.diskin.alon.ccv.validation.presentation.model.CardType
import io.reactivex.subjects.BehaviorSubject
import java.util.*

/**
 * Card validation screen view model contract.
 */
interface CardValidationViewModel {

    /**
     * Credit card type.
     */
    val cardType: BehaviorSubject<CardType>

    /**
     * Credit card number.
     */
    val cardNumber: BehaviorSubject<String>

    /**
     * Credit card cvc number.
     */
    val cardCvc: BehaviorSubject<String>

    /**
     * Credit card expiry date.
     */
    val cardExpiry: BehaviorSubject<Calendar>

    /**
     * Credit card detail validation status.
     *
     * @return [LiveData] whether card is valid or not.
     */
    val isCardValid: LiveData<Boolean>

    /**
     * Credit card number validation status.
     *
     * @return [LiveData] containing number validation status.
     */
    val isCardNumberValid: LiveData<Boolean>

    /**
     * Credit card cvc validation status.
     *
     * @return [LiveData] containing card cvc validation status.
     */
    val isCardCvcValid: LiveData<Boolean>

    /**
     * Credit card expiry date validation status.
     *
     * @return [LiveData] containing card expiry validation status.
     */
    val isCardExpiryValid: LiveData<Boolean>
}