package com.diskin.alon.ccv.validation.services

import com.diskin.alon.ccv.validation.domain.CardType
import com.diskin.alon.ccv.validation.services.model.CardTypeDto
import com.diskin.alon.ccv.validation.services.util.mapToDomainType
import com.google.common.truth.Truth.assertThat
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Mapper top level functions test cases
 */
@RunWith(JUnitParamsRunner::class)
class MapperTest {

    @Test
    @Parameters(method = "mapToDomainParams")
    fun mapCardTypeDtoToDomainTypeTest(cardTypeDto: CardTypeDto, cardType: CardType) {
        assertThat(cardTypeDto.mapToDomainType()).isEqualTo(cardType)
    }

    fun mapToDomainParams() = arrayOf(
        arrayOf(CardTypeDto.VISA, CardType.VISA),
        arrayOf(CardTypeDto.MASTER_CARD, CardType.MASTER_CARD),
        arrayOf(CardTypeDto.AMERICAN_EXPRESS, CardType.AMERICAN_EXPRESS))
}