package com.netanel.tmdb.domain.image

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * Created by netanelamar on 23/10/2025.
 * NetanelCA2@gmail.com
 */
interface ImageApi {

    /**
     * לדוגמה: https://image.tmdb.org/t/p/original/7QirCB1o80NEFpQGlQRZerZbQEp.jpg
     * מחזיר תוכן בינארי של התמונה (JPEG/PNG וכו').
     */
    @GET
    @Streaming
    suspend fun fetchImage(
        @Url fullImageUrl: String
    ): Response<ResponseBody>
}
