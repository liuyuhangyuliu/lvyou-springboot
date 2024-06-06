package edu.hit.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void expire(String key, long time) {
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void remove(String... key) {
        if (key != null && key.length > 0) {
            redisTemplate.delete(Arrays.asList(key));
        }
    }


    // ============================String=============================

    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, long time) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } else {
            set(key, value);
        }
    }

    public Long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public Long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }


    // ================================Map=================================

    public Object hGet(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    public Map<Object, Object> hGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void hSet(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public void hSetAndTime(String key, Map<String, Object> map, long time) {
        redisTemplate.opsForHash().putAll(key, map);
        if (time > 0) {
            expire(key, time);
        }
    }

    public void hSet(String key, String item, Object value) {
        redisTemplate.opsForHash().put(key, item, value);
    }

    public void hSetAndTime(String key, String item, Object value, long time) {
        redisTemplate.opsForHash().put(key, item, value);
        if (time > 0) {
            expire(key, time);
        }
    }

    public void hRemove(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    public double hIncr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    public double hDecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }


    // ============================set=============================

    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public Boolean sHasKey(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }


    public Long sSet(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    public Long sSetAndTime(String key, long time, Object... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        if (time > 0)
            expire(key, time);
        return count;
    }

    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    // ===============================list=================================

    public List<Object> lGet(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public Long lSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public Object lGetByIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    public void lSet(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public void lSetAndTime(String key, Object value, long time) {
        redisTemplate.opsForList().rightPush(key, value);
        if (time > 0)
            expire(key, time);
    }

    public void lSet(String key, List<Object> value) {
        redisTemplate.opsForList().rightPushAll(key, value);
    }

    public void lSetAndTime(String key, List<Object> value, long time) {
        redisTemplate.opsForList().rightPushAll(key, value);
        if (time > 0)
            expire(key, time);
    }

    public void lSetByIndex(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public Long lRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }
}
