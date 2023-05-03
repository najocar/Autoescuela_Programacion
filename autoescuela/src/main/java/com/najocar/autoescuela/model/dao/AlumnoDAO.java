package com.najocar.autoescuela.model.dao;

import com.najocar.autoescuela.model.connections.ConnectionMySQL;
import com.najocar.autoescuela.model.domain.Alumno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO implements DAO<Alumno>{
    private final static String FINDALL ="SELECT * from alumnos";
    private final static String FINBYID ="SELECT * from alumnos WHERE dni=?";

    private Connection conn;
    public AlumnoDAO(Connection conn) {
        this.conn = conn;
    }
    public AlumnoDAO() {
        this.conn= ConnectionMySQL.getConnect();
    }

    @Override
    public List<Alumno> findAll() throws SQLException {
        List<Alumno> result = new ArrayList();
        try(PreparedStatement pst=this.conn.prepareStatement(FINDALL)){
            try(ResultSet res = pst.executeQuery()){
                while(res.next()) {
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
        try(PreparedStatement pst=this.conn.prepareStatement(FINBYID)){
            pst.setString(1, id);
            try(ResultSet res = pst.executeQuery()){
                if(res.next()) {
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
        return null;
    }

    @Override
    public void delete(Alumno entity) throws SQLException {

    }

    @Override
    public void close() throws Exception {

    }
}
