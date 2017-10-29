# timeservlet
Servlet to be deployed in a servlet container.
Alternatively, the servlet can be started using `mvn jetty:run`.
The servlet supplies the current time in a triple:
```bash
$ rapper http://localhost:8080/
```
yields
```Turtle
@prefix dct: <http://purl.org/dc/terms/> .
@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .

<>
    dct:created "2017-10-29T15:53:44.498Z"^^xsd:dateTime .
```
## Configuration
You can set the speed at which time passes (from startup time) in two ways:
* On command line, eg when starting using `mvn jetty:run` (tested)
```
-Dtimeservlet.speedupfactor=1000
```
* In some properties file, we also check the servlet's `getInitParameter("timeservlet.speedupfactor")` (untested)
