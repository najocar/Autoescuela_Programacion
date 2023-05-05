package com.najocar.autoescuela.model.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Clase {
    private int id;
    private String name;
    private double price;

    private List<Alumno> alumnos = null;

    public Clase(String name, double price){
        this.name = name;
        this.price = price;
    }

    public Clase(){
        this("",0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }
    public void addAlumno(Alumno alumnos) {
        if(this.alumnos==null) {
            this.alumnos = new ArrayList<>();
        }
        this.alumnos.add(alumnos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clase clase = (Clase) o;
        return id == clase.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Clase{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
