package ru.geekbrains.marketAPI;

import lombok.SneakyThrows;
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

public class EditProductTest {

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
    void editProductWithSpecificIdTest () {

        Product product = new Product()
                .withCategoryTitle(properties.getProperty("categoryTitle"))
                .withTitle(properties.getProperty("title"))
                .withPrice(Integer.parseInt(properties.getProperty("price")));

        Response<Product> response = productService.createProduct(product).execute();


        Product editedProduct = new Product()
                .withId(response.body().getId())
                .withCategoryTitle(properties.getProperty("categoryTitle"))
                .withTitle(properties.getProperty("editedTitle"))
                .withPrice(Integer.parseInt(properties.getProperty("editedPrice")));

        Response<Product> editedResponse = productService.editProduct(editedProduct).execute();

        assertThat(editedResponse.isSuccessful(), is(true));
        assertThat(editedResponse.body().getTitle(), is(properties.getProperty("editedTitle")));
        assertThat(editedResponse.body().getPrice(), is(Integer.parseInt(properties.getProperty("editedPrice"))));


    }


}
