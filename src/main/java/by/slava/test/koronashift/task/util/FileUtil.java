package by.slava.test.koronashift.task.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public List<String> readFile(String filePath) {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
}
