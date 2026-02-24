package se.iths.johan.grupp_5_projektarbete.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.iths.johan.grupp_5_projektarbete.exception.EmployeeNotFoundException;
import se.iths.johan.grupp_5_projektarbete.model.Employee;
import se.iths.johan.grupp_5_projektarbete.repository.EmployeeRepository;
import se.iths.johan.grupp_5_projektarbete.validator.EmployeeValidator;

import java.util.List;

@Service
public class EmployeeService {
    private static final Logger logService = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final EmployeeValidator employeeValidator;

    public EmployeeService(EmployeeRepository repository, EmployeeValidator validator) {
        this.employeeRepository = repository;
        this.employeeValidator = validator;
    }

    //get all
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    //get one
    public Employee getEmployeeById(Long id) {
        logService.info("Getting employee by id {}", id);

        return employeeRepository.findById(id).orElseThrow(() -> {
            logService.error("Employee with id {} not found", id);
            return new EmployeeNotFoundException("Employee with id " + id + " not found");
        });
    }

    //create, validerar så att entiteten inte bryter mot några regler innan create
    public Employee createEmployee(Employee employee) {
        employeeValidator.validateEmployee(employee.getName());
        employeeValidator.validateSalary(employee.getMonthlySalary());
        employeeValidator.validateEmploymentType(employee.getEmploymentType());
        employeeValidator.validateEmploymentPercentage(employee.getEmploymentPercentage());
        employeeValidator.validateEmployeeType(employee.getEmployeeType());
        return employeeRepository.save(employee);
    }

    //update
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        logService.info("Updating employee with id {}", id);

        Employee current = employeeRepository.findById(id).orElseThrow(() -> {
            logService.warn("Employee with id {} not found", id);
            return new EmployeeNotFoundException("Employee with id " + id + " not found and can therefore not be updated");
        });

        //validering av update innan infon lagras över till current
        employeeValidator.validateEmployee(updatedEmployee.getName());
        employeeValidator.validateSalary(updatedEmployee.getMonthlySalary());
        employeeValidator.validateEmploymentType(updatedEmployee.getEmploymentType());
        employeeValidator.validateEmploymentPercentage(updatedEmployee.getEmploymentPercentage());
        employeeValidator.validateEmployeeType(updatedEmployee.getEmployeeType());

        //uppdaterar existing med den validerade infon från update employee
        current.setName(updatedEmployee.getName());
        current.setMonthlySalary(updatedEmployee.getMonthlySalary());
        current.setEmploymentType(updatedEmployee.getEmploymentType());
        current.setEmploymentPercentage(updatedEmployee.getEmploymentPercentage());
        current.setEmployeeType(updatedEmployee.getEmployeeType());

        return employeeRepository.save(current);
    }

    //delete, letar efter employee med värdeparam, hittas ej så kastas annars delete
    public void deleteEmployee(Long id) {
        logService.info("Deleting employee with id {}", id);

        Employee current = employeeRepository.findById(id).orElseThrow(() -> {
            logService.warn("Employee with id {} not found for deletion", id);
            return new EmployeeNotFoundException("Employee with id " + id + " not found and can therefore not be deleted");
        });
        employeeRepository.delete(current);
    }
}
