# Plan de pruebas: Registraduría (enfoque cascada), ejemplo parcial

> **Este plan está incompleto a propósito.** Las secciones resueltas muestran
> el nivel de detalle que se espera; las marcadas **EJERCICIO** son las que
> usted completa. Al terminar, `mvn verify -Dplan=../ejemplo-registraduria`
> debe pasar sin hallazgos.

| Campo | Valor |
|---|---|
| Sistema y versión | `registraduria` 1.0-SNAPSHOT, servicio `POST /register` |
| Equipo y roles | (el suyo) |
| Fecha y versión del plan | (la suya) |

## 1. Contexto y objetivo

La Registraduría inscribe votantes. Este plan apoya una decisión concreta:
**¿puede la versión 1.0 abrir inscripciones para las elecciones?** Un error
aquí no es un mensaje feo: es una persona habilitada para votar que no debía
estarlo, o una que debía estarlo y no puede.

## 2. Alcance

| Dentro del alcance | Fuera del alcance | Por qué está fuera |
|---|---|---|
| Las reglas de inscripción R1 a R7 | Autenticación de quien inscribe | No existe en esta versión: el servicio es interno |
| Persistencia y unicidad del documento (R6) | Rendimiento bajo carga | Tiene su propio plan (taller de carga) |
| Respuesta del servicio ante entradas malformadas (RNF-01) | Interfaz gráfica | La inscripción se hace por API en esta versión |

## 3. Análisis del sistema

| Funcionalidad / módulo | Criticidad | Por qué |
|---|---|---|
| Validación de edad (R4, R5) | Alta | Un error de borde (17 o 18) habilita o excluye votantes |
| Unicidad del documento (R6) | Alta | Un duplicado es una persona que vota dos veces |
| Estado vital (R3) | Alta | Inscribir a una persona fallecida es la base de un fraude conocido |
| Validación de datos básicos (R1, R2) | Media | Protege al sistema; el error se ve de inmediato |
| Manejo de errores del servicio (RNF-01) | Media | Un `500` expone detalles internos y no dice qué corregir |

## 4. Requisitos

Están en [`requisitos.csv`](requisitos.csv): las siete reglas del
[Taller de Pruebas Unitarias](https://github.com/CesarAVegaF312/TYVS-Taller_Pruebas_Unitarias)
y un requisito no funcional. Se evalúan en orden: la primera regla que falla
determina el resultado.

## 5. Riesgos

### 5.1 Riesgos del producto

Ya evaluados, como modelo:

- **R5, mayoría de edad: impacto 3 × probabilidad 2 = 6, ALTO.** Impacto 3
  porque un menor inscrito vota. Probabilidad 2 porque el error típico es de
  borde (`>` en lugar de `>=`) y pasa cualquier prueba con edad 10 o 30. Por
  eso la matriz tiene casos en **17 y 18**, no "un menor y un adulto".
- **R6, unicidad: impacto 3 × probabilidad 1 = 3, MEDIO.** El impacto es
  máximo, pero la probabilidad es baja porque la base de datos impone la
  restricción. Note el razonamiento: un impacto alto **no** basta para que un
  riesgo sea alto.
- **R1, persona nula: impacto 2 × probabilidad 1 = 2, BAJO.**

**EJERCICIO.** Evalúe R2, R3, R4, R7 y RNF-01 en `requisitos.csv` y
justifique aquí cada uno con el mismo formato. Si alguno resulta ALTO, el
verificador le exigirá un caso de valor límite. Pregúntese cuál es su borde:
casi siempre existe, aunque no sea un número.

### 5.2 Riesgos del proyecto y preguntas abiertas

| Riesgo o pregunta | Efecto en las pruebas | Mitigación o responsable |
|---|---|---|
| ¿Una persona de 120 años y 6 meses tiene "120"? La regla habla de años cumplidos | Cambia el resultado esperado del borde superior de R4 | Preguntar al dueño del negocio antes de diseñar los casos de R4 |
| **EJERCICIO**: al menos dos más | | |

## 6. Estrategia

| Nivel o tipo de prueba | Qué cubre | Técnica | Herramienta | Manual o automatizada |
|---|---|---|---|---|
| Unitaria | R1 a R5 y R7 en el dominio | Clases de equivalencia, valores límite, tabla de decisión | JUnit 5 | Automatizada |
| Integración | R6 contra la base de datos | Clases de equivalencia | JUnit 5 + H2 | Automatizada |
| Sistema | RNF-01 y el contrato HTTP | Clases de equivalencia, exploratoria | Spring Boot Test, Postman | Automatizada y una sesión exploratoria |

## 7. Diseño de casos

### R5: valores límite de la edad (resuelto)

| Clase | Rango | Bordes probados | Caso |
|---|---|---|---|
| Menor de edad | `0 ≤ edad < 18` | 17 | CP-02 |
| Mayor de edad | `edad ≥ 18` | 18 | CP-03 |
| Representante de "menor" | | 10 | CP-04 |

El borde `0` y el borde `120` pertenecen a R4, no a R5: se diseñan allá.

### Orden de las reglas: tabla de decisión (iniciada)

Cuando dos reglas fallan a la vez, gana la que se evalúa primero.

| Condiciones | C1 | C2 | **EJERCICIO**: complete las columnas que faltan |
|---|---|---|---|
| ¿Viva? | No | No | |
| ¿Edad < 18? | Sí | No | |
| **Resultado** | `DEAD` (CP-06) | `DEAD` (CP-05) | |

### R2, R4, R6, R7 y RNF-01

**EJERCICIO.** Diseñe sus casos y agréguelos a
[`matriz-trazabilidad.csv`](matriz-trazabilidad.csv).

## 8. Criterios de entrada, salida y suspensión

**EJERCICIO.** Use la tabla de la plantilla. Cada criterio de salida debe
llevar un número.

## 9. Ambiente y datos de prueba

**EJERCICIO.**

## 10. Estimación y calendario

**EJERCICIO.** Estime con un supuesto explícito (por ejemplo, minutos por
caso) y no con una cifra redonda: el supuesto es lo que se puede discutir y
corregir.

## 11. Métricas y reporte

**EJERCICIO.**

## 12. El mismo sistema en ágil, y la comparación

**EJERCICIO.** Escriba las secciones 3 a 5 de
[`plan-de-pruebas-agil.md`](../plantillas/plan-de-pruebas-agil.md) para la
Registraduría: historias, criterios Given–When–Then, DoR/DoD y un plan de dos
sprints. Después compare los dos planes en esta sección (ver el módulo 6 del
taller).
