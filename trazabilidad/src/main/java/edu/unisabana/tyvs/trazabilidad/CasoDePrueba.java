package edu.unisabana.tyvs.trazabilidad;

import java.util.Set;

/**
 * Una fila de la matriz: un caso de prueba y los requisitos que demuestra.
 *
 * La tecnica se guarda como texto tal cual se escribio, para poder reportar
 * el valor exacto cuando no corresponde a ninguna {@link Tecnica}.
 */
public record CasoDePrueba(String id, Set<String> requisitos, String tecnica,
                           String entrada, String resultadoEsperado) {
}
