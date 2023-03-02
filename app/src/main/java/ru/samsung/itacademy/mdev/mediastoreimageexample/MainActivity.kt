package ru.samsung.itacademy.mdev.mediastoreimageexample

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    lateinit var textResults: TextView
    private val dateFormat = SimpleDateFormat.getDateTimeInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textResults = findViewById(R.id.textResults)
        findViewById<Button>(R.id.btnImages).setOnClickListener {
            loadImages()
        }
    }

    private fun loadImages() {
        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchImagesAndShowResult()
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQ_IMAGES
            )
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ) {

                fetchImagesAndShowResult()

        } else {
            Snackbar.make(
                    findViewById(android.R.id.content),
                    "Can't get data without permission",
                    Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun fetchImagesAndShowResult() {
        val stringBuilder = StringBuilder()
        val projection = arrayOf( // media-database-columns-to-retrieve
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_MODIFIED
        )
        val selection = null // sql-where-clause-with-placeholder-variables
        val selectionArgs = null // values-of-placeholder-variables
        val sortOrder =
                "${MediaStore.Images.ImageColumns.DATE_MODIFIED} DESC LIMIT 20" // sql-order-by-clause
        applicationContext.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
        )?.use { cursor ->
            val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME)
            val dateModifiedColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_MODIFIED)
Log.d("RRR","1231231")
            while (cursor.moveToNext()) {
                Log.d("RRR",stringBuilder.toString())
                stringBuilder.append(cursor.getString(nameColumn)).append("\n")
                        .append(dateFormat.format(cursor.getLong(dateModifiedColumn) * 1000L))
                        .append("\n\n")
            }
            textResults.text = stringBuilder
        }
    }

    companion object {
        const val TAG = "MainActivity"
        const val REQ_IMAGES = 0
    }

}