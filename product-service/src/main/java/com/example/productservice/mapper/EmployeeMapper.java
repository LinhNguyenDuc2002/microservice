package com.example.productservice.mapper;

import com.example.productservice.dto.EmployeeDTO;
import com.example.productservice.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper extends AbstractMapper<Employee, EmployeeDTO>{
    @Override
    public Class<EmployeeDTO> getDtoClass() {
        return EmployeeDTO.class;
    }

    @Override
    public Class<Employee> getEntityClass() {
        return Employee.class;
    }
}
