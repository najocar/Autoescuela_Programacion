import com.najocar.autoescuela.model.dao.AlumnoDAO;
import com.najocar.autoescuela.model.domain.Alumno;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AlumnoDAOTest {

    // Definimos el objeto para usarlo en todos los tests
    static AlumnoDAO alumnoDAO;

    Alumno alumno1 = new Alumno("12345678p", "Juan");

    @BeforeAll
    public static void setUpClass(){
        alumnoDAO = new AlumnoDAO();
    }

    @Test
    @DisplayName("guarda el alumno y hace update si el dni es el mismo")
    void save() throws SQLException {
        assertEquals(alumno1, alumnoDAO.save(alumno1));
        alumno1.setName("pepe");
        assertEquals(alumno1, alumnoDAO.save(alumno1));
    }

    @Test
    @DisplayName("encuentra un alumno por dni")
    void findByDni() throws SQLException {
        assertEquals(alumno1, alumnoDAO.delete(alumno1));
    }

    @Test
    @DisplayName("Borra un alumno")
    void delete() throws SQLException {
        assertEquals(alumno1, alumnoDAO.delete(alumno1));
    }

    /*
    @Test
    @DisplayName("Divisiones positivos y negativos")
    void testDivisionesPositivosYNegativos() throws Exception {
        assertTrue(miCalculadora.divide(4,2) > 0);
        assertTrue(miCalculadora.divide(4,-2)< 0);
        assertTrue(miCalculadora.divide(-4,2) < 0);
        assertTrue(miCalculadora.divide(-4,-2) > 0);
    }

    @Test
    @DisplayName("División por cero causa excepción")
    void testDivisionPorCero() {
        Exception thrown = assertThrows(Exception.class, () -> {
            miCalculadora.divide(5,0);
        });

        assertEquals("El divisor es 0", thrown.getMessage());
    }

     */
}