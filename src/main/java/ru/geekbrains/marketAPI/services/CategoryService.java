package ru.geekbrains.marketAPI.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.geekbrains.marketAPI.dto.Category;

public interface CategoryService {

    @GET("categories/{id}")
    Call<Category> getCategory(@Path("id") int id);
}
