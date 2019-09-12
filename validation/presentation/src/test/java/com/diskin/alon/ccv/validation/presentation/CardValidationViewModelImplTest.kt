package com.diskin.alon.ccv.validation.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import com.diskin.alon.ccv.validation.presentation.model.*
import com.diskin.alon.ccv.validation.presentation.util.ServiceExecutor
import com.diskin.alon.ccv.validation.presentation.viewmodel.CardValidationViewModelImpl
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations

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
    private val cardNumberValidationStatusSubject: BehaviorSubject<CardDetailValidationStatus> = BehaviorSubject.create()
    private val cardCvcValidationStatusSubject: BehaviorSubject<CardDetailValidationStatus> = BehaviorSubject.create()
    private val cardExpiryValidationStatusSubject: BehaviorSubject<CardDetailValidationStatus> = BehaviorSubject.create()

    @Before
    fun setUp() {
        // init mocks
        MockitoAnnotations.initMocks(this)

        // stub mocks
        whenever(serviceExecutor.execute(any<CardNumberValidationRequest>()))
            .thenReturn(cardNumberValidationStatusSubject)
        whenever(serviceExecutor.execute(any<CardCvcValidationRequest>()))
            .thenReturn(cardCvcValidationStatusSubject)
        whenever(serviceExecutor.execute(any<CardExpiryValidationRequest>()))
            .thenReturn(cardExpiryValidationStatusSubject)

        // init SUT
        viewModel =
            CardValidationViewModelImpl(serviceExecutor)
    }

    @Test
    @Parameters(method = "cardDetailParams")
    fun shouldUpdateCardState_whenReceiveViewUpdates(type: CardType, number: String, cvc: String, expiry: String) {
        // Given an initialized view model

        // When card detail is updated by view
        viewModel.cardType = type
        viewModel.cardNumber = number
        viewModel.cardCvc = cvc
        viewModel.cardExpiry = expiry

        // Then view model should update card detail backing properties
        assertThat((WhiteBox.getInternalState(viewModel,"_cardType") as BehaviorSubject<*>).value)
            .isEqualTo(type)
        assertThat((WhiteBox.getInternalState(viewModel,"_cardNumber") as BehaviorSubject<*>).value)
            .isEqualTo(number)
        assertThat((WhiteBox.getInternalState(viewModel,"_cardCvc") as BehaviorSubject<*>).value)
            .isEqualTo(cvc)
        assertThat((WhiteBox.getInternalState(viewModel,"_cardExpiry") as BehaviorSubject<*>).value)
            .isEqualTo(expiry)
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
    @Parameters(method = "cardNumberValidationParams")
    fun shouldValidateCardNumber_whenCardNumberUpdated(numberUpdate: String, validationStatus: CardDetailValidationStatus) {
        // Given an initialized view model
        val expectedEmptyInvocations = 2

        // When card number updated by view
        viewModel.cardNumber = numberUpdate

        // Then view model should perform card number validation via service executor
        if (numberUpdate.isEmpty()) {
            verify(serviceExecutor, times(expectedEmptyInvocations))
                .execute(eq(CardNumberValidationRequest(viewModel.cardType,"")))

        } else {
            verify(serviceExecutor).execute(eq(CardNumberValidationRequest(viewModel.cardType,numberUpdate)))
        }

        // When service executor return result
        cardNumberValidationStatusSubject.onNext(validationStatus)

        // And update execution result to number validation state view observer
        assertThat(viewModel.isCardNumberValid.value).isEqualTo(validationStatus)
    }

    @Test
    @Parameters(method = "cardCvcValidationParams")
    fun shouldValidateCardCvc_whenCardCvcUpdated(cvcUpdate: String, validationStatus: CardDetailValidationStatus) {
        // Given an initialized view model
        val expectedEmptyInvocations = 2

        // When card number updated by view
        viewModel.cardCvc = cvcUpdate

        // Then view model should perform card number validation via service executor
        if (cvcUpdate.isEmpty()) {
            verify(serviceExecutor, times(expectedEmptyInvocations))
                .execute(eq(CardCvcValidationRequest(viewModel.cardType,"")))

        } else {
            verify(serviceExecutor).execute(eq(CardCvcValidationRequest(viewModel.cardType,cvcUpdate)))
        }

        // When service executor return result
        cardCvcValidationStatusSubject.onNext(validationStatus)

        // And update execution result to card cvc validation state view observer
        assertThat(viewModel.isCardCvcValid.value).isEqualTo(validationStatus)
    }

    @Test
    @Parameters(method = "cardExpiryValidationParams")
    fun shouldValidateCardExpiry_whenCardCvcUpdated(expiryUpdate: String, validationStatus: CardDetailValidationStatus) {
        // Given an initialized view model
        val expectedEmptyInvocations = 2

        // When card number updated by view
        viewModel.cardExpiry = expiryUpdate

        // Then view model should perform card number validation via service executor
        if (expiryUpdate.isEmpty()) {
            verify(serviceExecutor, times(expectedEmptyInvocations))
                .execute(eq(CardExpiryValidationRequest("")))

        } else {
            verify(serviceExecutor).execute(eq(CardExpiryValidationRequest(expiryUpdate)))
        }

        // When service executor return result
        cardExpiryValidationStatusSubject.onNext(validationStatus)

        // And update execution result to expiry validation state view observer
        assertThat(viewModel.isCardExpiryValid.value).isEqualTo(validationStatus)
    }

    @Test
    @Parameters(method = "cardValidationParams")
    fun shouldValidateCard_whenCardDetailValidationUpdates(number: String,
                                                           numberStatus: CardDetailValidationStatus,
                                                           cvc: String,
                                                           cvcStatus: CardDetailValidationStatus,
                                                           expiry: String,
                                                           expiryStatus: CardDetailValidationStatus,
                                                           validation: Boolean) {
        // Given an initialized view model

        // When view updates view model card detail
        viewModel.cardNumber = number
        cardNumberValidationStatusSubject.onNext(numberStatus)
        viewModel.cardCvc = cvc
        cardCvcValidationStatusSubject.onNext(cvcStatus)
        viewModel.cardExpiry = expiry
        cardExpiryValidationStatusSubject.onNext(expiryStatus)

        // Then view model should validate card
        assertThat(viewModel.isCardValid.value).isEqualTo(validation)
    }

    fun cardDetailParams() = arrayOf(arrayOf(CardType.VISA,"123445","345","12/20"),
        arrayOf(CardType.MASTER_CARD,"123456745","4356345","10/20"))

    fun cardNumberValidationParams() = arrayOf(arrayOf("123445",CardDetailValidationStatus.valid()),
        arrayOf("",CardDetailValidationStatus.invalid("number validation error message")),
        arrayOf("1",CardDetailValidationStatus.valid()),
        arrayOf("654",CardDetailValidationStatus.invalid("validation error message")))

    fun cardCvcValidationParams() = arrayOf(arrayOf("125",CardDetailValidationStatus.valid()),
        arrayOf("09876",CardDetailValidationStatus.invalid("message")),
        arrayOf("",CardDetailValidationStatus.invalid("cvc validation error message")),
        arrayOf("15959",CardDetailValidationStatus.valid()))

    fun cardExpiryValidationParams() = arrayOf(arrayOf("12/24",CardDetailValidationStatus.valid()),
        arrayOf("09876",CardDetailValidationStatus.invalid("message")),
        arrayOf("",CardDetailValidationStatus.invalid("cvc validation error message")),
        arrayOf("15/59",CardDetailValidationStatus.valid()))

    fun cardValidationParams() = arrayOf(
        arrayOf("",
            CardDetailValidationStatus.invalid("message"),
            "",
            CardDetailValidationStatus.invalid("error"),
            "12/30",
            CardDetailValidationStatus.valid(),
            false),
        arrayOf("123",
            CardDetailValidationStatus.valid(),
            "",
            CardDetailValidationStatus.valid(),
            "12/30",
            CardDetailValidationStatus.valid(),
            true))
}