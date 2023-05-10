package com.najocar.autoescuela.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.FileHandler;
import java.util.logging.Level;

public class Logger {
    public static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Logger.class.getName());
    public static void main(String[] args) {

        FileHandler file = null;

        try {
            file = new FileHandler("loggin.xml");
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        logger.addHandler(file);

        logger.setLevel(Level.INFO);

    }

    public static void info(String texto) {
        FileHandler file = null;

        try {
            file = new FileHandler("loggin.xml");
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        logger.addHandler(file);
        logger.setLevel(Level.INFO);

        logger.info(texto);
    }

    public static void leeArchivo(String nombreArchivo) {
        try {
            FileReader archivo = new FileReader(nombreArchivo);
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "no se puede leer el arhivo " + nombreArchivo, e);
        }
    }
}