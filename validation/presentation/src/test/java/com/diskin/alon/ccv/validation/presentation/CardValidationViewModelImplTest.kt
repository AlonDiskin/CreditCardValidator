package com.diskin.alon.ccv.validation.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.diskin.alon.ccv.validation.presentation.model.CardCvcValidationRequest
import com.diskin.alon.ccv.validation.presentation.model.CardExpiryValidationRequest
import com.diskin.alon.ccv.validation.presentation.model.CardNumberValidationRequest
import com.diskin.alon.ccv.validation.presentation.util.ServiceExecutor
import com.diskin.alon.ccv.validation.presentation.viewmodel.CardValidationViewModelImpl
import com.diskin.alon.ccv.validation.services.model.CardType
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.SingleSubject
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*

/**
 * [CardValidationViewModelImpl] unit test class.
 */
@RunWith(JUnitParamsRunner::class)
class CardValidationViewModelImplTest {

    companion object {

        @JvmStatic
        @BeforeClass
        fun setupClass() {
            // Set Rx framework for testing
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        }
    }

    // Lifecycle testing rule
    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // System under test
    private lateinit var viewModel: CardValidationViewModelImpl

    // Mocked collaborators
    @Mock
    lateinit var serviceExecutor: ServiceExecutor

    // Collaborator Stubs
    private val cardNumberValidationStatusSubject: SingleSubject<Boolean> = SingleSubject.create()
    private val cardCvcValidationStatusSubject: SingleSubject<Boolean> = SingleSubject.create()
    private val cardExpiryValidationStatusSubject: SingleSubject<Boolean> = SingleSubject.create()

    @Before
    fun setUp() {
        // init mocks
        MockitoAnnotations.initMocks(this)

        // stub mocks
        whenever(serviceExecutor.execute(any<CardNumberValidationRequest>()))
            .doReturn(cardNumberValidationStatusSubject)
        whenever(serviceExecutor.execute(any<CardCvcValidationRequest>()))
            .doReturn(cardCvcValidationStatusSubject)
        whenever(serviceExecutor.execute(any<CardExpiryValidationRequest>()))
            .doReturn(cardExpiryValidationStatusSubject)

        // init SUT
        viewModel =
            CardValidationViewModelImpl(serviceExecutor)
    }

    @Test
    fun shouldDisposeInputObservables_whenCleared() {
        // Given an initialized view model
        val compositeDisposable = WhiteBox.getInternalState(viewModel,"compositeDisposable")
                as CompositeDisposable

        // When view model is cleared
        val method = ViewModel::class.java.getDeclaredMethod("onCleared")
        method.isAccessible = true
        method.invoke(viewModel)

        // Then view model should dispose of all its observable subscriptions
        assertThat(compositeDisposable.isDisposed).isTrue()
    }

    @Test
    fun shouldSetCardValidationAsFalse_whenCreated() {
        // Given an initialized view model

        // Then view model should set card validation state as 'false' by default
        assertThat(viewModel.isCardValid.value).isFalse()
    }

    @Test
    @Parameters(method = "cardNumberValidationParams")
    fun shouldValidateCardNumber_whenCardNumberAndCardTypeDistinctlyUpdated(numberUpdate: String,
                                                                            type: CardType,
                                                                            validation: Boolean) {
        // Given an initialized view model

        // When card number and type updated by view
        viewModel.cardNumber.onNext(numberUpdate)
        viewModel.cardType.onNext(type)

        // Then view model should perform card number validation via service executor
        verify(serviceExecutor).execute(eq(CardNumberValidationRequest(type,numberUpdate)))

        // When service executor return result
        cardNumberValidationStatusSubject.onSuccess(validation)

        // Then update execution result to number validation state view observer
        assertThat(viewModel.isCardNumberValid.value).isEqualTo(validation)

        // When card number and type updated again by view with same values
        viewModel.cardNumber.onNext(numberUpdate)
        viewModel.cardType.onNext(type)

        // Then view model should not perform card number validation on not distinct values
        verify(serviceExecutor, times(1)).execute(eq(CardNumberValidationRequest(type,numberUpdate)))
    }

    @Test
    @Parameters(method = "cardCvcValidationParams")
    fun shouldValidateCardCvc_whenCvcCodeAndCardTypeDistinctlyUpdated(cvcUpdate: String,
                                                 type: CardType,
                                                 validation: Boolean) {
        // Given an initialized view model

        // When cvc code and card type properties are updated by view
        viewModel.cardCvc.onNext(cvcUpdate)
        viewModel.cardType.onNext(type)

        // Then view model should perform cvc code validation via service executor
        verify(serviceExecutor).execute(eq(CardCvcValidationRequest(type,cvcUpdate)))

        // When service executor return result
        cardCvcValidationStatusSubject.onSuccess(validation)

        // Then update execution result to card cvc validation state view observer
        assertThat(viewModel.isCardCvcValid.value).isEqualTo(validation)

        // When cvc code and card type properties are updated by view with same values
        viewModel.cardCvc.onNext(cvcUpdate)
        viewModel.cardType.onNext(type)

        // Then view model should not perform cvc validation on not distinct values
        assertThat(viewModel.isCardCvcValid.value).isEqualTo(validation)
    }

    @Test
    @Parameters(method = "cardExpiryValidationParams")
    fun shouldValidateCardExpiry_whenCardExpiryDistinctlyUpdated(expiryUpdate: Calendar, validation: Boolean) {
        // Given an initialized view model

        // When card number updated by view
        viewModel.cardExpiry.onNext(expiryUpdate)

        // Then view model should perform card number validation via service executor
        verify(serviceExecutor).execute(eq(CardExpiryValidationRequest(expiryUpdate)))

        // When service executor return result
        cardExpiryValidationStatusSubject.onSuccess(validation)

        // Then update execution result to expiry validation state view observer
        assertThat(viewModel.isCardExpiryValid.value).isEqualTo(validation)

        // When card number updated by view with same value
        viewModel.cardExpiry.onNext(expiryUpdate)

        // Then view model should not validate expiry on not distinct value
        assertThat(viewModel.isCardExpiryValid.value).isEqualTo(validation)
    }

    @Test
    @Parameters(method = "cardValidationParams")
    fun shouldValidateCard_whenCardDetailValidationUpdates( type: CardType,
                                                            number: String,
                                                           numberStatus: Boolean,
                                                           cvc: String,
                                                           cvcStatus: Boolean,
                                                           expiry: Calendar,
                                                           expiryStatus: Boolean,
                                                           validation: Boolean) {
        // Given an initialized view model

        // When view updates view model card detail
        viewModel.cardType.onNext(type)
        viewModel.cardNumber.onNext(number)
        cardNumberValidationStatusSubject.onSuccess(numberStatus)
        viewModel.cardCvc.onNext(cvc)
        cardCvcValidationStatusSubject.onSuccess(cvcStatus)
        viewModel.cardExpiry.onNext(expiry)
        cardExpiryValidationStatusSubject.onSuccess(expiryStatus)

        // Then view model should validate card
        assertThat(viewModel.isCardValid.value).isEqualTo(validation)
    }

    fun cardNumberValidationParams() = arrayOf(arrayOf("123445",CardType.MASTER_CARD,true),
        arrayOf("",CardType.VISA,true),
        arrayOf("1",CardType.AMERICAN_EXPRESS,false))

    fun cardCvcValidationParams() = arrayOf(arrayOf("125",CardType.AMERICAN_EXPRESS,true),
        arrayOf("09876",CardType.VISA,false),
        arrayOf("",CardType.MASTER_CARD,false))

    fun cardExpiryValidationParams() = arrayOf(arrayOf(Calendar.getInstance(),true),
        arrayOf(GregorianCalendar(2020,2,8),false),
        arrayOf(GregorianCalendar(2010,10,2),false),
        arrayOf(GregorianCalendar(2024,7,18),true))

    fun cardValidationParams() = arrayOf(
        arrayOf(CardType.MASTER_CARD,
            "",
            false,
            "",
            true,
            GregorianCalendar(2010,2,8),
            true,
            false),
        arrayOf(CardType.AMERICAN_EXPRESS,
            "123",
            true,
            "",
            true,
            GregorianCalendar(2020,2,8),
            true,
            true))
}