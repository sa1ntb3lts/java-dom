# Práctica de laboratorio: Interpretación y representación de gráficos SVG utilizando Java DOM y Java 2D


## 1. Propósito de la práctica

Desarrollar una aplicación Java que permita interpretar documentos SVG (*Scalable Vector Graphics*) mediante el modelo DOM (*Document Object Model*) y representar gráficamente los elementos `line`, `circle`, `polyline`, `rect`, `ellipse` y `text` utilizando Java Swing y Java 2D.

La práctica permitirá aplicar los conocimientos de procesamiento de documentos XML, navegación de árboles DOM, programación orientada a objetos y generación de gráficos bidimensionales.

## 2. Objetivo de aprendizaje

Al finalizar la práctica, el estudiante será capaz de desarrollar una aplicación Java que procese archivos SVG mediante DOM, extraiga los atributos y contenidos de sus elementos, construya una representación interna de las figuras y genere su visualización utilizando Java 2D.

## 3. Contexto y planteamiento del problema

SVG es un formato de gráficos vectoriales basado en XML que permite describir figuras geométricas, texto y otros elementos visuales mediante etiquetas y atributos.

Los navegadores web interpretan estos documentos y generan su representación gráfica. Sin embargo, también es posible implementar un visualizador básico utilizando Java.

**El reto consiste en desarrollar un visualizador SVG simplificado**, capaz de abrir un archivo `.svg`, analizar su estructura mediante DOM y representar los elementos gráficos encontrados dentro de una ventana Java Swing.

La aplicación deberá reconocer los siguientes elementos:

| Elemento SVG | Descripción |
|---|---|
| `line` | Segmento de línea definido por dos puntos. |
| `circle` | Circunferencia o círculo definido por centro y radio. |
| `polyline` | Secuencia de segmentos conectados. |
| `rect` | Rectángulo definido por posición y dimensiones. |
| `ellipse` | Elipse definida por centro y dos radios. |
| `text` | Cadena de texto posicionada mediante coordenadas. |

**No se permite utilizar bibliotecas externas que implementen automáticamente el procesamiento o renderizado SVG.**

## 4. Requerimientos funcionales

### RF-01. Lectura de documentos SVG

La aplicación deberá permitir seleccionar un archivo `.svg` desde el sistema de archivos.

El programa deberá:

1. Verificar que el archivo exista y pueda leerse.
2. Procesarlo mediante `DocumentBuilderFactory` y `DocumentBuilder`.
3. Construir un árbol DOM.
4. Identificar el elemento raíz `svg`.
5. Recuperar sus elementos gráficos y atributos.
6. Informar al usuario cuando el documento no pueda procesarse.

### RF-02. Interpretación de elementos gráficos

Para cada elemento reconocido, la aplicación deberá extraer los atributos geométricos necesarios.

| Elemento | Atributos geométricos |
|---|---|
| `line` | `x1`, `y1`, `x2`, `y2` |
| `circle` | `cx`, `cy`, `r` |
| `polyline` | `points` |
| `rect` | `x`, `y`, `width`, `height` |
| `ellipse` | `cx`, `cy`, `rx`, `ry` |
| `text` | `x`, `y`, contenido textual |

Además, deberá interpretar los atributos de presentación:

- `stroke`: color del contorno.
- `stroke-width`: grosor del contorno.
- `fill`: color de relleno.
- `font-family`: familia tipográfica del texto.
- `font-size`: tamaño de fuente.

Se deberán aplicar los valores predeterminados de SVG cuando un atributo no esté presente, dentro del subconjunto soportado por la aplicación.

### RF-03. Representación gráfica

La aplicación deberá utilizar `Graphics2D` para dibujar los elementos interpretados.

El dibujo se realizará dentro de un componente Swing, utilizando el método `paintComponent(Graphics g)`.

Los elementos deberán representarse respetando el orden en que aparecen en el documento SVG, para conservar su superposición.

### RF-04. Visualización de documentos

La interfaz deberá permitir:

- Seleccionar un documento SVG.
- Mostrar las figuras contenidas en el documento.
- Cargar un documento diferente sin reiniciar la aplicación.
- Mostrar mensajes descriptivos ante errores de lectura o interpretación.

## 5. Actividades de desarrollo

### Actividad 1. Análisis de la estructura XML de SVG

Investigue la estructura de un documento SVG e identifique la relación entre sus etiquetas XML y su representación visual.

Analice el siguiente ejemplo:

```xml
<svg xmlns="http://www.w3.org/2000/svg"
     width="500" height="350">
    <line x1="20" y1="20" x2="180" y2="100"
          stroke="blue" stroke-width="3"/>
    <circle cx="250" cy="100" r="50"
            fill="yellow" stroke="black"/>
</svg>
```

Explique cómo se representa cada elemento dentro de un árbol DOM y qué atributos se requieren para dibujarlo.

### Actividad 2. Implementación del procesador DOM

Implemente un componente que permita cargar un documento SVG y recorrer su estructura mediante DOM.

Deberá utilizar las clases e interfaces `DocumentBuilderFactory`, `DocumentBuilder`, `Document`, `Element`, `Node` y `NodeList`.

El procesador deberá identificar los seis tipos de elementos solicitados y recuperar sus atributos.

Considere que un documento SVG puede contener elementos anidados dentro de grupos `g`.

Configure el analizador para procesar espacios de nombres XML y deshabilitar la resolución de entidades externas.

### Actividad 3. Diseño de las clases de figuras

Diseñe una estructura orientada a objetos para representar internamente los elementos SVG.

Cada figura deberá almacenar sus propiedades geométricas y de presentación.

Se recomienda definir una abstracción común que permita representar los distintos tipos de figuras mediante una operación de dibujo.

El diseño deberá facilitar la incorporación de nuevos elementos sin modificar sustancialmente el componente encargado de visualizar las figuras.

**Producto:** diagrama de clases UML de la solución propuesta.

### Actividad 4. Implementación del renderizado

Implemente la representación gráfica de cada elemento utilizando Java 2D.

**A. Elemento `line`**

```xml
<line x1="30" y1="30" x2="250" y2="120"
      stroke="red" stroke-width="4"/>
```

Deberá dibujar el segmento definido por sus extremos, respetando color y grosor.

**B. Elemento `circle`**

```xml
<circle cx="150" cy="150" r="60"
        fill="orange" stroke="black"
        stroke-width="2"/>
```

Deberá calcular la región rectangular que delimita el círculo y representar tanto el relleno como el contorno.

**C. Elemento `polyline`**

```xml
<polyline points="20,200 80,150 140,200 200,150"
          fill="none" stroke="blue"
          stroke-width="3"/>
```

Deberá interpretar la secuencia de coordenadas, admitir múltiples vértices y dibujar los segmentos conectados. El contorno permanecerá abierto; cuando exista relleno, se aplicará el cierre implícito requerido para rellenar la figura.

**D. Elemento `rect`**

```xml
<rect x="50" y="50" width="180" height="90"
      fill="lightblue" stroke="navy"
      stroke-width="3"/>
```

Deberá interpretar la posición y dimensiones del rectángulo y dibujar su relleno y contorno.

**E. Elemento `ellipse`**

```xml
<ellipse cx="250" cy="160" rx="100" ry="45"
         fill="lightgreen" stroke="green"
         stroke-width="2"/>
```

Deberá calcular la región rectangular que delimita la elipse y representar correctamente figuras con radios diferentes.

**F. Elemento `text`**

```xml
<text x="60" y="300"
      font-family="Arial"
      font-size="24"
      fill="black">Gráficos SVG con Java</text>
```

Deberá recuperar el contenido textual mediante DOM y dibujarlo respetando la posición de su línea base, el tamaño de fuente y el color.

### Actividad 5. Desarrollo de la interfaz gráfica

Implemente una ventana Swing que contenga un área de dibujo y un mecanismo para seleccionar archivos SVG.

La aplicación deberá utilizar un componente derivado de `JPanel` para realizar la representación gráfica.

El método `paintComponent` deberá limitarse a dibujar las figuras previamente interpretadas. No deberá realizar operaciones de lectura de archivos ni procesamiento XML.

Cuando se cargue un nuevo documento, la aplicación deberá actualizar las figuras y solicitar el repintado del componente.

### Actividad 6. Pruebas e integración

Construya un archivo denominado `test_svg_dom.svg` que incluya al menos dos instancias de cada uno de los seis elementos solicitados.

El documento deberá utilizar diferentes posiciones, dimensiones, colores y grosores de contorno.

Realice las siguientes verificaciones:

1. El documento puede abrirse correctamente.
2. Se identifican todos los elementos.
3. Cada figura se representa en las coordenadas especificadas.
4. Los colores de relleno y contorno se aplican correctamente.
5. Los elementos se dibujan en el orden definido por el documento.
6. El programa maneja adecuadamente atributos inválidos y documentos XML mal formados.
7. Es posible cargar un segundo archivo sin reiniciar la aplicación.
8. La representación obtenida es comparable con la de un navegador compatible con SVG, considerando las limitaciones del visualizador.

## 6. Restricciones y alcance técnico

La implementación deberá cumplir las siguientes condiciones:

- Utilizar DOM para interpretar los documentos XML.
- Utilizar Swing y Java 2D para el renderizado.
- No emplear bibliotecas externas de renderizado SVG.
- Utilizar clases con responsabilidades claramente definidas.
- Evitar duplicación innecesaria de código.
- Conservar el orden de los elementos gráficos.
- Utilizar `double` para representar coordenadas y dimensiones.
- Implementar un mecanismo reutilizable para interpretar colores y atributos numéricos.
- Manejar los errores mediante excepciones y mensajes descriptivos.

El alcance se limitará a los seis elementos solicitados y a valores numéricos simples, colores básicos y hexadecimales, y atributos de presentación definidos directamente en cada elemento.

No se requiere implementar CSS, gradientes, transformaciones, máscaras, recortes, animaciones, rutas `path` ni soporte general para unidades SVG. Tampoco se requiere implementar el escalamiento automático mediante `viewBox`.

## 7. Entregables

Los estudiantes deberán entregar:

1. **Proyecto Java completo:** código fuente y archivos de configuración necesarios para su compilación.
2. **Archivo de pruebas:** `test_svg_dom.svg`, con los elementos solicitados.
4. **Evidencias de funcionamiento:** capturas de la aplicación y comparación con un navegador.
5. **README.md:** Reporte técnico con ladescripción de la arquitectura, principales decisiones de implementación, pruebas realizadas, problemas encontrados y limitaciones.

## 8. Criterios de evaluación

| Criterio | Ponderación |
|---|---:|
| Procesamiento del documento SVG mediante DOM | 15% |
| Implementación de `line` y `circle` | 10% |
| Implementación de `polyline` | 10% |
| Implementación de `rect` y `ellipse` | 15% |
| Implementación de `text` | 10% |
| Integración con Swing y Java 2D | 15% |
| Diseño orientado a objetos y calidad del código | 15% |
| Pruebas, documentación y evidencias | 10% |
| **Total** | **100%** |

Se evaluará la correspondencia entre los atributos del documento SVG y la representación gráfica obtenida, así como la organización del código, la reutilización de componentes y el manejo de errores.

## 9. Preguntas de reflexión

Responda las siguientes preguntas en README.md:

1. ¿Qué ventajas y desventajas presenta DOM frente a SAX para procesar documentos SVG?
2. ¿Cómo se relaciona la estructura jerárquica de un documento XML con su representación mediante objetos Java?
3. ¿Qué ventajas ofrece utilizar polimorfismo para implementar el renderizado de diferentes figuras?
4. ¿Por qué es conveniente separar el procesamiento XML de la representación gráfica?
5. ¿Qué dificultades aparecen al interpretar atributos geométricos expresados como cadenas de texto?
6. ¿Cómo podría ampliarse la arquitectura para incorporar nuevos elementos SVG?
7. ¿Qué modificaciones serían necesarias para implementar transformaciones geométricas como traslación, rotación y escalamiento?

