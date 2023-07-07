package com.ldy.ex3.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ldy.ex3.MainActivity;
import com.ldy.ex3.POJO.City;
import com.ldy.ex3.R;

import java.util.List;

public class CitiesAddapter extends ArrayAdapter<City> {
    private int resourceID;
    private MainActivity clickActivity;

    public CitiesAddapter(@NonNull Context context, int resource, @NonNull List<City> objects, MainActivity clickActivity) {
        super(context, resource, objects);
        this.resourceID = resource;
        this.clickActivity = clickActivity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
        } else {
            view = convertView;
        }
        view = LayoutInflater.from(getContext()).inflate(resourceID, null);
        City city = getItem(position);
        TextView text_selectCity = view.findViewById(R.id.text_selectCity);
        text_selectCity.setText(city.getName());
        view.setTag(city);
        view.setOnClickListener(clickActivity);
        return view;
    }
}
