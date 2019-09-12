package com.diskin.alon.ccv.validation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diskin.alon.ccv.validation.presentation.model.*
import com.diskin.alon.ccv.validation.presentation.util.ServiceExecutor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

/**
 * Card validation screen view model.
 */
class CardValidationViewModelImpl @Inject constructor(private val serviceExecutor: ServiceExecutor)
    : ViewModel(), CardValidationViewModel  {

    companion object {
        private val DEFAULT_CARD_TYPE = CardType.VISA
        private const val DEFAULT_CARD_NUMBER = ""
        private const val DEFAULT_CARD_CVC = ""
        private const val DEFAULT_CARD_EXPIRY = ""
    }

    private val _cardType: BehaviorSubject<CardType> = BehaviorSubject.createDefault(DEFAULT_CARD_TYPE)
    override var cardType: CardType
        get() = _cardType.value!! // throw exception if backing property wasn't initialized with default value
        set(value) {_cardType.onNext(value)}

    private val _cardNumber: BehaviorSubject<String> = BehaviorSubject.createDefault(DEFAULT_CARD_NUMBER)
    override var cardNumber: String
        get() = _cardNumber.value!! // throw exception if backing property wasn't initialized with default value
        set(value) {_cardNumber.onNext(value)}

    private val _cardCvc: BehaviorSubject<String> = BehaviorSubject.createDefault(DEFAULT_CARD_CVC)
    override var cardCvc: String
        get() = _cardCvc.value!! // throw exception if backing property wasn't initialized with default value
        set(value) {_cardCvc.onNext(value)}

    private val _cardExpiry: BehaviorSubject<String> = BehaviorSubject.createDefault(DEFAULT_CARD_EXPIRY)
    override var cardExpiry: String
        get() = _cardExpiry.value!! // throw exception if backing property wasn't initialized with default value
        set(value) {_cardExpiry.onNext(value)}

    private val _isCardNumberValid: MutableLiveData<CardDetailValidationStatus> = MutableLiveData()
    override val isCardNumberValid: LiveData<CardDetailValidationStatus> = _isCardNumberValid

    private val _isCardCvcValid: MutableLiveData<CardDetailValidationStatus> = MutableLiveData()
    override val isCardCvcValid: LiveData<CardDetailValidationStatus> = _isCardCvcValid

    private val _isCardExpiryValid: MutableLiveData<CardDetailValidationStatus> = MutableLiveData()
    override val isCardExpiryValid: LiveData<CardDetailValidationStatus> = _isCardExpiryValid

    private val _isCardValid: MutableLiveData<Boolean> = MutableLiveData()
    override val isCardValid: LiveData<Boolean> = _isCardValid

    private val compositeDisposable = CompositeDisposable()

    init {
        // create view input observables, and add subscriptions to disposables container
        compositeDisposable.addAll(subscribeCardNumberObservable(),
            subscribeCardCvcObservable(),
            subscribeCardExpiryObservable())
    }

    override fun onCleared() {
        super.onCleared()
        // dispose of all observable subscriptions
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    /**
     * Subscribes view model to an observable that update the validation status of card number, whenever
     * the number and card type properties are changed.
     */
    private fun subscribeCardNumberObservable() =
        Observable.combineLatest<CardType, String, CardNumberValidationRequest>(
            _cardType,
            _cardNumber,
            BiFunction { type, number -> CardNumberValidationRequest(type, number) })
            .switchMap { request -> serviceExecutor.execute(request) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ status ->
                _isCardNumberValid.value = status
                updateCardValidation() },
                { it.printStackTrace() })

    /**
     * Subscribes view model to an observable that update the validation status of card cvc, whenever
     * the cvc number and card type properties are changed.
     */
    private fun subscribeCardCvcObservable() =
        Observable.combineLatest<CardType, String, CardCvcValidationRequest>(
            _cardType,
            _cardCvc,
            BiFunction { type, cvc -> CardCvcValidationRequest(type, cvc) })
            .switchMap { request -> serviceExecutor.execute(request) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ status ->
                _isCardCvcValid.value = status
                updateCardValidation() },
                { it.printStackTrace() })

    /**
     * Subscribes view model to an observable that update the validation status of card expiry, whenever
     * the expiry date property is changed.
     */
    private fun subscribeCardExpiryObservable() =
        _cardExpiry
            .switchMap { expiry -> serviceExecutor.execute(CardExpiryValidationRequest(expiry)) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ status ->
                _isCardExpiryValid.value = status
                updateCardValidation() },
                { it.printStackTrace() })

    /**
     * Determines if card is valid by checking the validation status of all card details, such
     * as number,cvc number, and expiry date/
     */
    private fun updateCardValidation() {
        val isNumberValid = _isCardNumberValid.value?.isValid
        val isCvcValid = _isCardCvcValid.value?.isValid
        val isExpiryValid = _isCardExpiryValid.value?.isValid

        _isCardValid.value = (isNumberValid ?: false)
                && (isCvcValid ?: false)
                && (isExpiryValid ?: false)
    }
}