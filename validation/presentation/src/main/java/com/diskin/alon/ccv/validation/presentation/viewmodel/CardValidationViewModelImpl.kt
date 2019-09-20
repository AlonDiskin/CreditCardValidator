package com.diskin.alon.ccv.validation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diskin.alon.ccv.validation.presentation.model.CardCvcValidationRequest
import com.diskin.alon.ccv.validation.presentation.model.CardExpiryValidationRequest
import com.diskin.alon.ccv.validation.presentation.model.CardNumberValidationRequest
import com.diskin.alon.ccv.validation.presentation.util.ServiceExecutor
import com.diskin.alon.ccv.validation.services.model.CardType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import javax.inject.Inject

/**
 * Card validation screen view model.
 */
class CardValidationViewModelImpl @Inject constructor(private val serviceExecutor: ServiceExecutor)
    : ViewModel(), CardValidationViewModel  {

    override val cardType: BehaviorSubject<CardType> = BehaviorSubject.create()

    override val cardNumber: BehaviorSubject<String> = BehaviorSubject.create()

    override val cardCvc: BehaviorSubject<String> = BehaviorSubject.create()

    override val cardExpiry: BehaviorSubject<Calendar> = BehaviorSubject.create()

    private val _isCardNumberValid: MutableLiveData<Boolean> = MutableLiveData()
    override val isCardNumberValid: LiveData<Boolean> = _isCardNumberValid

    private val _isCardCvcValid: MutableLiveData<Boolean> = MutableLiveData()
    override val isCardCvcValid: LiveData<Boolean> = _isCardCvcValid

    private val _isCardExpiryValid: MutableLiveData<Boolean> = MutableLiveData()
    override val isCardExpiryValid: LiveData<Boolean> = _isCardExpiryValid

    private val _isCardValid: MutableLiveData<Boolean> = MutableLiveData()
    override val isCardValid: LiveData<Boolean> = _isCardValid

    private val compositeDisposable = CompositeDisposable()

    init {
        // create view input observables, and add subscriptions to disposables container
        compositeDisposable.addAll(subscribeCardNumberObservable(),
            subscribeCardCvcObservable(),
            subscribeCardExpiryObservable())

        // set card validation to false
        _isCardValid.value = false
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
            cardType.distinctUntilChanged(),
            cardNumber.distinctUntilChanged(),
            BiFunction { type, number -> CardNumberValidationRequest(type, number) })
            .switchMapSingle { request -> serviceExecutor.execute(request) }
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
            cardType.distinctUntilChanged(),
            cardCvc.distinctUntilChanged(),
            BiFunction { type, cvc -> CardCvcValidationRequest(type, cvc) })
            .switchMapSingle { request -> serviceExecutor.execute(request) }
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
        cardExpiry.distinctUntilChanged()
            .switchMapSingle { expiry -> serviceExecutor.execute(CardExpiryValidationRequest(expiry)) }
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
        _isCardValid.value = _isCardNumberValid.value ?: false
                && _isCardCvcValid.value ?: false
                && _isCardExpiryValid.value ?: false
    }
}