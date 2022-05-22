package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.iesgala.qremember.R;

/**
 * Clase controladora de eventos de StartActivity contiene los métodos a ser ejecutados por los botones
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class StartActivityController {

    public static void accederButton (String usuario, String pass, Activity activity){
        System.out.println((usuario));
        activity.setContentView(R.layout.activity_main);

    }
}
