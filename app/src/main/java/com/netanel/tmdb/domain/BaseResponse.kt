package com.netanel.tmdb.domain

/**
 * A generic wrapper for API responses.
 * Used to communicate Success / Error / Loading states
 * from Repository to ViewModel.
 */
sealed class BaseResponse<out T> {

    /**
     * Success state with data.
     */
    data class Success<T>(
        val data: T,
        val code: Int? = null
    ) : BaseResponse<T>()

    /**
     * Error state with optional error body and HTTP code.
     */
    data class Error(
        val message: String,
        val code: Int? = null,
        val throwable: Throwable? = null
    ) : BaseResponse<Nothing>()

}
