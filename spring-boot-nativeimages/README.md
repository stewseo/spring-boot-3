## Going Native with Spring Boot

#### This project's goal is to demonstrate how to:
- Build native images using GraalVM. This is a faster, more efficient version of our application than we could ever build using the standard JVM.
    - Retrofit our application for GraalVM
    - Run our native Spring Boot application inside GraalVM
- Bake native images into a Docker container using Paketo Buildpacks.
    - Bake a Docker container with GraalVM

### What is GraalVM?

#### In the space of production systems, executable functions make it possible to deploy a single function as an entire application on platforms such as AWS Lambda. And their results could be piped right into another deployed function.
- Because functions are spun up immediately upon demand, startup time HEAVILY drives what technologies people use.
- GraalVM by Oracle is essentially a new virtual machine that  supports just about any programming language out there. We can run our Java JAR files on GraalVM instead of the JVM.
- Almost every facet of the Spring portfolio has been tuned and adjusted to support this endeavor to bring the power of GraalVM to any Spring Boot application.

### Retrofitting our application for GraalVM

Because of Spring Boot 3's adoption of native application support, we can update an existing application to use GraalVM instead of the JVM.

Java Virtual Machine code has always been compiled into bytecode, meant to be run on Java Virtual Machine(JVM). Any compiled Java bytecode, due to every aspect of these files being captured by the Java specification, can be run on any compliant JVM, no matter what machine it lives on.

#### Compiling applications for GraalVM involves trading in some of that preserved flexibility for faster, more memory-efficient code:
- Limited support for reflection because not everything is visible directly, it can require extra configuration so that nothing is missed.
- Limited support for dynamic proxies due to the above mentioned issues. Any proxies that are to be supported must be generated at the time of native image building.
- Special handling of external resources

GraalVM performs advanced analysis of our code using the reachability concept, where it essentially starts the app and then analyzes what code GraalVM can see. Anything that is NOT reachable is simply cut out of the final native image.

#### This means that accessing bits of code through reflection tactics, deserialization of data, and proxies are not as straightforward as they once were. The risk is that certain parts of our app may be cut out if we don’t properly capture them.
- Every Spring portfolio project ensures that whatever bits NEED to be in our applications have the necessary hints for GraalVM to find them.
- Spring Framework 6 has slimmed down its usage of reflection tactics to manage the application context.
- Spring Boot 3 has adopted a general approach of not proxying configuration classes containing bean definitions to reduce the number of actual proxies in an application.


### Running our native Spring Boot application inside GraalVM

Installing(bash environment) sdkman, an open source tool that allows you to install multiple JDKs and switch between them and allows us to install GraalVM’s own JDK, which includes all the tools needed to build native images on our machine:
```
ln -s "C:\Program Files\7-Zip\7z.exe" "C:\Git\mingw64\bin\zip.exe"
export SDKMAN_DIR="/c/sdkman" && curl -s "https://get.sdkman.io" | bash
```

#### To build native applications on GraalVM, we need to install and use a version of Java 17 that includes GraalVM tools by typing the following commands:
```
% sdk install java 22.3.r17-grl
% sdk use java 22.3.r17-grl
```

#### Taking a peek at what this version of Java has:
```
% java -version
openjdk version "17.0.5" 2022-10-18
OpenJDK Runtime Environment GraalVM CE 22.3.0 (build 17.0.5+8-jvmci-22.3-b08)
OpenJDK 64-Bit Server VM GraalVM CE 22.3.0 (build 17.0.5+8-jvmci-22.3-b08, mixed mode, sharing)
```
- This is OpenJDKversion 17, also known as Java 17, but it has GraalVM Community Edition(CE) version 22.3.0. Essentially, it has all the bits for Java 17 stirred together with GraalVM 22.3.

With GraalVM CE’s Java 17 active, we can build our application natively by executing the following command:
```
$ ./gradlew spring-boot-native:nativeCompile
```
- This command will compile our application with the native profile switched on. It leverages the gradle's native plugin.
- The process involves completely scanning the code and performing AOT compilation. Instead of leaving things in a bytecode format, to be converted into local machine code when the JVM starts up, it instead converts things in advance.
- This requires certain features to be curtailed, such as the usage of proxies and reflection. Part of Spring’s support in making itself GraalVM-ready was to reduce the usage of proxies and to avoid reflection when not necessary. There are ways to still use such features, but they can bloat up the native executable and remove some of the benefits. The AOT tools also can’t see everything on the other side of reflection calls and proxy usage, so they require additional metadata to be registered.
- The resulting artifact is neither an uber JAR file nor an executable JAR file. Instead, it’s an executable file for the platform it was built on.


### Baking a Docker container with GraalVM

To run our application on a cloud configuration based on Linux.

#### Build a native application inside a Docker container, assembled by Paketo Buildpack :
```
./mvnw -Pnative spring-boot:build-image
```
```
./gradlew :spring-boot-nativeimages:bootBuildImage
```
- This process may take even longer than building the native application locally, but the benefit is that, when completed, we will have a fully baked Docker container with a native application in it.