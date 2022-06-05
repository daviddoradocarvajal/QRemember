package com.iesgala.qremember.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.iesgala.qremember.R;
import com.iesgala.qremember.adapters.LocalesAdapter;
import com.iesgala.qremember.controllers.MainActivityController;
import com.iesgala.qremember.utils.FakeDb;
import com.iesgala.qremember.utils.MySQLClient;

/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
       Intent intent = getIntent();
       String usuario = intent.getStringExtra("Nombre");
       */
        FakeDb db = new FakeDb(this);
        LocalesAdapter localesAdapter = new LocalesAdapter(this,db.lugares);
        ListView lvLugares = findViewById(R.id.lvLugares);
        lvLugares.setClickable(true);
        lvLugares.setAdapter(localesAdapter);
        lvLugares.setOnItemClickListener((adapterView, view, i, l) -> {
            System.out.println(adapterView.getItemAtPosition(i));
            MainActivityController.clickLugar(MainActivity.this,db.lugares.get(i));
        });
        Button btnNuevoLugar = findViewById(R.id.btnNuevoLugar);
        btnNuevoLugar.setOnClickListener(l -> MainActivityController.nuevoLugar(this));

    }
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()!=null){
               System.out.println(result.getContents());
                Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();
            }else Toast.makeText(this, "Leer cancelado",Toast.LENGTH_LONG).show();
        }else
        super.onActivityResult(requestCode, resultCode, data);
    }
}
