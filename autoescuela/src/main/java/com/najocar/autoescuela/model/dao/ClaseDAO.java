package com.najocar.autoescuela.model.dao;

import com.najocar.autoescuela.model.connections.ConnectionMySQL;
import com.najocar.autoescuela.model.domain.*;
import com.najocar.autoescuela.model.domain.Clase;
import com.najocar.autoescuela.model.domain.Clase;
import com.najocar.autoescuela.model.domain.Clase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClaseDAO implements DAO<Clase>{
    private final static String FINDALL ="SELECT * from clases";
    private final static String FINBYID ="SELECT * from clases WHERE id=?";
    private final static String FINBYNAME ="SELECT * from clases WHERE nombre=?";
    private final static String INSERT ="INSERT INTO clases (nombre,precio) VALUES (?,?)";
    private final static String UPDATE ="UPDATE clases SET precio=? WHERE id=?";
    private final static String DELETE ="DELETE FROM clases WHERE id=?";

    private final static String SAVERELACION ="INSERT INTO clases_alumnos (clase_id, alumnos_dni, fecha, precio) VALUES (?,?,?,?)";
    private final static String DELETERELACION ="DELETE FROM clases_alumnos WHERE clase_id = ? and alumnos_dni = ? and fecha = ?";

    private Connection conn;
    public ClaseDAO(Connection conn) {
        this.conn = conn;
    }
    public ClaseDAO() {
        this.conn= ConnectionMySQL.getConnect();
    }

    /**
     * find all classes
     * @return list of classes
     * @throws SQLException
     */
    @Override
    public List findAll() throws SQLException {
        List<Clase> result = new ArrayList();
        try(PreparedStatement pst=this.conn.prepareStatement(FINDALL)){
            try(ResultSet res = pst.executeQuery()){
                while(res.next()) {
                    Clase a = new Clase();
                    a.setId(res.getInt("id"));
                    a.setName(res.getString("nombre"));
                    a.setPrice(res.getDouble("precio"));
                    result.add(a);
                }
            }
        }
        return result;
    }

    /**
     * find class by name
     * @param nombre name of the class
     * @return class or null if not found
     * @throws SQLException
     */
    @Override
    public Clase findById(String nombre) throws SQLException {
        Clase result = null;
        try(PreparedStatement pst=this.conn.prepareStatement(FINBYNAME)){
            pst.setString(1, nombre);
            try(ResultSet res = pst.executeQuery()){
                if(res.next()) {
                    result = new Clase();
                    result.setId(res.getInt("id"));
                    result.setName(res.getString("nombre"));
                    result.setPrice(res.getDouble("precio"));
                }
            }
        }
        return result;
    }

    /**
     * find class by id
     * @param id id of the class
     * @return class or null if not found
     * @throws SQLException
     */
    public Clase findById(int id) throws SQLException {
        Clase result = null;
        try(PreparedStatement pst=this.conn.prepareStatement(FINBYID)){
            pst.setInt(1, id);
            try(ResultSet res = pst.executeQuery()){
                if(res.next()) {
                    result = new Clase();
                    result.setId(res.getInt("id"));
                    result.setName(res.getString("nombre"));
                    result.setPrice(res.getDouble("precio"));
                }
            }
        }
        return result;
    }

    /**
     * save a clase
     * @param entity clase to save
     * @return clase saved
     * @throws SQLException
     */
    @Override
    public Clase save(Clase entity) throws SQLException {
        Clase result = new Clase();
        if(entity==null) {
            return result;
        }
        Clase a = findById(entity.getName());
        if(a == null) {
            //INSERT
            try(PreparedStatement pst=this.conn.prepareStatement(INSERT)){
                pst.setString(1, entity.getName());
                pst.setDouble(2, entity.getPrice());
                pst.executeUpdate();
                /** Clases */

            }
        }else {
            //UPDATE
            try(PreparedStatement pst=this.conn.prepareStatement(UPDATE)){
                pst.setDouble(1, entity.getPrice());
                pst.setInt(2, a.getId());
                pst.executeUpdate();
            }
            /** Clases */

        }
        result=entity;
        return result;
    }

    /**
     * delete a clase
     * @param entity clase to delete
     * @return clase deleted
     * @throws SQLException
     */
    @Override
    public Clase delete(Clase entity) throws SQLException {
        Clase result = new Clase();
        if (entity == null){
            return result;
        }
        Clase a = findById(entity.getId());
        if (a == null){
            return result;
        }
        try(PreparedStatement pst=this.conn.prepareStatement(DELETE)){
            pst.setInt(1, entity.getId());
            pst.executeUpdate();
            result = entity;
        }
        return result;
    }

    @Override
    public void close() throws Exception {

    }

    /**
     * add student to the class
     * @param id id of the class
     * @param dni dni of the student
     * @return list of classes that the student is enrolled in
     */
    public List<Clase> insertAlumno(int id, String dni) throws SQLException {
        AlumnoDAO adao = new AlumnoDAO(this.conn);
        Alumno alumno = adao.findById(dni);
        Clase clase = findById(id);
        //List<Alumno> alumnosAntiguos = adao.findByClase(clase);
        //List<Clase> clasesAntiguos = alumno.getClases();

        clase.addAlumno(alumno);
        save(clase);

        alumno.addClase(clase);
        adao.save(alumno);
        //Date fecha = new Date();

        try(PreparedStatement pst=this.conn.prepareStatement(SAVERELACION)){
            pst.setInt(1, clase.getId());
            pst.setString(2, alumno.getDni());
            pst.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            pst.setDouble(4, clase.getPrice());
            pst.executeUpdate();
        }
        return alumno.getClases();
    }

    /**
     * remove student to the class
     * @param id id of the class
     * @param dni dni of the student
     * @return list of classes that the student is enrolled in
     * @throws SQLException
     */
    public List<Clase> removeAlumno(int id, String dni, Date fecha) throws SQLException {
        AlumnoDAO adao = new AlumnoDAO(this.conn);
        Alumno alumno = adao.findById(dni);
        Clase clase = findById(id);

        try(PreparedStatement pst=this.conn.prepareStatement(DELETERELACION)){
            pst.setInt(1, clase.getId());
            pst.setString(2, alumno.getDni());
            pst.setDate(3, (java.sql.Date) fecha);
            pst.executeUpdate();
        }
        return alumno.getClases();
    }
}
