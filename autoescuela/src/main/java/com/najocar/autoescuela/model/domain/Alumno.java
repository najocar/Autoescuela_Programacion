package com.najocar.autoescuela.model.domain;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Alumno extends Persona {
    /*
    private String dni;
    private String name;
    */

    private List<Clase> clases = null;

    public Alumno(String dni, String name) {
        super(dni, name);
    }

    public Alumno() {
        super();
    }

    /*
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

    public List<Clase> getClases() {
        return clases;
    }


    public void setClases(List<Clase> clases) {
        this.clases = clases;
    }
    public void addClase(Clase clase) {
        if(this.clases==null) {
            this.clases = new ArrayList<>();
        }
        this.clases.add(clase);
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

     */
    public List<Clase> getClases() {
        return clases;
    }


    public void setClases(List<Clase> clases) {
        this.clases = clases;
    }
    public void addClase(Clase clase) {
        if(this.clases==null) {
            this.clases = new ArrayList<>();
        }
        this.clases.add(clase);
    }
}
