package com.ldy.ex3.Utils;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ldy.ex3.MainActivity;
import com.ldy.ex3.POJO.City;
import com.ldy.ex3.POJO.Weather;
import com.ldy.ex3.adapter.FavouriteAdapter;
import com.ldy.ex3.adapter.ViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetWork {
    static List<Weather> cityWeathers = new ArrayList<>();

    //    获取一些城市的天气信息，并将其更新到界面上
    static public void getWeather(List<String> cityIDs, ListView listView, Activity context, int resource, MainActivity clickActivity) {
//        创建httpClient对象，用于发送请求
        OkHttpClient httpClient = new OkHttpClient();
//        保存天气信息的队列
        List<Weather> weathers = new ArrayList<>();
//        遍历每个需要查询的城市
        for (String cityID : cityIDs) {
//            创建请求对象，将url拼接上城市ID，用于获取指定城市的天气数据
            Request request = new Request.Builder().url("http://t.weather.sojson.com/api/weather/city/" + cityID).build();
//            通过异步的方式发送请求
            httpClient.newCall(request).enqueue(new Callback() {
                //                请求失败的回调
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("onFailure", "net work Failure");
                }

                //                请求成功的回调
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                    获取相应体，转成string类型
                    String res = response.body().string();
//                    返回的数据是JSON格式，使用fastjson将其转换成对象
                    JSONObject object = JSONObject.parseObject(res);
//                    获取该对象的cityInfo属性，也是一个对象，转换成对象
                    JSONObject cityInfo = object.getJSONObject("cityInfo");
//                    获取cityInfo对象的city属性，这是城市名
                    String cityName = cityInfo.getString("city");
                    //                    获取cityInfo对象的citykey属性，这是城市ID
                    String cityKey = cityInfo.getString("citykey");
//                    获取cityInfo对象的data属性，这是对象，转换成对象
                    JSONObject data = object.getJSONObject("data");
//                    获取data对象的qualityy属性，这是天气质量
                    String quality = data.getString("quality");
                    //                    获取data对象的forecast属性，这是对象数组，每一个对象都是描述某一天的天气
                    JSONArray forecast = data.getJSONArray("forecast");
//                    将data第一个元素转换成对象，分别获取其high，low，fx，fl，type，ymd属性，分别是最高温，最低温，风力，风向，天气，年月日
                    JSONObject day = (JSONObject) forecast.get(0);
                    String high = day.getString("high");
                    String low = day.getString("low");
                    String fx = day.getString("fx");
                    String fl = day.getString("fl");
                    String type = day.getString("type");
                    String ymd = day.getString("ymd");
//                    使用获取的信息构造城市对象
                    City city = new City(cityName, cityKey, null, -1);
//                    使用获取的数据构造天气对象
                    Weather weather = new Weather(type, city, quality, high + "-" + low, fx, fl, ymd);
//                    将这个天气对象放入队列中
                    weathers.add(weather);
//                    下面更新ui，需要在ui线程中更新
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            使用weathers构造adapter，listview设置上更新的adapter来更新数据
                            FavouriteAdapter favouriteAdapter = new FavouriteAdapter(context, resource, weathers, clickActivity);
                            listView.setAdapter(favouriteAdapter);
                        }
                    });
                }
            });
        }
        return;
    }

    static public void getCityWeather(String cityID, ListView listView, Activity context, int resource, TextView text_weather, TextView text_city, TextView text_viewTemperature, TextView text_viewHumidity, TextView text_viewPM25, TextView text_viewUpdateTime) {
        OkHttpClient httpClient = new OkHttpClient();
        cityWeathers.clear();
        Request request = new Request.Builder().url("http://t.weather.sojson.com/api/weather/city/" + cityID).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("onFailure", "net work Failure");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                JSONObject object = JSONObject.parseObject(res);
                JSONObject cityInfo = object.getJSONObject("cityInfo");
                String cityName = cityInfo.getString("city");
                String cityKey = cityInfo.getString("cityKey");
                String updateTime = cityInfo.getString("updateTime");
                JSONObject data = object.getJSONObject("data");
                String pm25 = data.getString("pm25");
                String shidu = data.getString("shidu");
                String wendu = data.getString("wendu");
                String quality = data.getString("quality");
                JSONArray forecast = data.getJSONArray("forecast");
                String nowWeather = "";
                String nowCity = cityName;
                for (int i = 0; i < 10; i++) {
                    JSONObject day = (JSONObject) forecast.get(i);
                    String high = day.getString("high");
                    String low = day.getString("low");
                    String fx = day.getString("fx");
                    String fl = day.getString("fl");
                    String type = day.getString("type");
                    City city = new City(cityName, cityKey, null, -1);
                    String ymd = day.getString("ymd");
                    Weather weather = new Weather(type, city, quality, high + low, fx, fl, ymd);
                    cityWeathers.add(weather);
                    if (i == 0) {
                        nowWeather = type;

                    }
                }
                String finalNowWeather = nowWeather;
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text_city.setText(nowCity);
                        text_weather.setText(finalNowWeather);
                        text_viewTemperature.setText("温度：" + wendu);
                        text_viewPM25.setText("PM2.5：" + pm25);
                        text_viewHumidity.setText("湿度：" + shidu);
                        text_viewUpdateTime.setText("更新时间：" + updateTime);
                        ViewAdapter viewAdapter = new ViewAdapter(context, resource, cityWeathers);
                        listView.setAdapter(viewAdapter);
                    }
                });
            }
        });
        return;
    }
}
