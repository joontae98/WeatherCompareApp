package com.example.weathercompareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int LOAD_SUCCESS = 101;
    public static Bitmap curBit; //다른 클래스에서 사용하기 위해 public 선언
    public static List<WeatherData> weathers = new ArrayList<>();
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    TextView textviewweather;
    TextView textviewcity;
    TextView textviewtemp;
    TextView textviewhumidity;
    TextView textviewtempmin;
    TextView textviewtempmax;
    ImageView imageviewicon;
    ProgressDialog progressDialog;
    String result;
    String key = "96d98409169b4ef34c4529ad092f8471";
    String lat = "35.2450439";
    String lon = "129.0189522";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Button buttonRequestJSON = (Button) findViewById(R.id.button_main_requestjson);  //button_main_requestjson 연결
        textviewweather = (TextView) findViewById(R.id.textview_main_weather);
        textviewcity = (TextView) findViewById(R.id.textview_main_city);
        textviewtemp = (TextView) findViewById(R.id.textview_main_temp);
        textviewhumidity = (TextView) findViewById(R.id.textview_main_humidity);
        textviewtempmin = (TextView) findViewById(R.id.textview_main_temp_min);
        textviewtempmax = (TextView) findViewById(R.id.textview_main_temp_max);
        imageviewicon = (ImageView) findViewById(R.id.imageview_main_icon);

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

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait.....");
        progressDialog.show();

        getJSON();
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
                        JSONObject jsonObj = null;
                        try {
                            jsonObj = new JSONObject(jsonString);
                            JSONObject curObj = jsonObj.getJSONObject("current").getJSONArray("weather").getJSONObject(0);
                            JSONObject daiObj = jsonObj.getJSONArray("daily").getJSONObject(0).getJSONObject("temp");
                            mainactivity.textviewweather.setText(curObj.getString("description"));                          //current.weather.description 변경
                            mainactivity.textviewtemp.setText(jsonObj.getJSONObject("current").getString("temp") + "℃");       //current.temp 변경
                            mainactivity.textviewtempmin.setText("최저 온도: "+daiObj.getString("min") + "℃");                //daily.temp.min 변경
                            mainactivity.textviewtempmax.setText("최고 온도: "+daiObj.getString("max") + "℃");                //daily.temp.max 변경
                            mainactivity.textviewhumidity.setText(jsonObj.getJSONObject("current").getString("humidity") + "%");
                            mainactivity.imageviewicon.setImageBitmap(curBit);                                                          //current.weather.icon 변경
                            mainactivity.recyclerView.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                }
            }
        }
    }

    public void getJSON() {
        String requestUrl = "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=minutely&appid="+key+"&lang=kr&units=metric";


        //thread 사용 이유 - 네트워크 작업은 메인 thread 가 아닌 별도의 thread에 해야함 그러지 않으면 err: NetworkOnMainThreadException 발생
        Thread thread = new Thread(new Runnable() {
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

                    JSONObject obj = new JSONObject(result);
                    String curIcon = obj.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("icon");
                    curBit = getBitmap(curIcon);

                    //WeatherData 생성 코드
                    JSONArray jsonArr = obj.getJSONArray("hourly");
                    for (int i = 1; i < 25; i++) {                                       // i < 25 인 이유 = 배열 24개를 추출하기 위해
                        JSONObject hourObj = jsonArr.getJSONObject(i);
                        WeatherData weatherData = new WeatherData();
                        weatherData.setTime(hourObj.getString("dt"));
                        weatherData.setHourTemp(hourObj.getString("temp"));
//                        weatherData.setLastTemp();                                    //dt를 이용하여 같은시간의 전날 온도를 추출하여 WeatherData에 저장
                        String hourBit = hourObj.getJSONArray("weather").getJSONObject(0).getString("icon");
                        weatherData.setHourIcon(getBitmap(hourBit));
                        weathers.add(weatherData);
                    }
                    //-----------------------
                    reader.close();
                    Message message = mHandler.obtainMessage(LOAD_SUCCESS, result);
                    mHandler.sendMessage(message);
                    mAdapter = new MyAdapter(weathers);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    //iconUrl -> Bitmap 변환 메서드
    public Bitmap getBitmap(String iconCode) throws IOException {

        URL iconUrl = new URL("http://openweathermap.org/img/wn/"+ iconCode +"@2x.png");
        HttpURLConnection iconConn = (HttpURLConnection) iconUrl.openConnection();
        iconConn.setDoInput(true);
        iconConn.connect();
        InputStream is = iconConn.getInputStream();
        return BitmapFactory.decodeStream(is);
    }

}