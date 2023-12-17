package com.challenge.currencyconverter.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.challenge.currencyconverter.utils.ResourceStatus.Status.*
import kotlinx.coroutines.Dispatchers

fun <T, A> performGetOperation(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> ResourceStatus<A>,
    saveCallResult: suspend (A) -> Unit): LiveData<ResourceStatus<T>> =
    liveData(Dispatchers.IO) {
        Log.d("performGetOperation", "Start")
        emit(ResourceStatus.loading())
        val source = databaseQuery.invoke().map { ResourceStatus.success(it) }

        emitSource(source)
        val responseStatus = networkCall.invoke()
        if (responseStatus.status == SUCCESS && responseStatus.data != null) {
            Log.d("performGetOperation", "networkCall")
            saveCallResult(responseStatus.data)
        } else if (responseStatus.status == ERROR) {
            Log.d("performGetOperation", responseStatus.toString())
            emit(ResourceStatus.error(responseStatus.message!!))
        }
        emitSource(source)
    }