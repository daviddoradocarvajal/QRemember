package com.iesgala.qremember.adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.MainActivityController;
import com.iesgala.qremember.model.Imagen;
import com.iesgala.qremember.model.Lugar;
import com.iesgala.qremember.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author David Dorado
 * @version 1.0
 */
public class ImagesAdapter extends BaseAdapter {
    private ArrayList<Imagen> imagenes;
    private Activity activity;

    public ImagesAdapter(@NonNull Activity activity, @NonNull List<Imagen> objects) {
        this.activity = activity;
        this.imagenes = new ArrayList<>(objects);
    }

    @Override
    public int getCount() {
        return imagenes.size();
    }

    @Override
    public Object getItem(int position) {
        return imagenes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = this.activity;
        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.layout_imagenes, null);
        ImageView ivLugar = convertView.findViewById(R.id.ivLugar);
        ivLugar.setImageDrawable(imagenes.get(position).getImagen());
        CheckBox chkLugar = convertView.findViewById(R.id.chkLugar);
        chkLugar.setOnClickListener(v -> {
            imagenes.get(position).setSeleccionado(chkLugar.isChecked());
        });
        return convertView;

    }


}
