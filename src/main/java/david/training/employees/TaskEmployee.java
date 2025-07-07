package david.training.employees;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TaskEmployee {
	public static void solve() {
		EmployeeService service = new EmployeeService();

		CompletableFuture<List<Employee>> employeesFuture = CompletableFuture.supplyAsync(service::hiredEmployees);

		CompletableFuture<List<Employee>> employeesWithSalaryFuture = employeesFuture.thenCompose(employees -> {

			List<CompletableFuture<Employee>> futures = employees.stream()
					.map(employee -> CompletableFuture.supplyAsync(() -> service.getSalary(employee.getId()))
							.thenApply(salary -> {
								employee.setSalary((salary + 100) * 3);
								return employee;
							}))
					.toList();

			CompletableFuture<Void> all = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

			return all.thenApply(v -> futures.stream()
					.map(CompletableFuture::join)
					.collect(Collectors.toList()));
		});

		employeesWithSalaryFuture.thenAccept(list -> list.forEach(System.out::println));
	}
}
