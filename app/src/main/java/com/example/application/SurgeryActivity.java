package com.example.application;
import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.Locale;
/**
 * "Find My Surgery" screen: uses GPS (TrackGPS) to show the user's position,
 * their distance from the surgery, and opens directions in a maps app.
 */
public class SurgeryActivity extends AppCompatActivity {
    // --- Surgery details (placeholder location - replace with the real surgery) ---
    private static final String SURGERY_NAME = "ScotMedic Surgery";
    private static final double SURGERY_LAT = 55.9533;
    private static final double SURGERY_LNG = -3.1883;
    private static final int LOCATION_REQUEST_CODE = 1;
    private TrackGPS gps;              // References our location helper
    private TextView textLocation;     // Shows the user's coordinates
    private TextView textDistance;     // Shows the distance to the surgery
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surgery);
        textLocation = findViewById(R.id.textLocation);
        textDistance = findViewById(R.id.textDistance);
        TextView textSurgery = findViewById(R.id.textSurgery);
        textSurgery.setText(SURGERY_NAME);
        Button buttonLocate = findViewById(R.id.buttonLocate);
        Button buttonDirections = findViewById(R.id.buttonDirections);
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findMyLocation();
            }
        });
        buttonDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDirections();
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Ask for location permission when the screen opens (if not already granted)
        if (!hasLocationPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
    }