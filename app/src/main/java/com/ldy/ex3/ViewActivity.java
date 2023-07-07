package com.ldy.ex3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ldy.ex3.Utils.NetWork;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends AppCompatActivity implements View.OnClickListener {
    private String cityID;
    private static String favouriteTag[] = {"favourite1", "favourite2", "favourite3"};
    private static String historyTag[] = {"history1", "history2", "history3"};
    private List<String> favouriteID;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private List<String> historyID;
    private ListView list_viewList;
    private TextView text_viewCity;
    private TextView text_viewWeather;
    private TextView text_viewPM25;
    private TextView text_viewHumidity;
    private TextView text_viewUpdateTime;
    private TextView text_viewTemperature;
    private ImageButton btn_reload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Intent intent = getIntent();
        cityID = intent.getStringExtra("cityCode");
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        favouriteID = new ArrayList<>();
        historyID = new ArrayList<>();
        list_viewList = findViewById(R.id.list_viewList);
        text_viewCity = findViewById(R.id.text_viewCity);
        text_viewWeather = findViewById(R.id.text_viewWeather);
        text_viewPM25 = findViewById(R.id.text_viewPM25);
        text_viewHumidity = findViewById(R.id.text_viewHumidity);
        text_viewUpdateTime = findViewById(R.id.text_viewUpdateTime);
        text_viewTemperature = findViewById(R.id.text_viewTemperature);
        btn_reload = findViewById(R.id.btn_reload);
        btn_reload.setOnClickListener(this);
        Button btn_viewFollow = findViewById(R.id.btn_viewFollow);
        btn_viewFollow.setOnClickListener(this);
        getHistory();
        if (checkIsFollowed()) {
            btn_viewFollow.setText("取消关注");
        }

        if (!loadHistory()) {
            NetWork.getCityWeather(cityID, list_viewList, this, R.layout.show_item, text_viewWeather, text_viewCity, text_viewTemperature, text_viewHumidity, text_viewPM25, text_viewUpdateTime);
//            saveHistory();
        }
        updateHistory(cityID);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveHistory();
    }

    private boolean loadHistory() {
        SharedPreferences history = getSharedPreferences("history_" + cityID, MODE_PRIVATE);
        String viewWeather = history.getString("viewWeather", "");
        String viewCity = history.getString("viewCity", "");
        String viewTemperature = history.getString("viewTemperature", "");
        String viewHumidity = history.getString("viewHumidity", "");
        String viewPM25 = history.getString("viewPM25", "");
        String viewUpdateTime = history.getString("viewWeather", "");
        if (!viewWeather.isEmpty()) {
            text_viewWeather.setText(viewWeather);
        } else {
            return false;
        }
        if (!viewCity.isEmpty()) {
            text_viewCity.setText(viewCity);
        } else {
            return false;
        }
        if (!viewTemperature.isEmpty()) {
            text_viewTemperature.setText(viewTemperature);
        } else {
            return false;
        }
        if (!viewHumidity.isEmpty()) {
            text_viewHumidity.setText(viewHumidity);
        } else {
            return false;
        }
        if (!viewPM25.isEmpty()) {
            text_viewPM25.setText(viewPM25);
        } else {
            return false;
        }
        if (!viewUpdateTime.isEmpty()) {
            text_viewUpdateTime.setText(viewUpdateTime);
        } else {
            return false;
        }
        return true;
    }

    private void saveHistory() {
        SharedPreferences history = getSharedPreferences("history_" + cityID, MODE_PRIVATE);
        SharedPreferences.Editor historyEditor = history.edit();
        String viewWeather = text_viewWeather.getText().toString();
        String viewCity = text_viewCity.getText().toString();
        String viewTemperature = text_viewTemperature.getText().toString();
        String viewHumidity = text_viewHumidity.getText().toString();
        String viewPM25 = text_viewPM25.getText().toString();
        String viewUpdateTime = text_viewUpdateTime.getText().toString();
        historyEditor.putString("viewWeather", viewWeather);
        historyEditor.putString("viewCity", viewCity);
        historyEditor.putString("viewTemperature", viewTemperature);
        historyEditor.putString("viewHumidity", viewHumidity);
        historyEditor.putString("viewPM25", viewPM25);
        historyEditor.putString("viewUpdateTime", viewUpdateTime);
        historyEditor.commit();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_reload) {
            NetWork.getCityWeather(cityID, list_viewList, this, R.layout.show_item, text_viewWeather, text_viewCity, text_viewTemperature, text_viewHumidity, text_viewPM25, text_viewUpdateTime);
            Toast.makeText(this, "刷新成功", Toast.LENGTH_SHORT).show();
        } else {
            Button button = (Button) view;
            getFavourite();
            if ("关注".equals(button.getText())) {
                updateFavourite();
                button.setText("取消关注");
            } else {
                for (int i = 0; i < favouriteID.size(); i++) {
                    if (Integer.parseInt(favouriteID.get(i)) == Integer.parseInt(cityID)) {
                        favouriteID.remove(i);
                    }
                }
                for (int i = 0; i < 3; i++) {
                    editor.putString(favouriteTag[i], "");
                }
                editor.commit();
                for (int i = 0; i < favouriteID.size(); i++) {
                    editor.putString(favouriteTag[i], favouriteID.get(i));
                }
                editor.commit();
                button.setText("关注");
            }
        }


    }

    boolean checkIsFollowed() {
        getFavourite();
        for (int i = 0; i < favouriteID.size(); i++) {
            Log.d("checkIsFollowed", favouriteID.get(i) + " " + cityID);
            if (Integer.parseInt(favouriteID.get(i)) == Integer.parseInt(cityID)) {
                return true;
            }
        }
        return false;
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
    }

    private void updateFavourite() {
        getFavourite();
        if (favouriteID.size() == 3) {
            ArrayList<String> strings = new ArrayList<>();
            strings.add(cityID);
            strings.add(favouriteID.get(0));
            strings.add(favouriteID.get(1));
            favouriteID.clear();
            favouriteID.addAll(strings);
        } else if (favouriteID.size() == 2) {
            ArrayList<String> strings = new ArrayList<>();
            strings.add(cityID);
            strings.add(favouriteID.get(0));
            strings.add(favouriteID.get(1));
            favouriteID.clear();
            favouriteID.addAll(strings);
        } else if (favouriteID.size() == 1) {
            ArrayList<String> strings = new ArrayList<>();
            strings.add(cityID);
            strings.add(favouriteID.get(0));
            favouriteID.clear();
            favouriteID.addAll(strings);
        } else {
            favouriteID.add(cityID);
        }
        for (int i = 0; i < favouriteID.size(); i++) {
            editor.putString(favouriteTag[i], favouriteID.get(i));
        }
        editor.commit();
    }

    private void getHistory() {
        historyID.clear();
        String history1 = sharedPreferences.getString(historyTag[0], "");
        String history2 = sharedPreferences.getString(historyTag[1], "");
        String history3 = sharedPreferences.getString(historyTag[2], "");
        if (!history1.isEmpty()) {
            historyID.add(history1);
            if (!history2.isEmpty()) {
                historyID.add(history2);
                if (!history3.isEmpty()) {
                    historyID.add(history3);
                }
            }
        }
    }

    private void updateHistory(String cityID) {
        getHistory();
        for (int i = 0; i < historyID.size(); i++) {
            Log.d("updateHistory", historyID.get(i) + " " + cityID);
            if (Integer.parseInt(historyID.get(i)) == Integer.parseInt(cityID)) {
                return;
            }
        }
        for (int i = 0; i < historyID.size(); i++) {
            Log.d("updateHistory3", historyID.get(i));
        }
        if (historyID.size() == 3) {
            ArrayList<String> strings = new ArrayList<>();
            strings.add(cityID);
            strings.add(historyID.get(0));
            strings.add(historyID.get(1));
            getSharedPreferences("history_" + historyID.get(2), MODE_PRIVATE).edit().clear().commit();
            historyID.clear();
            historyID.addAll(strings);
        } else if (historyID.size() == 2) {
            ArrayList<String> strings = new ArrayList<>();
            strings.add(cityID);
            strings.add(historyID.get(0));
            strings.add(historyID.get(1));
            historyID.clear();
            historyID.addAll(strings);
        } else if (historyID.size() == 1) {
            ArrayList<String> strings = new ArrayList<>();
            strings.add(cityID);
            strings.add(historyID.get(0));
            historyID.clear();
            historyID.addAll(strings);
        } else {
            historyID.add(cityID);
        }
        for (int i = 0; i < historyID.size(); i++) {
            Log.d("updateHistory2", historyID.get(i));
        }
        for (int i = 0; i < historyID.size(); i++) {
            Log.d("updateHistory", String.valueOf(i));
            editor.putString(historyTag[i], historyID.get(i));
        }
        editor.commit();
    }
}