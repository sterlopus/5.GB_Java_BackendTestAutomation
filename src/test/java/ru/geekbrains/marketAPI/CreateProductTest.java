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


public class CreateProductTest {

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
    void createProductTest () {
        Product product = new Product()
                .withCategoryTitle(properties.getProperty("categoryTitle"))
                .withTitle(properties.getProperty("title"))
                .withPrice(Integer.parseInt(properties.getProperty("price")));

        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.isSuccessful(), is(true));
        assertThat(response.code(), is(201));
        assert response.body() != null;
        assertThat(response.body().getCategoryTitle(), is(properties.getProperty("categoryTitle")));
        assertThat(response.body().getTitle(), is(properties.getProperty("title")));
        assertThat(response.body().getPrice(), is(Integer.parseInt(properties.getProperty("price"))));

    }

    @SneakyThrows
    @Test
    void createNewProductWithIdErrorTest() {
        Product product = new Product()
                .withId(999)
                .withCategoryTitle(properties.getProperty("categoryTitle"))
                .withTitle(properties.getProperty("title"))
                .withPrice(Integer.parseInt(properties.getProperty("price")));

        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.code(), is(400));
    }




    // TODO: how could I put nonInt to integer place???
    // todo: real API creates new product when put string into price - it's wrong
/*    @SneakyThrows
    @Test
    void createNewProductWithWrongTypeDataTest() {
        Product product = new Product()
                .withCategoryTitle(properties.getProperty("categoryTitle"))
                .withTitle(properties.getProperty("title"))
                .withPrice("wrongDataStringNotInt");

        Response<Product> response = productService.createNewProduct(product).execute();
        assertThat(response.code(), is(400));
    }*/


        // TODO: report Undocumented API response [500] when category has wrong title name
    @SneakyThrows
    @Test
    void createNewProductWithWrongCategoryTitleTest() {
        Product product = new Product()
                .withCategoryTitle(properties.getProperty("categoryTitle") + "_")
                .withTitle(properties.getProperty("title"))
                .withPrice(Integer.parseInt(properties.getProperty("price")));

        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.code(), is(404));
    }


    // TODO: report Undocumented API response [500] when no data in product
    @SneakyThrows
    @Test
    void createNewProductWithEmptyDataTest() {
        Product product = new Product();

        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.code(), CoreMatchers.is(404));
    }


}
