
# Servidor Web Tipo Apache con IoC

Proyecto el cual consiste en construir un servidor Web tipo Apache en Java, capaz de entregar páginas HTML e imágenes PNG, y proveer un framework IoC para aplicaciones web basadas en POJOs. El servidor atiende múltiples solicitudes no concurrentes y permite demostrar las capacidades reflexivas de Java.

## Comenzando

Estas instrucciones te permitirán obtener una copia del proyecto y ejecutarlo en tu máquina local para desarrollo y pruebas. Consulta la sección de despliegue para notas sobre cómo implementar el proyecto en un sistema en producción.

## Prerrequisitos

Antes de comenzar, asegúrate de tener instalado lo siguiente:

- Java JDK 8 o superior
- Maven 3.6+

Puedes descargar Java desde [Oracle](https://www.oracle.com/java/technologies/downloads/) y Maven desde [Apache Maven](https://maven.apache.org/download.cgi).

## Instalación
A continuación se describen los pasos para instalar y ejecutar el entorno de desarrollo:

1. **Clona el repositorio:**
    ```cmd
    git clone https://github.com/AngieRamosCortes/servidor-Web-tipo-Apache-.git

2. **Accede al directorio del proyecto:**
    ```cmd
    cd Swift-NetFramework
    ```

3. **Compila el proyecto usando Maven:**
    ```cmd
    mvn clean install
    ```

4. **Ejecuta la aplicación:**
    ```cmd
    mvn exec:java -Dexec.mainClass="co.edu.escuelaing.webframe.WebFrameworkApp"
    ```

Al finalizar, podrás acceder a la aplicación web desde tu navegador en `http://localhost:8080`.

## Ejecución de las pruebas
Para ejecutar las pruebas automatizadas del sistema:
```cmd
mvn test
```

Las pruebas unitarias se encuentran en el directorio `src/test/java/co/edu/escuelaing/webframe/ioc/SimpleIoCContainerTest.java` y validan el correcto funcionamiento del contenedor IoC y otros componentes clave.


## Construido con

- **Java** - Lenguaje principal
- **Maven** - Gestión de dependencias y construcción


## Authors
- **Angie Julieth Ramos Cortes** - Desarrollo completo


## License
Este proyecto está licenciado bajo la Licencia MIT - consulta el archivo `LICENSE` para más detalles.

## Acknowledgments
- Inspiración en frameworks como Spring y Dropwizard
- Agradecimientos a los profesores y compañeros de la Escuela de Ingeniería
- Recursos y documentación de Java y Maven

