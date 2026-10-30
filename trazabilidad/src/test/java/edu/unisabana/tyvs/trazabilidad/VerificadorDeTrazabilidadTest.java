package edu.unisabana.tyvs.trazabilidad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

class VerificadorDeTrazabilidadTest {

    private final VerificadorDeTrazabilidad verificador = new VerificadorDeTrazabilidad();

    private static Requisito requisito(String id, int impacto, int probabilidad) {
        return new Requisito(id, "descripcion de " + id, impacto, probabilidad);
    }

    private static CasoDePrueba caso(String id, String tecnica, String... requisitos) {
        return new CasoDePrueba(id, Set.of(requisitos), tecnica, "entrada", "VALID");
    }

    private List<Hallazgo.Tipo> tipos(Plan plan) {
        return verificador.verificar(plan).hallazgos().stream().map(Hallazgo::tipo).toList();
    }

    @Test
    void unaMatrizCompletaNoTieneHallazgos() {
        Plan plan = new Plan(
                List.of(requisito("R1", 3, 3), requisito("R2", 1, 1)),
                List.of(caso("CP-01", "VALOR_LIMITE", "R1"), caso("CP-02", "CLASE_EQUIVALENCIA", "R2")));

        VerificadorDeTrazabilidad.Resultado resultado = verificador.verificar(plan);

        assertTrue(resultado.aprobado(), resultado.reporte());
        assertEquals(100.0, resultado.metricas().coberturaFuncional());
    }

    @Test
    void detectaUnRequisitoSinCasos() {
        Plan plan = new Plan(
                List.of(requisito("R1", 1, 1), requisito("R2", 1, 1)),
                List.of(caso("CP-01", "CLASE_EQUIVALENCIA", "R1")));

        VerificadorDeTrazabilidad.Resultado resultado = verificador.verificar(plan);

        assertEquals(List.of(Hallazgo.Tipo.REQUISITO_SIN_CASOS), tipos(plan));
        assertEquals(50.0, resultado.metricas().coberturaFuncional());
    }

    @Test
    void detectaUnCasoQueNoTrazaANingunRequisito() {
        Plan plan = new Plan(
                List.of(requisito("R1", 1, 1)),
                List.of(caso("CP-01", "CLASE_EQUIVALENCIA", "R1"), caso("CP-02", "CLASE_EQUIVALENCIA")));

        assertEquals(List.of(Hallazgo.Tipo.CASO_HUERFANO), tipos(plan));
    }

    @Test
    void detectaUnCasoQueCitaUnRequisitoInexistente() {
        Plan plan = new Plan(
                List.of(requisito("R1", 1, 1)),
                List.of(caso("CP-01", "CLASE_EQUIVALENCIA", "R1"), caso("CP-02", "CLASE_EQUIVALENCIA", "R9")));

        assertEquals(List.of(Hallazgo.Tipo.REQUISITO_INEXISTENTE), tipos(plan));
    }

    @Test
    void exigeValorLimiteEnLosRequisitosDeRiesgoAlto() {
        Plan plan = new Plan(
                List.of(requisito("R1", 3, 2)),
                List.of(caso("CP-01", "CLASE_EQUIVALENCIA", "R1")));

        assertEquals(List.of(Hallazgo.Tipo.RIESGO_ALTO_SIN_VALOR_LIMITE), tipos(plan));
    }

    @Test
    void noExigeValorLimiteEnRiesgoMedio() {
        Plan plan = new Plan(
                List.of(requisito("R1", 2, 2)),
                List.of(caso("CP-01", "CLASE_EQUIVALENCIA", "R1")));

        assertTrue(tipos(plan).isEmpty());
    }

    @Test
    void unRequisitoSinCasosNoSeReportaTambienComoSinValorLimite() {
        // Un solo hallazgo, el de raiz: sin casos no tiene sentido pedir uno de bordes.
        Plan plan = new Plan(List.of(requisito("R1", 3, 3)), List.of());

        assertEquals(List.of(Hallazgo.Tipo.REQUISITO_SIN_CASOS), tipos(plan));
    }

    @Test
    void detectaRiesgoSinEvaluar() {
        Plan plan = new Plan(
                List.of(new Requisito("R1", "x", null, 2)),
                List.of(caso("CP-01", "CLASE_EQUIVALENCIA", "R1")));

        assertEquals(List.of(Hallazgo.Tipo.RIESGO_SIN_EVALUAR), tipos(plan));
    }

    @Test
    void detectaUnaTecnicaDesconocida() {
        Plan plan = new Plan(
                List.of(requisito("R1", 1, 1)),
                List.of(caso("CP-01", "intuicion", "R1")));

        assertEquals(List.of(Hallazgo.Tipo.TECNICA_DESCONOCIDA), tipos(plan));
    }

    @Test
    void detectaUnCasoSinResultadoEsperado() {
        Plan plan = new Plan(
                List.of(requisito("R1", 1, 1)),
                List.of(new CasoDePrueba("CP-01", Set.of("R1"), "CLASE_EQUIVALENCIA", "edad=30", " ")));

        assertEquals(List.of(Hallazgo.Tipo.SIN_RESULTADO_ESPERADO), tipos(plan));
    }

    @Test
    void detectaIdsDuplicados() {
        Plan plan = new Plan(
                List.of(requisito("R1", 1, 1), requisito("R1", 1, 1)),
                List.of(caso("CP-01", "CLASE_EQUIVALENCIA", "R1"), caso("CP-01", "CLASE_EQUIVALENCIA", "R1")));

        List<Hallazgo.Tipo> tipos = tipos(plan);

        assertTrue(tipos.contains(Hallazgo.Tipo.REQUISITO_DUPLICADO), tipos.toString());
        assertTrue(tipos.contains(Hallazgo.Tipo.CASO_DUPLICADO), tipos.toString());
    }

    @Test
    void unCasoPuedeDemostrarVariosRequisitosSeparadosPorBarra() {
        List<CasoDePrueba> casos = Plan.leerCasos(LectorCsv.leer(
                "caso,requisitos,tecnica,entrada,resultado_esperado\nCP-01, R1 | R2 ,TABLA_DECISION,x,DEAD\n",
                "matriz.csv"));
        Plan plan = new Plan(List.of(requisito("R1", 1, 1), requisito("R2", 1, 1)), casos);

        assertEquals(Set.of("R1", "R2"), casos.get(0).requisitos());
        assertTrue(verificador.verificar(plan).aprobado());
    }

    @Test
    void elReporteNombraCadaHallazgo() {
        Plan plan = new Plan(List.of(requisito("R7", 1, 1)), List.of());

        String reporte = verificador.verificar(plan).reporte();

        assertTrue(reporte.contains("0 de 1 requisitos"), reporte);
        assertTrue(reporte.contains("R7"), reporte);
    }
}
