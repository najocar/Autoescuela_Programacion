package com.najocar.autoescuela;

import com.najocar.autoescuela.model.dao.AlumnoDAO;
import com.najocar.autoescuela.model.dao.ClaseDAO;
import com.najocar.autoescuela.model.domain.Alumno;
import com.najocar.autoescuela.model.domain.Clase;

import java.sql.SQLException;
import java.util.ArrayList;

public class TestRelacion {
    public static void main(String[] args) throws SQLException {
        Alumno a = new Alumno("2asdf","Alvaro");

        Clase c = new Clase("camión", 70);

        AlumnoDAO adao = new AlumnoDAO();
        ClaseDAO cdao = new ClaseDAO();
        //cdao.save(c);
        //adao.save(a);

        cdao.insertAlumno(9, "12345p");

    }
}
