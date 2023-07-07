package com.ldy.ex3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ldy.ex3.POJO.City;
import com.ldy.ex3.Utils.Json;
import com.ldy.ex3.Utils.NetWork;
import com.ldy.ex3.adapter.CitiesAddapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static String favouriteTag[] = {"favourite1", "favourite2", "favourite3"};
    private static String historyTag[] = {"history1", "history2", "history3"};
    private List<String> favouriteID;
    private List<String> historyID;
    private TextView text_history1;
    private TextView text_history2;
    private TextView text_history3;
    private ListView list_favourite;
    private EditText edit_search;
    private ListView list_provinces;
    private ListView list_cities;
    private ListView list_countries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        favouriteID = new ArrayList<>();
        historyID = new ArrayList<>();
        Button btn_search = findViewById(R.id.btn_search);
        list_favourite = findViewById(R.id.list_favourite);
        list_provinces = findViewById(R.id.list_provinces);
        list_cities = findViewById(R.id.list_cities);
        list_countries = findViewById(R.id.list_countries);
        edit_search = findViewById(R.id.edit_search);
        text_history1 = findViewById(R.id.text_history1);
        text_history2 = findViewById(R.id.text_history2);
        text_history3 = findViewById(R.id.text_history3);
        getFavourite();
        getHistory();
        text_history1.setOnClickListener(this);
        text_history2.setOnClickListener(this);
        text_history3.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        try {
            List<City> cities = Json.getCities("0", 0, this);
            CitiesAddapter citiesAddapter = new CitiesAddapter(this, R.layout.cities_item, cities, this);
            list_provinces.setAdapter(citiesAddapter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getFavourite();
        if (favouriteID.size() == 0) {
            list_favourite.setVisibility(View.GONE);
        } else {
            list_favourite.setVisibility(View.VISIBLE);
            NetWork.getWeather(favouriteID, list_favourite, this, R.layout.favourite_item, this);
        }

        getHistory();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_search) {
            String cityID = edit_search.getText().toString();
            try {
                if (Json.checkCode(cityID, this)) {
                    Intent intent = new Intent(this, ViewActivity.class);
                    intent.putExtra("cityCode", cityID);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "cityID出错", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else if (view.getId() == R.id.text_history3 || view.getId() == R.id.text_history2 || view.getId() == R.id.text_history1) {
            Intent intent = new Intent(this, ViewActivity.class);
            String cityID = (String) view.getTag();
            intent.putExtra("cityCode", cityID);
            startActivity(intent);
        } else if (view.getId() == R.id.layout_favourite) {
            Intent intent = new Intent(this, ViewActivity.class);
            String cityID = (String) view.getTag();
            intent.putExtra("cityCode", cityID);
            startActivity(intent);
        } else {
            City city = (City) view.getTag();
            List<City> newCities = new ArrayList<City>();
            if (city.getLevel() == 0) {
                try {
                    newCities.addAll(Json.getCities(city.getId(), city.getLevel() + 1, this));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                CitiesAddapter citiesAddapter = new CitiesAddapter(this, R.layout.cities_item, newCities, this);
                list_cities.setAdapter(citiesAddapter);
            } else if (city.getLevel() == 1) {
                if (!city.getCode().isEmpty()) {
                    newCities.add(new City(city.getName(), city.getCode(), city.getId(), city.getLevel() + 1));
                }
                try {
                    newCities.addAll(Json.getCities(city.getId(), city.getLevel() + 1, this));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                CitiesAddapter citiesAddapter = new CitiesAddapter(this, R.layout.cities_item, newCities, this);
                list_countries.setAdapter(citiesAddapter);
            } else {
                Intent intent = new Intent(this, ViewActivity.class);
                intent.putExtra("cityCode", city.getCode());
                startActivity(intent);
            }

        }

    }

    private void getFavourite() {
        favouriteID.clear();
        String favourite1 = sharedPreferences.getString(favouriteTag[0], "");
        String favourite2 = sharedPreferences.getString(favouriteTag[1], "");
        String favourite3 = sharedPreferences.getString(favouriteTag[2], "");
        if (!favourite1.isEmpty()) {
            favouriteID.add(favourite1);
            if (!favourite2.isEmpty()) {
                favouriteID.add(favourite2);
                if (!favourite3.isEmpty()) {
                    favouriteID.add(favourite3);
                }
            }
        }
        Log.d("getFavourite", favouriteID.toString());
    }

    private void getHistory() {
        historyID.clear();
        String history1 = sharedPreferences.getString(historyTag[0], "");
        String history2 = sharedPreferences.getString(historyTag[1], "");
        String history3 = sharedPreferences.getString(historyTag[2], "");
        if (!history1.isEmpty()) {
            text_history1.setTag(history1);
            try {
                text_history1.setText(Json.getName(history1, this));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            text_history1.setVisibility(View.VISIBLE);
            historyID.add(history1);
            if (!history2.isEmpty()) {
                text_history2.setTag(history2);
                try {
                    text_history2.setText(Json.getName(history2, this));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                text_history2.setVisibility(View.VISIBLE);
                historyID.add(history2);
                if (!history3.isEmpty()) {
                    text_history3.setTag(history3);
                    try {
                        text_history3.setText(Json.getName(history3, this));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    text_history3.setVisibility(View.VISIBLE);
                    historyID.add(history3);
                }
            }
        }
    }

}