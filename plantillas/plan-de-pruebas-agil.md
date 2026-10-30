# Plan de pruebas: <sistema> (enfoque ágil)

> Plantilla del Taller de Plan de Pruebas. Borre las instrucciones entre
> `<...>` y las citas en bloque al completar cada sección.
>
> **Ágil**: no hay un plan que se apruebe una vez. Hay una **estrategia
> estable** (secciones 1 a 4) y un **plan por sprint** (sección 5) que se
> revisa en cada refinamiento. Las pruebas se diseñan junto con la historia,
> no después de construirla.

| Campo | Valor |
|---|---|
| Sistema y versión | <nombre, versión o commit> |
| Equipo y roles | <quién refina, quién automatiza, quién acepta> |
| Duración del sprint | <1 o 2 semanas> |

## 1. Contexto y objetivo

<Qué sistema es, para quién, y qué significa "listo para salir" en este equipo.>

## 2. Estrategia estable

| Nivel o tipo de prueba | Qué cubre | Cuándo se ejecuta | Herramienta |
|---|---|---|---|
| <unitarias> | <reglas de negocio> | <en cada commit> | <...> |
| <integración, sistema, rendimiento...> | <...> | <en cada pull request, cada noche, antes del release> | <...> |

## 3. Historias de usuario y criterios de aceptación

Las historias van en [`requisitos.csv`](requisitos.csv) con su id (`HU-01`), y
sus criterios de aceptación aquí, en formato Given–When–Then:

```gherkin
Historia HU-01: <Como ... quiero ... para ...>

  Escenario: <nombre del escenario>
    Given <contexto>
    When <acción>
    Then <resultado observable>
```

> Un criterio de aceptación que no se puede convertir en un caso con
> resultado esperado no es un criterio: es una intención.

## 4. Definición de Listo (DoR) y de Hecho (DoD)

En ágil, estas dos listas cumplen el papel de los criterios de entrada y
salida del enfoque cascada, pero se aplican **a cada historia**, no a una fase.

| Lista | Criterio | Cómo se verifica |
|---|---|---|
| DoR (entrada) | <la historia tiene criterios Given–When–Then y riesgo evaluado> | <...> |
| DoD (salida) | <casos automatizados en verde en CI, matriz sin hallazgos, 0 defectos críticos> | <...> |

## 5. Plan por sprint

| Sprint | Historias | Riesgo | Casos nuevos | Regresión | Horas de prueba estimadas |
|---|---|---|---|---|---|
| <1> | <HU-01, HU-02> | <ALTO, MEDIO> | <CP-01..CP-06> | <qué se vuelve a ejecutar> | <...> |

> La estimación va en la planificación del sprint, junto con la de desarrollo:
> una historia cuya prueba no cabe en el sprint no está lista.

## 6. Riesgos y preguntas abiertas

| Riesgo o pregunta | Historia afectada | Cuándo se resuelve (refinamiento, sprint) |
|---|---|---|
| <...> | <...> | <...> |

## 7. Diseño de casos y trazabilidad

<La técnica elegida para cada historia y su desarrollo.> Los casos están en
[`matriz-trazabilidad.csv`](matriz-trazabilidad.csv), trazados a la historia
que demuestran.

## 8. Métricas y reporte

<Qué se muestra en cada revisión del sprint: cobertura funcional, casos
automatizados, defectos abiertos por severidad, tendencia entre sprints.>
