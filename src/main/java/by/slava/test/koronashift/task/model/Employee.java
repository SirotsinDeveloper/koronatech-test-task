package by.slava.test.koronashift.task.model;

import by.slava.test.koronashift.task.model.person.Worker;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee extends Worker {
    private int managerId;

    public Employee(String position, int id, String name, double salary, int managerId) {
        super(position, id, name, salary);
        this.managerId = managerId;
    }

}
