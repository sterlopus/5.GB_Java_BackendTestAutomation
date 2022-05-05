package ru.geekbrains.marketAPI.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import ru.geekbrains.marketAPI.dto.Product;

import java.util.List;

public interface ProductService {

    @GET("products")
    Call<List<Product>> getProducts();  // TODO: or ResponseBody?

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") int id);

    @POST("products")
    Call<Product> createNewProduct(@Body Product product);

    @PUT("products/{id}")
    Call<Product> editProduct(@Path("id") int id, @Body Product product);

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") int id);

}
