package com.ldy.ex3.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ldy.ex3.MainActivity;
import com.ldy.ex3.POJO.Weather;
import com.ldy.ex3.R;

import java.util.List;

public class FavouriteAdapter extends ArrayAdapter<Weather> {
    private final int resourceID;
    private MainActivity clickActivity;

    public FavouriteAdapter(@NonNull Context context, int resource, @NonNull List objects, MainActivity clickActivity) {
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
        Weather weather = getItem(position);
        TextView text_favouriteCity = view.findViewById(R.id.text_favouriteCity);
        TextView text_favouriteQuality = view.findViewById(R.id.text_favouriteQuality);
        TextView text_favouriteTemperature = view.findViewById(R.id.text_favouriteTemperature);
        TextView text_favouriteWeather = view.findViewById(R.id.text_favouriteWeather);
        TextView text_favouriteWindDirection = view.findViewById(R.id.text_favouriteWindDirection);
        TextView text_favouriteWindForce = view.findViewById(R.id.text_favouriteWindForce);

        if (weather != null) {
            text_favouriteCity.setText(weather.getCity().getName());
            text_favouriteQuality.setText("空气质量："+weather.getQuality());
            text_favouriteTemperature.setText(weather.getTemperature());
            text_favouriteWeather.setText("天气："+weather.getWeather());
            text_favouriteWindDirection.setText(weather.getWindDirection());
            text_favouriteWindForce.setText(weather.getWindForce());
            view.setTag(weather.getCity().getCode());
            view.setOnClickListener(clickActivity);
        }

        return view;
    }
}
