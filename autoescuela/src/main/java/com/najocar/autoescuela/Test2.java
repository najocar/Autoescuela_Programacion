package com.najocar.autoescuela;

import com.najocar.autoescuela.model.dao.AlumnoDAO;
import com.najocar.autoescuela.model.dao.ClaseDAO;
import com.najocar.autoescuela.model.domain.Alumno;
import com.najocar.autoescuela.model.domain.Clase;
import com.najocar.autoescuela.utils.Logger;
import javafx.scene.paint.Color;

import java.sql.SQLException;

public class Test2 {
    public static void main(String[] args) throws SQLException {
        Alumno a = new Alumno("2asdf","Isabel");

        Clase c = new Clase("camión", 70);

        AlumnoDAO adao = new AlumnoDAO();
        ClaseDAO cdao = new ClaseDAO();
        //cdao.save(c);
        //adao.delete(a);
        //System.out.println(adao.findById(a.getDni()));

        /*
        ArrayList<Alumno> alumnos = (ArrayList<Alumno>) adao.findAll();
        for (Alumno alumno: alumnos) {
            System.out.println(alumno);
        }

        ArrayList<Clase> clases = (ArrayList<Clase>) cdao.findAll();
        for (Clase clase: clases) {
            System.out.println(clase);
        }

         */

        //System.out.println(cdao.findById(1));
        //System.out.println(adao.findById("12345p"));
        //System.out.println(adao.alumnoAllClases("12345p").size());
        //adao.save(a);
        //Logger.logger.info("hola");

        try {
            int value = Integer.parseInt("");
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }
}
