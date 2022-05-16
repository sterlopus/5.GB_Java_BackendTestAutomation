package ru.geekbrains.marketAPI;

import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSession;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.marketAPI.db.dao.ProductsMapper;
import ru.geekbrains.marketAPI.db.model.Products;
import ru.geekbrains.marketAPI.db.model.ProductsExample;
import ru.geekbrains.marketAPI.dto.Product;
import ru.geekbrains.marketAPI.services.ProductService;
import ru.geekbrains.marketAPI.util.MyBatisUtility;
import ru.geekbrains.marketAPI.util.RetrofitUtility;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class GetProductsListTest {

    static ProductService productService;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtility.getRetrofit().create(ProductService.class);

    }

    @SneakyThrows
    @Test
    void getProductListPositiveTest() {
        Response<List<Product>> response = productService.getProducts().execute();
        // check by API
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }

}
