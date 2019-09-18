package com.diskin.alon.ccv.validation.presentation

import com.diskin.alon.ccv.validation.presentation.util.ServiceRequest
import com.diskin.alon.ccv.validation.presentation.util.UseCaseMediator
import com.diskin.alon.ccv.validation.services.usecase.UseCase
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * [UseCaseMediator]
 */
class UseCaseMediatorTest {

    // System under test
    private lateinit var mediator: UseCaseMediator

    // Mocked collaborators
    @Mock
    lateinit var testRequestUseCase: UseCase<Int,Unit>

    // Stub collaborator
    private val dispatch = HashMap<Class<out ServiceRequest<*, *>>, UseCase<*, *>>()
    private val testRequestUnmatchedUseCase: UseCase<String, Unit> = object : UseCase<String, Unit> {
        override fun execute(param: String) {

        }
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        dispatch.put(TestRequest::class.java,testRequestUseCase)
        dispatch.put(TestRequest2::class.java,testRequestUnmatchedUseCase)

        mediator = UseCaseMediator(dispatch)
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldThrowException_whenUnknownRequestExecuted() {
        // Given an initialized mediator

        // When mediator is asked to execute an unknown request
        mediator.execute(UnknownTestRequest("str"))

        // Then mediator should throw an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldThrowException_whenUnknownRequestUseCaseTypesNotMatching() {
        // Given an initialized mediator

        // When mediator is asked to execute a known request that has incorrect types matching with use case
        mediator.execute(TestRequest2(3))

        // Then mediator should throw an IllegalArgumentException
    }

    @Test
    fun shouldExecuteMatchingUseCase_whenKnownRequestExecuted() {
        // Given an initialized mediator
        val testParam = 10

        // When mediator is asked to execute a known request
        mediator.execute(TestRequest(testParam))

        // Then mediator should throw an IllegalArgumentException
        verify(testRequestUseCase).execute(eq(testParam))
    }

    data class TestRequest(val testParam: Int) : ServiceRequest<Int,Unit>(testParam)

    data class TestRequest2(val testParam: Int) : ServiceRequest<Int,String>(testParam)

    data class UnknownTestRequest(val testParam: String) : ServiceRequest<String,Unit>(testParam)
}