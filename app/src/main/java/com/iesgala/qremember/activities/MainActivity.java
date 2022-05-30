package com.iesgala.qremember.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        Intent intent = getIntent();
       //String usuario = intent.getStringExtra("Nombre");
        FakeDb db = new FakeDb(this);
        LocalesAdapter localesAdapter = new LocalesAdapter(this,db.lugares);
        ListView lvLugares = findViewById(R.id.lvLugares);
        lvLugares.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        lvLugares.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivityController.clickLugar();
            }
        });
        lvLugares.setAdapter(localesAdapter);

    }
}
