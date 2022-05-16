package ru.geekbrains.marketAPI;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import ru.geekbrains.marketAPI.db.dao.CategoriesMapper;
import ru.geekbrains.marketAPI.db.model.CategoriesExample;

import java.io.IOException;
import java.io.InputStream;

public class MybatisUsageSamples {

    public static void main(String[] args) throws IOException {

        String resource = "mybatis-config.xml";
        SqlSessionFactory sqlSessionFactory;
        InputStream inputStream = Resources.getResourceAsStream(resource);

        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        CategoriesMapper mapper = sqlSession.getMapper(CategoriesMapper.class);
        CategoriesExample example = new CategoriesExample();

        System.out.println(mapper.countByExample(example));
        System.out.println(mapper.selectByPrimaryKey(2).getTitle());

    }

}