package ru.geekbrains.marketAPI.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.FileInputStream;
import java.util.Properties;


@UtilityClass
public class RetrofitUtility {

    private static Properties properties = new Properties();

    @SneakyThrows
    public String getBaseURL (){
        properties.load(new FileInputStream(
                "src/main/resources/ru.geekbrains/marketAPI/application.properties"));
        return properties.getProperty("baseURL");
    }


    @SneakyThrows
    public Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(getBaseURL())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
