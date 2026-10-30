package edu.unisabana.tyvs.trazabilidad;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Revisa una matriz de trazabilidad contra la lista de requisitos.
 *
 * Una matriz escrita a mano envejece en silencio: alguien agrega un requisito
 * y nadie le escribe un caso, o borra un requisito y sus casos quedan
 * huerfanos. Revisarla a ojo funciona con 10 filas y deja de funcionar con 80.
 * Este verificador hace con la matriz lo que una prueba hace con el codigo:
 * convierte "creo que esta completa" en una comprobacion que falla.
 */
public final class VerificadorDeTrazabilidad {

    public Resultado verificar(Plan plan) {
        List<Hallazgo> hallazgos = new ArrayList<>();
        Map<String, Requisito> requisitos = new LinkedHashMap<>();

        for (Requisito r : plan.requisitos()) {
            if (requisitos.putIfAbsent(r.id(), r) != null) {
                hallazgos.add(new Hallazgo(Hallazgo.Tipo.REQUISITO_DUPLICADO,
                        "el id " + r.id() + " aparece mas de una vez en " + Plan.REQUISITOS));
            }
            if (!r.riesgoEvaluado()) {
                hallazgos.add(new Hallazgo(Hallazgo.Tipo.RIESGO_SIN_EVALUAR,
                        r.id() + ": impacto y probabilidad deben ser enteros de 1 a 3"));
            }
        }

        Map<String, List<CasoDePrueba>> casosPorRequisito = new LinkedHashMap<>();
        requisitos.keySet().forEach(id -> casosPorRequisito.put(id, new ArrayList<>()));
        Set<String> idsDeCasos = new HashSet<>();

        for (CasoDePrueba caso : plan.casos()) {
            if (!idsDeCasos.add(caso.id())) {
                hallazgos.add(new Hallazgo(Hallazgo.Tipo.CASO_DUPLICADO,
                        "el caso " + caso.id() + " aparece mas de una vez en " + Plan.MATRIZ));
            }
            if (caso.requisitos().isEmpty()) {
                hallazgos.add(new Hallazgo(Hallazgo.Tipo.CASO_HUERFANO,
                        caso.id() + " no traza a ningun requisito: o sobra, o falta el requisito que prueba"));
            }
            for (String idRequisito : caso.requisitos()) {
                List<CasoDePrueba> casos = casosPorRequisito.get(idRequisito);
                if (casos == null) {
                    hallazgos.add(new Hallazgo(Hallazgo.Tipo.REQUISITO_INEXISTENTE,
                            caso.id() + " cita " + idRequisito + ", que no esta en " + Plan.REQUISITOS));
                } else {
                    casos.add(caso);
                }
            }
            if (Tecnica.desde(caso.tecnica()).isEmpty()) {
                hallazgos.add(new Hallazgo(Hallazgo.Tipo.TECNICA_DESCONOCIDA,
                        caso.id() + ": '" + caso.tecnica() + "' no es una tecnica valida. Use una de "
                        + java.util.Arrays.toString(Tecnica.values())));
            }
            if (caso.resultadoEsperado() == null || caso.resultadoEsperado().isBlank()) {
                hallazgos.add(new Hallazgo(Hallazgo.Tipo.SIN_RESULTADO_ESPERADO,
                        caso.id() + " no tiene resultado esperado: sin el no hay contra que comparar"));
            }
        }

        for (Map.Entry<String, List<CasoDePrueba>> e : casosPorRequisito.entrySet()) {
            Requisito r = requisitos.get(e.getKey());
            if (e.getValue().isEmpty()) {
                hallazgos.add(new Hallazgo(Hallazgo.Tipo.REQUISITO_SIN_CASOS,
                        r.id() + " (" + r.descripcion() + ") no tiene ningun caso de prueba"));
            } else if (r.nivel() == NivelDeRiesgo.ALTO && e.getValue().stream()
                    .noneMatch(c -> Tecnica.desde(c.tecnica()).equals(Optional.of(Tecnica.VALOR_LIMITE)))) {
                hallazgos.add(new Hallazgo(Hallazgo.Tipo.RIESGO_ALTO_SIN_VALOR_LIMITE,
                        r.id() + " es de riesgo ALTO y ningun caso prueba sus bordes (tecnica VALOR_LIMITE)"));
            }
        }

        return new Resultado(hallazgos, metricas(requisitos, casosPorRequisito, plan.casos()));
    }

    private static Metricas metricas(Map<String, Requisito> requisitos,
                                     Map<String, List<CasoDePrueba>> casosPorRequisito,
                                     List<CasoDePrueba> casos) {
        int cubiertos = (int) casosPorRequisito.values().stream().filter(l -> !l.isEmpty()).count();
        Map<NivelDeRiesgo, int[]> porNivel = new EnumMap<>(NivelDeRiesgo.class);
        for (Requisito r : requisitos.values()) {
            if (r.nivel() != null) {
                int[] cuenta = porNivel.computeIfAbsent(r.nivel(), n -> new int[2]);
                cuenta[1]++;
                if (!casosPorRequisito.get(r.id()).isEmpty()) {
                    cuenta[0]++;
                }
            }
        }
        Map<Tecnica, Integer> porTecnica = new EnumMap<>(Tecnica.class);
        for (CasoDePrueba c : casos) {
            Tecnica.desde(c.tecnica()).ifPresent(t -> porTecnica.merge(t, 1, Integer::sum));
        }
        return new Metricas(requisitos.size(), cubiertos, casos.size(), porNivel, porTecnica);
    }

    /** Lo que el equipo puede reportar en su plan: cobertura funcional y su distribucion. */
    public record Metricas(int requisitos, int requisitosCubiertos, int casos,
                           Map<NivelDeRiesgo, int[]> cubiertosPorNivel, Map<Tecnica, Integer> casosPorTecnica) {

        public double coberturaFuncional() {
            return requisitos == 0 ? 0 : 100.0 * requisitosCubiertos / requisitos;
        }
    }

    public record Resultado(List<Hallazgo> hallazgos, Metricas metricas) {

        public boolean aprobado() {
            return hallazgos.isEmpty();
        }

        public String reporte() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format(Locale.ROOT, "Cobertura funcional: %d de %d requisitos con al menos un caso (%.1f%%)%n",
                    metricas.requisitosCubiertos(), metricas.requisitos(), metricas.coberturaFuncional()));
            for (NivelDeRiesgo n : NivelDeRiesgo.values()) {
                int[] c = metricas.cubiertosPorNivel().get(n);
                if (c != null) {
                    sb.append(String.format(Locale.ROOT, "  Riesgo %-5s: %d de %d cubiertos%n", n, c[0], c[1]));
                }
            }
            sb.append("Casos: ").append(metricas.casos()).append(" ").append(metricas.casosPorTecnica()).append('\n');
            if (hallazgos.isEmpty()) {
                sb.append("Sin hallazgos: la matriz esta completa.\n");
            } else {
                sb.append(hallazgos.size()).append(" hallazgo(s):\n");
                hallazgos.forEach(h -> sb.append("  - ").append(h).append('\n'));
            }
            return sb.toString();
        }
    }
}
