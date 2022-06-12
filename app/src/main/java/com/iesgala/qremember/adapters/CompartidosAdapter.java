package com.iesgala.qremember.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.iesgala.qremember.model.LugarUsuario;

import java.util.ArrayList;

public class CompartidosAdapter extends BaseAdapter {
    private ArrayList<LugarUsuario> compartidos;
    private Activity activity;


    @Override
    public int getCount() {
        return compartidos.size();
    }

    @Override
    public Object getItem(int position) {
        return compartidos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
