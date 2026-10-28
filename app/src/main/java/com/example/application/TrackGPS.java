package com.example.application;

import android.Manifest; // Contains Android permission names
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface; // Handles dialog button clicks
import android.content.Intent;
import android.content.pm.PackageManager; // Provides permission result constants
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings; // Provides the location settings action
import android.widget.Toast; // Displays short messages

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog; // Creates the alert dialog
import androidx.core.content.ContextCompat; // Checks whether permission is granted

/**
 * Location helper based on the GPS lab (LocationManager + LocationListener).
 * Create it with an Activity context, then read getLatitude()/getLongitude().
 */
public class TrackGPS extends Service implements LocationListener {

    private final Context ctxt;          // Activity that is using this helper

    boolean checkGPS = false;            // Is the GPS provider switched on?
    Location mylocation;                 // Most recent location
    double latitude;
    double longitude;

    private static final long MINDISTANCE = 10;           // Minimum metres between updates
    private static final long MINDELAY = 1000 * 60 * 1;   // Minimum time between updates (1 minute)

    protected LocationManager locationManager;

    public TrackGPS(Context ctxt) {
        this.ctxt = ctxt;
        getLocation();
    }

    private Location getLocation() {
        // Get Android's location service
        locationManager = (LocationManager) ctxt.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) { // Check the service before using it
            Toast.makeText(ctxt, "Location service unavailable", Toast.LENGTH_SHORT).show();
            return null; // Stop because the service is unavailable
        }

        // Check whether the GPS provider is enabled
        checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!checkGPS) { // Stop if GPS is switched off
            Toast.makeText(ctxt, "Please enable location", Toast.LENGTH_SHORT).show();
            return null;
        }

        // Check permission before accessing GPS
        if (ContextCompat.checkSelfPermission(ctxt, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(ctxt, "Precise location permission is required", Toast.LENGTH_SHORT).show();
            return null; // Stop if permission has not been granted
        }

        try {
            // Request future GPS updates using our time and distance settings
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, MINDELAY, MINDISTANCE, this);

            // Read the GPS location already stored by Android
            mylocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (mylocation != null) { // Only read coordinates if a location exists
                latitude = mylocation.getLatitude();   // Store latitude
                longitude = mylocation.getLongitude(); // Store longitude
            }
        } catch (SecurityException e) { // Handle permission being removed
            Toast.makeText(ctxt, "Location permission is unavailable", Toast.LENGTH_SHORT).show();
            return null;
        }
        return mylocation; // Return the stored location, or null if unavailable
    }

    // --- Getters used by the Activity ---
    public boolean canGetLocation() {
        return checkGPS;
    }

    public boolean hasLocation() {
        return mylocation != null;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void showAlert() {
        // Create the dialog using the Activity context
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctxt);
        dialog.setTitle("GPS disabled"); // Dialog heading
        dialog.setMessage("Do you want to turn on GPS?"); // Question

        // YES opens the device's location settings
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ctxt.startActivity(intent);
            }
        });

        // NO closes the dialog
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show(); // Display the dialog
    }

    public void stopGPS() {
        if (locationManager != null) { // Check that the location manager exists
            try {
                // Stop sending location updates to this listener
                locationManager.removeUpdates(TrackGPS.this);
            } catch (SecurityException e) { // Handle a permission issue
                Toast.makeText(ctxt, "No permission to access GPS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not used as a bound service
    }

    // --- LocationListener: keep the stored location up to date ---
    @Override
    public void onLocationChanged(@NonNull Location location) {
        mylocation = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        checkGPS = true;
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        checkGPS = false;
    }
}
