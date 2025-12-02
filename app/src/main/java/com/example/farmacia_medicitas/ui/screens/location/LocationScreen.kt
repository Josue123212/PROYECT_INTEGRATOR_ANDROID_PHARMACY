package com.example.farmacia_medicitas.ui.screens.location

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.example.farmacia_medicitas.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(onBack: (() -> Unit)? = null) {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    ) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        hasPermission = granted
    }
    LaunchedEffect(Unit) {
        if (!hasPermission) launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.046374, -77.042793), 12f)
    }
    Scaffold(topBar = { TopAppBar(title = { Text("Ubícanos y contáctanos") }) }) { inner ->
        Column(modifier = Modifier.fillMaxSize().padding(inner)) {
            Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = hasPermission),
                    uiSettings = MapUiSettings(myLocationButtonEnabled = true)
                ) {
                    Marker(state = MarkerState(LatLng(-12.046374, -77.042793)), title = "Sede Centro", snippet = "Av. Principal 123")
                    Marker(state = MarkerState(LatLng(-12.089, -76.997)), title = "Sede Este", snippet = "Calle Salud 456")
                    Marker(state = MarkerState(LatLng(-12.072, -77.080)), title = "Sede Oeste", snippet = "Jr. Bienestar 789")
                }
                
            }
            Spacer(modifier = Modifier.height(16.dp))
            androidx.compose.material3.Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(id = R.drawable.icono_app), contentDescription = null, modifier = Modifier.height(96.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Farmacia Medicitas")
                    Text("Tel: 987 654 321")
                    Text("Correo: medicitas@gmail.com")
                    Text("Atención: Lun–Dom 8:00–20:00")
                }
            }
        }
    }
}
