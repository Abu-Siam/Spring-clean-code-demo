package com.example.ffmsdemo.specification;

import com.example.ffmsdemo.model.Employee;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {
    public static Specification<Employee> filterEmployees(String name, String email, String department, String keyword) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (email != null && !email.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }

            if (department != null && !department.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("department"), department));
            }

            // Keyword search across multiple fields: name, email, and department
            if (keyword != null && !keyword.isEmpty()) {
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
                Predicate emailPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + keyword.toLowerCase() + "%");
                Predicate departmentPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("department")), "%" + keyword.toLowerCase() + "%");

                // Combine keyword search with 'OR' logic for name, email, and department
                predicates.add(criteriaBuilder.or(namePredicate, emailPredicate, departmentPredicate));
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
