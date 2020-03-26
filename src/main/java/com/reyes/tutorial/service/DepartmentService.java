package com.reyes.tutorial.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.reyes.tutorial.entity.Department;
import com.reyes.tutorial.mapper.DepartmentMapper;

@Service
public class DepartmentService {
	
	private final static Logger logger = LoggerFactory.getLogger(Department.class);
	
	@Autowired
	private DepartmentMapper departmentMapper;
	
//	
//	@Cacheable(cacheNames = {"dept"}, key="#root.methodName+'['+#id+']'")
	@Cacheable(cacheNames = {"dept"}, key="#id")
	public Department getDepartmentById(Integer id){
		logger.info("[DepartmentService]-[getDepartmentById]-[id: {}]", id);
		return departmentMapper.getDeptById(id);
	}
	
//	result為返回對象，在此是Department，所以key可以寫為#result.id
//	也可以指定為參數列表的department key=#department.id
	@CachePut(cacheNames = {"dept"}, key="#result.id")
	public Department updateDepartment(Department department){
		logger.info("[DepartmentService]-[updateDepartment]-[Department: {}]", department);
		departmentMapper.updateDepartment(department);
		return department;
	}
	
	@CacheEvict(cacheNames = {"dept"}, key="#id")
	public void deleteDepartment(Integer id){
		logger.info("[DepartmentService]-[deleteDepartment]-[id: {}]", id);
//		departmentMapper.deleteDeptById(id);
	}
}
