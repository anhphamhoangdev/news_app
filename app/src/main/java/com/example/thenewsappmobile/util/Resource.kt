package com.example.thenewsappmobile.util


/*
                        Person Responsible for this class : PHAM HOANG ANH - 21110753
*/



// manage and handle all the possible states that can occur during the data fetch process: success, error, and loading.
sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    // represents the successful data fetch state, containing the returned data.
    class Success<T>(data: T?): Resource<T>(data)

    // represents the error state, containing the error message and potentially the data (if any).
    class Error<T>(message: String?, data: T? = null): Resource<T>(data, message)

    // represents the state of the data fetch being in progress.
    class Loading<T>: Resource<T>()

}