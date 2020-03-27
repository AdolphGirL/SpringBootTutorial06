package com.reyes.tutorial.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

/**
 * 緩存讀寫異常，忽略的異常控制
 */
public class IgnoreExceptionCacheErrorHandler implements CacheErrorHandler {
	
	private final static Logger logger = LoggerFactory.getLogger(IgnoreExceptionCacheErrorHandler.class);

	@Override
	public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
		logger.error("[IgnoreExceptionCacheErrorHandler]-[handleCacheGetError]-[異常資訊: {}、異常種類: {}]", exception.getMessage(), exception);
	}

	@Override
	public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
		logger.error("[IgnoreExceptionCacheErrorHandler]-[handleCachePutError]-[異常資訊: {}、異常種類: {}]", exception.getMessage(), exception);
	}

	@Override
	public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
		logger.error("[IgnoreExceptionCacheErrorHandler]-[handleCacheEvictError]-[異常資訊: {}、異常種類: {}]", exception.getMessage(), exception);
	}

	@Override
	public void handleCacheClearError(RuntimeException exception, Cache cache) {
		logger.error("[IgnoreExceptionCacheErrorHandler]-[handleCacheClearError]-[異常資訊: {}、異常種類: {}]", exception.getMessage(), exception);
	}

}
