package com.example.weathercompareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final int LOAD_SUCCESS = 101;
    TextView textviewJSONText;
    TextView textviewweather;
    TextView textviewcity;
    TextView textviewtemp;
    TextView textviewtempmin;
    TextView textviewtempmax;
    TextView textviewhumidity;
    ProgressDialog progressDialog;
    String result;
    String key = "96d98409169b4ef34c4529ad092f8471";
    String lat = "35.2450439";
    String lon = "129.0189522";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonRequestJSON = (Button) findViewById(R.id.button_main_requestjson);  //button_main_requestjson 연결
        textviewJSONText = (TextView) findViewById(R.id.textview_main_jsontext);         //textview_main_jsontext 연결
        textviewweather = (TextView) findViewById(R.id.textview_main_weather);
        textviewcity = (TextView) findViewById(R.id.textview_main_city);
        textviewtemp = (TextView) findViewById(R.id.textview_main_temp);
        textviewtempmin = (TextView) findViewById(R.id.textview_main_temp_min);
        textviewtempmax = (TextView) findViewById(R.id.textview_main_temp_max);
        textviewhumidity = (TextView) findViewById(R.id.textview_main_humidity);

        buttonRequestJSON.setOnClickListener(new View.OnClickListener() {               //버튼 클릭 시 onClick실행
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Please wait.....");
                progressDialog.show();

                getJSON();

            }
        });
    }

    //err: Only the original thread that created a view hierarchy can touch its views 발생
    //원인 main thread 외의 thread에서 ui를 임의로 변경해서 발생
    //해결 방법 handler를 이용하여 main thread를 간접적으로 사용
    private final MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity mainactivity) {
            weakReference = new WeakReference<MainActivity>(mainactivity);
        }

        @Override
        public void handleMessage(Message msg) {

            MainActivity mainactivity = weakReference.get();

            if (mainactivity != null) {
                switch (msg.what) {

                    case LOAD_SUCCESS:
                        mainactivity.progressDialog.dismiss();
                        String jsonString = (String) msg.obj;
                        mainactivity.textviewJSONText.setText(jsonString);
                        JSONObject jsonObj = null;
                        try {
                            jsonObj = new JSONObject(jsonString);
                            JSONObject obj = jsonObj.getJSONObject("main");
                            JSONArray array = jsonObj.getJSONArray("weather");
                            mainactivity.textviewweather.setText(array.getJSONObject(0).getString("main"));
                            mainactivity.textviewcity.setText(jsonObj.getString("name"));
                            mainactivity.textviewtemp.setText(changeKToC(obj.getString("temp")) + "℃");
                            mainactivity.textviewtempmin.setText(changeKToC(obj.getString("temp_min")) + "℃");
                            mainactivity.textviewtempmax.setText(changeKToC(obj.getString("temp_max")) + "℃");
                            mainactivity.textviewhumidity.setText(obj.getString("humidity") + "%");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                }
            }
        }

        public String changeKToC(String str) {       // 절대온도에서 섭씨온도로 변환 메서드

            double temp = Double.parseDouble(str);
            double result = temp - 275.15;

            return Double.toString(result);
        }
    }

    public void getJSON() {
        String requestUrl = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + key;

        Thread thread = new Thread(new Runnable() { //thread 사용 이유?
            @Override
            public void run() {
                try {

                    Log.d("연결된 url", requestUrl);
                    URL url = new URL(requestUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();     //http 연결을 생성

                    httpURLConnection.setRequestMethod("GET");

                    InputStream inputStream;
                    inputStream = httpURLConnection.getInputStream();                                   // url 결과를 받음


                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));        //받은 결과를 buffer에 저장
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);                               //buffer의 결과를 조합하여 최종 결과 문자열을 생성
                    }

                    result = builder.toString();
                    Log.d("결과", result);
                    reader.close();
                    Message message = mHandler.obtainMessage(LOAD_SUCCESS, result);
                    mHandler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}