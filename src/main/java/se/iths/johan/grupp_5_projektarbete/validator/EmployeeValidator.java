package se.iths.johan.grupp_5_projektarbete.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.iths.johan.grupp_5_projektarbete.exception.EmployeeValidationException;
import se.iths.johan.grupp_5_projektarbete.service.EmployeeService;

import java.util.List;

@Component
public class EmployeeValidator {
    private static final Logger logValidator = LoggerFactory.getLogger(EmployeeService.class);

    private static final List<String> employmentType = List.of(
            "Permanent", "Fixed term", "Part"
    );
    private static final List<String> employeeType = List.of(
            "Employee", "Consult", "Leased"
    );

    public void validateEmployee(String name) {
        if (name == null || name.isBlank()) {
            logValidator.warn("Employee name is null or blank");
            throw new EmployeeValidationException("Employee name is empty");
        }
    }

    public void validateSalary(int salary) {
        int min = 0, max = 200000;
        if (salary < min || salary > max) {
            logValidator.warn("Salary is out of range");
            throw new EmployeeValidationException("Employee monthly salary is invalid");
        }
    }

    //validate för anställningsform
    public void validateEmploymentType(String employment) {
        if (!employmentType.contains(employment)) {
            logValidator.warn("Employment type is empty");
            throw new EmployeeValidationException("There is no such employment type");
        }
    }

    public void validateEmploymentPercentage(int percentage) {
        int min = 0, max = 100;
        if (percentage < min || percentage > max) {
            logValidator.warn("Employment percentage is out of range");
            throw new EmployeeValidationException("Employment percentage must be between 0 and 100");
        }
    }

    //validering för anställnings titel
    public void validateEmployeeType(String typeOfEmployee) {
        if (!employeeType.contains(typeOfEmployee)) {
            logValidator.warn("Employee type is empty");
            throw new EmployeeValidationException("There is no such employee type");
        }
    }
}
