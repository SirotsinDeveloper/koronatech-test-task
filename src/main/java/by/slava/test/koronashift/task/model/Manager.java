package by.slava.test.koronashift.task.model;

import by.slava.test.koronashift.task.model.person.Worker;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Manager extends Worker {
    private String department;

    public Manager(String position, int id, String name, double salary, String department) {
        super(position, id, name, salary);
        this.department = department;
    }
}
