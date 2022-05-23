package com.iesgala.qremember.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iesgala.qremember.R;
import com.iesgala.qremember.model.Lugar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class LocalesAdapter extends BaseAdapter {
    private ArrayList<Lugar> lugares;
    private Context context;
    public LocalesAdapter(@NonNull Context context, @NonNull List<Lugar> objects) {
        this.context = context;
        this.lugares = new ArrayList<>(objects);
    }

    @Override
    public int getCount() {
        return this.lugares.size();
    }

    @Override
    public Object getItem(int i) {
        return this.lugares.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Activity activity = (Activity) context;
        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.lugares_layout,null);

        TextView tvNombre = convertView.findViewById(R.id.tvNombre);
        TextView tvCategoria = convertView.findViewById(R.id.tvCategoria);
        ImageView ivFoto = convertView.findViewById(R.id.ivFoto);
        tvNombre.setText(lugares.get(position).getNombre());
        tvCategoria.setText(lugares.get(position).getTvCategorias());
        ivFoto.setImageDrawable(lugares.get(position).getPrincipal().getImagen());
        return convertView;
    }
}
