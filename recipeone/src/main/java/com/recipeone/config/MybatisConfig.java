package com.recipeone.config;

import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Log4j2
@Configuration
@MapperScan(value =  {"com.recipeone.repository.store"})
@EnableTransactionManagement
@Primary
public class MybatisConfig {

/*    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:oracle:thin:@localhost:1521:xe")
                .driverClassName("oracle.jdbc.OracleDriver")
                .username("doodoo")
                .password("doodoo")
                .build();
    }*/

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        Resource[] res = new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/recipe-mapper.xml");

        sessionFactory.setMapperLocations(res);
        log.info("-------------SqlSessionFactory Config--------------");

        return sessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        log.info("-------------SqlSessionTemplate Config--------------");
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
