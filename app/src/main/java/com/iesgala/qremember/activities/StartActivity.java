package com.iesgala.qremember.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.iesgala.qremember.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        TextView tv = findViewById(R.id.tvPulsaAqui);
        tv.setOnClickListener(e -> System.out.println("He sido pulsado"));
        Button btn = findViewById(R.id.btnLogin);
        btn.setOnClickListener(e -> System.out.println("Acceder"));
    }
}