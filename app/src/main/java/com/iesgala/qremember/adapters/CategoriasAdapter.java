package com.iesgala.qremember.adapters;

import android.app.Activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.Switch;


import com.iesgala.qremember.R;

import com.iesgala.qremember.model.Categoria;


import java.util.ArrayList;


public class CategoriasAdapter extends BaseAdapter {
    private ArrayList<Categoria> categorias;
    private Activity activity;

    public CategoriasAdapter(ArrayList<Categoria> categorias, Activity activity) {
        this.categorias = categorias;
        this.activity = activity;
    }


    @Override
    public int getCount() {
        return this.categorias.size();
    }

    @Override
    public Object getItem(int i) {
        return this.categorias.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Activity activity = this.activity;
        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.layout_categorias,null);
        Switch swCategorias = convertView.findViewById(R.id.swCategoria);
        swCategorias.setText(this.categorias.get(i).getNombre());
        //swCategorias.

        return convertView;
    }
}
