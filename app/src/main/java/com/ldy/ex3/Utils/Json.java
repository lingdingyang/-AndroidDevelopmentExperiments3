package com.ldy.ex3.Utils;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.ldy.ex3.MainActivity;
import com.ldy.ex3.POJO.City;
import com.ldy.ex3.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Json {

    static public List<City> getCities(String provinceCode, int level, MainActivity activity) throws IOException {
        List<City> cities = new ArrayList<>();
        InputStream inputStream = activity.getResources().openRawResource(R.raw.city);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String res = "";
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            if (str.trim().equals("{")) {
                res = "";
                res += (str.trim());
                while ((str = bufferedReader.readLine()) != null) {
                    if (str.trim().equals("},") || str.trim().equals("}")) {
                        res += ("}");
                        break;
                    }
                    res += (str);
                }
                JSONObject jsonObject = JSONObject.parseObject(res.toString());
                String pid = jsonObject.getString("pid");
                if (pid.equals(provinceCode)) {
                    City city = new City(jsonObject.getString("city_name"), jsonObject.getString("city_code"), jsonObject.getString("id"), level);
                    cities.add(city);
                }
            }
        }
        inputStream.close();
        bufferedReader.close();
        return cities;
    }

    static public boolean checkCode(String code, MainActivity activity) throws IOException {
        InputStream inputStream = activity.getResources().openRawResource(R.raw.city);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String res = "";
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            if (str.trim().equals("{")) {
                res = "";
                res += (str.trim());
                while ((str = bufferedReader.readLine()) != null) {
                    if (str.trim().equals("},") || str.trim().equals("}")) {
                        res += ("}");
                        break;
                    }
                    res += (str);
                }
                JSONObject jsonObject = JSONObject.parseObject(res.toString());
                String cityCode = jsonObject.getString("city_code");
                if (cityCode.equals(code)) {
                    return true;
                }
            }
        }
        inputStream.close();
        bufferedReader.close();
        return false;
    }

    public static String getName(String code, MainActivity activity) throws IOException {
        InputStream inputStream = activity.getResources().openRawResource(R.raw.city);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String res = "";
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            if (str.trim().equals("{")) {
                res = "";
                res += (str.trim());
                while ((str = bufferedReader.readLine()) != null) {
                    if (str.trim().equals("},") || str.trim().equals("}")) {
                        res += ("}");
                        break;
                    }
                    res += (str);
                }
                JSONObject jsonObject = JSONObject.parseObject(res.toString());
                String cityCode = jsonObject.getString("city_code");
                String cityName = jsonObject.getString("city_name");
                if (cityCode.equals(code)) {
                    return cityName;
                }
            }
        }
        inputStream.close();
        bufferedReader.close();
        return null;
    }
}
