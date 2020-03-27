#### 緩存
JSR-107
JAVA定義五個核心接口，分別是CacheProvider、CacheManager、Cache、Entry、Expiry
- CacheProvider，定義了創建、配置、獲取和管理多個CacheManager。
  一個應用可以在運行期間訪問多個CacheProvider
- CacheManager，定義了創建、配置、獲取、管理多個唯一命名的Cache，
  這些Cache存在CacheManager的上下文中。一個CacheManager僅被一個CacheProvider所用有。
- Cache，一個類似Map的數據結構，並臨時存除以key為索引的值，一個cache僅被一個CacheManager所用有。
- Entity，一個儲存在Cache中的Key-value
- Expiry，每個儲存在Cache中的條目有一個定義的有效日期，一但操過這個時間，
  條目為過期的狀態，將不可以在訪問；緩存的有效日期可以通過ExpiryPolicy設置。
  
- spring boot提供了JSR-107封裝後的抽象類別，實際應用將以此使用。  
- spring從3.1開始定義了org.springframework.cache.Cache
  和org.springframework.cache.CacheManager接口來統一管理不同的緩存技術，並支持JSR-107
  Cache接口為緩存的組件規範定義，包含緩存的各種操作集合；
  Cache接口下，spring提供了各種xxxCache的實現；如RedisCache、EhCache。。。
  
重要的緩存註解 
- @Cacheable，主要針對方法配置，能夠根據方法的請求參數對其結果進行緩存
- @CacheEvict，清空緩存
- @CachePut，保證方法被調用，又希望結果被緩存
- @EnableCaching，開啟基於註解的緩存
- keyGenerator，緩存數據時key生成策略
- serialize，緩存數據時value序列化策略

#### springboot配置
- 原理參照``https://blog.csdn.net/zhoujian_Liu/article/details/86610271``
- 開啟基於註解的緩存@EnableCaching
- CacheAutoConfiguration.class自動配置，默認使用org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration，並且給容器生成一個CacheManager-ConcurrentMapCacheManager。
- 標註註解即可
  - @Cacheable，先查緩存，緩存沒有再調用查詢方法
    - CacheManager管理多的Cache，每個緩存組件有自己的名稱
    - cacheName/value，查詢結果，指定放置的緩存名稱，可以指定多個
    - key，緩存數據時的key，不指定時就是方法傳入參數的值，可以用SpEL表達式，#參數名
    - keyGenerator，key的生成器，也可以自己指定keyGenerator(需自行實現)。key/keygenerator二選一即可
    - cacheManagercacheResolver，指定緩存管理器，二選一
    - condition，指定符合條件的情況下，才使用緩存，可以用SpEL表達式，#參數名(condition="#id > 0")
    - unless，條件符合就不緩存，和condition相反(#result == null，#result是預設使用的返回值名稱)
    - sync，是否使用異步模式
    - 範例
    ```
    @Cacheable(cacheNames = {"dept"}, key="#root.methodName+'['+#id+']'")
	public Department getDepartmentById(Integer id){
		logger.info("[DepartmentService]-[service]-[id: {}]", id);
		return departmentMapper.getDeptById(id);
	}
    ```
  - @CacheEvict
    - 刪除緩存
    - 默認要刪除的key值為，參數列表。也可以指定
    - allEntries = true，會刪除該Cache內所有的緩存
    - beforeInvocation = true，緩存刪除執行，是在方法之前，還是之後。預設false，為之後；若設定為true，不管接下來的方法的執行是否有錯誤，都會被刪除掉
  - @CachePut
    - 保證SQL方法先被調用，再將結果緩存，應用場景，在更新時。
    - 要留意放入緩存的key，默認適用傳入的參數，因此若要更新到原先查詢的設定的緩存，key需要指定與查詢的key一樣。
    - 另外，因為SQL方法先被調用，在保存到緩存，因此key也可使用#result指定
    ```
    result為返回對象，在此是Department，所以key可以寫為#result.id
    也可以指定為參數列表的department key=#department.id

	@CachePut(cacheNames = {"dept"}, key="#result.id")
	public Department updateDepartment(Department department){
		logger.info("[DepartmentService]-[updateDepartment]-[Department: {}]", department);
		departmentMapper.updateDepartment(department);
		return department;
	}
    ```
  - Caching
    - 可以將上述的方式組合
  - CacheConfig
    - class註解，一次設定該類下的方法，使用緩存註解相同的部分，例如cacheNames

#### ConcurrentMapCacheManager將緩存放在hashmap(內存)
#### 整合Redis
- 使用spring-boot-starter-data-redis
- 經由StringRedisTemplate操作: key值、value為string的操作，默認key的序列化器StringRedisSerializer.UTF_8
- 經由RedisTemplate操作: key值、value都為Object的操作，默認key的序列化器JdkSerializationRedisSerializer
- 以上兩個方法，都可以使用opsXXX來操作Redis
- JdkSerializationRedisSerializer，可以自訂義改為jsonSerializationRedisSerializer；或者將對象改為json物件，再傳入
- 引入Redis會匹配為RedisAutoConfiguration，因此預設的ConcurrentMapCacheManager會取消，改為RedisCacheManager，由RedisCacheManager創建RedisCache來做為緩存組件
- 雖然引用了Redis，其默認k-v都是物件的序列化(經由註解操作時)，因為RedisCacheManager創建時會傳入的參數為RedisTemplate<object, object> redisTemplate
- 若要改為json，則需要自訂義RedisCacheManager
- 原理參考文件``https://blog.csdn.net/weixin_37910453/article/details/89520719``
- 將對象改為json物件、寫入(1.x版本)
  ```
  效果不佳
  redisTemplate.opsForValue().set(g.toJson("d1"), g.toJson(d));
  ```
  
  ```
  自行實作，根據RedisAutoConfiguration.class重新設計redisTemplate

  @Configuration
  public class RedisConfig {
    
    
    @Bean
    public RedisTemplate<Object, Department> departmentRedisTemplate(RedisConnectionFactory redisConnectionFactory)
        throws UnknownHostException {
      RedisTemplate<Object, Department> template = new RedisTemplate<>();
      template.setConnectionFactory(redisConnectionFactory);		
      GenericJackson2JsonRedisSerializer ser = new GenericJackson2JsonRedisSerializer();
      template.setDefaultSerializer(ser);
      
      return template;
    }
  ```
- 2.x版本的配置
  ```
  ```