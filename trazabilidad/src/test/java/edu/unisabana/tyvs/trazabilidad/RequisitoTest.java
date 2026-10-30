package edu.unisabana.tyvs.trazabilidad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Los umbrales de riesgo tambien tienen valores limite: 6 es ALTO, 4 es MEDIO. */
class RequisitoTest {

    @ParameterizedTest(name = "impacto {0} x probabilidad {1} = {2}")
    @CsvSource({
            "3, 3, ALTO",
            "3, 2, ALTO",   // 6: el borde inferior de ALTO
            "2, 2, MEDIO",  // 4: el producto mas alto que no llega a ALTO
            "3, 1, MEDIO",  // 3: el borde inferior de MEDIO
            "2, 1, BAJO",
            "1, 1, BAJO"
    })
    void elNivelDependeDelProducto(int impacto, int probabilidad, NivelDeRiesgo esperado) {
        assertEquals(esperado, new Requisito("R1", "x", impacto, probabilidad).nivel());
    }

    @ParameterizedTest(name = "impacto {0}, probabilidad {1} no es un riesgo evaluado")
    @CsvSource(value = {"NULL, 2", "2, NULL", "0, 2", "4, 2", "2, -1"}, nullValues = "NULL")
    void fueraDeEscalaCuentaComoSinEvaluar(Integer impacto, Integer probabilidad) {
        Requisito r = new Requisito("R1", "x", impacto, probabilidad);

        assertFalse(r.riesgoEvaluado());
        assertNull(r.nivel());
    }
}
