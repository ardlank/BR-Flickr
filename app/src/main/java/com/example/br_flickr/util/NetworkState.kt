package com.example.br_flickr.util


//Store the current state of async calls for display of loaders
@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status,
    val msg: String? = null
) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)
        fun error(msg: String?) = NetworkState(Status.FAILED, msg)
    }
}

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}