package by.slava.test.koronashift.task.service.impl;

import by.slava.test.koronashift.task.model.Employee;
import by.slava.test.koronashift.task.model.Manager;
import by.slava.test.koronashift.task.service.PersonServiceInterface;
import by.slava.test.koronashift.task.util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class PersonService implements PersonServiceInterface {
    private List<Employee> employees = new ArrayList<>();
    private final List<Manager> managers = new ArrayList<>();
    private final List<String> invalidData = new ArrayList<>();

    @Override
    public void processData(List<String> lines) {
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 5) {
                try {
                    if ("manager".equalsIgnoreCase(parts[0])) {
                        Manager manager = new Manager(parts[0], Integer.parseInt(parts[1]), parts[2],
                                Double.parseDouble(parts[3]), parts[4]);
                        managers.add(manager);
                    } else if ("employee".equalsIgnoreCase(parts[0])) {
                        Employee employee = new Employee(parts[0], Integer.parseInt(parts[1]), parts[2],
                                Double.parseDouble(parts[3]), Integer.parseInt(parts[4]));
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
        List<String> lines = getFormatedData();

        if ("file".equalsIgnoreCase(output) && filePath != null) {
            FileUtil.writeFile(filePath, lines);
        } else {
            lines.forEach(System.out::println);
        }
    }

    private List<String> getFormatedData() {
        List<String> data = new ArrayList<>();
        Map<String, List<Employee>> departments = new TreeMap<>();

        for (Manager manager : managers) {
            departments.putIfAbsent(manager.getDepartment(), new ArrayList<>());
        }

        for (Employee employee : employees) {
            Manager manager = findManagerById(employee.getManagerId());
            if (manager != null) {
                departments.get(manager.getDepartment()).add(employee);
            } else {
                invalidData.add(employee.toString());
            }
        }

        for (Map.Entry<String, List<Employee>> entry : departments.entrySet()) {
            String departmentName = entry.getKey();
            List<Employee> departmentEmployee = entry.getValue();

            data.add(departmentName);
            Manager departmentManager = findManagerByDepartment(departmentName);
            if (departmentManager != null) {
                data.add(departmentManager.toString());
            }

            for (Employee employee : departmentEmployee) {
                data.add(employee.toString());
            }

            double avgSalary = departmentEmployee.stream()
                    .mapToDouble(Employee::getSalary)
                    .average()
                    .orElse(0.0);

            data.add(departmentEmployee.size() + "," + String.format("%.2f", avgSalary));
        }

        if (!invalidData.isEmpty()) {
            data.add("Некорректные данные");
            data.addAll(invalidData);
        }

        return data;
    }

    private Manager findManagerById(int managerId) {
        for (Manager manager : managers) {
            if (manager.getId() == managerId) {
                return manager;
            }
        }
        return null;
    }

    private Manager findManagerByDepartment(String departmentName) {
        for (Manager manager : managers) {
            if (manager.getDepartment().equals(departmentName)) {
                return manager;
            }
        }
        return null;
    }
}
