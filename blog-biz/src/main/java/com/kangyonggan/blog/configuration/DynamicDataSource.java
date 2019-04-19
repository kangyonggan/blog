package com.kangyonggan.blog.configuration;

import com.kangyonggan.blog.constants.MultiDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author kangyonggan
 * @since 2019-04-18
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<MultiDataSource> DATA_SOURCES = new ThreadLocal<>();

    public static void setDataSource(MultiDataSource dataSource) {
        DATA_SOURCES.set(dataSource);
    }

    @Override
    protected MultiDataSource determineCurrentLookupKey() {
        return DATA_SOURCES.get();
    }

    public static void remove() {
        DATA_SOURCES.remove();
    }
}
