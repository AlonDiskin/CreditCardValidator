package com.diskin.alon.ccv.validation.services

import com.diskin.alon.ccv.validation.domain.validateCardNumber
import com.diskin.alon.ccv.validation.services.usecase.CardNumberValidationUseCase.Params
import com.diskin.alon.ccv.validation.services.model.CardType
import com.diskin.alon.ccv.validation.services.usecase.CardNumberValidationUseCase
import com.diskin.alon.ccv.validation.services.util.mapToDomainType
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * [CardNumberValidationUseCase] unit test class.
 */
@RunWith(JUnitParamsRunner::class)
class CardNumberValidationUseCaseTest {

    // System under test
    private lateinit var useCase: CardNumberValidationUseCase

    @Before
    fun setUp() {
        useCase = CardNumberValidationUseCase()
    }

    @Test
    @Parameters(method = "useExecutionParams")
    fun shouldValidateCardNumber_whenExecuted(params: Params,validation: Boolean) {
        // Test case fixture
        mockkStatic("com.diskin.alon.ccv.validation.domain.CardValidationKt")
        every { validateCardNumber(params.number,params.cardType.mapToDomainType()) } returns validation

        // Given an initialized use case

        // When use case is executed
        val isValid = useCase.execute(params).blockingGet()

        // Then use case should return a single observable that validates number
        verify { validateCardNumber(params.number,params.cardType.mapToDomainType()) }
        assertThat(isValid).isEqualTo(validation)

        // And subscribe its validation on computation thread

    }

    fun useExecutionParams() = arrayOf(
        arrayOf(Params("1234", CardType.VISA),true),
        arrayOf(Params("1234634", CardType.MASTER_CARD),false),
        arrayOf(Params("6789634567", CardType.AMERICAN_EXPRESS),true))
}