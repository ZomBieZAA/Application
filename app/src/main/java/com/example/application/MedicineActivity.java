package com.example.application;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MedicineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showOrder(R.id.textName1, R.id.textStatus1, R.id.progress1,
                "Blood Pressure Tablets", 25);
    }

    private void showOrder(int nameId, int statusId, int progressId, String name, int progress) {
        TextView textName = findViewById(nameId);
        TextView textStatus = findViewById(statusId);
        ProgressBar progressBar = findViewById(progressId);

        textName.setText(name);
        textStatus.setText(getStatusText(progress));
        progressBar.setProgress(progress);
    }

    private String getStatusText(int progress) {
        if (progress < 50) {
            return "Ordered - being prepared\nShips in about 2 days";
        } else if (progress < 100) {
            return "Shipped - on its way\nArrives in about 1 day";
        } else {
            return "Delivered";
        }
    }
}
