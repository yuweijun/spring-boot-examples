package com.example.spring.jdbc.dao;

import com.example.spring.jdbc.model.City;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yuweijun 2016-06-11.
 */
@Component
public class CityMybatisDao {

    @Autowired
    private SqlSession sqlSession;

    public City selectCityById(long id) {
        return this.sqlSession.selectOne("selectCityById", id);
    }

}