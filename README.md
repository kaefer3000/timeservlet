# timeservlet
Servlet to supply the current time in a triple:
```bash
$ rapper http://localhost:8080/
```
yields
```N3
<http://localhost:8080/> <http://purl.org/dc/terms/created> "2017-10-30T02:25:05.843Z"^^<http://www.w3.org/2001/XMLSchema#dateTime> .
```
## Configuration
You can set the speed at which time passes (from startup time) in two ways:
* On command line, eg when starting using `mvn jetty:run` (tested)
```
-Dtimeservlet.speedupfactor=1000
```
* In some properties file, we also check the servlet's `getInitParameter("timeservlet.speedupfactor")` (untested)
