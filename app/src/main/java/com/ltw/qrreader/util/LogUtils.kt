package com.ltw.qrreader.util

import android.util.Log
import com.ltw.qrreader.BuildConfig

/**
 * Utilidad para gestionar logs de forma configurable según el entorno (debug/release)
 */
object LogUtils {
    /**
     * Log nivel error - solo se muestra en debug
     */
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.ENABLE_LOGS) {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        }
    }

    /**
     * Log nivel info - solo se muestra en debug
     */
    fun i(tag: String, message: String) {
        if (BuildConfig.ENABLE_LOGS) {
            Log.i(tag, message)
        }
    }

    /**
     * Log nivel debug - solo se muestra en debug
     */
    fun d(tag: String, message: String) {
        if (BuildConfig.ENABLE_LOGS) {
            Log.d(tag, message)
        }
    }

    /**
     * Log nivel warning - solo se muestra en debug
     */
    fun w(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.ENABLE_LOGS) {
            if (throwable != null) {
                Log.w(tag, message, throwable)
            } else {
                Log.w(tag, message)
            }
        }
    }
}
