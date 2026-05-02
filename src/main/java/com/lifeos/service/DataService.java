package com.lifeos.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.lifeos.model.DailyLog;
import com.lifeos.model.User;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataService {

    private static final String DATA_DIR   = "lifeos_data";
    private static final String USERS_FILE = DATA_DIR + "/users.json";

    private final Gson gson;

    public DataService() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class,
                        (JsonSerializer<LocalDate>) (src, t, ctx) -> new JsonPrimitive(src.toString()))
                .registerTypeAdapter(LocalDate.class,
                        (JsonDeserializer<LocalDate>) (json, t, ctx) -> LocalDate.parse(json.getAsString()))
                .setPrettyPrinting()
                .create();
        new File(DATA_DIR).mkdirs();
    }

    // ── Users ──────────────────────────────────────────────────
    public List<User> loadAllUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<List<User>>(){}.getType();
            List<User> users = gson.fromJson(reader, type);
            return users != null ? users : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void saveAllUsers(List<User> users) {
        try (Writer writer = new FileWriter(USERS_FILE)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public void saveUser(User user) {
        List<User> users = loadAllUsers();
        users.removeIf(u -> u.getId() != null && u.getId().equals(user.getId()));
        users.add(user);
        saveAllUsers(users);
    }

    public void deleteUser(User user) {
        List<User> users = loadAllUsers();
        users.removeIf(u -> u.getId() != null && u.getId().equals(user.getId()));
        saveAllUsers(users);
        // Also delete their logs
        File logsFile = new File(DATA_DIR + "/logs_" + user.getId() + ".json");
        if (logsFile.exists()) logsFile.delete();
    }

    // ── Logs (per user) ───────────────────────────────────────
    private String logsFile(User user) {
        return DATA_DIR + "/logs_" + user.getId() + ".json";
    }

    public void saveLogs(List<DailyLog> logs, User user) {
        try (Writer writer = new FileWriter(logsFile(user))) {
            gson.toJson(logs, writer);
        } catch (IOException e) {
            System.err.println("Error saving logs: " + e.getMessage());
        }
    }

    public List<DailyLog> loadLogs(User user) {
        File file = new File(logsFile(user));
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<List<DailyLog>>(){}.getType();
            List<DailyLog> logs = gson.fromJson(reader, type);
            return logs != null ? logs : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public DailyLog getTodayLog(List<DailyLog> logs) {
        LocalDate today = LocalDate.now();
        return logs.stream()
                .filter(l -> l.getDate().equals(today))
                .findFirst().orElse(null);
    }
}