package edu.unisabana.tyvs.trazabilidad;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Los dos archivos de un plan de pruebas que el verificador lee:
 * <ul>
 *   <li>{@value #REQUISITOS}: {@code id, descripcion, impacto, probabilidad}</li>
 *   <li>{@value #MATRIZ}: {@code caso, requisitos, tecnica, entrada, resultado_esperado}</li>
 * </ul>
 * En la columna {@code requisitos} un caso puede citar varios, separados por
 * barra vertical: {@code R5|R6}.
 */
public record Plan(List<Requisito> requisitos, List<CasoDePrueba> casos) {

    public static final String REQUISITOS = "requisitos.csv";
    public static final String MATRIZ = "matriz-trazabilidad.csv";

    private static final List<String> COLUMNAS_REQUISITOS =
            List.of("id", "descripcion", "impacto", "probabilidad");
    private static final List<String> COLUMNAS_MATRIZ =
            List.of("caso", "requisitos", "tecnica", "entrada", "resultado_esperado");

    public static Plan desde(Path carpeta) throws IOException {
        return new Plan(
                leerRequisitos(LectorCsv.leer(archivo(carpeta, REQUISITOS))),
                leerCasos(LectorCsv.leer(archivo(carpeta, MATRIZ))));
    }

    private static Path archivo(Path carpeta, String nombre) {
        Path archivo = carpeta.resolve(nombre);
        if (!Files.isRegularFile(archivo)) {
            throw new IllegalArgumentException("No existe " + archivo.toAbsolutePath()
                    + ". La carpeta del plan debe tener " + REQUISITOS + " y " + MATRIZ + ".");
        }
        return archivo;
    }

    static List<Requisito> leerRequisitos(List<Map<String, String>> filas) {
        exigirColumnas(filas, COLUMNAS_REQUISITOS, REQUISITOS);
        List<Requisito> requisitos = new ArrayList<>();
        for (Map<String, String> f : filas) {
            requisitos.add(new Requisito(f.get("id"), f.get("descripcion"),
                    entero(f.get("impacto")), entero(f.get("probabilidad"))));
        }
        return requisitos;
    }

    static List<CasoDePrueba> leerCasos(List<Map<String, String>> filas) {
        exigirColumnas(filas, COLUMNAS_MATRIZ, MATRIZ);
        List<CasoDePrueba> casos = new ArrayList<>();
        for (Map<String, String> f : filas) {
            Set<String> requisitos = Arrays.stream(f.get("requisitos").split("\\|"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            casos.add(new CasoDePrueba(f.get("caso"), requisitos, f.get("tecnica"),
                    f.get("entrada"), f.get("resultado_esperado")));
        }
        return casos;
    }

    private static void exigirColumnas(List<Map<String, String>> filas, List<String> columnas, String archivo) {
        if (filas.isEmpty()) {
            return;
        }
        for (String columna : columnas) {
            if (!filas.get(0).containsKey(columna)) {
                throw new IllegalArgumentException(archivo + ": falta la columna '" + columna
                        + "'. Columnas esperadas: " + String.join(", ", columnas));
            }
        }
    }

    /** Vacio o no numerico cuenta como "sin evaluar", no como error de lectura. */
    private static Integer entero(String texto) {
        try {
            return texto == null || texto.isBlank() ? null : Integer.valueOf(texto.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
