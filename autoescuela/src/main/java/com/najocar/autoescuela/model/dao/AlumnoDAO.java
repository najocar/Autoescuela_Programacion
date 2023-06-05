package com.najocar.autoescuela.model.dao;

import com.najocar.autoescuela.model.connections.ConnectionMySQL;
import com.najocar.autoescuela.model.domain.Alumno;
import com.najocar.autoescuela.model.domain.Clase;
import com.najocar.autoescuela.model.domain.Inscripcion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AlumnoDAO implements DAO<Alumno> {
    private final static String FINDALL = "SELECT * from alumnos";
    private final static String FINBYID = "SELECT * from alumnos WHERE dni=?";
    private final static String INSERT = "INSERT INTO alumnos (dni,nombre) VALUES (?,?)";
    private final static String UPDATE = "UPDATE alumnos SET nombre=? WHERE dni=?";
    private final static String UPDATEJOIN = "UPDATE clases_alumnos SET alumnos_dni=? WHERE dni=?";
    private final static String DELETE = "DELETE FROM alumnos WHERE dni=?";

    private final static String DELETECLASES = "DELETE FROM clases_alumnos WHERE alumnos_dni=?";
    private final static String FINDBYCLASE = "SELECT * from clases_alumnos WHERE clase_id=?";

    private final static String FINDALLCLASES = "SELECT c.id, c.nombre, a.precio, a.fecha from clases_alumnos a join clases c on a.clase_id = c.id WHERE a.alumnos_dni = ? order by fecha desc";
    private final static String FINDALLRECENTCLASES = "SELECT c.id, c.nombre, a.precio, a.fecha from clases_alumnos a join clases c on a.clase_id = c.id WHERE a.alumnos_dni = ? AND a.fecha > DATE_SUB(CURDATE(), INTERVAL 1 MONTH) order by fecha desc";

    private Connection conn;

    public AlumnoDAO(Connection conn) {
        this.conn = conn;
    }

    public AlumnoDAO() {
        this.conn = ConnectionMySQL.getConnect();
    }

    /**
     * find all students
     * @return List of students
     * @throws SQLException
     */
    @Override
    public List<Alumno> findAll() throws SQLException {
        List<Alumno> result = new ArrayList();
        try (PreparedStatement pst = this.conn.prepareStatement(FINDALL)) {
            try (ResultSet res = pst.executeQuery()) {
                while (res.next()) {
                    Alumno a = new Alumno();
                    a.setDni(res.getString("dni"));
                    a.setName(res.getString("nombre"));
                    result.add(a);
                }
            }
        }
        return result;
    }

    /**
     * find student by dni
     * @param id dni of student to find
     * @return student
     * @throws SQLException
     */
    @Override
    public Alumno findById(String id) throws SQLException {
        Alumno result = null;
        try (PreparedStatement pst = this.conn.prepareStatement(FINBYID)) {
            pst.setString(1, id);
            try (ResultSet res = pst.executeQuery()) {
                if (res.next()) {
                    result = new Alumno();
                    result.setDni(res.getString("dni"));
                    result.setName(res.getString("nombre"));
                }
            }
        }
        return result;
    }

    /**
     * insert student
     * @param entity student to insert
     * @return inserted student
     * @throws SQLException
     */
    @Override
    public Alumno save(Alumno entity) throws SQLException {
        Alumno result = new Alumno();
        if (entity == null) {
            return result;
        }

        Alumno a = findById(entity.getDni());
        if (a == null) {
            //INSERT
            try (PreparedStatement pst = this.conn.prepareStatement(INSERT)) {
                pst.setString(1, entity.getDni());
                pst.setString(2, entity.getName());
                pst.executeUpdate();
                /** Clases */

            }
        } else {
            //UPDATE
            try (PreparedStatement pst = this.conn.prepareStatement(UPDATE)) {
                pst.setString(1, entity.getName());
                pst.setString(2, entity.getDni());
                pst.executeUpdate();
            }
            /** Clases */
                /*
                if (alumnoAllClases(entity.getDni()).size() > 0) {
                    try (PreparedStatement pst = this.conn.prepareStatement(UPDATEJOIN)) {
                        pst.setString(1, entity.getDni());
                        pst.setString(2, entity.getDni());
                        pst.executeUpdate();
                    }
                }
               */
        }
        result = entity;
        return result;
    }

    /**
     * Delete alumn from database
     * @param entity alumn  to remove
     * @return eliminated student
     * @throws SQLException
     */
    @Override
    public Alumno delete(Alumno entity) throws SQLException {
        Alumno result = new Alumno();
        if (entity == null) {
            return result;
        }

        Alumno a = findById(entity.getDni());
        if (a == null) {
            return result;
        }

        if (alumnoAllClases(entity.getDni()).size() > 0) {
            try (PreparedStatement pst = this.conn.prepareStatement(DELETECLASES)) {
                pst.setString(1, entity.getDni());
                pst.executeUpdate();
            }
        }
        try (PreparedStatement pst = this.conn.prepareStatement(DELETE)) {
            pst.setString(1, entity.getDni());
            pst.executeUpdate();
            result = entity;
        }

        return result;
    }

    @Override
    public void close() throws Exception {

    }

    /**
     * Find all students by class
     * @param a class to find students
     * @return List of students found in the class
     * @throws SQLException
     */
    public List<Alumno> findByClase(Clase a) throws SQLException {
        List<Alumno> result = null;
        if (a == null) {
            return result;
        }

        result = new ArrayList();
        ClaseDAO cdao = new ClaseDAO(this.conn);
        Clase _a = cdao.findById(a.getId());
        if (_a == null) {
            return result;
        }

        try (PreparedStatement pst = this.conn.prepareStatement(FINDBYCLASE)) {
            pst.setInt(1, a.getId());
            try (ResultSet res = pst.executeQuery()) {
                while (res.next()) {
                    Alumno l = new Alumno();
                    l.setDni(res.getString("dni"));
                    l.setName(res.getString("nombre"));
                    l.addClase(_a);
                    result.add(l);
                }
            }
        }
        return result;
    }

    /**
     * all students classes
     * @param dni dni of student
     * @return list of classes the student is in
     * @throws SQLException
     */
    public List<Inscripcion> alumnoAllClases(String dni) throws SQLException {
        List<Inscripcion> result = null;
        if (dni == null && findById(dni) == null) {
            return result;
        }

        result = new ArrayList();
        try (PreparedStatement pst = this.conn.prepareStatement(FINDALLCLASES)) {
            pst.setString(1, dni);
            try (ResultSet res = pst.executeQuery()) {
                while (res.next()) {
                    Inscripcion a = new Inscripcion();
                    a.setId(res.getInt("id"));
                    a.setName(res.getString("nombre"));
                    a.setPrice(res.getDouble("precio"));
                    a.setDate(res.getDate("fecha").toLocalDate());
                    result.add(a);
                }
            }
        }
        return result;
    }

    public List<Inscripcion> alumnoAllRecentClases(String dni) throws SQLException {
        List<Inscripcion> result = null;
        if (dni == null && findById(dni) == null) {
            return result;
        }

        result = new ArrayList();
        try (PreparedStatement pst = this.conn.prepareStatement(FINDALLRECENTCLASES)) {
            pst.setString(1, dni);
            try (ResultSet res = pst.executeQuery()) {
                while (res.next()) {
                    Inscripcion a = new Inscripcion();
                    a.setId(res.getInt("id"));
                    a.setName(res.getString("nombre"));
                    a.setPrice(res.getDouble("precio"));
                    a.setDate(res.getDate("fecha").toLocalDate());
                    a.setDateEnd(a.getDate().plusDays(30));

                    result.add(a);
                }
            }
        }
        return result;
    }
}
