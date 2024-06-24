package com.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String email;
    private String name;
    private List<String> sudokuSolutions;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
        this.sudokuSolutions = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public List<String> getSudokuSolutions() {
        return sudokuSolutions;
    }

    public void addSolution(String solution) {
        sudokuSolutions.add(solution);
    }
}
