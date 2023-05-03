package com.najocar.autoescuela;

import com.najocar.autoescuela.model.dao.AlumnoDAO;
import com.najocar.autoescuela.model.domain.Alumno;

import java.sql.SQLException;
import java.util.ArrayList;

public class Test1 {
    public static void main(String[] args) throws SQLException {
        Alumno a = new Alumno("2asdf","Isabel");

        AlumnoDAO adao = new AlumnoDAO();

        ArrayList<Alumno> alumnos = (ArrayList<Alumno>) adao.findAll();
        for (Alumno alumno: alumnos) {
            System.out.println(alumno);
        }
    }
}
