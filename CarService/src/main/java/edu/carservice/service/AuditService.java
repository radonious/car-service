package edu.carservice.service;

import edu.carservice.model.User;
import edu.carservice.util.LogType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AuditService {
    private static final Path path = Path.of("log.txt").toAbsolutePath();
    private static final ArrayList<String> logs = new ArrayList<>();

    public static void addLog(LogType type, User user, String message) {
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        logs.add(String.format("[%s] [%s] - %s - %s\n", timeStamp, type, user.getName(), message));
    }

    public void printLogs() {
        for (String log : logs) {
            System.out.println(log);
        }
    }

    public void display(ArrayList<String> logs) {
        for (String log : logs) {
            System.out.println(log);
        }
    }

    public ArrayList<String> logsByDate(String date) throws IOException {
        ArrayList<String> logs = new ArrayList<>();
        for (String line : Files.readAllLines(path)) if (line.contains(date)) logs.add(line);
        return logs;
    }

    public ArrayList<String> logsByUser(String username) throws IOException {
        ArrayList<String> logs = new ArrayList<>();
        for (String line : Files.readAllLines(path)) if (line.contains(username)) logs.add(line);
        return logs;
    }

    public ArrayList<String> logsByAction(String action) throws IOException {
        ArrayList<String> logs = new ArrayList<>();
        for (String line : Files.readAllLines(path)) if (line.contains(action)) logs.add(line);
        return logs;
    }

    public void save() throws IOException {
        for (String log : logs) {
            Files.write(path, log.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }

    }
}
