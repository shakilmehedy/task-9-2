package com.example.demo;

import com.example.demo.Employee;
import org.springframework.web.bind.annotation.*;
import service.EmployeeValidationService;
import utils.EmployeeFileUtils;

import java.io.File;
import java.util.List;
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeValidationService validationService = new
            EmployeeValidationService();
    private final File employeeFile = new File("employee.json");
    @PostMapping("/add")
    public String addEmployee(@RequestBody Employee employee) {
        List<Employee> employees =
                EmployeeFileUtils.readEmployeesFromFile(employeeFile);
// Validate age
        if (!validationService.validateAge(employee)) {
            return "Error: Employee must be at least 18 years old.";
        }
// Validate unique passport number
        if (!validationService.validateUniquePassport(employee,
                employees)) {
            return "Error: Duplicate passport number!";
        }
// Validate unique email
        if (!validationService.validateUniqueEmail(employee,
                employees)) {
            return "Error: Duplicate email!";
        }
// Add employee and save the list
        employees.add(employee);
        EmployeeFileUtils.writeEmployeesToFile(employeeFile,
                employees);
        return "Employee added successfully!";
    }
    @GetMapping("/list")
    public List<Employee> getAllEmployees() {
        return EmployeeFileUtils.readEmployeesFromFile(employeeFile);
    }
}
