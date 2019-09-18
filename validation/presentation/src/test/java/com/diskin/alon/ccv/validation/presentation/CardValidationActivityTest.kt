package com.diskin.alon.ccv.validation.presentation

import android.content.Context
import android.content.DialogInterface
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diskin.alon.ccv.validation.presentation.controller.CardValidationActivity
import com.diskin.alon.ccv.validation.presentation.model.CardType
import com.diskin.alon.ccv.validation.presentation.viewmodel.CardValidationViewModel
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.content_validation.*
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowDatePickerDialog
import java.util.*
import javax.inject.Inject

/**
 * [CardValidationActivity] unit test class.
 */
@RunWith(AndroidJUnit4::class)
@Config(application = TestApplication::class)
class CardValidationActivityTest {

    // System under test
    private lateinit var scenario: ActivityScenario<CardValidationActivity>

    // Mocked collaborators
    @Inject
    lateinit var viewModel: CardValidationViewModel

    // Collaborators Stubs
    private val cardType: BehaviorSubject<CardType> = BehaviorSubject.create()
    private val cardNumber: BehaviorSubject<String> = BehaviorSubject.create()
    private val cardCvc: BehaviorSubject<String> = BehaviorSubject.create()
    private val cardExpiry: BehaviorSubject<Calendar> = BehaviorSubject.create()
    private val isCardValid: MutableLiveData<Boolean> = MutableLiveData()
    private val isCardNumberValid: MutableLiveData<Boolean> = MutableLiveData()
    private val isCardCvcValid: MutableLiveData<Boolean> = MutableLiveData()
    private val isCardExpiryValid: MutableLiveData<Boolean> = MutableLiveData()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setUp() {
        // stub view model
        whenever(viewModel.cardType).doReturn(cardType)
        whenever(viewModel.cardNumber).doReturn(cardNumber)
        whenever(viewModel.cardCvc).doReturn(cardCvc)
        whenever(viewModel.cardExpiry).doReturn(cardExpiry)
        whenever(viewModel.isCardValid).doReturn(isCardValid)
        whenever(viewModel.isCardNumberValid).doReturn(isCardNumberValid)
        whenever(viewModel.isCardCvcValid).doReturn(isCardCvcValid)
        whenever(viewModel.isCardExpiryValid).doReturn(isCardExpiryValid)

        // launch activity under test
        scenario = ActivityScenario.launch(CardValidationActivity::class.java)
    }

    @Test
    fun shouldCheckCardTypeFromViewModel_whenCreated() {
        // Given a resumed activity with no prev state

        // Then activity should show visa card type as checked
        onView(withId(R.id.radio_visa))
            .check(matches(isChecked()))

        // And pass type state to view model
        assertThat(cardType.value).isEqualTo(CardType.VISA)
    }

    @Test
    fun shouldUpdateViewModelCardType_whenCardTypeChecked() {
        // Given a resumed activity

        // When user check card type as 'visa'
        onView(withText(R.string.radio_visa_label))
            .perform(click())

        // Then activity should pass selection to view model
        assertThat(cardType.value).isEqualTo(CardType.VISA)

        // When user check card type as 'master card'
        onView(withText(R.string.radio_mastercard_label))
            .perform(click())

        // Then activity should pass selection to view model
        assertThat(cardType.value).isEqualTo(CardType.MASTER_CARD)

        // When user check card type as 'american express'
        onView(withText(R.string.radio_american_express_label))
            .perform(click())

        // Then activity should pass selection to view model
        assertThat(cardType.value).isEqualTo(CardType.AMERICAN_EXPRESS)
    }

    @Test
    fun shouldRestoreCardType_whenRecreated() {
        // Given a resumed activity with no prev state

        // When user check the card type as 'MasterCard'
        onView(withId(R.id.radio_master_card))
            .perform(click())

        // And activity is recreated from a previous state (after process kill)
        scenario.recreate()

        // Then activity should restore card type and show 'MasterCard' checked
        onView(withId(R.id.radio_master_card))
            .check(matches(isChecked()))

        // And pass restored type to view model
        assertThat(cardType.value).isEqualTo(CardType.MASTER_CARD)
    }

    @Test
    fun shouldUpdateSubmitButtonEnable_whenCardValidationUpdates() {
        // Given a resumed activity

        // When validation state updates to invalid
        isCardValid.value = false

        // Then activity should disable submit button
        onView(withId(R.id.button_submit))
            .check(matches(not(isEnabled())))

        // When validation state updates to valid
        isCardValid.value = true

        // Then activity should enable submit button
        onView(withId(R.id.button_submit))
            .check(matches(isEnabled()))
    }

    @Test
    fun shouldShowCardDetailInputHints_whenInputsEmpty() {
        // Given a resumed activity with now prev state

        // Then activity should show crd number edit hint in number text edit field
        onView(withHint(R.string.card_number_hint))
            .check(matches(isDisplayed()))
        onView(withHint(R.string.card_cvc_hint))
            .check(matches(isDisplayed()))
        onView(withHint(R.string.card_expiry_hint))
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldUpdateViewModelCardNumber_whenCardEditTextChanged() {
        // Given a resumed activity

        // When user types in card number
        onView(withId(R.id.card_number_input_edit))
            .perform(typeText("12"))

        // Then activity should send number text changes to view model
        assertThat(cardNumber.value).isEqualTo("12")
    }

    @Test
    fun shouldRestoreCardNumber_whenRecreated() {
        // Given a resumed activity with no prev state

        // When user enters card number
        onView(withId(R.id.card_number_input_edit))
            .perform(typeText("123"))

        // And activity is recreated (from process kill)
        scenario.recreate()

        // Then activity should restore last state of card number in ui
        onView(allOf(withId(R.id.card_number_input_edit), withText("123")))
            .check(matches(isDisplayed()))

        // And should pass restored value to view model
        assertThat(cardNumber.value).isEqualTo("123")
    }

    @Test
    fun shouldUpdateCardNumberInfoText_whenValidationStateUpdates() {
        // Given a resumed activity

        // When card number updates to invalid
        isCardNumberValid.value = false

        // Then activity should display an error message in card number edit field
        scenario.onActivity { activity ->
            assertThat(activity.card_number_input.error).isEqualTo(context.getString(R.string.invalid_card_number))
            assertThat(activity.card_number_input.helperText).isNull()
        }

        // When card number updates to valid
        isCardNumberValid.value = true

        // Then activity should remove error message from card number edit field, and show valid number message
        scenario.onActivity { activity ->
            assertThat(activity.card_number_input.error).isNull()
            assertThat(activity.card_number_input.helperText).isEqualTo(context.getString(R.string.valid_card_number))
        }
    }

    @Test
    fun shouldUpdateViewModelCardCvc_whenCardCvcEditTextChanged() {
        // Given a resumed activity

        // When user types in card cvc number
        onView(withId(R.id.card_cvc_input_edit))
            .perform(typeText("123"))

        // Then activity should send number text changes to view model
        assertThat(cardCvc.value).isEqualTo("123")
    }

    @Test
    fun shouldRestoreCardCvc_whenRecreated() {
        // Given a resumed activity with no prev state

        // When user enters card number
        onView(withId(R.id.card_cvc_input_edit))
            .perform(typeText("123"))

        // And activity is recreated (from process kill)
        scenario.recreate()

        // Then activity should restore last state of card number in ui
        onView(allOf(withId(R.id.card_cvc_input_edit), withText("123")))
            .check(matches(isDisplayed()))

        // And should pass restored value to view model
        assertThat(cardCvc.value).isEqualTo("123")
    }

    @Test
    fun shouldUpdateCardCvcInfoText_whenValidationStateUpdates() {
        // Given a resumed activity

        // When card cvc updates to invalid
        isCardCvcValid.value = false

        // Then activity should display an error message in card cvc edit field
        scenario.onActivity { activity ->
            assertThat(activity.card_cvc_input.error).isEqualTo(context.getString(R.string.invalid_card_cvc))
            assertThat(activity.card_cvc_input.helperText).isNull()
        }

        // When card cvc updates to valid
        isCardCvcValid.value = true

        // Then activity should remove error message from card cvc edit field, and show valid number message
        scenario.onActivity { activity ->
            assertThat(activity.card_cvc_input.error).isNull()
            assertThat(activity.card_cvc_input.helperText).isEqualTo(context.getString(R.string.valid_card_cvc))
        }
    }

    @Test
    fun shouldUpdateCardExpiryFieldAndState_whenDatePicked() {
        // Given a resumed activity

        //  When expiry edit field is clicked
        onView(withId(R.id.card_expiry_input_edit))
            .perform(click())

        // And expiry date is picked
        val dialog = ShadowDatePickerDialog.getLatestDialog()!! as DatePickerDialog
        val datePicker = WhiteBox.getInternalState(dialog,"mDatePicker") as DatePicker
        val method = DatePicker::class.java.getDeclaredMethod("updateDate",
            Int::class.java,Int::class.java,Int::class.java)

        method.isAccessible = true
        method.invoke(datePicker,2020,0,10)

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()

        // Then activity should update card expiry field with selected date
        onView(withId(R.id.card_expiry_input_edit))
            .check(matches(allOf(withText("01/20"), isDisplayed())))

        // Then activity should update view model with selected date
        assertThat(cardExpiry.value?.get(Calendar.YEAR)).isEqualTo(2020)
        assertThat(cardExpiry.value?.get(Calendar.MONTH)).isEqualTo(0)
        assertThat(cardExpiry.value?.get(Calendar.DAY_OF_MONTH)).isEqualTo(1)
    }

    @Test
    fun shouldRestoreCardExpiry_whenRecreated() {
        // Given a resumed activity

        //  When expiry edit field is clicked
        onView(withId(R.id.card_expiry_input_edit))
            .perform(click())

        // And expiry date is picked
        val dialog = ShadowDatePickerDialog.getLatestDialog()!! as DatePickerDialog
        val datePicker = WhiteBox.getInternalState(dialog,"mDatePicker") as DatePicker
        val method = DatePicker::class.java.getDeclaredMethod("updateDate",
            Int::class.java,Int::class.java,Int::class.java)

        method.isAccessible = true
        method.invoke(datePicker,2020,0,10)

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()

        // When activity is recreated
        scenario.recreate()

        // Then activity should restore card expiry field with selected date
        onView(withId(R.id.card_expiry_input_edit))
            .check(matches(allOf(withText("01/20"), isDisplayed())))

        // And activity should restore saved date to view model
        assertThat(cardExpiry.value?.get(Calendar.YEAR)).isEqualTo(2020)
        assertThat(cardExpiry.value?.get(Calendar.MONTH)).isEqualTo(0)
        assertThat(cardExpiry.value?.get(Calendar.DAY_OF_MONTH)).isEqualTo(1)
    }

    @Test
    fun shouldUpdateCardExpiryInfoText_whenValidationStateUpdates() {
        // Given a resumed activity

        // When card expiry updates to invalid
        isCardExpiryValid.value = false

        // Then activity should display an error message in card expiry edit field
        scenario.onActivity { activity ->
            assertThat(activity.card_expiry_input.error).isEqualTo(context.getString(R.string.invalid_card_expiry))
            assertThat(activity.card_expiry_input.helperText).isNull()
        }

        // When card expiry updates to valid
        isCardExpiryValid.value = true

        // Then activity should remove error message from card expiry edit field, and show valid number message
        scenario.onActivity { activity ->
            assertThat(activity.card_expiry_input.error).isNull()
            assertThat(activity.card_expiry_input.helperText).isEqualTo(context.getString(R.string.valid_card_expiry))
        }
    }
}