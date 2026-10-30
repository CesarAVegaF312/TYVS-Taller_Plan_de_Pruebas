package edu.unisabana.tyvs.trazabilidad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class LectorCsvTest {

    @Test
    void leeEncabezadoYFilasConComa() {
        List<Map<String, String>> filas = LectorCsv.leer("id,descripcion\nR1,Mayor de edad\n", "prueba.csv");

        assertEquals(1, filas.size());
        assertEquals("R1", filas.get(0).get("id"));
        assertEquals("Mayor de edad", filas.get(0).get("descripcion"));
    }

    @Test
    void aceptaPuntoYComaComoGuardaExcelEnEspanol() {
        List<Map<String, String>> filas = LectorCsv.leer("id;descripcion\nR1;Edad, en anios\n", "prueba.csv");

        assertEquals("Edad, en anios", filas.get(0).get("descripcion"));
    }

    @Test
    void ignoraLaMarcaBomYNormalizaLosEncabezados() {
        List<Map<String, String>> filas = LectorCsv.leer("﻿ ID , Descripcion \nR1,x\n", "prueba.csv");

        assertEquals("R1", filas.get(0).get("id"));
    }

    @Test
    void respetaComillasConSeparadorYComillasDobladas() {
        List<Map<String, String>> filas = LectorCsv.leer(
                "caso,entrada\nCP-01,\"edad=17, vivo, dice \"\"hola\"\"\"\n", "prueba.csv");

        assertEquals("edad=17, vivo, dice \"hola\"", filas.get(0).get("entrada"));
    }

    @Test
    void ignoraLineasVacias() {
        List<Map<String, String>> filas = LectorCsv.leer("\nid,x\n\nR1,a\n\n", "prueba.csv");

        assertEquals(1, filas.size());
    }

    @Test
    void reportaLaLineaCuandoFaltanColumnas() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> LectorCsv.leer("id,descripcion\nR1,a\nR2\n", "requisitos.csv"));

        assertTrue(e.getMessage().contains("requisitos.csv, linea 3"), e.getMessage());
    }

    @Test
    void rechazaUnaComillaSinCerrar() {
        assertThrows(IllegalArgumentException.class,
                () -> LectorCsv.leer("id,descripcion\nR1,\"sin cerrar\n", "requisitos.csv"));
    }

    @Test
    void rechazaUnArchivoVacio() {
        assertThrows(IllegalArgumentException.class, () -> LectorCsv.leer("\n\n", "vacio.csv"));
    }
}
