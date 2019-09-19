package com.diskin.alon.ccv.validation.services

import com.diskin.alon.ccv.validation.domain.ValidatedCardType
import com.diskin.alon.ccv.validation.services.model.CardType
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
    fun mapCardTypeDtoToDomainTypeTest(cardTypeDto: CardType, cardType: ValidatedCardType) {
        assertThat(cardTypeDto.mapToDomainType()).isEqualTo(cardType)
    }

    fun mapToDomainParams() = arrayOf(
        arrayOf(CardType.VISA, ValidatedCardType.VISA),
        arrayOf(CardType.MASTER_CARD, ValidatedCardType.MASTER_CARD),
        arrayOf(CardType.AMERICAN_EXPRESS, ValidatedCardType.AMERICAN_EXPRESS))
}