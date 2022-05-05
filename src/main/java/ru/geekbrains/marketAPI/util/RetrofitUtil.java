package ru.geekbrains.marketAPI.util;

import lombok.experimental.UtilityClass;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


@UtilityClass
public class RetrofitUtil {

    public Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl("http://80.78.248.82:8189/market/api/v1/") //TODO: get URL from properties
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
