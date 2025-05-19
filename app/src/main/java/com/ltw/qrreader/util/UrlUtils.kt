package com.chris.simpleqrreader.util

import android.util.Patterns
import java.util.regex.Pattern

/**
 * Utility class for URL related operations.
 */
object UrlUtils {
    
    /**
     * Checks if the given text is a valid URL.
     * @param text The text to check
     * @return True if the text is a valid URL, false otherwise
     */
    fun isValidUrl(text: String?): Boolean {
        if (text.isNullOrBlank()) return false
        
        val pattern = Patterns.WEB_URL
        val matcher = pattern.matcher(text.trim())
        return matcher.matches()
    }
    
    /**
     * Ensures the URL has a proper scheme (http/https).
     * @param url The URL to process
     * @return The URL with a proper scheme
     */
    fun ensureHttpScheme(url: String): String {
        val trimmedUrl = url.trim()
        return if (!trimmedUrl.startsWith("http://") && !trimmedUrl.startsWith("https://")) {
            "https://$trimmedUrl"
        } else {
            trimmedUrl
        }
    }
}
