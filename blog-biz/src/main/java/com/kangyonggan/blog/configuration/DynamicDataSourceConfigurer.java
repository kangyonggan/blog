package com.kangyonggan.blog.configuration;

import com.kangyonggan.blog.constants.MultiDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 动态数据源配置
 *
 * @author kangyonggan
 * @since 2019-04-18
 */
@Configuration
public class DynamicDataSourceConfigurer {

    @Autowired
    private Environment env;

    @Bean
    public DataSource defaultDataSource() {
        Properties props = new Properties();
        props.put("driverClassName", env.getRequiredProperty("jdbc.default.driverClassName"));
        props.put("jdbcUrl", env.getRequiredProperty("jdbc.default.url"));
        props.put("username", env.getRequiredProperty("jdbc.default.username"));
        props.put("password", env.getRequiredProperty("jdbc.default.password"));

        return new HikariDataSource(new HikariConfig(props));
    }

    @Bean
    @Primary
    public DynamicDataSource dynamicDataSource(@Qualifier("defaultDataSource") DataSource defaultDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(8);
        targetDataSources.put(MultiDataSource.DEFAULT, defaultDataSource);

        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(defaultDataSource);

        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("defaultDataSource") DataSource defaultDataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dynamicDataSource(defaultDataSource));
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getRequiredProperty("mybatis.mapper-locations")));

        return factoryBean.getObject();
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
