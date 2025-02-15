package com.capstone.hidroqu.ui.screen.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.capstone.hidroqu.R
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder
import java.util.concurrent.Executors

@Composable
fun CameraPermissionScreen(cameraMode: String, navHostController: NavHostController) {

    var hasCameraPermission by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }
    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                hasCameraPermission = true
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    if (hasCameraPermission) {
        CameraScreen(cameraMode, navHostController)
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Izin kamera diperlukan untuk melanjutkan",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(cameraMode: String, navHostController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var isFrontCamera by remember { mutableStateOf(false) }
    var flashEnabled by remember { mutableStateOf(false) }

    var cameraControl: CameraControl? by remember { mutableStateOf(null) }
    var cameraInfo: CameraInfo? by remember { mutableStateOf(null) }
    var cameraProvider: ProcessCameraProvider? by remember { mutableStateOf(null) }
    var showTooltipOverlay by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val encodedUri = URLEncoder.encode(uri.toString(), "UTF-8")
                if (cameraMode == "Poto Tanam") {
                    navHostController.navigate("ResultPotoTanam/$encodedUri")
                } else if (cameraMode == "Scan Tanam") {
                    navHostController.navigate("ResultScanTanam/$encodedUri")
                }
            }
        }
    )

    fun bindCameraUseCases() {
        val cameraSelector = if (isFrontCamera) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }

        try {
            cameraProvider?.unbindAll()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                .setFlashMode(if (flashEnabled) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF)
                .build()

            val camera = cameraProvider?.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
            cameraControl = camera?.cameraControl
            cameraInfo = camera?.cameraInfo

            if (isFrontCamera) {
                cameraControl?.enableTorch(false)
                flashEnabled = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LaunchedEffect(Unit) {
        flashEnabled = false
        cameraControl?.enableTorch(flashEnabled)
    }

    var animationProgress by remember { mutableStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = animationProgress,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing), label = ""
    )

    val density = LocalDensity.current
    val animatedCornerRadius = with(density) { animateFloatAsState(
        targetValue = 25.dp.toPx() * animatedProgress,
        animationSpec = tween(durationMillis = 2000, easing = LinearOutSlowInEasing), label = ""
    ).value }

    LaunchedEffect(Unit) {
        animationProgress = 1f
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        cameraMode,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (!isFrontCamera) {
                            flashEnabled = !flashEnabled
                            cameraControl?.enableTorch(flashEnabled)
                        } else {
                            flashEnabled = false
                            cameraControl?.enableTorch(false)
                        }
                    }) {
                        Icon(
                            painter = if (flashEnabled) painterResource(R.drawable.ic_flash) else painterResource(R.drawable.ic_flash_off),
                            contentDescription = "Flash",
                            tint = if (flashEnabled) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            BottomBar(
                onGalleryClick = { galleryLauncher.launch("image/*") },
                onCaptureClick = {
                    takePhoto(context, imageCapture, cameraExecutor, flashEnabled, isFrontCamera, cameraMode, navHostController)
                },
                onSwitchCameraClick = {
                    isFrontCamera = !isFrontCamera
                    bindCameraUseCases()
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.onPrimary)
            )
        }
    ) { padding ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier
                    .fillMaxSize()
            )
            IconButton(
                onClick = { showTooltipOverlay = !showTooltipOverlay },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Show Tooltip",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
            if (showTooltipOverlay) {
                Box(
                    modifier = Modifier
                        .zIndex(1f)
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f))
                        .pointerInput(Unit) {
                            detectTapGestures {
                                // Block any interaction with underlying elements
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.onPrimary,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(30.dp)
                            .widthIn(max = 260.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Panduan diagnosis",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )

                        val steps = if (cameraMode == "Poto Tanam") {
                            listOf(
                                "Dekatkan Kamera ke Tanaman: Pastikan tanaman yang ingin kamu cek kondisi nutrisinya masuk dalam bingkai layar dengan jelas.",
                                "Fokuskan Kamera pada Daun: Arahkan fokus kamera tepat pada daun tanaman untuk mendapatkan hasil terbaik.",
                                "Cahaya yang Tepat: Pastikan cahaya cukup agar tanaman terlihat jelas, tidak terlalu terang atau gelap.",
                            )
                        } else {
                            listOf(
                                "Arahkan Kamera ke Tanaman: Pastikan tanaman yang ingin dikenali jenisnya berada dalam bingkai layar.",
                                "Fokus pada Tanaman Utama: Pastikan tanaman yang akan dikenali terlihat jelas.",
                                "Pencahayaan yang Pas: Gunakan pencahayaan yang cukup agar detail tanaman terlihat jelas, tanpa bayangan atau silau."
                            )
                        }
                        steps.forEachIndexed { index, step ->
                            Row(
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = String.format("%-4s", "${index + 1}."),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                                Text(
                                    text = step,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                            }
                        }
                        Button(
                            onClick = { showTooltipOverlay = !showTooltipOverlay },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                text = "Tutup Panduan",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }

            val color = MaterialTheme.colorScheme.onPrimaryContainer
            Canvas(modifier = Modifier
                .fillMaxSize()
                .zIndex(0f)
            ) {
                val strokeWidth = 5f
                val baseCornerLength = 60f
                val cornerLength = baseCornerLength + (animatedProgress * 150f)
                val centerX = size.width / 2
                val centerY = size.height / 2
                val widthOffset = animatedProgress * (size.width / 3f)
                val heightOffset = animatedProgress * (size.height / 3f)

                drawRoundRect(
                    color = color,
                    topLeft = Offset(centerX - widthOffset, centerY - heightOffset),
                    size = androidx.compose.ui.geometry.Size(cornerLength, strokeWidth),
                    cornerRadius = CornerRadius(animatedCornerRadius, animatedCornerRadius)
                )
                drawRoundRect(
                    color = color,
                    topLeft = Offset(centerX - widthOffset, centerY - heightOffset),
                    size = androidx.compose.ui.geometry.Size(strokeWidth, cornerLength),
                    cornerRadius = CornerRadius(animatedCornerRadius, animatedCornerRadius)
                )
                drawRoundRect(
                    color = color,
                    topLeft = Offset(centerX + widthOffset - cornerLength, centerY - heightOffset),
                    size = androidx.compose.ui.geometry.Size(cornerLength, strokeWidth),
                    cornerRadius = CornerRadius(animatedCornerRadius, animatedCornerRadius)
                )
                drawRoundRect(
                    color = color,
                    topLeft = Offset(centerX + widthOffset - strokeWidth, centerY - heightOffset),
                    size = androidx.compose.ui.geometry.Size(strokeWidth, cornerLength),
                    cornerRadius = CornerRadius(animatedCornerRadius, animatedCornerRadius)
                )
                drawRoundRect(
                    color = color,
                    topLeft = Offset(centerX - widthOffset, centerY + heightOffset - strokeWidth),
                    size = androidx.compose.ui.geometry.Size(cornerLength, strokeWidth),
                    cornerRadius = CornerRadius(animatedCornerRadius, animatedCornerRadius)
                )
                drawRoundRect(
                    color = color,
                    topLeft = Offset(centerX - widthOffset, centerY + heightOffset - cornerLength),
                    size = androidx.compose.ui.geometry.Size(strokeWidth, cornerLength),
                    cornerRadius = CornerRadius(animatedCornerRadius, animatedCornerRadius)
                )
                drawRoundRect(
                    color = color,
                    topLeft = Offset(centerX + widthOffset - cornerLength, centerY + heightOffset - strokeWidth),
                    size = androidx.compose.ui.geometry.Size(cornerLength, strokeWidth),
                    cornerRadius = CornerRadius(animatedCornerRadius, animatedCornerRadius)
                )
                drawRoundRect(
                    color = color,
                    topLeft = Offset(centerX + widthOffset - strokeWidth, centerY + heightOffset - cornerLength),
                    size = androidx.compose.ui.geometry.Size(strokeWidth, cornerLength),
                    cornerRadius = CornerRadius(animatedCornerRadius, animatedCornerRadius)
                )
            }

            DisposableEffect(Unit) {
                cameraProvider = cameraProviderFuture.get()
                bindCameraUseCases()

                onDispose { cameraExecutor.shutdown() }
            }
        }
    }
}

@Composable
fun BottomBar(
    onGalleryClick: () -> Unit,
    onCaptureClick: () -> Unit,
    onSwitchCameraClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(onClick = onGalleryClick) {
            Icon(
                painter = painterResource(R.drawable.ic_gallery),
                contentDescription = "Galeri",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        IconButton(onClick = onCaptureClick) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_camera_click),
                    contentDescription = "Ambil Foto",
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxSize(),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

        }
        IconButton(onClick = onSwitchCameraClick) {
            Icon(
                painter = painterResource(R.drawable.ic_camera_rotate),
                contentDescription = "Ganti Kamera",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}


fun takePhoto(
    context: Context,
    imageCapture: ImageCapture?,
    executor: java.util.concurrent.Executor,
    flashEnabled: Boolean,
    isFrontCamera: Boolean,
    cameraMode: String,
    navController: NavController
) {
    val photoFile = File(context.externalCacheDir, "${System.currentTimeMillis()}.jpg")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture?.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exception: ImageCaptureException) {
                exception.printStackTrace()
            }
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    if (cameraMode == "Poto Tanam") {
                        if (isFrontCamera) {
                            val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                            val exif = ExifInterface(photoFile)
                            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
                            val rotatedBitmap = when (orientation) {
                                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
                                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
                                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
                                else -> bitmap
                            }
                            val mirroredBitmap = Bitmap.createBitmap(
                                rotatedBitmap, 0, 0, rotatedBitmap.width, rotatedBitmap.height,
                                Matrix().apply { preScale(-1f, 1f) }, false
                            )
                            val mirroredFile = File(context.externalCacheDir, "${System.currentTimeMillis()}_mirrored.jpg")
                            FileOutputStream(mirroredFile).use { out ->
                                mirroredBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                            }
                            val mirroredUri = Uri.fromFile(mirroredFile)
                            val encodedUri = URLEncoder.encode(mirroredUri.toString(), "UTF-8")
                            navController.navigate("ResultPotoTanam/$encodedUri")
                        } else {
                            val encodedUri = URLEncoder.encode(savedUri.toString(), "UTF-8")
                            navController.navigate("ResultPotoTanam/$encodedUri")
                        }
                    } else {
                        if (isFrontCamera) {
                            val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                            val exif = ExifInterface(photoFile)
                            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
                            val rotatedBitmap = when (orientation) {
                                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
                                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
                                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
                                else -> bitmap
                            }
                            val mirroredBitmap = Bitmap.createBitmap(
                                rotatedBitmap, 0, 0, rotatedBitmap.width, rotatedBitmap.height,
                                Matrix().apply { preScale(-1f, 1f) }, false
                            )
                            val mirroredFile = File(context.externalCacheDir, "${System.currentTimeMillis()}_mirrored.jpg")
                            FileOutputStream(mirroredFile).use { out ->
                                mirroredBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                            }
                            val mirroredUri = Uri.fromFile(mirroredFile)
                            val encodedUri = URLEncoder.encode(mirroredUri.toString(), "UTF-8")
                            navController.navigate("ResultScanTanam/$encodedUri")
                        } else {
                            val encodedUri = URLEncoder.encode(savedUri.toString(), "UTF-8")
                            navController.navigate("ResultScanTanam/$encodedUri")
                        }
                    }
                }

            }
        }
    )
}
fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}