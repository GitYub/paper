package com.yxy.service;

import com.google.common.base.Joiner;
import com.yxy.beans.CacheKeyConstants;
import com.yxy.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;

/**
 * SysCacheService
 *
 * @author 余昕宇
 * @date 2018-06-22 18:03
 **/
@Service
@Slf4j
public class SysCacheService {

    @Resource(name = "redisPool")
    private RedisPool redisPool;

    public void saveCache(String toSavedValue, int timeoutSeconds, CacheKeyConstants prefix) {

        saveCache(toSavedValue, timeoutSeconds, prefix, (String[]) null);

    }

    public void saveCache(String toSavedValue, int timeoutSeconds, CacheKeyConstants prefix, String... keys) {

        if (toSavedValue == null) {

            return;

        }

        ShardedJedis shardedJedis = null;
        try {

            String cacheKey = generateCacheKey(prefix, keys);
            shardedJedis = redisPool.instance();
            shardedJedis.setex(cacheKey, timeoutSeconds, toSavedValue);

        } catch (Exception e) {

            log.error("save cache exception, prefix:{}, keys:{}", prefix.name(), JsonMapper.obj2String(keys));

        } finally {

            redisPool.safeClose(shardedJedis);

        }

    }

    public String getFromCache(CacheKeyConstants prefix, String... keys) {

        ShardedJedis shardedJedis = null;
        String cacheKey = generateCacheKey(prefix, keys);
        try {

            shardedJedis = redisPool.instance();
            return shardedJedis.get(cacheKey);

        } catch (Exception e) {

            log.error("get from cache exception, prefix:{}, keys:{}", prefix, JsonMapper.obj2String(keys));
            return null;

        } finally {

            redisPool.safeClose(shardedJedis);

        }

    }

    private String generateCacheKey(CacheKeyConstants prefix, String... keys) {

        String key = prefix.name();
        if (keys != null && keys.length > 0) {

            key += "_" + Joiner.on("_").join(keys);

        }
        return key;

    }

}
