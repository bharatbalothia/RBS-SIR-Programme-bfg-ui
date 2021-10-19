package com.ibm.sterling.bfg.app.config.cache;

import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

import static com.ibm.sterling.bfg.app.config.cache.CacheSpec.CACHE_BP_HEADERS;
import static com.ibm.sterling.bfg.app.config.cache.CacheSpec.CACHE_PERMISSIONS;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${cache.bpheaders.timeout}")
    private Long bpheadersTimeout;

    @Value("${cache.permissions.timeout}")
    private Long permissionsTimeout;

    @Bean("bpNames")
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(String name) {
                long timeOut = 10;
                if (CACHE_PERMISSIONS.equals(name)) {
                    timeOut = bpheadersTimeout;
                } else if (CACHE_BP_HEADERS.equals(name)) {
                    timeOut = permissionsTimeout;
                }
                return new ConcurrentMapCache(
                        name,
                        CacheBuilder.newBuilder()
                                .expireAfterWrite(timeOut, TimeUnit.MINUTES)
                                .build().asMap(),
                        false);
            }
        };
    }
}
