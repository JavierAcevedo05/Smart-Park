package com.example.smartpark;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class Mapa extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mapa;
    private FusedLocationProviderClient fusedLocationClient;

    // Launcher para pedir permisos
    private ActivityResultLauncher<String[]> locationPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Configurar launcher de permisos
        locationPermissionLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.RequestMultiplePermissions(),
                        result -> {
                            Boolean fineLocation = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocation = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);

                            if (fineLocation || coarseLocation) {
                                initMap();
                            } else {
                                Toast.makeText(this,
                                        "Permiso de ubicación denegado",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

        // Comprobar permisos
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            locationPermissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        } else {
            initMap();
        }
    }

    // Inicializa el fragmento de Google Maps
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);   // llamará a onMapReady()
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Comprobamos permisos antes de activar Mi ubicación
        boolean fineGranted = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarseGranted = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (fineGranted || coarseGranted) {
            try {
                mapa.setMyLocationEnabled(true);
                mapa.getUiSettings().setMyLocationButtonEnabled(true);
                mapa.getUiSettings().setZoomControlsEnabled(true);

                // Centrar cámara en la última ubicación conocida
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            if (location != null) {
                                moverCamaraALocalizacion(location);
                            } else {
                                Toast.makeText(this,
                                        "No se pudo obtener la ubicación",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this,
                    "Sin permiso de ubicación, no se puede mostrar Mi ubicación",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void moverCamaraALocalizacion(@NonNull Location location) {
        LatLng actual = new LatLng(location.getLatitude(), location.getLongitude());
        // Zoom 15 aprox. nivel calle
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(actual, 15f));
    }
}
