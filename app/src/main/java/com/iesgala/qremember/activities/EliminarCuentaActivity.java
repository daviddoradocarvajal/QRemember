package com.iesgala.qremember.activities;

import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.utils.Config;

/**
 *
 * @author David Dorado
 * @version 1.0
 */
public class EliminarCuentaActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return Config.createMenu(menu,this);
    }
}
