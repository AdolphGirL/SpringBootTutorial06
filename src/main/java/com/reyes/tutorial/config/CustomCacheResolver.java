package com.reyes.tutorial.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 多層緩存機制
 * - 查詢: 先從內存找，找不到再從redis讀取，最後在db查詢
 * - 寫入: 並且將讀取到的資料，一次保存在redis和內存
 */
public class CustomCacheResolver implements CacheResolver, InitializingBean {
	
	@Nullable
	private List<CacheManager> cacheManagerList;
	
	public CustomCacheResolver() {
		
	}

	public CustomCacheResolver(List<CacheManager> cacheManagerList) {
		this.cacheManagerList = cacheManagerList;
	}

	public void setCacheManagerList(@Nullable List<CacheManager> cacheManagerList) {
		this.cacheManagerList = cacheManagerList;
	}

	public List<CacheManager> getCacheManagerList() {
		return cacheManagerList;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		 Assert.notNull(this.cacheManagerList, "CacheManager is required");
	}

	@Override
	public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
		Collection<String> cacheNames = getCacheNames(context);
		
		if (cacheNames == null) {
			return Collections.emptyList();
		}
		
		Collection<Cache> result = new ArrayList<>();
		for (CacheManager cacheManager : getCacheManagerList()) {
			for (String cacheName : cacheNames) {
				Cache cache = cacheManager.getCache(cacheName);
				if (cache == null) {
					throw new IllegalArgumentException(
							"Cannot find cache named '" + cacheName + "' for " + context.getOperation());
				}
				result.add(cache);
			}
		}
		
		return result;
	}
	
	private Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
		return context.getOperation().getCacheNames();
	}
}
