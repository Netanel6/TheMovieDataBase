package com.netanel.tmdb.core.extension

import kotlin.math.roundToInt


/**
 * Created by netanelamar on 28/02/2026.
 * NetanelCA2@gmail.com
 */

fun Double.formatToOneDecimalPlace(): String? {
    return this.takeIf { this > 0.0 }
        ?.let { ((this * 10).roundToInt() / 10.0).toString() }
}
