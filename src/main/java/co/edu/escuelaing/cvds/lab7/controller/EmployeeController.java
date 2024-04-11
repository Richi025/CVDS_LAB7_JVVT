package co.edu.escuelaing.cvds.lab7.controller;

import co.edu.escuelaing.cvds.lab7.model.Employee;
import co.edu.escuelaing.cvds.lab7.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/toList")
    public String toList(Model model) {
        List<Employee> employees=employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "toIndex";
    }


    @GetMapping("/create")
    public String createEmployee(Model model) {
        model.addAttribute("employees", new Employee());
        return "create";
    }

    @PostMapping("/save")
    public String save(String idEmployee, String firstName, String lastName, String role, double salary) {
        employeeService.addEmployee(new Employee(idEmployee, firstName, lastName, role, salary));
        return "redirect:/employee/toList";
    }

    @GetMapping("/update/{id}")
    public String updateEmployee(@PathVariable String id, Model model) {
        Employee employee = employeeService.getEmployee(id);
        if (employee != null) {
            model.addAttribute("employee", employee);
            return "update";
        } else {
            // Manejo de error si no se encuentra el empleado
            return "error";
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable String id, String firstName, String lastName, String role, double salary) {
        Employee employee = employeeService.getEmployee(id);
        if (employee != null) {
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setRole(role);
            employee.setSalary(salary);
            employeeService.updateEmployee(employee);
            return "redirect:/employee/api";
        } else {
            // Manejo de error si no se encuentra el empleado
            return "error"; // Puedes redirigir a una página de error
        }
    }


    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable String id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employee/toList";
    }

    @GetMapping("/api/employees")
    @ResponseBody
    public List<Employee> exampleApiEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping("/api/employees")
    @ResponseBody
    public List<Employee> exampleApiEmployees(@RequestBody Employee employee) {
        employeeService.addEmployee(employee);
        return employeeService.getAllEmployees();
    }
}