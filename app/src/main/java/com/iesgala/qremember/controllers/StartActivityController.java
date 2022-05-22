package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.iesgala.qremember.R;

public class StartActivityController {

    public static void accederButton (String usuario, String pass, Activity activity){
        System.out.println((usuario));
        activity.setContentView(R.layout.activity_main);

    }
}
