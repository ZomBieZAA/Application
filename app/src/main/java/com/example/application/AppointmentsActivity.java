package com.example.application;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AppointmentsActivity extends AppCompatActivity {

    private static final String[] TIMES = {
            "10:00", "12:15", "13:30", "14:00", "15:30", "16:15"
    };
    private static final String[] DOCTORS = {
            "Dr Patel", "Dr Smith", "Dr Khan", "Dr Jones", "Dr Patel", "Dr Smith"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout appointmentList = findViewById(R.id.appointmentList);
        for (int i = 0; i < TIMES.length; i++) {
            appointmentList.addView(createSlotButton(TIMES[i], DOCTORS[i]));
        }
    }

    private Button createSlotButton(final String time, final String doctor) {
        final Button button = new Button(this);
        button.setText(time + "\n" + doctor);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setBackgroundResource(R.drawable.buttonshape);
        button.setPadding(dp(16), dp(12), dp(16), dp(12));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = dp(20);
        button.setLayoutParams(params);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmBooking(button, time, doctor);
            }
        });
        return button;
    }

    private void confirmBooking(final Button button, final String time, final String doctor) {
        new AlertDialog.Builder(this)
                .setTitle("Book appointment?")
                .setMessage("Book with " + doctor + " at " + time + "?")
                .setPositiveButton("Book", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        button.setText(time + "\n" + doctor + " - Booked");
                        button.setEnabled(false);
                        button.setAlpha(0.6f);
                        Toast.makeText(AppointmentsActivity.this,
                                "Booked with " + doctor + " at " + time,
                                Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
