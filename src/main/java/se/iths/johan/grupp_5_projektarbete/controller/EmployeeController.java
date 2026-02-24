package se.iths.johan.grupp_5_projektarbete.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.iths.johan.grupp_5_projektarbete.model.Employee;
import se.iths.johan.grupp_5_projektarbete.service.EmployeeService;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    private static final Logger logController = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAllEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employees";
    }

    @GetMapping("/{id}")
    public String getEmployeeById(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "employee-details";
    }

    @GetMapping("/newEmployee")
    public String getNewEmployee(Model model) {
        model.addAttribute("employee", new Employee());
        return "create-employee";
    }

    @PostMapping
    public String createEmployee(@ModelAttribute("employee") Employee employee) {
        employeeService.createEmployee(employee);
        return "redirect:/employees";
    }

    @PutMapping("/{id}/update")
    public String updateEmployee(@PathVariable Long id, @ModelAttribute("employee") Employee employee) {
        Employee employee1 = employeeService.updateEmployee(id, employee);
        return "redirect:/employees";
    }

    @GetMapping("/{id}/edit")
    public String getEditEmployee(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "edit-employee";
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees";
    }


}
