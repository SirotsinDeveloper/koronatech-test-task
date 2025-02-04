package by.slava.test.koronashift.task.app;

import by.slava.test.koronashift.task.service.impl.PersonService;
import by.slava.test.koronashift.task.util.FileUtil;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Ошибка: параметры командной строки не заданы.");
            return;
        }

        String filePath = null;
        String sortField = null;
        String sortOrder = null;
        String output = "console";
        String outputPath = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--file":
                case "-f":
                    if (i + 1 < args.length) {
                        filePath = args[++i];
                    } else {
                        System.out.println("Ошибка: не указан путь к входному файлу.");
                        return;
                    }
                    break;
                case "--sort":
                case "-s":
                    if (i + 1 < args.length) {
                        sortField = args[++i];
                    } else {
                        System.out.println("Ошибка: не указан параметр сортировки.");
                        return;
                    }
                    break;
                case "--order":
                    if (i + 1 < args.length) {
                        sortOrder = args[++i];
                    } else {
                        System.out.println("Ошибка: не указан порядок сортировки.");
                        return;
                    }
                    break;
                case "--output":
                    if (i + 1 < args.length) {
                        output = args[++i];
                    } else {
                        System.out.println("Ошибка: не указан параметр вывода.");
                        return;
                    }
                    break;
                case "--path":
                    if (i + 1 < args.length) {
                        outputPath = args[++i];
                    } else {
                        System.out.println("Ошибка: не указан путь к выходному файлу.");
                        return;
                    }
                    break;
                default:
                    System.out.println("Ошибка: неизвестный параметр " + args[i]);
                    return;
            }
        }

        if (filePath == null) {
            System.out.println("Ошибка: не указан путь к входному файлу.");
            return;
        }

        if (output.equals("file") && outputPath == null) {
            System.out.println("Ошибка: не указан путь к выходному файлу.");
            return;
        }

        if (sortField != null && sortOrder == null) {
            System.out.println("Ошибка: указан параметр сортировки, но не указан порядок сортировки.");
            return;
        }

        PersonService dataProcessor = new PersonService();
        List<String> lines = FileUtil.readFile(filePath);
        dataProcessor.processData(lines);

        if (sortField != null) {
            dataProcessor.sortEmployees(sortField, sortOrder);
        }

        dataProcessor.printData(output, outputPath);
    }
}
