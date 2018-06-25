package com.yxy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * RedisPool
 *
 * @author 余昕宇
 * @date 2018-06-22 17:33
 **/
@Service("redisPool")
@Slf4j
public class RedisPool {

    @Resource(name = "shardedJedisPool")
    private ShardedJedisPool shardedJedisPool;

    public ShardedJedis instance() {

        return shardedJedisPool.getResource();

    }

    public void  safeClose(ShardedJedis shardedJedis) {

        try {

            if (shardedJedis != null) {

                shardedJedis.close();

            }

        } catch (Exception e) {

            log.error("return redis resource exception", e);

        }
    }

}
