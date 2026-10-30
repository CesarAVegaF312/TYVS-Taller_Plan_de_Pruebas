package edu.unisabana.tyvs.trazabilidad;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Optional;

/**
 * Tecnicas de diseno de casos que acepta la matriz.
 *
 * Se escriben en la columna {@code tecnica} con el nombre de la constante.
 * El lector tolera minusculas, tildes, espacios y guiones:
 * "valor limite", "Valor-Límite" y "VALOR_LIMITE" son lo mismo.
 */
public enum Tecnica {
    CLASE_EQUIVALENCIA,
    VALOR_LIMITE,
    TABLA_DECISION,
    TRANSICION_ESTADOS,
    CASO_DE_USO,
    EXPLORATORIA;

    public static Optional<Tecnica> desde(String texto) {
        if (texto == null || texto.isBlank()) {
            return Optional.empty();
        }
        String normalizado = Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase(Locale.ROOT)
                .replaceAll("[\\s-]+", "_");
        for (Tecnica t : values()) {
            if (t.name().equals(normalizado)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }
}
