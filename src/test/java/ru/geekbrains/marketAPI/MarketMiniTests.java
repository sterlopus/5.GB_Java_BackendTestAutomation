package ru.geekbrains.marketAPI;

import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.marketAPI.dto.Category;
import ru.geekbrains.marketAPI.services.CategoryService;
import ru.geekbrains.marketAPI.services.ProductService;
import ru.geekbrains.marketAPI.util.RetrofitUtil;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

public class MarketMiniTests {

    static CategoryService categoryService;
    static ProductService productService;

    @BeforeAll
    static void beforeAll(){
        categoryService = RetrofitUtil.getRetrofit().create(CategoryService.class);
    }

    @SneakyThrows
    @Test
     void getCategoryPositiveTest () {
        Response<Category> response = categoryService.getCategory(1).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));

    }

}
