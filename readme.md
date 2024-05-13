### description
* example of how to externalize http session using redis

---

### requirements
* apache tomcat
* redis

---

### current settings

* http session timeout = 1 minute
* redis timeout = 2 minute

---

### test session management scenario

[call the servlet](http://localhost:8080/java-web-externalized-session/hello)  
* first call
adds new session attribute and prints all session attributes
* second call after 1 minute (when http session timeout occurs)
simulates situation when node is restarted or request is redirected to another node
session attributes will survive
* third call after 2 minutes (when redis timeout occurs)
everything is cleared
