package com.example.orderservice.mapper;

import com.example.orderservice.dto.EmployeeDTO;
import com.example.orderservice.entity.Employee;
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
