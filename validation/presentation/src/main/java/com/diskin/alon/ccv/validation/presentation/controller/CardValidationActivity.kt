package com.diskin.alon.ccv.validation.presentation.controller

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.diskin.alon.ccv.validation.presentation.R
import com.diskin.alon.ccv.validation.presentation.model.CardType
import com.diskin.alon.ccv.validation.presentation.viewmodel.CardValidationViewModel
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_validation.*
import kotlinx.android.synthetic.main.content_validation.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Credit card validation screen controller.
 */
class CardValidationActivity : AppCompatActivity() {

    companion object {
        // activity saved instance state keys
        private const val KEY_CARD_TYPE = "key card type"
        private const val KEY_CARD_NUMBER = "key card number"
        private const val KEY_CARD_CVC_NUMBER = "key card cvc number"
        private const val KEY_CARD_EXPIRY = "key card expiry"
    }

    @Inject
    lateinit var viewModel: CardValidationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validation)
        setSupportActionBar(toolbar)

        // restore saved state if exist
        if (savedInstanceState != null) {
            val cardType = CardType.values()[savedInstanceState.getInt(KEY_CARD_TYPE)]
            val cardNumber: String? = savedInstanceState.getString(KEY_CARD_NUMBER)
            val cardCvcNumber: String? = savedInstanceState.getString(KEY_CARD_CVC_NUMBER)
            val cardExpiry: Calendar? = (savedInstanceState.getSerializable(KEY_CARD_EXPIRY) as Calendar?)

            viewModel.cardType.onNext(cardType)

            when(cardType) {
                CardType.VISA -> radio_visa.isChecked = true
                CardType.MASTER_CARD -> radio_master_card.isChecked = true
                CardType.AMERICAN_EXPRESS -> radio_american.isChecked = true
            }

            if (cardNumber != null) {
                // view model number property will be updated when text listener be added
                card_number_input_edit.setText(cardNumber)
            }

            if (cardCvcNumber != null) {
                // view model cvc code property will be updated when text listener be added
                card_cvc_input_edit.setText(cardCvcNumber)
            }

            if (cardExpiry != null) {
                viewModel.cardExpiry.onNext(cardExpiry)
                card_expiry_input_edit.setText(formatCalendar(cardExpiry))
            }

        } else{
            // set current card type selection state as 'Visa'
            viewModel.cardType.onNext(CardType.VISA)
            radio_visa.isChecked = true
        }

        // setup card number text input listener
        card_number_input_edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // update view model card number
                viewModel.cardNumber.onNext(s.toString())
            }
        })

        // setup card cvc number text input listener
        card_cvc_input_edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // update view model cvc number
                viewModel.cardCvc.onNext(s.toString())
            }
        })

        // setup card expiry date field listeners
        card_expiry_input_edit.inputType = InputType.TYPE_NULL
        val newCalendar = Calendar.getInstance()
        val datePickerDialog = SpinnerDatePickerDialogBuilder()
            .context(this)
            .callback { view, year, monthOfYear, dayOfMonth ->
                // compose selected date as calendar instance
                val calendar = Calendar.getInstance()
                calendar.set(year,monthOfYear,1)

                // update card expiry text field
                card_expiry_input_edit.setText(formatCalendar(calendar))
                // update view model
                viewModel.cardExpiry.onNext(calendar)
            }
            .showDaySpinner(false)
            .minDate(newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), 1)
            .build()

        card_expiry_input_edit.setOnClickListener {
            datePickerDialog.show()
        }
        card_expiry_input_edit.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                datePickerDialog.show()
            }
        }

        // observe card validation state
        viewModel.isCardValid.observe(this, Observer { onCardValidationUpdate(it) })
        // observe card number validation state
        viewModel.isCardNumberValid.observe(this, Observer { onCardNumberValidationUpdate(it) })
        // observe card number validation state
        viewModel.isCardCvcValid.observe(this, Observer { onCardCvcValidationUpdate(it) })
        // observe card expiry validation state
        viewModel.isCardExpiryValid.observe(this, Observer { onCardExpiryValidationUpdate(it) })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(KEY_CARD_TYPE, viewModel.cardType.value?.ordinal!!) // type should be selected
        outState?.putString(KEY_CARD_NUMBER,viewModel.cardNumber.value)
        outState?.putString(KEY_CARD_CVC_NUMBER,viewModel.cardCvc.value)
        outState?.putSerializable(KEY_CARD_EXPIRY,viewModel.cardExpiry.value)
    }

    /**
     * Handles user ui card type selections.
     */
    fun onCardTypeRadioButtonClicked(view: View) {
        // pass card type selection to view model
        when(view.id) {
            R.id.radio_visa -> viewModel.cardType.onNext(CardType.VISA)
            R.id.radio_master_card -> viewModel.cardType.onNext(CardType.MASTER_CARD)
            R.id.radio_american -> viewModel.cardType.onNext(CardType.AMERICAN_EXPRESS)
        }
    }

    /**
     * Handles view model card validation state changes.
     */
    private fun onCardValidationUpdate(isValid: Boolean) {
        // change detail submit button according to validation state
        button_submit.isEnabled = isValid
    }

    /**
     * Handles view model card number validation state changes.
     */
    private fun onCardNumberValidationUpdate(isValid: Boolean) {
        // display card number status error message if invalid
        when(isValid) {
            false -> {
                card_number_input.helperText = null
                card_number_input.error = getString(R.string.invalid_card_number)
            }

            true -> {
                card_number_input.error = null
                card_number_input.helperText = getString(R.string.valid_card_number)
            }
        }
    }

    /**
     * Handles view model card cvc validation state changes.
     */
    private fun onCardCvcValidationUpdate(isValid: Boolean) {
        // display card cvc status error message if invalid
        when(isValid) {
            false -> {
                card_cvc_input.helperText = null
                card_cvc_input.error = getString(R.string.invalid_card_cvc)
            }

            true -> {
                card_cvc_input.error = null
                card_cvc_input.helperText = getString(R.string.valid_card_cvc)
            }
        }
    }

    /**
     * Handles view model card expiry date validation state changes.
     */
    private fun onCardExpiryValidationUpdate(isValid: Boolean) {
        // display card expiry status error message if invalid
        when(isValid) {
            false -> {
                card_expiry_input.helperText = null
                card_expiry_input.error = getString(R.string.invalid_card_expiry)
            }

            true -> {
                card_expiry_input.error = null
                card_expiry_input.helperText = getString(R.string.valid_card_expiry)
            }
        }
    }

    /**
     * Format the given calendar value into a ui presentable string.
     */
    private fun formatCalendar(calendar: Calendar) =
        SimpleDateFormat(getString(R.string.date_pattern)).format(calendar.time)

}
