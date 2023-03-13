# Method-level security


## Locking down access to the owner of the data

```java
  @PreAuthorize("#entity.username == authentication.name")
  @Override
  void delete(VideoEntity entity);
```
- @Override: This annotation will ensure that we don’t alter the name of the method or any other aspect of the method signature
- @PreAuthorize: This is Spring Security’s method-based annotation that allows us to write customized security checks 
- #entity.username: This de-references the entity argument in the first parameter and then looks up the username parameter using Java bean properties
- authentication.name: A Spring Security argument to access the current security context’s authentication object and look up the principal’s name


## Enabling method-level security
- @EnableMethodSecurity is Spring Security’s annotation to activate method-based security
- Activates Spring Security’s more powerful @PreAuthorize annotation
- Leverages Spring Security’s simplified AuthorizationManager API

