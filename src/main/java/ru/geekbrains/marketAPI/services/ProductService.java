package ru.geekbrains.marketAPI.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import ru.geekbrains.marketAPI.dto.Product;

import java.util.List;

public interface ProductService {

    @GET("products")
    Call<List<Product>> getProducts();

    @POST("products")
    Call<Product> createProduct(@Body Product product);

    @PUT("products")
    Call<Product> editProduct(@Body Product product);

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") int id);

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") int id);

}
