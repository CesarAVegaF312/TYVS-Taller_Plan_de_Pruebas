package edu.unisabana.tyvs.trazabilidad;

/** Un problema de la matriz, con un mensaje que dice que hacer para resolverlo. */
public record Hallazgo(Tipo tipo, String detalle) {

    public enum Tipo {
        /** Un requisito que ninguna prueba demuestra: cobertura funcional incompleta. */
        REQUISITO_SIN_CASOS,
        /** Un caso que no traza a ningun requisito: o sobra, o falta un requisito. */
        CASO_HUERFANO,
        /** Un caso que cita un requisito que no existe (casi siempre, un error de digitacion). */
        REQUISITO_INEXISTENTE,
        /** Un requisito de riesgo alto sin un caso que pruebe sus bordes. */
        RIESGO_ALTO_SIN_VALOR_LIMITE,
        /** Impacto o probabilidad vacios o fuera de la escala de 1 a 3. */
        RIESGO_SIN_EVALUAR,
        TECNICA_DESCONOCIDA,
        /** Sin resultado esperado no hay prueba: no hay contra que comparar. */
        SIN_RESULTADO_ESPERADO,
        CASO_DUPLICADO,
        REQUISITO_DUPLICADO
    }

    @Override
    public String toString() {
        return tipo + ": " + detalle;
    }
}
