package com.example.ffmsdemo.controller;

import com.example.ffmsdemo.model.Employee;
import com.example.ffmsdemo.service.EmployeeService;
import com.example.ffmsdemo.specification.EmployeeSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String listEmployees(@RequestParam(required = false) String name,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String department,
                                @RequestParam(required = false) String keyword,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "2") int size,
                                Model model) {
        Specification<Employee> spec = EmployeeSpecification.filterEmployees(name, email, department, keyword);
        Pageable pageable = PageRequest.of(page, size);


        Page<Employee> employeePage = employeeService.findAllEmployees(spec, pageable);
        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeePage.getTotalPages());
        model.addAttribute("name", name);
        model.addAttribute("email", email);
        model.addAttribute("department", department);
        model.addAttribute("keyword", keyword);
        return "employees";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee-form";
    }

    @PostMapping
    public String saveEmployee(@ModelAttribute Employee employee) {
        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "employee-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees";
    }

}
