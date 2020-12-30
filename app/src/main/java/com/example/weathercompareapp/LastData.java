package com.example.weathercompareapp;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.weathercompareapp.MainActivity.lastJSON;
import static com.example.weathercompareapp.MainActivity.temp;
import static com.example.weathercompareapp.MainActivity.weatherData;
import static com.example.weathercompareapp.MainActivity.weathers;

// 한 클래스에서 서로 다른 API를 호출 시 오류 발생 해결을 위한 클래스
public class LastData {

    public static void getJSON(String urlStr) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Log.d("연결된 url", urlStr);
                    //dt를 이용하여 같은시간의 전날 온도를 추출하여 WeatherData에 저장
                    URL url = new URL(urlStr);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();     //http 연결을 생성
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);


                    InputStream inputStream;
                    inputStream = httpURLConnection.getInputStream();                                   // url 결과를 받음

                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));        //받은 결과를 buffer 에 저장
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);                               //buffer 의 결과를 조합하여 최종 결과 문자열을 생성
                    }
                    lastJSON = builder.toString();
//                    Log.d("결과", lastJSON);
                    JSONObject lastObj = new JSONObject(lastJSON);
                    temp = lastObj.getJSONObject("current").getString("temp");
                    weatherData.setLastTemp(temp);
                    weathers.add(weatherData);
                    reader.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
