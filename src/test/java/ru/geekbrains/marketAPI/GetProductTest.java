package ru.geekbrains.marketAPI;

import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import retrofit2.Response;
import ru.geekbrains.marketAPI.dto.Product;
import ru.geekbrains.marketAPI.services.ProductService;
import ru.geekbrains.marketAPI.util.RetrofitUtility;

import static org.hamcrest.MatcherAssert.assertThat;

public class GetProductTest {

    static ProductService productService;

    @BeforeAll
    static void beforeAll() {
       productService = RetrofitUtility.getRetrofit().create(ProductService.class);
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"1, true", "0, false", "100, false", "-2, false"})
    void getProductTest(int id, boolean expectedResult){
        Response<Product> response = productService.getProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(expectedResult));
        if(response.body() != null) assertThat(response.body().getId(), CoreMatchers.is(id));
    }

}
