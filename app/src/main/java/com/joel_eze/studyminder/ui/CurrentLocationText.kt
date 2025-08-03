package com.joel_eze.studyminder.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

@Composable
fun CurrentLocationText(
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start
) {
    val context = LocalContext.current
    var locationText by remember { mutableStateOf("Getting location...") }

    // Check if location permission is already granted
    var isPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Launcher to request location permission at runtime
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        isPermissionGranted = granted
    }

    // Launch permission request if not granted
    LaunchedEffect(key1 = isPermissionGranted) {
        if (!isPermissionGranted) {
            delay(500) // small delay to allow the Composable to settle before launching
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Fetch and display location if permission is granted
    LaunchedEffect(isPermissionGranted) {
        if (isPermissionGranted) {
            val location = getLastKnownLocation(context)
            locationText = location?.let {
                getCityNameFromLocation(context, it)
            } ?: "Location not available"
        } else {
            locationText = "Permission not granted"
        }
    }

    // Display the final location text in the UI
    Text(text = locationText, modifier = modifier, textAlign = textAlign)
}

// Gets the last known location from the fused location provider
@SuppressLint("MissingPermission")
suspend fun getLastKnownLocation(context: Context): Location? {
    return withContext(Dispatchers.IO) {
        try {
            val fusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)

            suspendCancellableCoroutine { cont ->
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { cont.resume(it) }
                    .addOnFailureListener { cont.resume(null) }
            }
        } catch (e: Exception) {
            null
        }
    }
}

// Converts the location coordinates into a human-readable city name using Geocoder
suspend fun getCityNameFromLocation(context: Context, location: Location): String {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val city = addresses[0].locality ?: addresses[0].subAdminArea ?: addresses[0].adminArea
                "\uD83D\uDCCD $city"
            } else {
                "City not found"
            }
        } catch (e: Exception) {
            "Error retrieving city"
        }
    }
}
