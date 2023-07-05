package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.adapter.FutureWeatherAdapter;
import com.example.weather.bean.DayWeatherBean;
import com.example.weather.bean.WeatherBean;
import com.example.weather.util.NetUtil;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Database dbHelper;
    private AppCompatSpinner mSpinner;
    private ArrayAdapter<String> mSpAdapter;
    private String[] mCities;

    private TextView tvWeather,tvTemLowHigh,tvWin,tvDate,tvUpdate,current;

    private Button search,add,sub,refresh;

    private EditText text;
    private ImageView ivWeather;
    private RecyclerView rlvFutureWeather;
    private FutureWeatherAdapter mWeatherAdapter;
    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String weather = (String) msg.obj;
                Log.d("fan", "--主线程收到了天气数据-weather---" + weather);
                if(weather.length()<100){
                    Toast.makeText(MainActivity.this, "输入有误！", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(weather)) {
                    Toast.makeText(MainActivity.this, "天气数据为空！", Toast.LENGTH_LONG).show();
                    return;
                }


                Gson gson = new Gson();
                WeatherBean weatherBean = gson.fromJson(weather, WeatherBean.class);

                if (weatherBean != null) {
                    Log.d("fan", "--解析后的数据-weather---" + weatherBean.toString());
                }


                updateUiOfWeather(weatherBean);
            }

        }
    };

    private void updateUiOfWeather(WeatherBean weatherBean) {
        if (weatherBean == null) {
            return;
        }
        List<DayWeatherBean> dayWeathers = weatherBean.getDayWeathers();
        DayWeatherBean todayWeather = dayWeathers.get(0);

        if (todayWeather == null) {
            return;
        }
        current.setText(weatherBean.getCity());
        ivWeather.setImageResource(getImgResOfWeather(todayWeather.getWeaImg()));
        tvTemLowHigh.setText(todayWeather.getTem_night() + "℃~" + todayWeather.getTem_day()+"℃");
        tvWeather.setText(todayWeather.getWea());
        tvWin.setText(todayWeather.getWin() + todayWeather.getWinSpeed());
        tvDate.setText("(" + todayWeather.getDate() + ")");
        tvUpdate.setText("数据更新时间-"+weatherBean.getUpdateTime());


        //未来天气展示
        dayWeathers.remove(0);
        mWeatherAdapter = new FutureWeatherAdapter(this,dayWeathers);
        rlvFutureWeather.setAdapter(mWeatherAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rlvFutureWeather.setLayoutManager(layoutManager);



    }

    private int getImgResOfWeather(String weaStr){
        int result = 0;
        switch (weaStr) {
            case "qing":
                result = R.drawable.biz_plugin_weather_qing;
                break;
            case "yin":
                result = R.drawable.biz_plugin_weather_yin;
                break;
            case "yu":
                result = R.drawable.biz_plugin_weather_dayu;
                break;
            case "yun":
                result = R.drawable.biz_plugin_weather_duoyun;
                break;
            case "bingbao":
                result = R.drawable.biz_plugin_weather_leizhenyubingbao;
                break;
            case "wu":
                result = R.drawable.biz_plugin_weather_wu;
                break;
            case "shachen":
                result = R.drawable.biz_plugin_weather_shachenbao;
                break;
            case "lei":
                result = R.drawable.biz_plugin_weather_leizhenyu;
                break;
            case "xue":
                result = R.drawable.biz_plugin_weather_daxue;
                break;
            default:
                result = R.drawable.biz_plugin_weather_qing;
                break;
        }

        return result;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        initView();
    }


    private void initView() {
        mSpinner = findViewById(R.id.sp_city);
        // 获取数据库
        dbHelper = new Database(this,"city",null,1);
        dbHelper.getWritableDatabase();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //插入条数据
//        ContentValues values= new ContentValues();
//        values.put("city","成都");
//        db.insert("city",null,values);
//        Toast.makeText(this,"插入成功",Toast.LENGTH_LONG).show();
//        values.clear();
        String[] cities={"",""};
        int i=0;
        int k=0;
        Cursor cursor = db.query("city",null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do{
                i++;
            }while(cursor.moveToNext());
        }
        if(cursor.moveToFirst()) {
            do{
                //自动扩容
                if(cities.length<i){
                    cities = new String[i];
                    for(int j=0;j<cities.length;j++){
                        cities[j] = "";
                    }
                }
                @SuppressLint("Range") String city = cursor.getString(cursor.getColumnIndex("city"));
                cities[k]=city;
                k++;
            }while(cursor.moveToNext());
        }
        cursor.close();


        mCities = cities;
        mSpAdapter = new ArrayAdapter<>(this, R.layout.sp_item_layout, mCities);
        mSpinner.setAdapter(mSpAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = mCities[position];

                getWeatherOfCity(selectedCity);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ivWeather = findViewById(R.id.iv_weather);
        tvTemLowHigh = findViewById(R.id.tv_tem_low_high);
        tvWeather = findViewById(R.id.tv_weather);
        tvWin = findViewById(R.id.tv_win);
        tvDate = findViewById(R.id.tv_date);
        tvUpdate = findViewById(R.id.tv_update);
        rlvFutureWeather = findViewById(R.id.rlv_future_weather);
        current = findViewById(R.id.current);

        text = findViewById(androidx.core.R.id.text);
        add = findViewById(R.id.add);
        sub = findViewById(R.id.sub);
        search = findViewById(R.id.search);
        refresh = findViewById(R.id.refresh);

        add.setOnClickListener(this);
        sub.setOnClickListener(this);
        search.setOnClickListener(this);
        refresh.setOnClickListener(this);
    }

        private void getWeatherOfCity(String selectedCity) {
            // 开启子线程，请求网络
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 请求网络
                    String weatherOfCity = NetUtil.getWeatherOfCity(selectedCity);
                    // 使用handler将数据传递给主线程
                    Message message = Message.obtain();
                    message.what = 0;
                    message.obj = weatherOfCity;
                    mHandler.sendMessage(message);

                }
            }).start();

        }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.add){
            String a = current.getText().toString();
            // 获取数据库
            dbHelper = new Database(this,"city",null,1);
            dbHelper.getWritableDatabase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String[] cities={"",""};
            int i=0;
            int k=0;
            Cursor cursor = db.query("city",null,null,null,null,null,null);
            if(cursor.moveToFirst()) {
                do{
                    i++;
                }while(cursor.moveToNext());
            }
            if(cursor.moveToFirst()) {
                do{
                    //自动扩容
                    if(cities.length<i){
                        cities = new String[i];
                        for(int j=0;j<cities.length;j++){
                            cities[j] = "";
                        }
                    }
                    @SuppressLint("Range") String city = cursor.getString(cursor.getColumnIndex("city"));
                    cities[k]=city;
                    k++;
                }while(cursor.moveToNext());
            }
            cursor.close();
            .
            int f=0;
            for(int z=0;z < i;z++) {
                if (a.equals(cities[z])) {
                    Toast.makeText(MainActivity.this, "已添加", Toast.LENGTH_LONG).show();
                    f=1;
                    return;
                }
            }
            if(f==0){
                //插入条数据
                ContentValues values= new ContentValues();//保存日期
                values.put("city",a);
                db.insert("city",null,values);
                Toast.makeText(this,"添加成功",Toast.LENGTH_LONG).show();
                values.clear();
                initView();
                text.setText("");

            }

        }

        if (v.getId()== R.id.sub){
            String a = current.getText().toString();
            dbHelper = new Database(this,"city",null,1);
            dbHelper.getWritableDatabase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            db.delete("city","city=?",new String[]{a});
            Toast.makeText(this,"删除成功",Toast.LENGTH_LONG).show();
            initView();
            text.setText("");


        }

        if (v.getId()== R.id.search){
            String a = text.getText().toString();
            if(a.isEmpty()){
                Toast.makeText(this,"不能为空",Toast.LENGTH_LONG).show();
                return;
            }
            else{
                getWeatherOfCity(a);
            }

        }

        if (v.getId()== R.id.refresh){

            initView();
        }

    }

}