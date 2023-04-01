package com.example.demo.enumeration;

public enum FunctionDESC {

    DELETE_USER_ENTITY("Delete function from string"),
    INSERT_USER_ENTITY("Insert function description"),
    EDIT_PASSWORD_USER("Edit function description");

    private final String description;

    FunctionDESC(String description) {
        this.description = description;
    }

    public String getName() {
        return description;
    }

}
