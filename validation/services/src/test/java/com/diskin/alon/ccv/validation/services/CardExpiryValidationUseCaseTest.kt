package com.diskin.alon.ccv.validation.services

import com.diskin.alon.ccv.validation.domain.validateCardExpiry
import com.diskin.alon.ccv.validation.services.usecase.CardExpiryValidationUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * [CardExpiryValidationUseCase] unit test class.
 */
@RunWith(JUnitParamsRunner::class)
class CardExpiryValidationUseCaseTest {

    // System under test
    private lateinit var useCase: CardExpiryValidationUseCase

    @Before
    fun setUp() {
        useCase = CardExpiryValidationUseCase()
    }

    @Test
    @Parameters(method = "useExecutionParams")
    fun shouldValidateCardExpiry_whenExecuted(calendar: Calendar, validation: Boolean) {
        // Test case fixture
        mockkStatic("com.diskin.alon.ccv.validation.domain.CardValidationKt")
        every { validateCardExpiry(calendar) } returns validation

        // Given an initialized use case

        // When use case is executed
        val isValid = useCase.execute(calendar).blockingGet()

        // Then use case should return a single observable that validates card expiry date
        verify { validateCardExpiry(calendar) }
        assertThat(isValid).isEqualTo(validation)
    }

    fun useExecutionParams() = arrayOf(
        arrayOf(getTestExpiry(),false),
        arrayOf(getTestExpiry2(),false),
        arrayOf(getTestExpiry3(),true))

    private fun getTestExpiry() = Calendar.getInstance()

    private fun getTestExpiry2(): Calendar {
        val calendar = Calendar.getInstance()

        calendar.add(Calendar.YEAR,1)
        return calendar
    }

    private fun getTestExpiry3(): Calendar {
        val calendar = Calendar.getInstance()

        calendar.add(Calendar.MONTH,-10)
        return calendar
    }
}