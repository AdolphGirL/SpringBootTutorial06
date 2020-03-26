package com.reyes.tutorial;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import com.reyes.tutorial.entity.Department;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootTutorial06ApplicationTests {
//
//	@Autowired
//	private StringRedisTemplate stringRedisTemplate;
//	
//	@Autowired
//	private RedisTemplate redisTemplate;
//	
//	@Autowired
//	private RedisTemplate<Object, Department> departmentRedisTemplate;
//	
//	/**
//	 * String、List、Set、Hash、ZSet
//	 * 
//	 * stringRedisTemplate.opsForValue(): String
//	 * stringRedisTemplate.opsForList()
//	 * stringRedisTemplate.opsForSet()
//	 * stringRedisTemplate.opsForHash()
//	 * ...
//	 */
//	@Test
//	public void contextLoads() {
////		stringRedisTemplate.opsForValue().append("msg", "123");
////		System.out.println(stringRedisTemplate.opsForValue().get("msg"));
//		
////		stringRedisTemplate.opsForList().leftPush("a", "a-1");
////		stringRedisTemplate.opsForList().leftPush("a", "a-2");
//		
////		System.out.println(stringRedisTemplate.opsForList().leftPop("a"));
//		
//		Department d = new Department();
//		d.setId(1);
//		d.setName("reyes");
//		
//		departmentRedisTemplate.opsForValue().set("d-01", d);
//	}

}
