package com.netanel.tmdb.core.extension

import com.netanel.tmdb.domain.Constants


/**
 * Created by netanelamar on 28/02/2026.
 * NetanelCA2@gmail.com
 */

fun String?.toImageUrl() {
    this?.let { Constants.IMAGES_URL + it }
}