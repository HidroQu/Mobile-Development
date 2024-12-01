package com.capstone.hidroqu.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

fun compressImage(file: File, context: Context): File {
    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.fromFile(file))
    var compressedFile = File(context.cacheDir, "compressed_${file.name}")

    var quality = 100
    var outputStream = FileOutputStream(compressedFile)

    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    outputStream.close()

    while (compressedFile.length() > 1_024_000 && quality > 10) {
        quality -= 10
        compressedFile = File(context.cacheDir, "compressed_${file.name}")
        outputStream = FileOutputStream(compressedFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        outputStream.close()
    }
    return compressedFile
}


fun isFileSizeValid(file: File): Boolean {
    val fileSizeInKb = file.length() / 1024
    return fileSizeInKb <= 1024
}