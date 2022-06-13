package com.iesgala.qremember.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.CompartidoConmigoController;
import com.iesgala.qremember.model.LugarUsuario;

import java.util.ArrayList;

public class CompartidosAdapter extends BaseAdapter {
    private ArrayList<LugarUsuario> compartidos;
    private Activity activity;

    public CompartidosAdapter(Activity activity,ArrayList<LugarUsuario> compartidos) {
        this.compartidos = compartidos;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return this.compartidos.size();
    }

    @Override
    public Object getItem(int position) {
        return this.compartidos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = this.activity;
        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.layout_lugarcompartido,null);
        TextView tvUsuarioEmisor = convertView.findViewById(R.id.tvUsuarioEmisor);
        TextView tvEnlaceCompartir = convertView.findViewById(R.id.tvEnlaceComartir);
        Button btnAceptar = convertView.findViewById(R.id.btnAceptar);
        Button btnRechazar = convertView.findViewById(R.id.btnRechazar);
        tvUsuarioEmisor.setText(compartidos.get(position).getEmailEmisor());
        tvEnlaceCompartir.setText(compartidos.get(position).getEnlace());
        btnAceptar.setOnClickListener(l->{
            CompartidoConmigoController.aceptarCompartido(activity,compartidos.get(position));
        });
        btnRechazar.setOnClickListener(l->{
            CompartidoConmigoController.rechazarCompartido(activity,compartidos.get(position));
        });
        return convertView;
    }
}
