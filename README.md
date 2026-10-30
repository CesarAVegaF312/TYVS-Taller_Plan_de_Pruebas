# Taller de Plan de Pruebas

Un plan de pruebas que nadie verifica envejece desde el día en que se aprueba.
Alguien agrega un requisito y nadie le escribe un caso; alguien borra otro y
sus casos quedan huérfanos; el requisito más riesgoso termina con la misma
prueba superficial que el más trivial. Nada de eso produce un error: el
documento sigue viéndose completo.

Este taller trata el plan como se trata el código. Se escribe con técnicas
concretas, se prioriza con el riesgo y se **verifica automáticamente**: la
matriz de trazabilidad se revisa con un programa que falla cuando el plan
tiene huecos, igual que una prueba falla cuando el código tiene un defecto.

---

## Objetivos

- **Analizar** un sistema: funcionalidades, módulos críticos y lo que queda
  fuera del alcance.
- Expresar lo que se va a probar como **requisitos verificables** (enfoque
  cascada) o como **historias con criterios Given–When–Then** (enfoque ágil).
- **Priorizar con el riesgo** (impacto × probabilidad) y no con la intuición.
- Diseñar casos con **técnicas**: clases de equivalencia, valores límite,
  tabla de decisión, transición de estados.
- Mantener una **matriz de trazabilidad** y medir la **cobertura funcional**.
- Definir **criterios de entrada y salida** verificables, **estimar** el
  esfuerzo y **calendarizar** las pruebas.
- **Comparar** cómo se planifica el mismo sistema en cascada y en ágil.

---

## Índice

- [Sistema bajo prueba](#sistema-bajo-prueba)
- [Puesta en marcha](#puesta-en-marcha)
- [Formato de la entrega](#formato-de-la-entrega)
- [Ambiente de trabajo reproducible](#ambiente-de-trabajo-reproducible)
- [Módulos del taller](#módulos-del-taller)
- [Para entregar](#para-entregar-con-este-taller)
- [Rúbrica](#rúbrica)

---

## Sistema bajo prueba

La misma Registraduría del [Taller de Pruebas Unitarias](https://github.com/CesarAVegaF312/TYVS-Taller_Pruebas_Unitarias):
inscribe votantes aplicando siete reglas en orden (persona nula, documento
inválido, fallecida, edad imposible, menor de edad, duplicada, válida).

Aquí no se escribe código del sistema: se escribe **el plan** con el que se
probaría. El único código del taller es el verificador de la matriz.

```text
.
├─ ejemplo-registraduria/           # el EJERCICIO: un plan resuelto a medias
│   ├─ plan-de-pruebas.md
│   ├─ requisitos.csv               # requisitos con impacto y probabilidad
│   └─ matriz-trazabilidad.csv      # casos de prueba -> requisitos
├─ plantillas/                      # para empezar el plan del proyecto
│   ├─ plan-de-pruebas-cascada.md
│   ├─ plan-de-pruebas-agil.md
│   ├─ requisitos.csv
│   └─ matriz-trazabilidad.csv
└─ trazabilidad/                    # verificador (Java 17, JUnit 5)
```

---

## Puesta en marcha

```bash
cd trazabilidad
mvn verify                                    # prueba el verificador
mvn verify -Dplan=../ejemplo-registraduria    # verifica el plan del ejemplo
```

La primera línea termina en `BUILD SUCCESS` con 44 pruebas (una omitida: la
que verifica un plan, que solo corre con `-Dplan`).

La segunda **falla, y debe fallar**: el ejemplo está incompleto a propósito.
Esta es su salida real:

```text
Cobertura funcional: 4 de 8 requisitos con al menos un caso (50.0%)
  Riesgo ALTO : 1 de 1 cubiertos
  Riesgo MEDIO: 0 de 1 cubiertos
  Riesgo BAJO : 1 de 1 cubiertos
Casos: 6 {CLASE_EQUIVALENCIA=3, VALOR_LIMITE=2, TABLA_DECISION=1}
9 hallazgo(s):
  - RIESGO_SIN_EVALUAR: R2: impacto y probabilidad deben ser enteros de 1 a 3
  - ...
  - REQUISITO_SIN_CASOS: R4 (La edad debe estar entre 0 y 120: INVALID_AGE) no tiene ningun caso de prueba
  - ...
```

Completar el plan hasta que esa línea pase es el ejercicio. Es el mismo ciclo
de TDD aplicado a un documento: primero rojo, después verde.

> `-Dplan` acepta cualquier carpeta que tenga `requisitos.csv` y
> `matriz-trazabilidad.csv`, también con ruta absoluta. Así se verifica el plan
> del proyecto, que vive en otro repositorio.

---

## Formato de la entrega

| Pregunta | Respuesta |
|---|---|
| ¿Dónde se entrega? | En el **repositorio de GitHub** del equipo, en una carpeta `plan/` con el plan en Markdown y los dos CSV. Si no puede usar GitHub, entregue el mismo contenido en un `.zip`. |
| ¿Es un documento Word, un PDF o una presentación? | Ninguno de los tres: **Markdown** en el repositorio, que GitHub muestra formateado. No se pide presentación. Si su equipo necesita un PDF, expórtelo desde el Markdown. |
| ¿Qué extensión? | Orientativa. **Ejercicio**: de 4 a 8 páginas equivalentes (unas 1.500 a 3.000 palabras, sin contar tablas). **Proyecto**: de 8 a 15 páginas equivalentes (unas 3.000 a 5.000 palabras). Más largo no es mejor: un plan que nadie lee no orienta a nadie. |
| ¿Lleva normas APA? | Solo en la lista de **referencias citadas** (APA 7). Las tablas, la matriz y el resto del plan no necesitan formato APA. |
| ¿Qué herramientas? | Un editor de Markdown (VS Code o el editor web de GitHub); una hoja de cálculo (Excel, LibreOffice o Google Sheets) guardada como **CSV UTF-8** (con coma o punto y coma, el verificador acepta las dos); Java 17 y Maven para el verificador. Opcional: Mermaid para diagramas y GitHub Projects o Jira para el calendario ágil. |
| ¿Individual o en equipo? | En equipo, el mismo del proyecto. |

---

## Ambiente de trabajo reproducible

El repositorio trae su **ambiente definido como código** en [`.devcontainer/devcontainer.json`](.devcontainer/devcontainer.json): Java 17 y Maven, en las mismas versiones para todo el curso. Es **opcional**: si ya tiene Java 17 y Maven, siga con la puesta en marcha.

- **GitHub Codespaces**, sin instalar nada: *Code → Codespaces → Create codespace*. Consume la cuota gratuita de Codespaces: deténgalo al terminar.
- **VS Code y Docker** en su equipo: extensión *Dev Containers*, *Reopen in Container*.

---

## Módulos del taller

### Módulo 1 — Del sistema a los requisitos

Antes de diseñar un solo caso hay que saber **qué importa**. El análisis del
sistema responde tres preguntas: qué hace (funcionalidades), qué pasa si cada
parte falla (criticidad) y qué no se va a probar (alcance). La sección 3 del
[ejemplo](ejemplo-registraduria/plan-de-pruebas.md) lo muestra resuelto.

Lo que se prueba se escribe distinto según el enfoque:

| | Cascada | Ágil |
|---|---|---|
| Unidad | Requisito verificable (`R5`) | Historia de usuario (`HU-03`) |
| Formato | "La persona debe ser mayor de edad (edad ≥ 18): `UNDERAGE`" | "Como registrador quiero rechazar a los menores para que no voten", con criterios Given–When–Then |
| Cuándo se escribe | Todo al inicio, antes de construir | Historia por historia, en el refinamiento |

En los dos casos la prueba de calidad es la misma: **¿se puede escribir un
caso con un resultado esperado concreto?** "El sistema debe ser fácil de usar"
no pasa esa prueba; "un usuario nuevo completa la inscripción en menos de 2
minutos" sí.

### Módulo 2 — Riesgo y priorización

Nunca hay tiempo para probarlo todo con la misma profundidad. Priorizar con el
riesgo es decidir **dónde poner el esfuerzo** con un criterio explícito, en vez
de repartirlo por igual o por intuición.

Cada requisito recibe dos notas de 1 a 3 en `requisitos.csv`:

- **Impacto**: qué tan grave es si falla, y para quién.
- **Probabilidad**: qué tan probable es que la implementación falle. Depende
  de la complejidad, de los bordes, de si hay reglas que se cruzan, de si la
  protege algo más (una restricción de la base de datos, por ejemplo).

| Producto | Nivel | Qué exige el verificador |
|---|---|---|
| 6 a 9 | ALTO | Al menos un caso de `VALOR_LIMITE` |
| 3 a 4 | MEDIO | Al menos un caso |
| 1 a 2 | BAJO | Al menos un caso |

El ejemplo resuelve tres y deja el razonamiento escrito. Fíjese en R6
(unicidad): impacto 3, pero riesgo MEDIO, porque la base de datos impone la
restricción. **Un impacto alto no basta para que un riesgo sea alto**, y
confundir las dos cosas es la forma más común de llenar un plan de prioridades
que no priorizan nada.

Las ambigüedades también son riesgos. Una regla que se puede leer de dos
maneras producirá dos implementaciones, y las dos pasarán sus propias pruebas.
Se registran en la sección 5.2 como **preguntas abiertas**, con quién y cuándo
las resuelve.

### Módulo 3 — Técnicas de diseño de casos

| Técnica | Cuándo sirve | En la Registraduría |
|---|---|---|
| **Clases de equivalencia** | Entradas que el sistema trata igual: basta un representante por clase | Edad 10 y edad 30 representan "menor" y "adulto" |
| **Valores límite** | Los errores se concentran en los bordes entre clases | 17 y 18 en la mayoría de edad |
| **Tabla de decisión** | Varias condiciones combinadas, o reglas con precedencia | ¿Qué devuelve una persona fallecida de 15 años? |
| **Transición de estados** | El resultado depende de la historia, no solo de la entrada | Inscribir el mismo documento por primera y por segunda vez |
| **Caso de uso** | Flujos de punta a punta, con sus alternativas | Inscripción completa por la API |
| **Exploratoria** | Lo que nadie pensó: sesiones con un objetivo y un tiempo fijo | ¿Qué pasa con un nombre de 10.000 caracteres? |

Dos ideas que el ejemplo deja a la vista:

- **El borde no siempre es un número.** En la unicidad, el borde es la
  diferencia entre la primera inscripción y la segunda. En un cupo de 60, es
  la cita 60 y la 61. Si un requisito es de riesgo ALTO y "no tiene bordes",
  probablemente no se ha buscado bien.
- **La precedencia de reglas pide una tabla de decisión.** Cuando dos reglas
  fallan a la vez, gana la primera que se evalúa. Una prueba que solo hace
  fallar una regla por vez nunca verifica ese orden.

### Módulo 4 — Trazabilidad y cobertura funcional

La matriz une cada caso con el requisito que demuestra. Leída en un sentido,
responde **"¿este requisito está probado?"**; en el otro, **"¿para qué existe
esta prueba?"**.

El [verificador](trazabilidad/src/main/java/edu/unisabana/tyvs/trazabilidad/VerificadorDeTrazabilidad.java)
la recorre en los dos sentidos y reporta:

| Hallazgo | Qué significa |
|---|---|
| `REQUISITO_SIN_CASOS` | Un requisito que ninguna prueba demuestra |
| `CASO_HUERFANO` | Un caso que no traza a ningún requisito: o sobra, o falta el requisito |
| `REQUISITO_INEXISTENTE` | Un caso que cita un requisito que no existe (casi siempre, un error al digitar) |
| `RIESGO_ALTO_SIN_VALOR_LIMITE` | Lo más riesgoso, probado sin tocar sus bordes |
| `RIESGO_SIN_EVALUAR` | Un requisito sin impacto o sin probabilidad |
| `TECNICA_DESCONOCIDA` | Una técnica que no está en la lista del módulo 3 |
| `SIN_RESULTADO_ESPERADO` | Un caso sin resultado esperado no es una prueba: no hay contra qué comparar |
| `CASO_DUPLICADO`, `REQUISITO_DUPLICADO` | El mismo id dos veces |

Y calcula la **cobertura funcional**: qué porcentaje de los requisitos tiene
al menos un caso, desglosado por nivel de riesgo.

> **Cobertura funcional no es cobertura de código.** JaCoCo dice qué líneas se
> ejecutaron; esta métrica dice qué requisitos tienen una prueba. Un sistema
> puede tener 95% de cobertura de código y no probar nunca un requisito que no
> llegó a implementarse: ninguna línea lo representa. Solo la matriz lo ve.

### Módulo 5 — Criterios, estimación y calendario

**Criterios de entrada y salida.** Los de entrada evitan empezar a probar sobre
algo que no está listo (sin ambiente, sin datos, con la versión equivocada).
Los de salida son los que autorizan decir "terminamos". Deben llevar un número:
"100% de los requisitos ALTO con casos ejecutados y 0 defectos críticos
abiertos" se verifica; "cuando todo esté probado" no se cumple nunca, o se
cumple siempre.

**Estimación.** Se estima con un **supuesto explícito** (casos × minutos por
caso × ciclos de ejecución), no con una cifra redonda. El supuesto es lo que se
puede discutir y corregir después; la cifra redonda no se puede auditar.

**Calendario.** En cascada, por fases, cada una cerrada por un criterio de
salida. En ágil, por sprint: la prueba de una historia se estima con la
historia, y una historia cuya prueba no cabe en el sprint no está lista.

### Módulo 6 — El mismo sistema, dos planes

La comparación entre cascada y ágil no se responde en abstracto. Se responde
planificando **el mismo sistema** de las dos maneras y mirando qué cambia:

| Pregunta | Cascada | Ágil |
|---|---|---|
| ¿Cuándo se escribe el plan? | | |
| ¿Quién participa? | | |
| ¿Dónde viven los criterios de salida? | | |
| ¿Qué pasa si el negocio cambia la edad mínima a 16 a mitad del proyecto? | | |
| ¿Cuánto documento se produce, y quién lo lee? | | |
| ¿Cuándo se detecta el primer defecto? | | |

Completar esa tabla con evidencia de sus dos planes, y no con generalidades, es
parte de la entrega. Ninguno de los dos enfoques gana siempre: un sistema
regulado, con requisitos estables y auditoría externa, no se planifica igual
que un producto que cambia cada semana.

---

## PARA ENTREGAR CON ESTE TALLER

> **Ejercicio y proyecto.** El **ejercicio** de la unidad 2 es completar el
> plan de la Registraduría. El **proyecto** es el plan de pruebas del **caso
> asignado a su equipo** (ver el [catálogo de casos](https://github.com/CesarAVegaF312/TYVS-Proyecto_Pruebas_Unitarias/blob/master/casos/README.md)),
> con el enfoque (ágil o cascada) que el equipo eligió en la unidad 1. Es el
> plan con el que se probará ese mismo sistema en las unidades 3 a 5.

### 1) Ejercicio: el plan de la Registraduría

- Las secciones marcadas **EJERCICIO** de [`ejemplo-registraduria/plan-de-pruebas.md`](ejemplo-registraduria/plan-de-pruebas.md), completas.
- `mvn verify -Dplan=../ejemplo-registraduria` en **verde**: sin hallazgos y
  con 100% de cobertura funcional. Pegue la salida en el plan.
- La versión ágil de la sección 12 (historias, criterios Given–When–Then,
  DoR/DoD y dos sprints) y la **tabla del módulo 6** completa con evidencia de
  sus dos planes.

### 2) Proyecto: el plan del caso de su equipo

En el repositorio del proyecto, una carpeta `plan/` creada a partir de
[`plantillas/`](plantillas/), con la plantilla de su enfoque:

- **Análisis** del caso: funcionalidades, módulos críticos y alcance.
- **Requisitos o historias**: las reglas de la ficha, la restricción de
  unicidad, el comportamiento ante la caída del servicio externo y el SLO.
- **Riesgos**: cada requisito evaluado y justificado. Los **supuestos abiertos
  de la ficha** deben aparecer como preguntas abiertas en la sección de
  riesgos, con su decisión o con quién la tomará.
- **Diseño de casos** con técnicas, y la matriz verificada:
  `mvn verify -Dplan=<ruta a su plan>` en verde. Para que el verificador corra
  en el CI del proyecto, copie la carpeta `trazabilidad/` y agregue al
  workflow `mvn -B verify -Dplan=../plan` en esa carpeta.
- **Criterios** de entrada y salida, **estimación** y **calendario** hasta la
  unidad 5.

### 3) Reflexión final (en el plan)

Una de estas dos, a elección:

- ¿Qué requisito de su caso cambió de prioridad al evaluar su riesgo, y por
  qué su intuición inicial estaba equivocada?
- Si solo pudiera ejecutar el 20% de sus casos antes de una entrega, ¿cuáles
  elegiría y con qué criterio? ¿Ese criterio está escrito en su plan?

---

## Rúbrica

Evalúa el ejercicio. El plan del proyecto se evalúa con los mismos criterios,
aplicados al caso del equipo.

| **Criterios de evaluación** | **Indicadores** | **Excelente (5 pts)** | **Bueno (4 pts)** | **Necesita mejorar (3.5 pts)** | **Deficiente (2.5 pts)** | **No cumple (0 pts)** |
|---|---|---|---|---|---|---|
| **Estructura y verificación** | La matriz pasa el verificador. | Sin hallazgos y con 100% de cobertura funcional, salida pegada en el plan. | 1 o 2 hallazgos menores. | Varios hallazgos sin resolver. | El verificador no corre sobre el plan. | No entrega matriz. |
| **Análisis y requisitos** | Sistema analizado y requisitos verificables. | Criticidad justificada, alcance con exclusiones razonadas, todos los requisitos o historias verificables. | Análisis correcto, algún requisito ambiguo. | Análisis superficial o sin alcance. | Requisitos no verificables. | No analiza el sistema. |
| **Riesgos y priorización** **(vale por 2)** | Uso del riesgo para decidir. | Cada requisito evaluado y justificado; distingue impacto de riesgo; preguntas abiertas con responsable. | Evaluación completa con justificación parcial. | Notas asignadas sin justificar. | Todo con la misma prioridad. | No prioriza. |
| **Diseño de casos** **(vale por 2)** | Técnicas aplicadas, no listadas. | Clases, bordes (incluidos los no numéricos) y tabla de decisión para la precedencia, reflejados en la matriz. | Técnicas correctas con algún borde omitido. | Solo clases de equivalencia. | Casos sin técnica identificable. | No diseña casos. |
| **Criterios, estimación y calendario** | Verificabilidad y supuestos explícitos. | Criterios con números, estimación con supuesto auditable, calendario coherente con el enfoque. | Completo, algún criterio sin número. | Estimación sin supuesto o calendario genérico. | Criterios vagos ("cuando esté probado"). | No los incluye. |
| **Comparación ágil y cascada** | Evidencia de los dos planes. | Tabla del módulo 6 completa con ejemplos de sus dos planes y una conclusión situada. | Comparación clara con poca evidencia propia. | Comparación genérica. | Menciona diferencias sin análisis. | No compara. |
| **Documentación y reflexión** | Claridad y reflexión. | Plan legible, sin relleno, reflexión que conecta riesgo y decisión. | Claro pero sin profundidad. | Incompleto. | Mínimo. | Sin documentación. |

> **Cómo suma**: 9 criterios × 5 pts = **45 puntos**. Son 7 filas, pero
> *Riesgos y priorización* y *Diseño de casos* valen por dos cada una: son el
> núcleo del taller.

| Rango de puntaje | Desempeño |
| ---------------- | --------- |
| 41 – 45 | Excelente dominio técnico y metodológico. |
| 32 – 40 | Buen trabajo con documentación o análisis parcial. |
| 27 – 31 | Cumple con lo básico pero sin profundidad. |
| < 27 | No cumple con los criterios mínimos del taller. |

---

## Créditos y uso académico

Material del curso **Testing y Validación de Software**, Maestría en
Ingeniería de Software, **Universidad de La Sabana**.

Las plantillas se inspiran en los contenidos que la norma ISO/IEC/IEEE
29119-3 propone para la documentación de pruebas, adaptados a un proyecto
académico. No reproducen su texto.

---

### Licencia de uso

Este material se distribuye bajo la licencia [Creative Commons Atribución-NoComercial-CompartirIgual 4.0 Internacional (CC BY-NC-SA 4.0)](https://creativecommons.org/licenses/by-nc-sa/4.0/deed.es).

Puedes **usar, adaptar o compartir** este contenido con fines educativos, siempre que:

1. Se reconozca la autoría del profesor **César Augusto Vega Fernández**.
2. No se utilice con fines comerciales.
3. Las obras derivadas se distribuyan bajo la misma licencia.
