package com.iesgala.qremember.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.RutasController;
import com.iesgala.qremember.model.Ruta;

import java.util.ArrayList;

public class RutasAdapter extends BaseAdapter {
    private final ArrayList<Ruta> rutas;
    private final Activity activity;

    public RutasAdapter(@NonNull Activity activity,@NonNull ArrayList<Ruta> rutas) {
        this.rutas = rutas;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return this.rutas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.rutas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = this.activity;
        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.layout_rutas,null);

        TextView tvNombreRuta = convertView.findViewById(R.id.tvNombreRuta);
        TextView tvCategoriasRuta = convertView.findViewById(R.id.tvCategoriasRuta);
        tvNombreRuta.setText(rutas.get(position).getNombre());
        tvCategoriasRuta.setText(rutas.get(position).getTvCategorias());
        Button btnVerRuta = convertView.findViewById(R.id.btnVerRuta);
        btnVerRuta.setOnClickListener(e -> RutasController.verRuta(activity,this.rutas.get(position).getNombre(),this.rutas.get(position).getEmailUsuario(),this.rutas.get(position).getLugares()));
        return convertView;
    }
}
