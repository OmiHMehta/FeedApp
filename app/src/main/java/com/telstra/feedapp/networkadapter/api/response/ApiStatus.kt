package com.telstra.feedapp.networkadapter.api.response

abstract class ApiStatus {
    var status: Int? = 0
        get() = field ?: 0

    var message: String? = ""
        get() = field ?: ""
}