package com.diskin.alon.ccv.validation.presentation.controller

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.diskin.alon.ccv.validation.presentation.viewmodel.CardValidationViewModel
import com.diskin.alon.ccv.validation.presentation.R
import com.diskin.alon.ccv.validation.presentation.model.CardType
import com.diskin.alon.ccv.validation.presentation.model.CardValidationStatus
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_validation.*
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
            val cardNumber = savedInstanceState.getString(KEY_CARD_NUMBER)!!
            val cardCvcNumber = savedInstanceState.getString(KEY_CARD_CVC_NUMBER)!!
            val cardExpiry = savedInstanceState.getString(KEY_CARD_EXPIRY)!!

            viewModel.cardType = cardType
            viewModel.cardExpiry = cardExpiry

            card_expiry_edit.setText(cardExpiry)
            // view model will be updated with restored value from this edit fields listeners
            // upon adding them
            card_number_edit.setText(cardNumber)
            card_cvc_edit.setText(cardCvcNumber)
        }

        // setup current card type selection using view model current value
        when(viewModel.cardType) {
            CardType.VISA -> radio_visa.isChecked = true
            CardType.MASTER_CARD -> radio_master_card.isChecked = true
            CardType.AMERICAN_EXPRESS -> radio_american.isChecked = true
        }

        // setup card number text input listener
        card_number_edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // update view model card number
                viewModel.cardNumber = s.toString()
            }
        })

        // setup card cvc number text input listener
        card_cvc_edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // update view model cvc number
                viewModel.cardCvc = s.toString()
            }
        })

        // setup card expiry date field listeners
        card_expiry_edit.inputType = InputType.TYPE_NULL
        val newCalendar = Calendar.getInstance()
        val datePickerDialog = SpinnerDatePickerDialogBuilder()
            .context(this)
            .callback { view, year, monthOfYear, dayOfMonth ->
                // compose selected date as calendar instance
                val calendar = Calendar.getInstance()
                calendar.set(year,monthOfYear,dayOfMonth)

                // format date for ui
                val dateFormat = SimpleDateFormat("MM/YY")
                val strDate = dateFormat.format(calendar.time)

                // update card expiry text field
                card_expiry_edit.setText(strDate)
                // update view model
                viewModel.cardExpiry = strDate
            }
            .showDaySpinner(false)
            .minDate(newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), 1)
            .build()

        card_expiry_edit.setOnClickListener {
            datePickerDialog.show()
        }
        card_expiry_edit.setOnFocusChangeListener { v, hasFocus ->
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
        outState?.putInt(KEY_CARD_TYPE,viewModel.cardType.ordinal)
        outState?.putString(KEY_CARD_NUMBER,viewModel.cardNumber)
        outState?.putString(KEY_CARD_CVC_NUMBER,viewModel.cardCvc)
        outState?.putString(KEY_CARD_EXPIRY,viewModel.cardExpiry)
    }

    /**
     * Handles user ui card type selections.
     */
    fun onCardTypeRadioButtonClicked(view: View) {
        // pass card type selection to view model
        when(view.id) {
            R.id.radio_visa -> viewModel.cardType = CardType.VISA
            R.id.radio_master_card -> viewModel.cardType = CardType.MASTER_CARD
            R.id.radio_american -> viewModel.cardType = CardType.AMERICAN_EXPRESS
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
    private fun onCardNumberValidationUpdate(status: CardValidationStatus) {
        // display card number status error message if invalid
        card_number_edit.error = if (!status.isValid) status.errorMessage else null
    }

    /**
     * Handles view model card cvc validation state changes.
     */
    private fun onCardCvcValidationUpdate(status: CardValidationStatus) {
        // display card cvc status error message if invalid
        card_cvc_edit.error = if (!status.isValid) status.errorMessage else null
    }

    /**
     * Handles view model card expiry date validation state changes.
     */
    private fun onCardExpiryValidationUpdate(status: CardValidationStatus) {
        // display card expiry status error message if invalid
        card_expiry_edit.error = if (!status.isValid) status.errorMessage else null
    }
}
