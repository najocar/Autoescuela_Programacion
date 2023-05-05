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
import java.util.ArrayList;
import java.util.List;

public class ClaseDAO implements DAO<Clase>{
    private final static String FINDALL ="SELECT * from clases";
    private final static String FINBYID ="SELECT * from clases WHERE id=?";
    private final static String INSERT ="INSERT INTO clases (nombre,precio) VALUES (?,?)";
    private final static String UPDATE ="UPDATE clases SET nombre=?, precio=? WHERE id=?";
    private final static String DELETE ="DELETE FROM clases WHERE id=?";

    private final static String SAVERELACION ="INSERT INTO clases_alumnoes (clase_id, alumnoes_dni) VALUES (?,?)";

    private Connection conn;
    public ClaseDAO(Connection conn) {
        this.conn = conn;
    }
    public ClaseDAO() {
        this.conn= ConnectionMySQL.getConnect();
    }
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

    @Override
    public Clase findById(String nombre) throws SQLException {
        Clase result = null;
        try(PreparedStatement pst=this.conn.prepareStatement(FINBYID)){
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

    @Override
    public Clase save(Clase entity) throws SQLException {
        Clase result = new Clase();
        if(entity!=null) {
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
                    pst.setString(1, entity.getName());
                    pst.setDouble(2, entity.getPrice());
                    pst.setInt(3, a.getId());
                    pst.executeUpdate();
                }
                /** Clases */

            }
            result=entity;
        }
        return result;
    }

    @Override
    public Clase delete(Clase entity) throws SQLException {
        Clase result = new Clase();
        if (entity != null){
            Clase a = findById(entity.getId());
            if (a != null){
                try(PreparedStatement pst=this.conn.prepareStatement(DELETE)){
                    pst.setInt(1, entity.getId());
                    pst.executeUpdate();
                }
            }
        }

        return result;
    }

    @Override
    public void close() throws Exception {

    }

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

        try(PreparedStatement pst=this.conn.prepareStatement(SAVERELACION)){
            pst.setInt(1, clase.getId());
            pst.setString(2, alumno.getDni());
            pst.executeUpdate();
        }




        return alumno.getClases();
    }
}