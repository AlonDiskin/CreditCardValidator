package com.diskin.alon.ccv

import com.mauriciotogneri.greencoffee.GreenCoffeeTest
import com.mauriciotogneri.greencoffee.ScenarioConfig
import com.mauriciotogneri.greencoffee.GreenCoffeeConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.io.IOException

/**
 * 'Card validated' feature rule step definitions runner.
 */
@RunWith(Parameterized::class)
class CardValidatedStepsRunner(scenario: ScenarioConfig) : GreenCoffeeTest(scenario) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        @Throws(IOException::class)
        fun scenarios(): Iterable<ScenarioConfig> {
            return GreenCoffeeConfig()
                .withFeatureFromAssets("assets/feature/card_validation.feature")
                .withTags("@card-validated")
                .scenarios()
        }
    }

    @Test
    fun test() {
        start(CardValidatedSteps())
    }
}