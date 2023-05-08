package com.najocar.autoescuela.controllers;

import com.najocar.autoescuela.model.connections.ConnectionMySQL;

public class ControlDni {
    private String dni;

    private static ControlDni _newInstance;

    private ControlDni(){
        this.dni=null;
    }

    public static ControlDni getInstance(){
        if (_newInstance== null) {
            _newInstance= new ControlDni();
        }
        return _newInstance;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
