package com.example.application;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button; // ACCESS button
import android.widget.EditText; // Location display field
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static String RECEPTION_NUMBER = "01234567890";

    private Button button; // References the ACCESS button
    private EditText editText; // References the location field
    private TrackGPS gps; // References our location helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button); // Connect to the XML button
        editText = findViewById(R.id.editTextText); // Connect to the XML location field

        // Request location permission at runtime if not already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        Button buttonBook = findViewById(R.id.buttonBook);
        Button buttonMedicine = findViewById(R.id.buttonMedicine);
        Button buttonCall = findViewById(R.id.buttonCall);

        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AppointmentsActivity.class);
                startActivity(intent);
            }
        });

        buttonMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MedicineActivity.class);
                startActivity(intent);
            }
        });

        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                // "tel:" opens the phone's dialer with the number filled in (no permission needed)
                intent.setData(Uri.parse("tel:" + RECEPTION_NUMBER));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // e.g. tablets/emulators without a phone app - show a message instead of crashing
                    Toast.makeText(MainActivity.this,
                            "No phone app found. Please call " + RECEPTION_NUMBER,
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new TrackGPS(MainActivity.this);
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    String locationText = getCityAndPostcode(latitude, longitude);
                    editText.setText(locationText);
                    Toast.makeText(getApplicationContext(), locationText, Toast.LENGTH_SHORT).show();
                } else {
                    gps.showAlert();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private String getCityAndPostcode(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String city = address.getLocality();
                if (city == null || city.isEmpty()) {
                    city = address.getSubAdminArea();
                }
                String postcode = address.getPostalCode();

                if (city != null && postcode != null) {
                    return city + ", " + postcode;
                } else if (city != null) {
                    return city;
                } else if (postcode != null) {
                    return postcode;
                }
            }
        } catch (IOException e) {
            // Fall through to coordinates fallback if offline or lookup fails
        }
        return "Lat: " + latitude + "\nLon: " + longitude;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gps != null) {
            gps.stopGPS();
        }
    }
}
