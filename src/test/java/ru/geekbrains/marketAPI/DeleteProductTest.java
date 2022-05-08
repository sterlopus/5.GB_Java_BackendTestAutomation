package ru.geekbrains.marketAPI;

import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.marketAPI.dto.Product;
import ru.geekbrains.marketAPI.services.ProductService;
import ru.geekbrains.marketAPI.util.RetrofitUtility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DeleteProductTest {

    static ProductService productService;
    static Properties properties = new Properties();


    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtility.getRetrofit().create(ProductService.class);

        try {
            properties.load(new FileInputStream("src/test/resources/tests.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @SneakyThrows
    @Test
    void deleteProductWithSpecificIdTest() {

        Product product = new Product()
                .withCategoryTitle(properties.getProperty("categoryTitle"))
                .withTitle(properties.getProperty("title"))
                .withPrice(Integer.parseInt(properties.getProperty("price")));

        Response<Product> response = productService.createProduct(product).execute();

        productService.deleteProduct((response.body().getId())).execute();

        assertThat(productService.getProduct(
                response.body().getId()).execute().body(),
                CoreMatchers.nullValue());

    }

}
