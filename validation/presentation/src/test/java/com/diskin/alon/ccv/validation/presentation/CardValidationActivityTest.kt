package com.diskin.alon.ccv.validation.presentation

import android.content.Context
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.content.res.Configuration
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
import com.diskin.alon.ccv.validation.presentation.model.CardDetailValidationStatus
import com.diskin.alon.ccv.validation.presentation.viewmodel.CardValidationViewModel
import com.nhaarman.mockitokotlin2.*
import com.tsongkha.spinnerdatepicker.DatePicker
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import org.robolectric.shadows.ShadowDatePickerDialog
import java.util.*

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
    private val isCardValid: MutableLiveData<Boolean> = MutableLiveData()
    private val isCardNumberValid: MutableLiveData<CardDetailValidationStatus> = MutableLiveData()
    private val isCardCvcValid: MutableLiveData<CardDetailValidationStatus> = MutableLiveData()
    private val isCardExpiryValid: MutableLiveData<CardDetailValidationStatus> = MutableLiveData()

    @Before
    fun setUp() {
        // stub view model
        whenever(viewModel.isCardValid).doReturn(isCardValid)
        whenever(viewModel.isCardNumberValid).doReturn(isCardNumberValid)
        whenever(viewModel.isCardCvcValid).doReturn(isCardCvcValid)
        whenever(viewModel.isCardExpiryValid).doReturn(isCardExpiryValid)

        doAnswer { invocation ->
            whenever(viewModel.cardType).doReturn(invocation.arguments[0] as CardType)
        }.whenever(viewModel).cardType = any()

        viewModel.cardType =  CardType.VISA

        doAnswer { invocation ->
            whenever(viewModel.cardNumber).doReturn(invocation.arguments[0] as String)
        }.whenever(viewModel).cardNumber = any()

        viewModel.cardNumber = ""

        doAnswer { invocation ->
            whenever(viewModel.cardCvc).doReturn(invocation.arguments[0] as String)
        }.whenever(viewModel).cardCvc = any()

        viewModel.cardCvc = ""

        doAnswer { invocation ->
            whenever(viewModel.cardExpiry).doReturn(invocation.arguments[0] as String)
        }.whenever(viewModel).cardExpiry = any()

        viewModel.cardExpiry = ""

        // launch activity under test
        scenario = ActivityScenario.launch(CardValidationActivity::class.java)
    }

    @Test
    fun shouldSupportPortraitModeOnly() {
        // Given a resumed activity with no prev state shown in portrait mode

        // When device is rotated
        rotateScreen()

        // Then activity should remain in portrait mode
        scenario.onActivity { scenario ->
            val errMessage = "activity should be shown in portrait mode only"
            assertThat(errMessage,scenario.resources.configuration.orientation,
                equalTo(Configuration.ORIENTATION_PORTRAIT))
        }
    }

    @Test
    fun shouldCheckCardTypeFromViewModel_whenCreated() {
        // Given a resumed activity with no prev state

        // Then activity should retrieve card type state from view model
        verify(viewModel).cardType

        // And should check the card type returned by view model as current selection
        onView(withId(R.id.radio_visa))
            .check(matches(isChecked()))
    }

    @Test
    fun shouldUpdateViewModelCardType_whenCardTypeChecked() {
        // Given a resumed activity

        // When user check card type
        onView(withText(R.string.radio_visa_label))
            .perform(click())
        onView(withText(R.string.radio_mastercard_label))
            .perform(click())
        onView(withText(R.string.radio_american_express_label))
            .perform(click())

        // Then activity should pass selection to view model
        verify(viewModel, times(2)).cardType = eq(CardType.VISA)
        verify(viewModel).cardType = eq(CardType.MASTER_CARD)
        verify(viewModel).cardType = eq(CardType.AMERICAN_EXPRESS)
    }

    @Test
    fun shouldRestoreCardType_whenRecreated() {
        // Given a resumed activity with no prev state

        // Then 'Visa' card type should be checked
        onView(withId(R.id.radio_visa))
            .check(matches(isChecked()))

        // When user check the card type as 'MasterCard'
        onView(withId(R.id.radio_master_card))
            .perform(click())

        // And activity is recreated from a previous state (after process kill)
        scenario.recreate()

        // Then activity should restore card type and show 'MasterCard' checked
        onView(withId(R.id.radio_master_card))
            .check(matches(isChecked()))

        // And pass restored type to view model
        verify(viewModel, times(2)).cardType = eq(CardType.MASTER_CARD)
    }

    @Test
    fun shouldObserveViewModelCardValidationState() {
        // Given a resumed activity

        // Then activity should observe view model card validation state
        verify(viewModel).isCardValid

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
    fun shouldShowCardNumberEditHint_whenEditEmpty() {
        // Given a resumed activity with now prev state

        // Then activity should show crd number edit hint in number text edit field
        onView(withHint(R.string.card_number_hint))
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldUpdateViewModelCardNumber_whenCardEditTextChanged() {
        // Given a resumed activity

        // When user types in card number
        onView(withId(R.id.card_number_edit))
            .perform(typeText("123"))

        // Then activity should send number text changes to view model
        verify(viewModel).cardNumber = eq("1")
        verify(viewModel).cardNumber = eq("12")
        verify(viewModel).cardNumber = eq("123")
    }

    @Test
    fun shouldRestoreCardNumber_whenRecreated() {
        // Given a resumed activity with no prev state
        val cardNumber = "123"

        // When user enters card number
        onView(withId(R.id.card_number_edit))
            .perform(typeText(cardNumber))

        // And activity is recreated (from process kill)
        scenario.recreate()

        // Then activity should restore last state of card number in ui
        onView(allOf(withId(R.id.card_number_edit), withText(cardNumber)))
            .check(matches(isDisplayed()))

        // And should pass restored value to view model
        verify(viewModel, times(2)).cardNumber = eq(cardNumber)
    }

    @Test
    fun shouldObserveViewModelCardNumberValidationState() {
        // Given a resumed activity
        val errorMessage = "error message"

        // Then activity should observe view model card number validation state
        verify(viewModel).isCardNumberValid

        // When card number updates to invalid
        isCardNumberValid.value = CardDetailValidationStatus.invalid(errorMessage)

        // Then activity should display an error indicator in card number edit field
        onView(withId(R.id.card_number_edit))
                .check(matches(hasErrorText(errorMessage)))

        // When card number updates to valid
        isCardNumberValid.value = CardDetailValidationStatus.valid()

        // Then activity should remove error indicator from card number edit field
        onView(withId(R.id.card_number_edit))
            .check(matches(not(hasErrorText(errorMessage))))
    }

    @Test
    fun shouldShowCardCvcEditHint_whenEditEmpty() {
        // Given a resumed activity with now prev state

        // Then activity should show crd number edit hint in number text edit field
        onView(withHint(R.string.card_cvc_hint))
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldUpdateViewModelCardCvc_whenCardCvcEditTextChanged() {
        // Given a resumed activity

        // When user types in card cvc number
        onView(withId(R.id.card_cvc_edit))
            .perform(typeText("123"))

        // Then activity should send number text changes to view model
        verify(viewModel).cardCvc = eq("1")
        verify(viewModel).cardCvc = eq("12")
        verify(viewModel).cardCvc = eq("123")
    }

    @Test
    fun shouldLimitCardCvcInput() {
        // Given a resumed activity

        // When user types in card cvc number
        onView(withId(R.id.card_cvc_edit))
            .perform(typeText("12345"))

        // Then activity should send number text changes to view model limited by 4 digit input
        verify(viewModel).cardCvc = eq("1")
        verify(viewModel).cardCvc = eq("12")
        verify(viewModel).cardCvc = eq("123")
        verify(viewModel).cardCvc = eq("1234")
        verify(viewModel, times(0)).cardCvc = eq("12345")
    }

    @Test
    fun shouldRestoreCardCvc_whenRecreated() {
        // Given a resumed activity with no prev state
        val cardCvcNumber = "123"

        // When user enters card number
        onView(withId(R.id.card_cvc_edit))
            .perform(typeText(cardCvcNumber))

        // And activity is recreated (from process kill)
        scenario.recreate()

        // Then activity should restore last state of card number in ui
        onView(allOf(withId(R.id.card_cvc_edit), withText(cardCvcNumber)))
            .check(matches(isDisplayed()))

        // And should pass restored value to view model
        verify(viewModel, times(2)).cardCvc = eq(cardCvcNumber)
    }

    @Test
    fun shouldObserveViewModelCardCvcValidationState() {
        // Given a resumed activity
        val errorMessage = "error message"

        // Then activity should observe view model card number validation state
        verify(viewModel).isCardCvcValid

        // When card cvc updates to invalid
        isCardCvcValid.value = CardDetailValidationStatus.invalid(errorMessage)

        // Then activity should display an error indicator in card cvc edit field
        onView(withId(R.id.card_cvc_edit))
            .check(matches(hasErrorText(errorMessage)))

        // When card number updates to valid
        isCardCvcValid.value = CardDetailValidationStatus.valid()

        // Then activity should remove error indicator from card number edit field
        onView(withId(R.id.card_cvc_edit))
            .check(matches(not(hasErrorText(errorMessage))))
    }

    @Test
    fun shouldShowCardExpiryDateFieldHint_whenEmpty() {
        // Given a resumed activity with now prev state

        // Then activity should show crd number edit hint in number text edit field
        onView(withHint(R.string.card_expiry_hint))
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldOpenDatePickerDialog_whenCardExpiryFieldClicked() {
        // Given a resumed activity

        //  When expiry edit field is clicked
        onView(withId(R.id.card_expiry_edit))
            .perform(click())

        // Then date picker dialog should be displayed to user
        val dialog = ShadowDatePickerDialog.getLatestDialog()!!

        assertThat(dialog.isShowing, equalTo(true))
    }

    @Test
    fun shouldUpdateCardExpiryField_whenDatePicked() {
        // Given a resumed activity

        //  When expiry edit field is clicked
        onView(withId(R.id.card_expiry_edit))
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
        onView(withId(R.id.card_expiry_edit))
            .check(matches(allOf(withText("01/20"), isDisplayed())))
    }

    @Test
    fun shouldUpdateViewModelCardExpiry_whenDatePicked() {
        // Given a resumed activity

        //  When expiry edit field is clicked
        onView(withId(R.id.card_expiry_edit))
            .perform(click())

        // And expiry date is picked
        val dialog = ShadowDatePickerDialog.getLatestDialog()!! as DatePickerDialog
        val datePicker = WhiteBox.getInternalState(dialog,"mDatePicker") as DatePicker
        val method = DatePicker::class.java.getDeclaredMethod("updateDate",
            Int::class.java,Int::class.java,Int::class.java)

        method.isAccessible = true
        method.invoke(datePicker,2020,Calendar.JANUARY,1)

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()

        // Then activity should update view model with selected date
        verify(viewModel).cardExpiry = eq("01/20")
    }

    @Test
    fun shouldRestoreCardExpiry_whenRecreated() {
        // Given a resumed activity with no prev state

        //  When expiry edit field is clicked
        onView(withId(R.id.card_expiry_edit))
            .perform(click())

        // And expiry date is picked
        val dialog = ShadowDatePickerDialog.getLatestDialog()!! as DatePickerDialog
        val datePicker = WhiteBox.getInternalState(dialog,"mDatePicker") as DatePicker
        val method = DatePicker::class.java.getDeclaredMethod("updateDate",
            Int::class.java,Int::class.java,Int::class.java)

        method.isAccessible = true
        method.invoke(datePicker,2020,Calendar.JANUARY,1)

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()

        // And activity is recreated
        scenario.recreate()

        // Then activity should restore expiry text field
        onView(withId(R.id.card_expiry_edit))
            .check(matches(allOf(withText("01/20"), isDisplayed())))

        // And restore view model card expiry state
        verify(viewModel, times(2)).cardExpiry = eq("01/20")
    }

    @Test
    fun shouldObserveCardExpiryViewModelValidationState() {
        // Given a resumed activity
        val errorMessage = "error message"

        // Then activity should observe view model card expiry state
        verify(viewModel).isCardExpiryValid

        // When card expiry validation is updated to invalid
        isCardExpiryValid.value = CardDetailValidationStatus.invalid(errorMessage)

        // Then activity should display error message in expiry text field
        onView(withId(R.id.card_expiry_edit))
            .check(matches(hasErrorText(errorMessage)))

        // When card expiry validation is updated to valid
        isCardExpiryValid.value = CardDetailValidationStatus.valid()

        // Then activity should remove error message in expiry text field
        onView(withId(R.id.card_expiry_edit))
            .check(matches(not(hasErrorText(errorMessage))))
    }

    private fun rotateScreen() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val orientation = context.resources.configuration.orientation

        scenario.onActivity { activity ->
            activity.requestedOrientation = if (orientation == Configuration.ORIENTATION_PORTRAIT)
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            else
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}