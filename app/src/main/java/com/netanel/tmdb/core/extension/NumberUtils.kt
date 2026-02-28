package com.netanel.tmdb.core.extension

import kotlin.math.roundToInt


/**
 * Created by netanelamar on 28/02/2026.
 * NetanelCA2@gmail.com
 */

fun Double.formatToOneDecimalPlace(){
    this.takeIf { it > 0.0 }
        ?.let { ((it * 10).roundToInt() / 10.0).toString() }
}