package com.reyes.tutorial.config;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
//@ConfigurationProperties(prefix = "spring.cache.redis")
public class RedisConfig extends CachingConfigurerSupport {
	
//	/**spring boot 1.x
//	 * RedisCacheConfiguration.class(@ConditionalOnMissingBean(CacheManager.class))，根據此重新配置RedisCacheManager，因此重新配置後會替換掉原先
//	 * RedisAutoConfiguration.class，根據此重新配置RedisTemplate
//	 */
//	
//	@Bean
//	public RedisTemplate<Object, Department> departmentRedisTemplate(RedisConnectionFactory redisConnectionFactory)
//			throws UnknownHostException {
//		
//		RedisTemplate<Object, Department> template = new RedisTemplate<>();
//		template.setConnectionFactory(redisConnectionFactory);
//		
////		key、value都會使用此序列化器
////		RedisSerializer 抽象發法有GenericJackson2JsonRedisSerializer器
//		
//		GenericJackson2JsonRedisSerializer ser = new GenericJackson2JsonRedisSerializer();
//		template.setDefaultSerializer(ser);
//		
//		return template;
//	}
	
	/**
	 * spring boot 2.x 寫入資料使用json格式
	 * Jackson2JsonRedisSerializer和GenericJackson2JsonRedisSerializer都是序列化为json格式。
	 * 不同：如果存储的类型为List等带有泛型的对象，反序列化的时候 Jackson2JsonRedisSerializer序列化方式会报错，
	 * 		而GenericJackson2JsonRedisSerializer序列化方式是成功的，
	 * 原因
	 * 		GenericJackson2JsonRedisSerializer方式中有@class字段保存有类型的包路径，可以顺利的转换为我们需要的User类型
	 * 		"@class": "xxx.xxx.User"	
	 */
	private final static Logger logger = LoggerFactory.getLogger(RedisConfig.class);
	
//	設置過期時間1天
	private Duration timeToLive = Duration.ZERO;
	
//	開放由yml設定也可以
	public void setTimeToLive(Duration timeToLive) {
		logger.info("[RedisConfig]-[啟動後加載]-[timeToLive setting is {}", timeToLive);
		this.timeToLive = timeToLive;
	}
	
	// 如果有多個CacheManager，@Primary直接指定那個是默認的
	// @Primary
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		// RedisSerializationContext: This context provides {@link SerializationPair}s for key, value, hash-key (field), hash-value
		RedisSerializationContext.SerializationPair<String> keySerializationPair = 
						RedisSerializationContext.SerializationPair.fromSerializer(keySerializer());
		
		RedisSerializationContext.SerializationPair<Object> valueSerializationPair = 
				RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer());
		
		RedisCacheConfiguration config = RedisCacheConfiguration
					// 保存時間
					.defaultCacheConfig().entryTtl(this.timeToLive)
					// key序列化規則
					.serializeKeysWith(keySerializationPair)
					// value序列化規則
					.serializeValuesWith(valueSerializationPair)
					// 不緩存空值
					.disableCachingNullValues();
	
//		還未比較出兩種的差異
//		RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
//												.cacheDefaults(config).transactionAware().build();
		
		RedisCacheManager redisCacheManager = RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory))
				.cacheDefaults(config).build();
		
		logger.info("[RedisConfig]-[啟動後加載]-[redisCacheManager加載完成: {}", redisCacheManager);
		return redisCacheManager;
	}
	
	private RedisSerializer<String> keySerializer() {
		return new StringRedisSerializer();
	}

	private RedisSerializer<Object> valueSerializer() {
		return new GenericJackson2JsonRedisSerializer();
	}

}
