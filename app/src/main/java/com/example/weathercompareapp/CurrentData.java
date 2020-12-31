package com.example.weathercompareapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// 현재 날씨 데이터를 가지고 오는 클래스
public class CurrentData {
    private static String result;
    public static String getCurData(String strUrl) throws IOException {

        Log.d("연결된 url", strUrl);
        URL url = new URL(strUrl);
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
        result = builder.toString();
        //Log.d("결과", curJSON);
        reader.close();
        return result;
    }
}
