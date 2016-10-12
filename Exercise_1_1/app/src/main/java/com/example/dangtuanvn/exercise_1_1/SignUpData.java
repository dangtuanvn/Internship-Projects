package com.example.dangtuanvn.exercise_1_1;

/**
 * Created by dangtuanvn on 10/11/16.
 */

public class SignUpData {
    private String firstName, lastName, email, phone, gender;
    private int salary;
    private boolean[] sports = new boolean[6];

    public SignUpData(String firstName, String lastName, String email, String phone, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;

        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public boolean[] getSports() {
        return sports;
    }

    public void setSports(boolean[] sports) {
        this.sports = sports;
    }
}
