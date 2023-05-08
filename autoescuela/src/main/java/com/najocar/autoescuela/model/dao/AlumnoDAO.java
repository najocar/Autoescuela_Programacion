package com.najocar.autoescuela.model.dao;

import com.najocar.autoescuela.model.connections.ConnectionMySQL;
import com.najocar.autoescuela.model.domain.Alumno;
import com.najocar.autoescuela.model.domain.Clase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO implements DAO<Alumno> {
    private final static String FINDALL = "SELECT * from alumnos";
    private final static String FINBYID = "SELECT * from alumnos WHERE dni=?";
    private final static String INSERT = "INSERT INTO alumnos (dni,nombre) VALUES (?,?)";
    private final static String UPDATE = "UPDATE alumnos SET nombre=? WHERE dni=?";

    private final static String UPDATEJOIN = "UPDATE clases_alumnoes SET alumnoes_dni=? WHERE dni=?";
    private final static String DELETE = "DELETE FROM alumnos WHERE dni=?";

    private final static String DELETECLASES = "DELETE FROM clases_alumnoes WHERE alumnoes_dni=?";
    private final static String FINDBYCLASE = "SELECT * from clases_alumnoes WHERE clase_id=?";

    private final static String FINDALLCLASES = "SELECT c.id, c.nombre, c.precio from clases_alumnoes a join clases c on a.clase_id = c.id WHERE alumnoes_dni = ?";

    private Connection conn;

    public AlumnoDAO(Connection conn) {
        this.conn = conn;
    }

    public AlumnoDAO() {
        this.conn = ConnectionMySQL.getConnect();
    }

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

    @Override
    public Alumno save(Alumno entity) throws SQLException {
        Alumno result = new Alumno();
        if (entity != null) {
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
        }
        return result;
    }

    @Override
    public Alumno delete(Alumno entity) throws SQLException {
        Alumno result = new Alumno();
        if (entity != null) {
            Alumno a = findById(entity.getDni());
            if (a != null) {
                if (alumnoAllClases(entity.getDni()).size() > 0) {
                    try (PreparedStatement pst = this.conn.prepareStatement(DELETECLASES)) {
                        pst.setString(1, entity.getDni());
                        pst.executeUpdate();
                    }
                }
                try (PreparedStatement pst = this.conn.prepareStatement(DELETE)) {
                    pst.setString(1, entity.getDni());
                    pst.executeUpdate();
                }
            }
        }

        return result;
    }

    @Override
    public void close() throws Exception {

    }

    public List<Alumno> findByClase(Clase a) throws SQLException {
        List<Alumno> result = null;
        if (a != null) {
            result = new ArrayList();
            ClaseDAO cdao = new ClaseDAO(this.conn);
            Clase _a = cdao.findById(a.getId());
            if (_a != null) {
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
            }
        }
        return result;
    }

    public List<Clase> alumnoAllClases(String dni) throws SQLException {
        List<Clase> result = null;
        if(dni!= null && findById(dni)!= null){
            result = new ArrayList();
            try (PreparedStatement pst = this.conn.prepareStatement(FINDALLCLASES)) {
                pst.setString(1, dni);
                try (ResultSet res = pst.executeQuery()) {
                    while (res.next()) {
                        Clase a = new Clase();
                        a.setId(res.getInt("id"));
                        a.setName(res.getString("nombre"));
                        a.setPrice(res.getDouble("precio"));
                        result.add(a);
                    }
                }
            }
        }
        return result;
    }
}
