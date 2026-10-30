package edu.unisabana.tyvs.trazabilidad;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

/**
 * Las plantillas que se copian para empezar un plan deben poder leerse.
 * Si alguien cambia una columna de la plantilla y no el verificador (o al
 * reves), esta prueba lo detecta antes que los estudiantes.
 */
class PlantillasTest {

    private static final Path PLANTILLAS = Path.of("..", "plantillas");

    @Test
    void laPlantillaDelPlanTieneColumnasValidas() throws Exception {
        Plan plan = Plan.desde(PLANTILLAS);

        assertFalse(plan.requisitos().isEmpty());
        assertFalse(plan.casos().isEmpty());
    }

    @Test
    void elEjemploDeLaRegistraduriaSePuedeLeer() throws Exception {
        // Solo se exige que se pueda leer: el ejemplo esta incompleto a proposito
        // y completarlo es el ejercicio.
        Plan plan = Plan.desde(Path.of("..", "ejemplo-registraduria"));

        assertFalse(plan.requisitos().isEmpty());
    }
}
