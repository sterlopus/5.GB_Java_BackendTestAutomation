package ru.geekbrains.marketAPI.util;

import lombok.experimental.UtilityClass;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

@UtilityClass
public class MyBatisUtility {

    String resource = "mybatis-config.xml";
    InputStream inputStream;

    static {
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    SqlSessionFactory sqlSessionFactory  = new SqlSessionFactoryBuilder().build(inputStream);
    public SqlSession openSqlSession() {
        return sqlSessionFactory.openSession();
    }


}