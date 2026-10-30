package edu.unisabana.tyvs.trazabilidad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TecnicaTest {

    @ParameterizedTest
    @ValueSource(strings = {"VALOR_LIMITE", "valor limite", "Valor-Límite", "  valor   límite "})
    void toleraMinusculasTildesEspaciosYGuiones(String texto) {
        assertEquals(Optional.of(Tecnica.VALOR_LIMITE), Tecnica.desde(texto));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "limites", "BVA"})
    void rechazaLoQueNoEsUnaTecnicaDelCurso(String texto) {
        assertTrue(Tecnica.desde(texto).isEmpty());
    }

    @Test
    void nuloNoEsUnaTecnica() {
        assertTrue(Tecnica.desde(null).isEmpty());
    }
}
