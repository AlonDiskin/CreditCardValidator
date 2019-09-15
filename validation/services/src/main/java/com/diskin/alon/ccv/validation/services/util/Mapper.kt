package com.diskin.alon.ccv.validation.services.util

import com.diskin.alon.ccv.validation.domain.CardType
import com.diskin.alon.ccv.validation.services.model.CardTypeDto

/**
 * Extends card type enum to map its value to domain card type.
 */
fun CardTypeDto.mapToDomainType(): CardType =
    when(this) {
        CardTypeDto.VISA -> CardType.VISA
        CardTypeDto.MASTER_CARD -> CardType.MASTER_CARD
        CardTypeDto.AMERICAN_EXPRESS -> CardType.AMERICAN_EXPRESS
    }