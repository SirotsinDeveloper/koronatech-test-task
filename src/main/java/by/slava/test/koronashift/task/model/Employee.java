package by.slava.test.koronashift.task.model;

import by.slava.test.koronashift.task.model.person.Person;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee extends Person{
    private int managerId;

    public Employee(String position, int id, String name, double salary, int managerId) {
        super(position, id, name, salary);
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return super.toString() + "," + managerId;
    }
}
