package com.diskin.alon.ccv.validation.services

import com.diskin.alon.ccv.validation.domain.validateCardCvcCode
import com.diskin.alon.ccv.validation.services.model.CardTypeDto
import com.diskin.alon.ccv.validation.services.usecase.CardCvcValidationUseCase
import com.diskin.alon.ccv.validation.services.usecase.CardCvcValidationUseCase.Params
import com.diskin.alon.ccv.validation.services.util.mapToDomainType
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import io.reactivex.schedulers.TestScheduler
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * [CardCvcValidationUseCase] unit test class.
 */
@RunWith(JUnitParamsRunner::class)
class CardCvcValidationUseCaseTest {

    // System under test
    private lateinit var useCase: CardCvcValidationUseCase

    @Before
    fun setUp() {
        useCase = CardCvcValidationUseCase()
    }

    @Test
    @Parameters(method = "useExecutionParams")
    fun shouldValidateCardCvc_whenExecuted(params: Params, validation: Boolean) {
        // Test case fixture
        val testScheduler = TestScheduler()
        mockkStatic("com.diskin.alon.ccv.validation.domain.CardValidationKt")
        every { validateCardCvcCode(params.cvcCode,params.cardType.mapToDomainType()) } returns validation

        // Given an initialized use case

        // When use case is executed
        val isValid = useCase.execute(params).blockingGet()

        // Then use case should return a single observable that validates card cvc
        verify { validateCardCvcCode(params.cvcCode,params.cardType.mapToDomainType()) }
        assertThat(isValid).isEqualTo(validation)
    }

    fun useExecutionParams() = arrayOf(
        arrayOf(Params("1234", CardTypeDto.VISA),false),
        arrayOf(Params("1234634", CardTypeDto.MASTER_CARD),false),
        arrayOf(Params("678967", CardTypeDto.AMERICAN_EXPRESS),true))
}