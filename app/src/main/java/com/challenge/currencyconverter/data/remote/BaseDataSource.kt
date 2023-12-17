package com.challenge.currencyconverter.data.remote

import com.challenge.currencyconverter.utils.ResourceStatus
import retrofit2.Response
import timber.log.Timber

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): ResourceStatus<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return ResourceStatus.success(body)
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): ResourceStatus<T> {
        Timber.d(message)
        return ResourceStatus.error("Network call has failed for a following reason: $message")
    }

}