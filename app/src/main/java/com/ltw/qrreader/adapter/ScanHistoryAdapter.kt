package com.ltw.qrreader.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ltw.qrreader.R
import com.ltw.qrreader.util.ScanHistoryItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Adaptador para mostrar elementos de historial de escaneos en un RecyclerView
 */
class ScanHistoryAdapter : ListAdapter<ScanHistoryItem, ScanHistoryAdapter.ScanViewHolder>(SCAN_COMPARATOR) {

    // Interfaz para manejar clics en los elementos
    var onItemClickListener: ((ScanHistoryItem) -> Unit)? = null
    var onShareClickListener: ((ScanHistoryItem) -> Unit)? = null
    var onCopyClickListener: ((ScanHistoryItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scan_history, parent, false)
        return ScanViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    inner class ScanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvContent: TextView = itemView.findViewById(R.id.scanContentText)
        private val tvTimestamp: TextView = itemView.findViewById(R.id.scanDateText)
        private val btnOpen: View = itemView.findViewById(R.id.openButton)
        private val btnShare: View = itemView.findViewById(R.id.shareButton)
        private val btnCopy: View = itemView.findViewById(R.id.copyButton)

        init {
            // Configura listener de clics para todo el elemento
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(getItem(position))
                }
            }

            // Listener para el botón de compartir
            btnShare.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onShareClickListener?.invoke(getItem(position))
                }
            }

            // Listener para el botón de copiar
            btnCopy.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onCopyClickListener?.invoke(getItem(position))
                }
            }

            // Listener para el botón de abrir (solo para URLs)
            btnOpen.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item.isUrl) {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.content))
                            itemView.context.startActivity(intent)
                        } catch (e: Exception) {
                            // Si hay un error al abrir la URL, notificamos al listener
                            onItemClickListener?.invoke(item)
                        }
                    }
                }
            }
        }

        fun bind(item: ScanHistoryItem) {
            tvContent.text = item.content
            tvTimestamp.text = formatDate(item.timestamp)
            
            // Mostrar/ocultar botón de abrir según si es URL o no
            btnOpen.visibility = if (item.isUrl) View.VISIBLE else View.GONE
        }

        private fun formatDate(timestamp: Long): String {
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            return formatter.format(Date(timestamp))
        }
    }

    companion object {
        private val SCAN_COMPARATOR = object : DiffUtil.ItemCallback<ScanHistoryItem>() {
            override fun areItemsTheSame(oldItem: ScanHistoryItem, newItem: ScanHistoryItem): Boolean {
                return oldItem.content == newItem.content && oldItem.timestamp == newItem.timestamp
            }

            override fun areContentsTheSame(oldItem: ScanHistoryItem, newItem: ScanHistoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}