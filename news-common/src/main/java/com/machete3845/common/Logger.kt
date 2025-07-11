package com.machete3845.common

import android.util.Log

interface Logger {
    fun d(
        tag: String,
        message: String
    )

    fun e(
        tag: String,
        message: String
    )
}

fun AndroidLogcatLogger() =
    object : Logger {
        override fun d(
            tag: String,
            message: String
        ) {
            Log.d(tag, message)
        }

        override fun e(
            tag: String,
            message: String
        ) {
            Log.e(tag, message)
        }
    }
