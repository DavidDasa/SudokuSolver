package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users;

    public UserService() {
        try {
            users = FileUtil.loadUsers();
        } catch (IOException | ClassNotFoundException e) {
            users = new ArrayList<>();
        }
    }

    public void registerUser(String email, String name) throws IOException {
        if (findUserByEmail(email) != null) {
            throw new IllegalArgumentException("User already exists.");
        }

        users.add(new User(email, name));
        FileUtil.saveUsers(users);
    }

    public User authenticateUser(String email) {
        return findUserByEmail(email);
    }

    public void saveSolution(String email, String solution) throws IOException {
        User user = findUserByEmail(email);
        if (user != null) {
            user.addSolution(solution);
            FileUtil.saveUsers(users);
        }
    }

    private User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
}
