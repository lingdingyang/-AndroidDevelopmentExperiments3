package com.ldy.ex3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ldy.ex3.POJO.Weather;
import com.ldy.ex3.R;

import java.util.List;

public class ViewAdapter extends ArrayAdapter<Weather> {
    private int resourceID;

    public ViewAdapter(@NonNull Context context, int resource, @NonNull List<Weather> objects) {
        super(context, resource, objects);
        resourceID = resource;
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
        Weather weather = getItem(position);
        TextView text_showDate = view.findViewById(R.id.text_showDate);
        TextView text_showQuality = view.findViewById(R.id.text_showQuality);
        TextView text_showTemperature = view.findViewById(R.id.text_showTemperature);
        TextView text_showWeather = view.findViewById(R.id.text_showWeather);
        TextView text_showWindDirection = view.findViewById(R.id.text_showWindDirection);
        TextView text_showWindForce = view.findViewById(R.id.text_showWindForce);
        if (weather != null) {
            text_showDate.setText(weather.getDate());
            text_showQuality.setText(weather.getQuality());
            text_showTemperature.setText(weather.getTemperature());
            text_showWeather.setText(weather.getWeather());
            text_showWindDirection.setText(weather.getWindDirection());
            text_showWindForce.setText(weather.getWindForce());
        }
        return view;
    }
}
