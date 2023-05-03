package com.najocar.autoescuela.model.domain;

import java.util.Objects;

public class Alumno {
    public String dni;
    public String name;

    public Alumno(String dni, String name) {
        this.dni = dni;
        this.name = name;
    }

    public Alumno() {
        this("","");
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alumno alumno = (Alumno) o;
        return Objects.equals(dni, alumno.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni);
    }

    @Override
    public String toString() {
        return "Alumno{" +
                "dni='" + dni + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
