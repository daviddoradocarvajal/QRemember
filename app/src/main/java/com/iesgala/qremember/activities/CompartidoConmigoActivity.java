package com.iesgala.qremember.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.CompartidoConmigoController;
import com.iesgala.qremember.utils.Utils;

import java.util.Objects;

/**
 * @author David Dorado
 * @version 1.0
 */
public class CompartidoConmigoActivity extends AppCompatActivity {
    String emailReceptor;
    String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compartidoconmigo);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.conmigo);
        title = Objects.requireNonNull(getSupportActionBar().getTitle()).toString();
        try {
            Intent intent = getIntent();
            if (intent != null) {
                emailReceptor = intent.getStringExtra(Utils.INTENTS_EMAIL);
                CompartidoConmigoController.setAdapter(this,emailReceptor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(Utils.INTENTS_EMAIL,emailReceptor);
        this.startActivity(intent);
        this.finish();
    }

    @Override
    protected void onResume() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.conmigo);
        title = Objects.requireNonNull(getSupportActionBar().getTitle()).toString();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Utils.menuOption(this, item, emailReceptor, title);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return Utils.createMenu(menu, this);
    }
}
