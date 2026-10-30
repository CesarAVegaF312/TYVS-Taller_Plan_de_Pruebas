# Plan de pruebas: <sistema> (enfoque cascada)

> Plantilla del Taller de Plan de Pruebas. La estructura se inspira en los
> contenidos que la norma ISO/IEC/IEEE 29119-3 propone para un plan de
> pruebas, adaptados a un proyecto académico. Borre las instrucciones entre
> `<...>` y las citas en bloque al completar cada sección.
>
> **Cascada**: el plan se escribe completo **antes** de construir, se aprueba
> una vez y cada fase empieza cuando termina la anterior. Por eso pesa tanto
> que los criterios de entrada y salida sean verificables: son los que
> autorizan pasar de una fase a la siguiente.

| Campo | Valor |
|---|---|
| Sistema y versión | <nombre, versión o commit> |
| Equipo y roles | <nombre: rol (líder de pruebas, analista, automatizador)> |
| Fecha y versión del plan | <AAAA-MM-DD, v1.0> |

## 1. Contexto y objetivo

<Qué sistema es, para quién, y qué decisión va a apoyar este plan. Una o dos
frases: "Decidir si la versión X puede salir a producción" es un objetivo;
"probar el sistema" no lo es.>

## 2. Alcance

| Dentro del alcance | Fuera del alcance | Por qué está fuera |
|---|---|---|
| <funcionalidad o característica> | <lo que NO se prueba> | <razón: riesgo bajo, otro equipo, sin ambiente> |

> Lo que queda fuera del alcance es tan importante como lo que queda dentro:
> es lo que nadie podrá reclamar después como "se suponía que estaba probado".

## 3. Análisis del sistema

| Funcionalidad / módulo | Criticidad (alta, media, baja) | Por qué |
|---|---|---|
| <...> | <...> | <qué pasa si falla y a quién afecta> |

## 4. Requisitos

Los requisitos verificables están en [`requisitos.csv`](requisitos.csv), con su
impacto y probabilidad (escala de 1 a 3). Un requisito es verificable si se
puede escribir un caso con un resultado esperado concreto.

## 5. Riesgos

### 5.1 Riesgos del producto

Se priorizan con impacto × probabilidad en `requisitos.csv`:

| Producto | Nivel | Qué exige este plan |
|---|---|---|
| 6 a 9 | ALTO | Casos de valor límite y prioridad de ejecución |
| 3 a 4 | MEDIO | Al menos un caso por clase de equivalencia |
| 1 a 2 | BAJO | Al menos un caso |

<Justifique aquí cada requisito de riesgo ALTO: por qué ese impacto y por qué
esa probabilidad.>

### 5.2 Riesgos del proyecto y preguntas abiertas

| Riesgo o pregunta | Efecto en las pruebas | Mitigación o responsable |
|---|---|---|
| <ambiente no disponible, requisito ambiguo, dependencia externa...> | <...> | <...> |

## 6. Estrategia

| Nivel o tipo de prueba | Qué cubre | Técnica | Herramienta | Manual o automatizada |
|---|---|---|---|---|
| <unitaria, integración, sistema, aceptación, rendimiento, seguridad...> | <...> | <...> | <...> | <...> |

## 7. Diseño de casos

<Para cada requisito, la técnica elegida y su desarrollo: tabla de clases de
equivalencia, valores límite, tabla de decisión o diagrama de estados.> Los
casos resultantes están en [`matriz-trazabilidad.csv`](matriz-trazabilidad.csv).

## 8. Criterios de entrada, salida y suspensión

| Tipo | Criterio | Cómo se verifica |
|---|---|---|
| Entrada | <qué debe estar listo para empezar a probar> | <evidencia> |
| Salida | <qué debe cumplirse para dar por terminada la fase> | <evidencia, con un número> |
| Suspensión | <cuándo se detienen las pruebas> | <...> |
| Reanudación | <cuándo se retoman> | <...> |

> "Cuando todo esté probado" no es un criterio de salida: nunca se cumple, o se
> cumple siempre. "100% de los requisitos ALTO con casos ejecutados y 0
> defectos críticos abiertos" sí lo es.

## 9. Ambiente y datos de prueba

<Dónde se ejecutan las pruebas, con qué datos, cómo se preparan y cómo se
restauran entre ejecuciones.>

## 10. Estimación y calendario

| Actividad | Supuesto de cálculo | Horas | Responsable |
|---|---|---|---|
| Diseño de casos | <n casos × m minutos por caso> | <...> | <...> |
| Preparación del ambiente y los datos | <...> | <...> | <...> |
| Ejecución y re-ejecución | <n casos × m minutos × ciclos> | <...> | <...> |
| Reporte de defectos y regresión | <...> | <...> | <...> |
| **Total** | | <...> | |

| Fase | Inicio | Fin | Hito de salida |
|---|---|---|---|
| <análisis, diseño, ejecución, cierre> | <...> | <...> | <criterio de la sección 8 que la cierra> |

## 11. Métricas y reporte

<Qué se va a medir (cobertura funcional del verificador, casos ejecutados y
aprobados, defectos por severidad) y cada cuánto se reporta.>
