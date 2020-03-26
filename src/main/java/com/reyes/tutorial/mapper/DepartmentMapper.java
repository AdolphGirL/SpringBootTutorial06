package com.reyes.tutorial.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.reyes.tutorial.entity.Department;

@Mapper
public interface DepartmentMapper {
	
//	測試，故意亂用一個變數名xx
	@Select("select * from department where id = #{xx}")
	public Department getDeptById(Integer id);
	
	@Delete("select * from department where id = #{xx}")
	public int deleteDeptById(Integer id);
	
//	#{name}解析物件名，會自動將原先寫入後新增的主鍵的值，綁定回物件department
	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into department (name) values (#{name})")
	public int insertDepartment(Department department);
	
	@Update("update department set name = #{name} where id = #{id}")
	public int updateDepartment(Department department);
	

}
