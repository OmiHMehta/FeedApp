package com.telstra.feedapp.networkadapter.api.response

interface ApiResponse<ResponseClass : ApiStatus> {
    fun onSuccess(message: String, apiResponse: ResponseClass)
    fun onError(message: String)
    fun onError(throwable: Throwable)
}