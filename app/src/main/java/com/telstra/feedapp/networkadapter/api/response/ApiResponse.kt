package com.telstra.feedapp.networkadapter.api.response

interface ApiResponse<ResponseClass : ApiStatus> {
    fun onSuccess(apiTag:String, message: String, apiResponse: ResponseClass)
    fun onComplete(apiTag:String, message: String)
    fun onError(apiTag:String, message: String)
    fun onError(apiTag:String, throwable: Throwable)
}