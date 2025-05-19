package com.ltw.qrreader

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ltw.qrreader.adapter.ScanHistoryAdapter
import com.ltw.qrreader.util.ScanHistoryManager

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScanHistoryAdapter
    private lateinit var historyManager: ScanHistoryManager
    private lateinit var emptyView: TextView
    // Eliminamos el botón de limpiar que no está en el layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Habilitamos el botón de retroceso en la barra de acción
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.history)

        // Inicializamos el gestor de historial
        historyManager = ScanHistoryManager(this)

        // Configuramos las vistas
        recyclerView = findViewById(R.id.historyRecyclerView)
        emptyView = findViewById(R.id.emptyHistoryText)

        // Configuramos el adaptador
        adapter = ScanHistoryAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Configuramos los listeners para el adaptador
        adapter.onItemClickListener = { item ->
            // Al hacer clic en un elemento mostramos un diálogo de detalle
            Toast.makeText(this, item.content, Toast.LENGTH_SHORT).show()
        }

        adapter.onCopyClickListener = { item ->
            // Copiamos el contenido al portapapeles
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("QR Content", item.content)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show()
        }

        adapter.onShareClickListener = { item ->
            // Compartimos el contenido
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, item.content)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
        }

        // Añadimos botón de borrar historial via código
        supportActionBar?.let { actionBar ->
            // Agregamos un botón de menú en la barra de acción
            actionBar.setDisplayShowCustomEnabled(true)
        }

        // Cargamos los datos del historial
        loadHistoryData()
    }

    private fun loadHistoryData() {
        val historyList = historyManager.getHistoryList()
        adapter.submitList(historyList)
        
        // Mostrar mensaje cuando no hay historial
        if (historyList.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_clear_history -> {
                historyManager.clearHistory()
                loadHistoryData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        return true
    }
}