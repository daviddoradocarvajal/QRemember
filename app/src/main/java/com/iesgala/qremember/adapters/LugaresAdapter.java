package com.iesgala.qremember.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.MainActivityController;
import com.iesgala.qremember.model.Lugar;
import com.iesgala.qremember.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class LugaresAdapter extends BaseAdapter {
    private ArrayList<Lugar> lugares;
    private Activity activity;

    public LugaresAdapter(@NonNull Activity activity, @NonNull List<Lugar> objects) {
        this.activity = activity;
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


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Activity activity = this.activity;
        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.layout_lugares,null);

        TextView tvNombre = convertView.findViewById(R.id.tvNombre);
        TextView tvCategoria = convertView.findViewById(R.id.tvCategoria);
        ImageView ivFoto = convertView.findViewById(R.id.ivFoto);
        tvNombre.setText(lugares.get(position).getNombre());
        tvCategoria.setText(lugares.get(position).getTvCategorias());
        if(lugares.get(position).getImagenes()!=null && lugares.get(position).getImagenes().size()>0) {
            ivFoto.setImageDrawable(lugares.get(position).getImagenes().get(0).getImagen());
        }
        Button btnVer = convertView.findViewById(R.id.btnVer);
        View finalConvertView = convertView;
        btnVer.setOnClickListener(e -> MainActivityController.verEnlace(Uri.parse(lugares.get(position).getEnlace()), Objects.requireNonNull(Utils.getActivity(finalConvertView))) );
        return convertView;
    }
}
