package edu.unisabana.tyvs.trazabilidad;

/**
 * Un requisito (enfoque cascada) o una historia de usuario (enfoque agil).
 * Para el verificador son lo mismo: algo que el sistema debe cumplir y que
 * alguna prueba tiene que demostrar.
 *
 * El riesgo se evalua con dos escalas de 1 a 3, y su producto decide la
 * prioridad. Si alguna de las dos esta vacia o fuera de rango, el riesgo
 * queda sin evaluar: {@link #nivel()} devuelve {@code null}.
 */
public record Requisito(String id, String descripcion, Integer impacto, Integer probabilidad) {

    /** Producto minimo, en la escala de 1 a 9, para que un riesgo sea ALTO. */
    public static final int UMBRAL_ALTO = 6;

    /** Producto minimo para que un riesgo sea MEDIO. */
    public static final int UMBRAL_MEDIO = 3;

    public boolean riesgoEvaluado() {
        return enRango(impacto) && enRango(probabilidad);
    }

    public NivelDeRiesgo nivel() {
        if (!riesgoEvaluado()) {
            return null;
        }
        int producto = impacto * probabilidad;
        if (producto >= UMBRAL_ALTO) {
            return NivelDeRiesgo.ALTO;
        }
        return producto >= UMBRAL_MEDIO ? NivelDeRiesgo.MEDIO : NivelDeRiesgo.BAJO;
    }

    private static boolean enRango(Integer valor) {
        return valor != null && valor >= 1 && valor <= 3;
    }
}
