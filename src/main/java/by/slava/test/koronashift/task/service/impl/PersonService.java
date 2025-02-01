package by.slava.test.koronashift.task.service.impl;

import by.slava.test.koronashift.task.model.Employee;
import by.slava.test.koronashift.task.model.Manager;
import by.slava.test.koronashift.task.service.PersonServiceInterface;

import java.util.List;

public class PersonService implements PersonServiceInterface {
    @Override
    public void processData(List<String> lines) {
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts[0].equals("Manager")) {
                Manager manager = new Manager(parts[0], Integer.parseInt(parts[1]), parts[2], Double.parseDouble(parts[3]), parts[4]);
            } else if (parts[0].equals("Employee")) {
                Employee employee = new Employee(parts[0], Integer.parseInt(parts[1]), parts[2], Double.parseDouble(parts[3]), Integer.parseInt(parts[4]));
            }
        }
    }

    @Override
    public void sortEmployees(String sortField, String sortOrder) {

    }

    @Override
    public void printData(String output, String filePath) {

    }
}
