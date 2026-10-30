package edu.unisabana.tyvs.trazabilidad;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Verifica el plan de pruebas de SU equipo.
 *
 * <pre>
 *   mvn verify -Dplan=../ejemplo-registraduria     (el ejercicio)
 *   mvn verify -Dplan=../../ruta/a/su/plan         (el proyecto)
 * </pre>
 *
 * Sin {@code -Dplan} se omite: asi {@code mvn verify} solo prueba el
 * verificador. La ruta es relativa a la carpeta {@code trazabilidad/}.
 */
@EnabledIfSystemProperty(named = "plan", matches = ".+")
class MatrizDelEquipoTest {

    @Test
    void laMatrizNoTieneHallazgos() throws Exception {
        Path carpeta = Path.of(System.getProperty("plan"));
        VerificadorDeTrazabilidad.Resultado resultado =
                new VerificadorDeTrazabilidad().verificar(Plan.desde(carpeta));

        System.out.println("\n== Plan en " + carpeta.toAbsolutePath().normalize() + "\n" + resultado.reporte());
        assertTrue(resultado.aprobado(), "\n" + resultado.reporte());
    }
}
