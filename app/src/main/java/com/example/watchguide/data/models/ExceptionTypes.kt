package com.example.watchguide.data.models

import com.example.watchguide.R

enum class ExceptionTypes(val image: Int?, val message: String?) {
    NoInternetConnection(R.drawable.no_wifi, "No internet connection"),
    ServerSideError(R.drawable.server, "Server side error"),
    NotFound(R.drawable.server, "Not found"),
    NullException(null, null),
    UnexpectedError(R.drawable.server, "Unexpected error")
}