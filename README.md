# IRONHACK FINAL PROJECT

## BACKEND API REST BANK

Proyecto realizado como trabajo de fin de Bootcamp en Ironhack. Consiste en simular el Backend de una "Entidad Bancaria" utilizando el lenguaje de programación Java y el Framework de SpringBoot.

La API nos permitirá:

- Crear usuarios (Admins/Account Holders/ThirdParty).

- Crear cuentas (Checking Accounts/Student Accounts/Saving Accounts/Credit Cards).

- Ver cuentas y balances.

- Hacer transferencias entre distintas cuentas.

- Modificar balances o parámetros de los usuarios.


##  CONTENIDO DEL PROYECTO

**1.** Archivos de configuración y directorio del proyecto

**2.** Diagramas UML y de uso

**3.** Archivos exportados de Postman (.JSON) preparados para ser importados y poder testear el programa mas fácilmente, ya que contiene tanto los métodos HTTP, las rutas, los JSON, HEADERS asi como las credenciales necesarias.

[![Image from Gyazo](https://i.gyazo.com/ccf925ad263ae3c680e5384680f360a9.png)](https://gyazo.com/ccf925ad263ae3c680e5384680f360a9)

## INSTRUCCIONES DE USO

- Deberemos descargar MYSQL (ya que es la base de datos con la que trabaja la API) y crear un schema con el nombre de "bank".
- Cuando tengamos nuestro proyecto descargado, en application.properties deberemos modificar nuestras credenciales username y password de la base de datos, como también el puerto si se desea.
- Cuando ejecutamos el programa nos crea por defecto un Admin(username: AdminUser)(password: 123456).
- Ya podemos testear todas las funcionalidades.

## SEGURIDAD DE LA API

Por como está diseñada la API (especificaciones del proyecto), la seguridad se distribuye de la siguiente manera:

- ROLES

**1** Admin

**2** AccountHolder

**3** ThirdParty

Dependiendo del rol que tengas podrás acceder a un tipo de rutas u otras

- OTROS

Además de los roles existen otros aspectos de seguridad a tener en cuenta que se aplican en determiandas funcionalidades como:

El Authentication principal.

Pasar credenciales en el Header de la petición HTML.


# Autor

Manuel Ortega Fernández
https://github.com/Manuelorfe


