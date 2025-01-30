package by.slava.test.koronashift.task.model.person;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class Person {
    protected String position;
    protected int id;
    protected String name;
    protected double salary;

    @Override
    public String toString() {
        return position + "," + id + "," + name + "," + salary;
    }
}
