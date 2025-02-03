package by.slava.test.koronashift.task.service.impl;

import by.slava.test.koronashift.task.model.Employee;
import by.slava.test.koronashift.task.model.Manager;
import by.slava.test.koronashift.task.service.PersonServiceInterface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PersonService implements PersonServiceInterface {
    private List<Employee> employees = new ArrayList<>();
    private List<Manager> managers = new ArrayList<>();
    private List<String> invalidData = new ArrayList<>();

    @Override
    public void processData(List<String> lines) {
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 5) {
                try {
                    if ("manager".equalsIgnoreCase(parts[0])) {
                        Manager manager = new Manager(parts[0], Integer.parseInt(parts[1]), parts[2],Double.parseDouble(parts[3]), parts[4]);
                        managers.add(manager);
                    } else if ("employee".equalsIgnoreCase(parts[0])) {
                        Employee employee = new Employee(parts[0], Integer.parseInt(parts[1]), parts[2],Double.parseDouble(parts[3]), Integer.parseInt(parts[4]));
                        if (employee.getSalary() > 0) {
                            employees.add(employee);
                        } else {
                            invalidData.add(line);
                        }
                    }
                } catch (NumberFormatException e) {
                    invalidData.add(line);
                }
            } else {
                invalidData.add(line);
            }
        }
    }

    @Override
    public void sortEmployees(String sortField, String sortOrder) {
        Comparator<Employee> comparator;

        if ("name".equalsIgnoreCase(sortField)) {
            comparator = Comparator.comparing(Employee::getName);
        } else if ("salary".equalsIgnoreCase(sortField)) {
            comparator = Comparator.comparing(Employee::getSalary);
        } else {
            throw new IllegalArgumentException("Invalid sort field: " + sortField);
        }

        if ("desc".equalsIgnoreCase(sortOrder)) {
            employees.sort(comparator.reversed());
        } else if (!"asc".equalsIgnoreCase(sortOrder)) {
            throw new IllegalArgumentException("Invalid sort order: " + sortOrder);
        }

        employees = employees.stream()
                .sorted(comparator.reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void printData(String output, String filePath) {

    }
}
