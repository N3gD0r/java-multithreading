package david.training.employees;

import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
	private final List<Employee> employees;

	public EmployeeService() {
		employees = new ArrayList<>();
		employees.add(new Employee(1L, 1000, "Juanito"));
		employees.add(new Employee(2L, 500, "Chavito"));
		employees.add(new Employee(3L, 1500, "Carlitos"));
		employees.add(new Employee(4L, 2000, "Chuyito"));
		employees.add(new Employee(5L, 1800, "Josesito"));
		employees.add(new Employee(6L, 3500, "Mario"));
		employees.add(new Employee(7L, 4000, "El juan"));
		employees.add(new Employee(8L, 5000, "Oscar"));
	}

	public List<Employee> hiredEmployees() {
		return employees;
	}

	public float getSalary(long hiredEmployeeId) {
		return employees.stream()
				.filter(employee -> employee.getId() == hiredEmployeeId)
				.findAny()
				.orElseThrow()
				.getSalary();
	}
}
