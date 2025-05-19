package com.ltw.qrreader.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Clase para gestionar el historial de escaneos utilizando SharedPreferences
 */
class ScanHistoryManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    /**
     * Guarda un nuevo resultado de escaneo en el historial.
     * Mantiene solo los últimos MAX_HISTORY_ITEMS elementos.
     */
    fun saveToHistory(content: String, isUrl: Boolean) {
        val historyList = getHistoryList().toMutableList()
        val newItem = ScanHistoryItem(content, System.currentTimeMillis(), isUrl)
        
        // Comprueba si ya existe un elemento con el mismo contenido y lo elimina
        historyList.removeAll { it.content == content }
        
        // Añade el nuevo elemento al principio
        historyList.add(0, newItem)
        
        // Limita el tamaño de la lista a MAX_HISTORY_ITEMS
        if (historyList.size > MAX_HISTORY_ITEMS) {
            historyList.removeAt(historyList.size - 1)
        }
        
        // Guarda la lista actualizada
        val json = gson.toJson(historyList)
        prefs.edit().putString(KEY_SCAN_HISTORY, json).apply()
    }

    /**
     * Obtiene la lista completa del historial de escaneos.
     */
    fun getHistoryList(): List<ScanHistoryItem> {
        val json = prefs.getString(KEY_SCAN_HISTORY, "[]")
        val type = object : TypeToken<List<ScanHistoryItem>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    /**
     * Borra todo el historial de escaneos.
     */
    fun clearHistory() {
        prefs.edit().remove(KEY_SCAN_HISTORY).apply()
    }

    companion object {
        private const val PREFS_NAME = "qr_reader_prefs"
        private const val KEY_SCAN_HISTORY = "scan_history"
        private const val MAX_HISTORY_ITEMS = 10
    }
}

/**
 * Modelo de datos para un elemento del historial de escaneos.
 */
data class ScanHistoryItem(
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isUrl: Boolean = false
)