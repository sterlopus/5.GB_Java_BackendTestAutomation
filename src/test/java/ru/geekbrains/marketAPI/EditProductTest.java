package ru.geekbrains.marketAPI;

import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.marketAPI.db.dao.ProductsMapper;
import ru.geekbrains.marketAPI.dto.Product;
import ru.geekbrains.marketAPI.services.ProductService;
import ru.geekbrains.marketAPI.util.MyBatisUtility;
import ru.geekbrains.marketAPI.util.RetrofitUtility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EditProductTest {

    static Properties properties = new Properties();
    static ProductService productService;
    static SqlSession session;
    static ProductsMapper mapper;


    @BeforeAll
    static void beforeAll() {

        productService = RetrofitUtility.getRetrofit().create(ProductService.class);

        try {
            properties.load(new FileInputStream("src/test/resources/marketAPI/tests.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        session = MyBatisUtility.openSqlSession();
        mapper = session.getMapper(ProductsMapper.class);

    }

    @SneakyThrows
    @Test
    void editProductWithSpecificIdTest () {

        Product product = new Product()
                .withCategoryTitle(properties.getProperty("categoryTitle"))
                .withTitle(properties.getProperty("title"))
                .withPrice(Integer.parseInt(properties.getProperty("price")));

        Response<Product> response = productService.createProduct(product).execute();

        assert response.body() != null;
        int id = response.body().getId();

        Product editedProduct = new Product()
                .withId(id)
                .withCategoryTitle(properties.getProperty("categoryTitle"))
                .withTitle(properties.getProperty("editedTitle"))
                .withPrice(Integer.parseInt(properties.getProperty("editedPrice")));

        Response<Product> editedResponse = productService.editProduct(editedProduct).execute();

        // check by API
        assertThat(editedResponse.isSuccessful(), is(true));
        assertThat(editedResponse.body().getTitle(), is(properties.getProperty("editedTitle")));
        assertThat(editedResponse.body().getPrice(), is(Integer.parseInt(properties.getProperty("editedPrice"))));

        // check by DB
        assertThat(mapper.selectByPrimaryKey(id).getTitle(), is(properties.getProperty("editedTitle")));
        assertThat(mapper.selectByPrimaryKey(id).getPrice(), is(Integer.parseInt(properties.getProperty("editedPrice"))));

    }

    @AfterAll
    static void cleanUp() {
        session.close();
    }

}
