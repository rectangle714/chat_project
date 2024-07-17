package com.chat_project.common.util
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisUtil(
    private val redisTemplate: RedisTemplate<String, Any>
) {
    fun getData(key: String?): String? {
        return redisTemplate.opsForValue().get(key)?.toString()
    }

    fun setData(key: String, o: Any, hours: Long?) {
        redisTemplate.valueSerializer = StringRedisSerializer()
        redisTemplate.opsForValue()[key, o, hours!!] = TimeUnit.HOURS
    }

    fun deleteData(key: String): Boolean {
        return redisTemplate.delete(key)
    }

    fun hasKey(key: String): Boolean {
        return redisTemplate.hasKey(key)
    }
}