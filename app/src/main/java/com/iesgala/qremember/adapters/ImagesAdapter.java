package com.iesgala.qremember.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.iesgala.qremember.R;
import com.iesgala.qremember.model.Imagen;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase adaptadora para las imágenes se usa para adaptar el contenido de un ListView a los elementos
 * indicados en el archivo layout_imagenes.xml y rellenar su contenido
 * @author David Dorado
 * @version 1.0
 */
public class ImagesAdapter extends BaseAdapter {
    private final ArrayList<Imagen> imagenes;
    private final Activity activity;

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
        return imagenes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = this.activity;
        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.layout_imagenes, null);
        ImageView ivLugar = convertView.findViewById(R.id.ivLugar);
        ivLugar.setImageDrawable(imagenes.get(position).getImagen());
        CheckBox chkLugar = convertView.findViewById(R.id.chkLugar);
        chkLugar.setOnCheckedChangeListener((compoundButton, b) -> imagenes.get(position).setSeleccionado(b));

        //chkLugar.setOnClickListener(v -> imagenes.get(position).setSeleccionado(chkLugar.isChecked()));
        return convertView;

    }

    /**
     * Devuelve el número de tipos de la vista, se utiliza para reciclaje de
     * las vistas del adaptador
     * @return un entero con los elementos que hay en el adaptador
     */
    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    /**
     * Devuelve la posición del tipo de vista en la lista a efectos de reciclaje de la misma
     * @param position
     * @return posición del tipo de la vista
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
