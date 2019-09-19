package com.diskin.alon.ccv.validation.services.util

import com.diskin.alon.ccv.validation.domain.ValidatedCardType
import com.diskin.alon.ccv.validation.services.model.CardType

/**
 * Extends card type enum to map its value to domain card type.
 */
fun CardType.mapToDomainType(): ValidatedCardType =
    when(this) {
        CardType.VISA -> ValidatedCardType.VISA
        CardType.MASTER_CARD -> ValidatedCardType.MASTER_CARD
        CardType.AMERICAN_EXPRESS -> ValidatedCardType.AMERICAN_EXPRESS
    }