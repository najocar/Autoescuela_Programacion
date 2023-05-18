import com.najocar.autoescuela.model.dao.ClaseDAO;
import com.najocar.autoescuela.model.domain.Clase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClaseDAOTest {

    // Definimos el objeto para usarlo en todos los tests
    static ClaseDAO claseDAO;

    Clase clase1 = new Clase("prueba", 100);

    @BeforeAll
    public static void setUpClass() {
        claseDAO = new ClaseDAO();
    }

    @Test
    @DisplayName("guarda una clase")
    void save() throws SQLException {
        assertEquals(clase1, claseDAO.save(clase1));
    }

    @Test
    @DisplayName("Borra una clase")
    void deletea() throws SQLException {
        clase1 = claseDAO.findById("prueba");
        System.out.println(clase1);
        assertEquals(clase1, claseDAO.delete(clase1));
    }

}