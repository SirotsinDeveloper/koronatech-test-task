package by.slava.test.koronashift.task;

import by.slava.test.koronashift.task.model.Employee;
import by.slava.test.koronashift.task.model.Manager;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager("Manager", 1, "John", 5000, "HR");
        System.out.println(manager);
        Employee employee = new Employee("Employee", 101, "John", 5000, manager.getId());
        System.out.println(employee);
    }
}