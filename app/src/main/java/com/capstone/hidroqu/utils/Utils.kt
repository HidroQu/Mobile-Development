package com.capstone.hidroqu.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun createCustomTempFile(context: Context): File {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val fileName = "JPEG_${timeStamp}_"
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(fileName, ".jpg", storageDir)
}


fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
    outputStream.close()
    inputStream.close()
    return myFile
}

fun compressImage(file: File, context: Context): File {
    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.fromFile(file))
    var compressedFile = File(context.cacheDir, "compressed_${file.name}")

    var quality = 100
    var outputStream = FileOutputStream(compressedFile)

    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    outputStream.close()

    Log.d("FileUtils", "Compression completed, File size after compression: ${compressedFile.length() / 1024} KB")
    while (compressedFile.length() > 1_024_000 && quality > 10) {
        quality -= 10
        compressedFile = File(context.cacheDir, "compressed_${file.name}")
        outputStream = FileOutputStream(compressedFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        outputStream.close()
        Log.d("FileUtils", "Compression iteration with quality $quality, File size: ${compressedFile.length() / 1024} KB")
    }
    Log.d("FileUtils", "Final compressed file size: ${compressedFile.length() / 1024} KB")
    return compressedFile
}


fun isFileSizeValid(file: File): Boolean {
    val fileSizeInKb = file.length() / 2048
    return fileSizeInKb <= 2048
}