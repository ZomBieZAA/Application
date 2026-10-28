package com.example.application;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static String RECEPTION_NUMBER = "01234567890";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

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


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}