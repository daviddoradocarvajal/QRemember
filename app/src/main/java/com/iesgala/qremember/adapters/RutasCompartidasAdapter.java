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
import com.iesgala.qremember.model.RutaCompartida;

import java.util.ArrayList;

public class RutasCompartidasAdapter extends BaseAdapter {
    private ArrayList<RutaCompartida> compartidas;
    private Activity activity;

    public RutasCompartidasAdapter(ArrayList<RutaCompartida> compartidas, Activity activity) {
        this.compartidas = compartidas;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return this.compartidas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.compartidas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = this.activity;
        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.layout_rutacompartida,null);
        TextView tvUsuarioEmisor = convertView.findViewById(R.id.tvEmisorRuta);
        TextView tvNombreRutaCompartir = convertView.findViewById(R.id.tvNombreRutaCompartida);
        Button btnAceptar = convertView.findViewById(R.id.btnAceptarRuta);
        Button btnRechazar = convertView.findViewById(R.id.btnRechazarRuta);
        tvUsuarioEmisor.setText(compartidas.get(position).getEmailEmisor());
        tvNombreRutaCompartir.setText(compartidas.get(position).getNombreRuta());
        btnAceptar.setOnClickListener(l-> CompartidoConmigoController.aceptarRutaCompartida(activity,compartidas.get(position)));
        btnRechazar.setOnClickListener(l-> CompartidoConmigoController.rechazarRutaCompartida(activity,compartidas.get(position)));
        return convertView;
    }
}
