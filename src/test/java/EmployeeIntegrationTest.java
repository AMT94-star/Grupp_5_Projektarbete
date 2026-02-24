package se.iths.johan.grupp_5_projektarbete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import se.iths.johan.grupp_5_projektarbete.model.Employee;
import se.iths.johan.grupp_5_projektarbete.repository.EmployeeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void cleanup() {
        employeeRepository.deleteAll();
    }

    //skapa employee och verifiera sparning
    @Test
    void createEmployeeSavedTest() throws Exception {
        mockMvc.perform(post("/employees/")
                        .param("name", "Aslihan Taskin")
                        .param("monthlySalary", "35000")
                        .param("employmentType", "Fixed term")
                        .param("employmentPercentage", "35")
                        .param("employeeType", "Consult"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"));

        assertThat(employeeRepository.findAll()).hasSize(1);
    }


    @Test
    void getEmployeeByIdTest() throws Exception {
        Employee employee = new Employee();
        employee.setName("Aslihan Taskin");
        employee.setEmployeeType("Consult");
        employee.setMonthlySalary(45000);
        employee.setEmploymentType("Fixed term");
        employee.setEmploymentPercentage(100);

        Employee savedEmployee = employeeRepository.save(employee);

        //hämtar dvs gör get på employees id
        mockMvc.perform(get("/employees/" + savedEmployee.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-details"))
                .andExpect(model().attributeExists("employee"));
    }

    @Test
    public void getAllEmployeesTest() throws Exception {
        Employee employee1 = new Employee();
        employee1.setName("Aslihan Taskin");
        employee1.setEmployeeType("Consult");
        employee1.setMonthlySalary(45000);
        employee1.setEmploymentType("Fixed term");
        employee1.setEmploymentPercentage(100);

        Employee employee2 = new Employee();
        employee2.setName("Sandra Korall");
        employee2.setEmployeeType("Employee");
        employee2.setMonthlySalary(43000);
        employee2.setEmploymentType("Permanent");
        employee2.setEmploymentPercentage(100);

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        //gör get av employees
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("employees",
                        org.hamcrest.Matchers.hasSize(2)));
    }
}
