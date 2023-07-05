package com.example.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.bean.DayWeatherBean;

import java.util.List;

public class FutureWeatherAdapter extends RecyclerView.Adapter<FutureWeatherAdapter.WeatherViewHolder> {

    private Context mContext;
    private List<DayWeatherBean> mWeatherBeans;

    public FutureWeatherAdapter(Context context, List<DayWeatherBean> weatherBeans) {
        mContext = context;
        this.mWeatherBeans = weatherBeans;
    }



    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.weather_item_layout, parent, false);
        WeatherViewHolder weatherViewHolder = new WeatherViewHolder(view);
        return weatherViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        DayWeatherBean weatherBean = mWeatherBeans.get(position);

        holder.ivWeather.setImageResource(getImgResOfWeather(weatherBean.getWeaImg()));
        holder.tvTemLowHigh.setText(weatherBean.getTem_night() + "℃~" + weatherBean.getTem_day()+"℃");
        holder.tvWeather.setText(weatherBean.getWea());
        holder.tvWin.setText(weatherBean.getWin() + weatherBean.getWinSpeed());
        holder.tvDate.setText("(" + weatherBean.getDate() + ")");


    }

    private int getImgResOfWeather(String weaStr) {
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
    public int getItemCount() {
        return (mWeatherBeans == null) ? 0 : mWeatherBeans.size();
    }

    class  WeatherViewHolder extends RecyclerView.ViewHolder{
        private TextView tvWeather,tvTemLowHigh,tvWin,tvDate;
        private ImageView ivWeather;


        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);

            ivWeather = itemView.findViewById(R.id.iv_weather);
            tvWeather = itemView.findViewById(R.id.tv_weather);
            tvTemLowHigh = itemView.findViewById(R.id.tv_tem_low_high);
            tvWin = itemView.findViewById(R.id.tv_win);
            tvDate = itemView.findViewById(R.id.tv_date);



        }


    }

}
