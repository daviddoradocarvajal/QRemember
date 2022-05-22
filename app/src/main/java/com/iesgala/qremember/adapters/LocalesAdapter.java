package com.iesgala.qremember.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.iesgala.qremember.model.Local;

import java.util.List;

/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class LocalesAdapter extends ArrayAdapter<Local>{

    public LocalesAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Local> objects) {
        super(context, resource, textViewResourceId, objects);
    }
}
