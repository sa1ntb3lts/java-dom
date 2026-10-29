# Visualizador SVG con Java DOM y Java 2D

Estudiante: Santiago Beltran Astorga 225203551

Aplicación orientada a objetos desarrollada en Java para el análisis, procesamiento y renderizado gráfico de archivos SVG utilizando el modelo **Document Object Model (DOM)** y la API de **Java 2D**.

---

## 1. Descripción de la Arquitectura

El sistema se diseñó bajo una estricta separación de responsabilidades (SoC) y principios de diseño orientado a objetos:

* **`SVGParser`**: Encargado de la lectura, análisis del archivo XML utilizando `DocumentBuilder`, validación de seguridad básica y recorrido recursivo de nodos del árbol DOM para extraer atributos geométricos y de presentación.
* **Jerarquía de Objetos (`SVGElement`)**: Una clase abstracta base con subclases concretas (`LineElement`, `CircleElement`, `RectElement`, `EllipseElement`, `PolylineElement` y `TextElement`) consolidadas modularmente para representar cada entidad geométrica y encapsular su propia lógica de dibujo.
* **`SVGCanvas`**: Componente gráfico (`JPanel`) que extiende el lienzo visual, configurando el contexto de renderizado con `Graphics2D` y aplicando antialiasing para un despliegue gráfico fluido.
* **`Main` (GUI)**: Controlador principal que levanta una interfaz gráfica de usuario basada en `JFrame` y `JScrollPane` para visualizar el contenido completo del SVG.

---

## 2. Decisiones de Implementación

1. **Clases de figuras unificadas**: Se optó por agrupar las clases concretas de figuras en un único archivo (`SVGElement.java`) usando clases con visibilidad de paquete, simplificando la estructura del proyecto sin romper el diseño modular ni el polimorfismo.
2. **Robustez en el parseo de atributos**: Se implementaron métodos auxiliares de conversión segura para evitar excepciones de formato (`NumberFormatException`) ante atributos vacíos, nulos o con unidades de medida mixtas.
3. **Soporte de estilos básicos**: Se integró un analizador de colores (`parseColor`) capaz de interpretar nombres comunes de colores y códigos hexadecimales (`#RGB` / `#RRGGBB`).

---

## 3. Pruebas Realizadas y Evidencias

* **Archivo de pruebas**: Se utilizó el archivo obligatorio `xml/test_svg_dom.svg`, el cual contiene al menos dos instancias de cada una de las 6 figuras requeridas (`line`, `circle`, `rect`, `ellipse`, `polyline` y `text`).
* **Carpeta de evidencias**: Las capturas de pantalla de la ejecución de la aplicación y su comparativa visual se encuentran almacenadas en la carpeta `pruebas/`.

---

## 4. Problemas Encontrados y Limitaciones

* **Unidades de medida**: El parser actual asume valores numéricos base en pixeles y elimina caracteres no numéricos; no interpreta conversiones complejas de unidades relativas (como `em`, `rem` o porcentajes avanzados).
* **Falta de soporte avanzado para SVG**: No se contemplan estilos embebidos en CSS (`<style>`), transformaciones complejas anidadas en matrices `transform`, ni degradados (`gradients`) avanzados, limitándose a atributos directos de presentación (`fill`, `stroke`, `stroke-width`).

---

## 5. Preguntas de Reflexión

### 1. ¿Qué ventajas y desventajas presenta DOM frente al procesamiento SAX para documentos SVG?
* **Ventajas de DOM**: Carga todo el documento XML en memoria como un árbol jerárquico de nodos, lo que permite acceso aleatorio, consulta bidireccional, modificación de elementos y análisis estructural completo (ideal para SVG donde se requiere inspeccionar grupos o relaciones espaciales).
* **Desventajas de DOM**: Alto consumo de memoria RAM en archivos SVG sumamente pesados o complejos, ya que mantiene toda la estructura cargada simultáneamente.
* **Frente a SAX**: SAX es un modelo basado en eventos (forward-only) que consume muy poca memoria al procesar flujos grandes, pero carece de memoria estructural, lo que dificulta enormemente realizar operaciones donde se necesite navegar libremente por el árbol o renderizar elementos en orden arbitrario.

### 2. ¿Cómo se relaciona la estructura jerárquica de un documento XML con su representación mediante objetos Java?
Existe un mapeo isomórfico directo: el nodo raíz `<svg>` actúa como el contenedor global, los nodos de grupo `<g>` funcionan como contenedores lógicos o colecciones, y las etiquetas hoja (figuras y textos) se corresponden con objetos instanciados de clases específicas. La jerarquía de nodos anidados se traduce directamente en una estructura orientada a objetos basada en composición y herencia.

### 3. ¿Qué ventajas ofrece utilizar polimorfismo para implementar el renderizado de diferentes figuras?
El polimorfismo permite tratar a todas las figuras geométricas de forma totalmente uniforme a través de su contrato común en la clase abstracta `SVGElement` (ej. invocando universalmente al método abstracto `draw(Graphics2D g2d)`). Esto desacopla por completo al `SVGCanvas` del tipo específico de figura que está dibujando, facilitando la adición de nuevas figuras sin modificar el código de renderizado existente.

### 4. ¿Por qué es conveniente separar el procesamiento XML de la representación gráfica?
Responde al principio de responsabilidad única (Single Responsibility Principle). Permite que la lógica de E/S, validación y análisis del DOM opere de forma independiente del motor gráfico. Gracias a esto, el código es más limpio, mantenible, fácil de probar mediante pruebas unitarias y permite reutilizar los modelos de datos en diferentes contextos (por ejemplo, exportar a otros formatos o realizar cálculos geométricos sin necesidad de abrir una ventana gráfica).

### 5. ¿Qué dificultades aparecen al interpretar atributos geométricos expresados como cadenas de texto?
* La presencia de sufijos de unidades (como `px`, `pt`, `in` o `%`).
* La variedad de formatos para expresar colores (nombres en inglés, hexadecimales, formatos RGB o RGBA).
* La ambigüedad en los delimitadores de coordenadas en atributos complejos como `points` en polilíneas, donde los valores pueden estar separados por comas, espacios o saltos de línea combinados.
* La necesidad de manejar excepciones seguras ante valores nulos, vacíos o cadenas malformadas en el archivo XML.

### 6. ¿Cómo podría ampliarse la arquitectura para incorporar nuevos elementos SVG (ej. `<polygon>` o `<path>`)?
1. Crear una nueva clase concreta (ej. `PolygonElement` o `PathElement`) que extienda de `SVGElement` e implementar su respectiva lógica en el método `draw(Graphics2D g2d)` utilizando las formas geométricas de Java 2D (`Path2D`, etc.).
2. Añadir un nuevo caso `case "polygon":` dentro de la estructura `switch` en el `SVGParser` para extraer los atributos específicos del nuevo elemento e instanciar la clase creada.

### 7. ¿Qué modificaciones serían necesarias para implementar transformaciones geométricas como traslación, rotación y escalamiento?
* **A nivel de modelo**: Agregar atributos que almacenen las transformaciones (o leer el atributo genérico `transform="..."` del nodo XML).
* **A nivel de renderizado**: Utilizar la clase `AffineTransform` provista por Java 2D u operar directamente sobre el objeto `Graphics2D` mediante `g2d.translate()`, `g2d.rotate()` y `g2d.scale()` antes de invocar el método `draw()` de cada elemento, restaurando el contexto gráfico (`AffineTransform` original) al finalizar para evitar efectos colaterales entre figuras.