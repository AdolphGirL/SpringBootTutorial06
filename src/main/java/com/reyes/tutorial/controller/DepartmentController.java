package com.reyes.tutorial.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.reyes.tutorial.entity.Department;
import com.reyes.tutorial.service.DepartmentService;

@RestController
public class DepartmentController {
	
	private final static Logger logger = LoggerFactory.getLogger(DepartmentController.class);
	
	@Autowired
	private DepartmentService departmentService;
	
	@GetMapping(value = "/dept/{id}")
	public Department findDepartmentById(@PathVariable("id") Integer id){
		logger.info("[DepartmentController]-[findDepartmentById]-[id: {}]", id);
		return departmentService.getDepartmentById(id);
	}
	
	@GetMapping(value = "/dept")
	public Department updateDepartment(Department department){
		logger.info("[DepartmentController]-[updateDepartment]-[department: {}]", department);
		return departmentService.updateDepartment(department);
	}
	
	@GetMapping(value = "/deledept/{id}")
	public String deleteDepartmentById(@PathVariable("id") Integer id){
		logger.info("[DepartmentController]-[deleteDepartmentById]-[id: {}]", id);
		departmentService.deleteDepartment(id);
		return "success";
	}
}
