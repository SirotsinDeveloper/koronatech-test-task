package by.slava.test.koronashift.task.service.impl;

import by.slava.test.koronashift.task.model.Employee;
import by.slava.test.koronashift.task.model.Manager;
import by.slava.test.koronashift.task.service.PersonServiceInterface;
import by.slava.test.koronashift.task.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class PersonService implements PersonServiceInterface {
    private final List<Manager> managers = new ArrayList<>();
    private final List<Employee> employees = new ArrayList<>();
    private final List<String> invalidData = new ArrayList<>();

    @Override
    public void processData(List<String> lines) {
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 5) {
                try {
                    if ("Manager".equalsIgnoreCase(parts[0])) {
                        Manager manager = new Manager(parts[0], Integer.parseInt(parts[1].trim()), parts[2].trim(),
                                Double.parseDouble(parts[3].trim()), parts[4].trim());
                        managers.add(manager);
                    } else if ("Employee".equalsIgnoreCase(parts[0])) {
                        double salary = parts[3].trim().isEmpty() ? 0.0 : Double.parseDouble(parts[3].trim());
                        Employee employee = new Employee(parts[0], Integer.parseInt(parts[1].trim()), parts[2].trim(),
                                salary, Integer.parseInt(parts[4].trim()));
                        if (salary >= 0) {
                            employees.add(employee);
                        } else {
                            invalidData.add(line);
                        }
                    } else {
                        invalidData.add(line);
                    }
                } catch (NumberFormatException e) {
                    invalidData.add(line);
                }
            } else {
                invalidData.add(line);
            }
        }
    }

    private void sortEmployeesManually(List<Employee> employees, String sortField, String sortOrder) {
        for (int i = 0; i < employees.size() - 1; i++) {
            for (int j = 0; j < employees.size() - i - 1; j++) {
                boolean shouldSwap = false;

                if ("name".equalsIgnoreCase(sortField)) {
                    if ("asc".equalsIgnoreCase(sortOrder) && employees.get(j)
                            .getName()
                            .compareTo(employees.get(j + 1)
                                    .getName()) > 0) {
                        shouldSwap = true;
                    } else if ("desc".equalsIgnoreCase(sortOrder) && employees.get(j)
                            .getName()
                            .compareTo(employees.get(j + 1).getName()) < 0) {
                        shouldSwap = true;
                    }
                } else if ("salary".equalsIgnoreCase(sortField)) {
                    if ("asc".equalsIgnoreCase(sortOrder) && employees.get(j).getSalary() > employees.get(j + 1).getSalary()) {
                        shouldSwap = true;
                    } else if ("desc".equalsIgnoreCase(sortOrder) && employees.get(j).getSalary() < employees.get(j + 1).getSalary()) {
                        shouldSwap = true;
                    }
                } else {
                    throw new IllegalArgumentException("Invalid sort field: " + sortField);
                }

                if (shouldSwap) {
                    Employee temp = employees.get(j);
                    employees.set(j, employees.get(j + 1));
                    employees.set(j + 1, temp);
                }
            }
        }
    }

    @Override
    public void sortEmployees(String sortField, String sortOrder) {
        sortEmployeesManually(employees, sortField, sortOrder);
    }

    @Override
    public void printData(String output, String filePath) {
        List<String> lines = formatData();

        if ("file".equalsIgnoreCase(output) && filePath != null) {
            FileUtil.writeFile(filePath, lines);
        } else {
            lines.forEach(System.out::println);
        }
    }

    private List<String> formatData() {
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
            List<Double> salaries = new ArrayList<>();
            List<Employee> departmentEmployee = entry.getValue();

            data.add(departmentName);
            Manager departmentManager = findManagerByDepartment(departmentName);
            if (departmentManager != null) {
                data.add(departmentManager.toString());
                salaries.add(departmentManager.getSalary());
            }

            for (Employee employee : departmentEmployee) {
                data.add(employee.toString());
                salaries.add(employee.getSalary());
            }

            double avgSalary = 0.0;

            for (Double salary : salaries) {
                avgSalary += salary;
            }

            avgSalary /= salaries.size();

            data.add((departmentEmployee.size() + 1) + ", " + String.format(Locale.US, "%.1f", avgSalary));
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
