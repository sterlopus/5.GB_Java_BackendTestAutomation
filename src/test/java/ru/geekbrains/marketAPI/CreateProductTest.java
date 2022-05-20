package ru.geekbrains.marketAPI;

import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSession;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.marketAPI.db.dao.ProductsMapper;
import ru.geekbrains.marketAPI.db.model.ProductsExample;
import ru.geekbrains.marketAPI.dto.Product;
import ru.geekbrains.marketAPI.services.ProductService;
import ru.geekbrains.marketAPI.util.MyBatisUtility;
import ru.geekbrains.marketAPI.util.RetrofitUtility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


public class CreateProductTest {

    static ProductService productService;
    static Properties properties = new Properties();
    static SqlSession session;
    static ProductsMapper mapper;
    static ProductsExample example;
    static int price;


    @BeforeAll
    static void beforeAll() throws IOException {
        productService = RetrofitUtility.getRetrofit().create(ProductService.class);

        try {
            properties.load(new FileInputStream("src/test/resources/marketAPI/tests.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        price = Integer.parseInt(properties.getProperty("price"));

        session = MyBatisUtility.openSqlSession();
        mapper = session.getMapper(ProductsMapper.class);
        example = new ProductsExample();

    }


    @SneakyThrows
    @Test
    void createProductTest() {
        Product product = new Product()
                .withCategoryTitle(properties.getProperty("categoryTitle"))
                .withTitle(properties.getProperty("title"))
                .withPrice(price);

        Response<Product> response = productService.createProduct(product).execute();

        // check by API
        assertThat(response.isSuccessful(), is(true));
        assertThat(response.code(), is(201));
        assert response.body() != null;

        // check by DB
        int id = response.body().getId();
        assertThat(mapper.selectByPrimaryKey(id).getTitle(), is(properties.getProperty("title")));
        assertThat(mapper.selectByPrimaryKey(id).getPrice(), is(price));

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

        // check by API
        assertThat(response.code(), is(400));
        // check by DB
        assertThat(mapper.selectByPrimaryKey(999), nullValue());
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

        Long productsInDatabaseCounter = mapper.countByExample(example); // set initial counter

        Product product = new Product()
                .withCategoryTitle(properties.getProperty("categoryTitle") + "_")
                .withTitle(properties.getProperty("title"))
                .withPrice(Integer.parseInt(properties.getProperty("price")));

        Response<Product> response = productService.createProduct(product).execute();

        // check by API
        assertThat(response.code(), is(500));
        // check by DB
        assertThat(mapper.countByExample(example), is(productsInDatabaseCounter)); // no new products created

    }


    // TODO: report Undocumented API response [500] when no data in product
    @SneakyThrows
    @Test
    void createNewProductWithEmptyDataTest() {
        Product product = new Product();
        Long productsInDatabaseCounter = mapper.countByExample(example);

        Response<Product> response = productService.createProduct(product).execute();
        // check by API
        assertThat(response.code(), CoreMatchers.is(500));
        // check by DB
        assertThat(mapper.countByExample(example), is(productsInDatabaseCounter)); // no new products created

    }


    @AfterAll
    static void cleanUp() {
        session.close();
    }


}
