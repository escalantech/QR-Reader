package com.ltw.qrreader

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import com.ltw.qrreader.util.LogUtils
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.ltw.qrreader.util.ScanHistoryManager
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var barcodeScanner: BarcodeScanner
    private lateinit var previewView: PreviewView
    private lateinit var historyManager: ScanHistoryManager
    private var isScannerActive = true

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(
                this,
                R.string.permission_denied,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private val pickImagesLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { processImageUri(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            setContentView(R.layout.activity_main)

            // Inicializamos componentes de forma segura
            try {
                previewView = findViewById(R.id.previewView)
                barcodeScanner = BarcodeScanning.getClient()
                historyManager = ScanHistoryManager(this)
            } catch (e: Exception) {
                LogUtils.e("MainActivity", "Error al inicializar componentes: ${e.message}", e)
                Toast.makeText(this, "Error al inicializar la aplicación", Toast.LENGTH_LONG).show()
                return
            }

            // Configuramos los botones con manejo de excepciones
            try {
                findViewById<View>(R.id.galleryButton)?.setOnClickListener {
                    try {
                        pickImagesLauncher.launch("image/*")
                    } catch (e: Exception) {
                        LogUtils.e("MainActivity", "Error al abrir selector de imágenes: ${e.message}", e)
                    }
                }

                findViewById<View>(R.id.historyButton)?.setOnClickListener {
                    try {
                        startActivity(Intent(this, HistoryActivity::class.java))
                    } catch (e: Exception) {
                        LogUtils.e("MainActivity", "Error al abrir HistoryActivity: ${e.message}", e)
                    }
                }
            } catch (e: Exception) {
                LogUtils.e("MainActivity", "Error al configurar botones: ${e.message}", e)
            }

            // Retrasamos la inicialización de la cámara para evitar problemas
            cameraExecutor = Executors.newSingleThreadExecutor()
            
            // Retrasamos la configuración de la cámara para dar tiempo a que la actividad se estabilice
            findViewById<View>(android.R.id.content).post {
                try {
                    checkCameraPermission()
                } catch (e: Exception) {
                    LogUtils.e("MainActivity", "Error al verificar permisos: ${e.message}", e)
                }
            }
        } catch (e: Exception) {
            LogUtils.e("MainActivity", "Error fatal en onCreate: ${e.message}", e)
            // En caso de error crítico, mostramos un mensaje y terminamos la actividad
            Toast.makeText(this, "Error al iniciar la aplicación", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                if (!isScannerActive) {
                    imageProxy.close()
                    return@setAnalyzer
                }

                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    val image = InputImage.fromMediaImage(
                        mediaImage,
                        imageProxy.imageInfo.rotationDegrees
                    )

                    barcodeScanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            for (barcode in barcodes) {
                                barcode.rawValue?.let { value ->
                                    // Detenemos temporalmente el escáner
                                    isScannerActive = false
                                    // Procesamos el resultado del escaneo
                                    processScanResult(value, barcode.valueType == Barcode.TYPE_URL)
                                }
                            }
                        }
                        .addOnFailureListener {
                            LogUtils.e("MainActivity", "Error al escanear código: ${it.message}", it)
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                } else {
                    imageProxy.close()
                }
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalysis
                )
            } catch (e: Exception) {
                LogUtils.e("MainActivity", "Error al iniciar la cámara: ${e.message}", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun processImageUri(uri: Uri) {
        try {
            val inputImage = InputImage.fromFilePath(this, uri)
            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        for (barcode in barcodes) {
                            barcode.rawValue?.let { value ->
                                processScanResult(value, barcode.valueType == Barcode.TYPE_URL)
                                return@addOnSuccessListener
                            }
                        }
                    } else {
                        Toast.makeText(
                            this,
                            R.string.no_qr_code_found,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        R.string.image_scanning_error,
                        Toast.LENGTH_SHORT
                    ).show()
                    LogUtils.e("MainActivity", "Error al procesar imagen: ${it.message}", it)
                }
        } catch (e: Exception) {
            Toast.makeText(
                this,
                R.string.image_scanning_error,
                Toast.LENGTH_SHORT
            ).show()
            LogUtils.e("MainActivity", "Error al procesar imagen: ${e.message}", e)
        }
    }

    private fun processScanResult(content: String, isUrl: Boolean) {
        // Guardamos en el historial
        historyManager.saveToHistory(content, isUrl)

        // Mostramos el diálogo con el resultado
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.scan_result)
        builder.setMessage(content)

        // Botón para abrir URL (solo si es URL)
        if (isUrl) {
            builder.setPositiveButton(R.string.open_url) { _, _ ->
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(content))
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        R.string.no_url_found,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Botón para copiar
        builder.setNeutralButton(R.string.copy) { _, _ ->
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("QR Content", content)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                this,
                R.string.copied_to_clipboard,
                Toast.LENGTH_SHORT
            ).show()
        }

        // Botón para compartir
        builder.setNegativeButton(R.string.share) { _, _ ->
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, content)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(sendIntent, getString(R.string.share)))
        }

        // Cuando se cierra el diálogo, reactivamos el escáner
        builder.setOnDismissListener {
            isScannerActive = true
        }

        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        barcodeScanner.close()
    }
}