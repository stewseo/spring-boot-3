## Creating a Spring-Boot Web Application

This project has the following goals:
- Inject demo data using a service
- Create a web controller that uses Mustache to render dynamic content based on the demo data
- Create a JSON-based API allowing third-party apps to interact with our web app, whether it’s to retrieve data or send in updates
- Leverage Node.js to introduce some JavaScript to our web app using Gradle plugin:


### Creating a Spring MVC web controller

Web controllers are bits of code that respond to HTTP requests. These can comprise an HTTP GET/request that is asking for the root URL. Most websites respond with some HTML. But web controllers can also answer requests for APIs that yield JavaScript Object Notation (JSON), such as HTTP GET /api/videos. Furthermore, web controllers do the heavy lifting of transporting provided JSON when the user is affecting change with an HTTP POST.

Spring MVC is Spring Framework’s module that lets us build web apps on top of servlet-based containers using the Model-View-Controller(MVC) paradigm.

This dependency puts Spring MVC on our project’s classpath and gives us access to Spring MVC’s annotations and other components, allowing us to define web controllers. Its presence will trigger Spring Boot’s autoconfiguration settings to activate any web controller we create.

### Creating JSON-based APIs

### Hooking in Node.js to a Spring Boot web app

### Bundling JavaScript with Node.js

### Creating a React.js app

