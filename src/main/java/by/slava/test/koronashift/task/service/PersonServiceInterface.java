package by.slava.test.koronashift.task.service;

import java.util.List;

public interface PersonServiceInterface {
    void processData(List<String> lines);
    void sortEmployees(String sortField, String sortOrder);
    void printData(String output, String filePath);
}
