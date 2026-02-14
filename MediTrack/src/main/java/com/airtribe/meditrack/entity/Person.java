package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.util.Validator;

/**
 * Base class representing a Person in the MediTrack system.
 * Demonstrates inheritance and encapsulation.
 */
public abstract class Person {
    private String id;
    private String name;
    private int age;
    private String email;
    private String phone;

    // Static block for initialization
    static {
        System.out.println("Person class loaded - static initialization block executed");
    }

    /**
     * Constructor with validation
     */
    public Person(String id, String name, int age, String email, String phone) {
        this.setId(id);
        this.setName(name);
        this.setAge(age);
        this.setEmail(email);
        this.setPhone(phone);
    }

    // Getters and Setters with validation (Encapsulation)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (Validator.isValidId(id)) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("Invalid ID format");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (Validator.isValidName(name)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Invalid name");
        }
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (Validator.isValidAge(age)) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Invalid age");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (Validator.isValidEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (Validator.isValidPhone(phone)) {
            this.phone = phone;
        } else {
            throw new IllegalArgumentException("Invalid phone");
        }
    }

    @Override
    public String toString() {
        return String.format("Person{id='%s', name='%s', age=%d, email='%s', phone='%s'}", 
            id, name, age, email, phone);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return id != null && id.equals(person.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

