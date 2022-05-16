package ru.geekbrains.marketAPI;

import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import retrofit2.Response;
import ru.geekbrains.marketAPI.db.dao.ProductsMapper;
import ru.geekbrains.marketAPI.dto.Product;
import ru.geekbrains.marketAPI.services.ProductService;
import ru.geekbrains.marketAPI.util.MyBatisUtility;
import ru.geekbrains.marketAPI.util.RetrofitUtility;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetProductTest {

    static ProductService productService;
    static SqlSession session;
    static ProductsMapper mapper;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtility.getRetrofit().create(ProductService.class);
        session = MyBatisUtility.openSqlSession();
        mapper = session.getMapper(ProductsMapper.class);
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"1, true", "0, false", "100, false", "-2, false"})
    void getProductTest(int id, boolean expectedResult) {
        Response<Product> response = productService.getProduct(id).execute();

        // check by API
        assertThat(response.isSuccessful(), is(expectedResult));
        if (response.body() != null) assertThat(response.body().getId(), is(id));

        // check by DB
        assertThat(mapper.selectByPrimaryKey(id) != null, is(expectedResult));
    }

    @AfterAll
    static void cleanUp() {
        session.close();
    }

}
