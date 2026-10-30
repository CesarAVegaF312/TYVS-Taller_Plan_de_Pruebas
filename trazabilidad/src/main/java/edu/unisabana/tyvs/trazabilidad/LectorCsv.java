package edu.unisabana.tyvs.trazabilidad;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Lector de CSV minimo, sin dependencias.
 *
 * Acepta lo que de verdad produce una hoja de calculo al "Guardar como CSV":
 * <ul>
 *   <li>Separador coma o punto y coma. Excel en espanol guarda con punto y
 *       coma; se detecta mirando el encabezado.</li>
 *   <li>Marca BOM al inicio (Excel la agrega en "CSV UTF-8").</li>
 *   <li>Campos entre comillas dobles, con separadores o comillas dobladas
 *       dentro ("una ""cita"", con coma").</li>
 *   <li>Lineas vacias, que se ignoran.</li>
 * </ul>
 *
 * Cada fila se devuelve como un mapa encabezado -> valor, con los
 * encabezados en minuscula y sin espacios alrededor.
 */
public final class LectorCsv {

    private LectorCsv() {
    }

    public static List<Map<String, String>> leer(Path archivo) throws IOException {
        return leer(Files.readString(archivo, StandardCharsets.UTF_8), archivo.getFileName().toString());
    }

    static List<Map<String, String>> leer(String contenido, String origen) {
        if (contenido.startsWith("﻿")) {
            contenido = contenido.substring(1);
        }
        List<String> lineas = contenido.lines().toList();
        int primera = 0;
        while (primera < lineas.size() && lineas.get(primera).isBlank()) {
            primera++;
        }
        if (primera == lineas.size()) {
            throw new IllegalArgumentException(origen + ": el archivo esta vacio, falta el encabezado");
        }
        char separador = detectarSeparador(lineas.get(primera));
        List<String> encabezado = new ArrayList<>();
        for (String campo : partir(lineas.get(primera), separador, origen, primera + 1)) {
            encabezado.add(campo.trim().toLowerCase(Locale.ROOT));
        }

        List<Map<String, String>> filas = new ArrayList<>();
        for (int i = primera + 1; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            if (linea.isBlank()) {
                continue;
            }
            List<String> campos = partir(linea, separador, origen, i + 1);
            if (campos.size() != encabezado.size()) {
                throw new IllegalArgumentException(String.format(Locale.ROOT,
                        "%s, linea %d: se esperaban %d columnas y hay %d. "
                        + "Si un texto lleva el separador, escribalo entre comillas dobles.",
                        origen, i + 1, encabezado.size(), campos.size()));
            }
            Map<String, String> fila = new LinkedHashMap<>();
            for (int c = 0; c < encabezado.size(); c++) {
                fila.put(encabezado.get(c), campos.get(c).trim());
            }
            filas.add(fila);
        }
        return filas;
    }

    /** El separador es el que aparece mas veces en el encabezado, fuera de comillas. */
    private static char detectarSeparador(String encabezado) {
        int comas = 0;
        int puntoYComa = 0;
        boolean enComillas = false;
        for (char ch : encabezado.toCharArray()) {
            if (ch == '"') {
                enComillas = !enComillas;
            } else if (!enComillas && ch == ',') {
                comas++;
            } else if (!enComillas && ch == ';') {
                puntoYComa++;
            }
        }
        return puntoYComa > comas ? ';' : ',';
    }

    private static List<String> partir(String linea, char separador, String origen, int numero) {
        List<String> campos = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean enComillas = false;
        for (int i = 0; i < linea.length(); i++) {
            char ch = linea.charAt(i);
            if (enComillas) {
                if (ch == '"' && i + 1 < linea.length() && linea.charAt(i + 1) == '"') {
                    actual.append('"');
                    i++;
                } else if (ch == '"') {
                    enComillas = false;
                } else {
                    actual.append(ch);
                }
            } else if (ch == '"') {
                enComillas = true;
            } else if (ch == separador) {
                campos.add(actual.toString());
                actual.setLength(0);
            } else {
                actual.append(ch);
            }
        }
        if (enComillas) {
            throw new IllegalArgumentException(origen + ", linea " + numero + ": hay una comilla doble sin cerrar");
        }
        campos.add(actual.toString());
        return campos;
    }
}
