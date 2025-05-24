package com.shop.repository;

import com.shop.enums.EmployeePosition;
import com.shop.model.person.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository extends BaseRepository<Employee> {

    public List<Employee> findByPosition(EmployeePosition position) {
        List<Employee> result = new ArrayList<>();
        for (Employee employee : entities.values()) {
            if (employee.getPosition() == position) {
                result.add(employee);
            }
        }
        return result;
    }

    @Override
    protected int getEntityId(Employee employee) {
        return employee.getId();
    }
} 