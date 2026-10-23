# java_dom_xml — Procesamiento de XML con Java DOM

Proyecto didáctico que reúne ejemplos independientes de **lectura, recorrido, creación, transformación y análisis de documentos XML** mediante el modelo **Document Object Model (DOM)** de Java. Incluye conversión de registros CSV a XML y elaboración de reportes de ventas a partir de datos XML.

> **Alcance:** los ejemplos son programas de consola independientes; no forman una única aplicación con menú ni constituyen una biblioteca de propósito general. 

## Contenidos y objetivos de aprendizaje

- Comprender la representación de XML como árbol de nodos (`Document`, `Element`, `Node`, `NodeList`, `Attr`).
- Leer y recorrer documentos XML con `DocumentBuilderFactory` y `DocumentBuilder`.
- Crear documentos XML en memoria y serializarlos con `Transformer`.
- Convertir datos tabulares CSV a elementos XML.
- Consultar datos XML, agrupar registros y calcular estadísticas.
- Identificar limitaciones y buenas prácticas de validación y seguridad al procesar XML.

## Requisitos

- **Gradle Wrapper:** configurado para **Gradle 9.8.0**; no es necesario instalar Gradle por separado.
- Sistema operativo con terminal; IntelliJ IDEA es opcional.
- No se requieren bibliotecas externas para los ejemplos DOM: se utilizan APIs incluidas en el JDK.

Verifique las herramientas:

```bash
java -version
./gradlew --version          # macOS / Linux
# gradlew.bat --version      # Windows
```

## Estructura del proyecto

```text
java_dom_xml/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── gradle/wrapper/
├── usuarios.csv
├── xml/
│   ├── breakfast.xml
│   ├── matricula.xml
│   ├── matricula.dtd
│   ├── sales.xml
│   ├── dummy.svg
│   ├── ejemplo.svg
│   ├── figuras.svg
│   ├── fox.svg
│   └── simple.svg
└── src/main/java/mx/unison/
    ├── Main.java
    └── dom/
        ├── e01/SimpleParsing.java
        ├── sales/SalesReport.java
        └── users/
            ├── CreateDOMDocument.java
            ├── CSV2XML.java
            └── CSVaXML.java
```

Los archivos `.svg` también son documentos XML y pueden servir para explorar el árbol DOM;
**este repositorio no implementa un renderizador SVG**.

## Compilación

Desde la raíz del proyecto:

```bash
./gradlew classes       # macOS / Linux
# gradlew.bat classes   # Windows
```

Las clases compiladas se generan en `build/classes/java/main`. El proyecto aplica el plugin `java`, pero **no** define un `mainClass` ni aplica el plugin `application`; por ello, `./gradlew run` no está configurado.

Para ejecutar cada ejemplo, utilice la terminal desde la **raíz del proyecto** (importante para los archivos relativos):

```bash
java -cp build/classes/java/main NOMBRE_COMPLETO_DE_LA_CLASE [argumentos]
```

En Windows se utiliza la misma sintaxis de `-cp` porque aquí se referencia un único directorio.

## Catálogo de programas

| Clase | Paquete | Función | Entrada | Salida |
|---|---|---|---|---|
| `SimpleParsing` | `mx.unison.dom.e01` | Recorrer un árbol DOM e inspeccionar nodos y atributos | Ruta de XML por argumento | Información en consola |
| `SalesReport` | `mx.unison.dom.sales` | Calcular ventas totales, filtrar por estado y agrupar por departamento | Ruta de XML por argumento | Reportes en consola |
| `CreateDOMDocument` | `mx.unison.dom.users` | Construir un documento XML desde cero | Ninguna | XML en consola |
| `CSV2XML` | `mx.unison.dom.users` | Convertir filas CSV a nodos XML | `usuarios.csv` fijo | XML en consola |
| `CSVaXML` | `mx.unison.dom.users` | Convertir CSV a XML y opcionalmente guardarlo | `usuarios.csv` fijo; ruta de salida opcional | XML en consola y, opcionalmente, archivo |

### 1. `SimpleParsing.java` — Exploración del árbol DOM

**Ruta:** `src/main/java/mx/unison/dom/e01/SimpleParsing.java`

**Propósito:** mostrar cómo abrir un XML, acceder al elemento raíz, inspeccionar atributos y 
recorrer recursivamente sus descendientes.

**Funcionamiento:**

1. Exige **exactamente un argumento**, la ruta del archivo XML.
2. Configura `DocumentBuilderFactory` con `XMLConstants.FEATURE_SECURE_PROCESSING`.
3. Analiza el documento y normaliza su elemento raíz.
4. Imprime el nombre y los atributos del elemento raíz.
5. Inspecciona el primer hijo mediante `printNode(Node)`.
6. Recorre los elementos hijos y sus descendientes con `processNode(Node)`, mostrando atributos y contenido de nodos de texto.

**Métodos relevantes:** `main(String[])`, `printNode(Node)` (privado), `processNode(Node)` (público y recursivo).

**Ejecución:**

```bash
java -cp build/classes/java/main mx.unison.dom.e01.SimpleParsing xml/breakfast.xml
```

También se pueden explorar otros XML del directorio `xml/`, incluidos archivos SVG.

**Limitaciones:** no valida contra XSD/DTD de forma explícita; ante un error de análisis capturado, `document` puede permanecer `null` y producir una excepción al acceder a la raíz. Los nodos de texto con formato o espacios pueden aparecer en la salida; el código no ofrece una representación estructurada con indentación por profundidad.

### 2. `SalesReport.java` — Análisis de ventas en XML

**Ruta:** `src/main/java/mx/unison/dom/sales/SalesReport.java`

**Propósito:** ilustrar consultas y agregaciones sobre un documento DOM con registros de ventas.

**Estructura de datos esperada:** raíz `<sales_doc>` con elementos `<sale_record>`, cada uno con `id`, `first_name`, `last_name`, `sales`, `state` y `department`. El archivo `xml/sales.xml` incluido sigue esa estructura y declara una DTD interna.

**Funcionamiento:**

1. Lee el XML recibido como único argumento.
2. `salesTotal(Document)`: busca todos los elementos `<sales>`, convierte su contenido a `double`, imprime cada importe y devuelve la suma.
3. `salesState(Document, String)`: recorre `<sale_record>` y muestra los registros cuyo `<state>` coincide exactamente con el texto solicitado.
4. `reporteVentas(Document)`: agrupa importes por departamento mediante `HashMap<String, Double>` e imprime el total y porcentaje de cada departamento respecto del total general.

**Ejecución:**

```bash
java -cp build/classes/java/main mx.unison.dom.sales.SalesReport xml/sales.xml
```

**Comportamiento importante:** el `main` invoca `salesState(doc, "Arizona")`; **el estado está fijo en el código**, no se recibe por línea de comandos. La salida incluye todas las ventas individuales antes de los resúmenes, por lo que puede ser extensa. El orden de departamentos no está garantizado porque se utiliza `HashMap`.

**Limitaciones:** se asume que todos los campos existen y que `<sales>` contiene números válidos; pueden producirse `NullPointerException` o `NumberFormatException` con datos incompletos. No se emplea `BigDecimal` para cálculos monetarios ni se contempla el caso de total cero al calcular porcentajes.

### 3. `CreateDOMDocument.java` — Creación de XML

**Ruta:** `src/main/java/mx/unison/dom/users/CreateDOMDocument.java`

**Propósito:** demostrar la construcción programática de un documento XML.

**Funcionamiento:** crea un `Document` vacío, añade un comentario, un elemento raíz `<root>`, un elemento hijo `<saludo lenguaje="sp">` y el texto `Hola mundo.`. `prettyPrint(Document)` utiliza `Transformer`, `DOMSource` y `StreamResult` para serializar el documento en la consola, configurando codificación UTF-8 e indentación.

**Ejecución:**

```bash
java -cp build/classes/java/main mx.unison.dom.users.CreateDOMDocument
```

**Estructura conceptual de salida:**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--Este es un comentario-->
<root>
    <saludo lenguaje="sp">Hola mundo.</saludo>
</root>
```

El formato exacto de espacios y declaración XML puede variar según el transformador. El ejemplo **no escribe un archivo**: solamente imprime el documento.

### 4. `CSV2XML.java` — Conversión básica CSV a XML

**Ruta:** `src/main/java/mx/unison/dom/users/CSV2XML.java`

**Propósito:** transformar registros separados por comas en un árbol XML.

**Entrada:** lee **siempre** `usuarios.csv` desde el directorio de trabajo. Cada registro debe contener cinco columnas, en este orden: ID, nombre, apellido, correo e IP. El archivo incluido no tiene fila de encabezado.

**Funcionamiento:**

1. Crea un documento con raíz `<USERS-DATA>`.
2. Lee cada línea con `BufferedReader`.
3. Separa los campos mediante `StringTokenizer(registro, ",")`.
4. Crea un `<USER>` por registro y los elementos `<ID>`, `<FIRST_NAME>`, `<LAST_NAME>`, `<EMAIL>` y `<IP>`.
5. Serializa el documento mediante `prettyPrint(Document)` y lo imprime en consola.

**Ejecución:**

```bash
java -cp build/classes/java/main mx.unison.dom.users.CSV2XML
```

**Fragmento ilustrativo:**

```xml
<USERS-DATA>
    <USER>
        <ID>1</ID>
        <FIRST_NAME>Walsh</FIRST_NAME>
        <LAST_NAME>Sunshine</LAST_NAME>
        <EMAIL>wsunshine0@google.fr</EMAIL>
        <IP>95.211.128.249</IP>
    </USER>
</USERS-DATA>
```

**Limitaciones:** `StringTokenizer` no implementa el estándar CSV (no respeta comillas, comas escapadas ni campos vacíos). Más de cinco campos provocarán un acceso fuera de rango a `elementName`. Si no se abre el archivo, el programa registra la excepción pero puede continuar con `in == null`. El lector no se cierra mediante *try-with-resources*.

### 5. `CSVaXML.java` — Conversión CSV a XML con exportación opcional

**Ruta:** `src/main/java/mx/unison/dom/users/CSVaXML.java`

**Propósito:** implementar una segunda estrategia de conversión CSV a XML, utilizando constantes para las posiciones de las columnas y un `switch` para crear elementos.

**Funcionamiento:**

1. Lee `usuarios.csv` desde el directorio de trabajo.
2. Crea el elemento raíz `<users-data>`.
3. Por cada registro, crea `<user>` y sus campos `<usr_id>`, `<first_name>`, `<last_name>`, `<email>` y `<usr_ip>`.
4. Imprime las líneas CSV originales y, después, el documento XML completo.
5. Si se proporciona **al menos un argumento**, interpreta el primero como ruta de salida y llama a `saveDocument(Document, File)`.

**Métodos:** `main(String[])`, `prettyPrint(Document)` y `saveDocument(Document, File)`.

**Ejecución sin guardar:**

```bash
java -cp build/classes/java/main mx.unison.dom.users.CSVaXML
```

**Ejecución guardando un archivo:**

```bash
java -cp build/classes/java/main mx.unison.dom.users.CSVaXML usuarios.xml
```

La ruta `usuarios.xml` es **de salida**; el CSV de entrada sigue siendo `usuarios.csv`.

**Limitaciones:** comparte las restricciones del procesamiento CSV simple. El método `saveDocument` utiliza `FileWriter` sin cierre explícito, aunque la transformación escribe en él; además, `System.out.println(out.toString())` imprime una representación del escritor, no confirma el contenido guardado. No se valida el número de columnas ni se parametriza la ruta del CSV. La codificación real de `FileWriter` depende de su configuración/entorno y no queda garantizada solamente por `OutputKeys.ENCODING`.

### 6. `Main.java` — Ejemplo de consola del IDE

**Ruta:** `src/main/java/mx/unison/Main.java`

**Propósito:** clase de ejemplo generada por IntelliJ IDEA, sin relación directa con DOM/XML.

Contiene un método `main()` sin argumentos y no estático que imprime `Hello and welcome!` y los valores de `i` del 1 al 5 mediante `IO.println`.

**Ejecución** (con JDK compatible):

```bash
java -cp build/classes/java/main mx.unison.Main
```

Esta clase motiva la recomendación de JDK 25 o posterior; si se utiliza un JDK más antiguo, la compilación completa del proyecto puede fallar aunque las clases DOM sean compatibles con versiones anteriores.

## Flujo general de procesamiento DOM

```text
Archivo XML ──> DocumentBuilder.parse(...) ──> Document (árbol DOM)
                                                    │
                                                    ├── getDocumentElement()
                                                    ├── getElementsByTagName()
                                                    ├── getChildNodes()
                                                    └── getTextContent()
                                                              │
                                                              └── Consultas / reportes

Archivo CSV ──> Lectura de registros ──> createElement()/createTextNode()
                                             │
                                             v
                                        Document DOM
                                             │
                                             v
                                  Transformer / StreamResult
                                             │
                                       Consola o archivo
```

## Archivos de datos incluidos

| Archivo | Uso recomendado |
|---|---|
| `usuarios.csv` | Entrada para `CSV2XML` y `CSVaXML` (20 registros, cinco columnas). |
| `xml/sales.xml` | Entrada para `SalesReport`; registros con ventas, estados y departamentos. |
| `xml/breakfast.xml` | Ejercicio de lectura y recorrido DOM. |
| `xml/matricula.xml` y `xml/matricula.dtd` | Estudio de estructura XML y declaraciones DTD. |
| `xml/*.svg` | Ejemplos adicionales de XML para inspección con `SimpleParsing`. |

## Actividades sugeridas

1. Ejecutar `SimpleParsing` con dos documentos XML de estructura diferente y comparar los nodos y atributos.
2. Modificar `SalesReport` para recibir el estado como segundo argumento, evitando el valor fijo `Arizona`.
3. Agregar un reporte de ventas promedio y número de registros por departamento.
4. Modificar `CSV2XML` para recibir la ruta del CSV y del XML de salida como argumentos.
5. Reemplazar `StringTokenizer` por un lector CSV que soporte campos entrecomillados y vacíos.
6. Implementar pruebas unitarias para los cálculos de ventas y la estructura XML generada.
7. Configurar el parser para rechazar entidades externas y estudiar los riesgos de XXE.

## Observaciones técnicas y oportunidades de mejora

- **Seguridad XML:** `FEATURE_SECURE_PROCESSING` se activa en los dos programas lectores, pero no equivale por sí solo a una configuración completa contra XXE. Conviene deshabilitar DTD y entidades externas cuando no se necesiten, o establecer una política explícita para documentos que sí requieran DTD.
- **Manejo de errores:** comprobar resultados de `parse`, presencia de nodos y formato numérico antes de acceder a ellos.
- **Recursos:** utilizar `try-with-resources` para lectores y escritores de archivos.
- **Separación de responsabilidades:** distinguir entrada/salida, análisis XML, transformación y presentación de reportes.
- **Datos monetarios:** preferir `BigDecimal` si los importes se usan con requisitos de precisión financiera.
- **CSV robusto:** admitir encabezados, comillas, campos vacíos, codificación explícita y validación de columnas.
- **Configuración:** sustituir rutas y filtros codificados por argumentos o configuración externa.
- **Pruebas:** añadir casos para archivos inexistentes, XML mal formado, datos incompletos, números inválidos y documentos vacíos.
- **Compatibilidad:** declarar un *Java toolchain* en Gradle y revisar si `Main.java` debe conservarse o excluirse de las prácticas DOM.

## Tecnologías y referencias

- [Java DOM API (`org.w3c.dom`)](https://docs.oracle.com/en/java/javase/25/docs/api/java.xml/org/w3c/dom/package-summary.html)
- [Java XML Parsers (`javax.xml.parsers`)](https://docs.oracle.com/en/java/javase/25/docs/api/java.xml/javax/xml/parsers/package-summary.html)
- [Java XML Transformation (`javax.xml.transform`)](https://docs.oracle.com/en/java/javase/25/docs/api/java.xml/javax/xml/transform/package-summary.html)
- [Gradle User Manual](https://docs.gradle.org/current/userguide/userguide.html)

---
