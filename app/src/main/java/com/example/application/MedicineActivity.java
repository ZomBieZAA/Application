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
                "Paracetamol 500mg", 75);
        showOrder(R.id.textName2, R.id.textStatus2, R.id.progress2,
                "Blood Pressure Tablets", 25);
        showOrder(R.id.textName3, R.id.textStatus3, R.id.progress3,
                "Metformin 500mg", 45);
        showOrder(R.id.textName4, R.id.textStatus4, R.id.progress4,
                "Vitamin D 1000 IU", 100);
        showOrder(R.id.textName5, R.id.textStatus5, R.id.progress5,
                "Salbutamol Inhaler", 10);
        showOrder(R.id.textName6, R.id.textStatus6, R.id.progress6,
                "Atorvastatin 20mg", 90);
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
        if (progress < 30) {
            return "Ordered - being prepared\nShips in about 2 days";
        } else if (progress < 60) {
            return "Packing - getting ready to ship\nShips in about 1 day";
        } else if (progress < 100) {
            return "Shipped - on its way\nArrives in about 1 day";
        } else {
            return "Delivered";
        }
    }
}
