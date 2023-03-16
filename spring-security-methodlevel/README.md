## Method-level security

### spring-security-methodlevel aims to:
- Explain the benefits of method-level security and how to implement it using Spring Security
- Implement method-level security using Spring Security and enable it to provide more granular security to Spring Data methods by:
    - Creating a security protocol that restricts the deletion of videos to their respective uploaders.
    - Assigning ownership to VideoEntity objects in a POST request that creates new entries.
    - Rendering a delete button for each video along with its name in our mustache template, enabling users to delete the videos they own.


### Locking down access to the owner of the data

```java
  @PreAuthorize("#entity.username == authentication.name")
  @Override
  void delete(VideoEntity entity);
```
- @Override: This annotation will ensure that we don’t alter the name of the method or any other aspect of the method signature
- @PreAuthorize: This is Spring Security’s method-based annotation that allows us to write customized security checks 
- #entity.username: This de-references the entity argument in the first parameter and then looks up the username parameter using Java bean properties
- authentication.name: A Spring Security argument to access the current security context’s authentication object and look up the principal’s name

### Benefits of method-level security and how to implement it using Spring Security
- Spring Security also offers method-level security in addition to URL-based security provisions.
- Method-level security can be applied to controller methods, service methods, and any Spring bean's method calls.
- Applying method-level security provides a more granular level of control over access to resources.
- Using method-level security, specific methods within a class can be locked down, rather than applying security provisions to the entire class.
- Method-level security can be configured using annotations or XML configuration files.
- When using method-level security, it's important to define the roles and permissions necessary to access each method.

#### Including a username field to represent ownership of the data in our domain model:
```java
@Entity
class VideoEntity {
  private @Id @GeneratedValue Long id;
  private String username;
  private String name;
  private String description;
  protected VideoEntity() {
    this(null, null, null);
  }
  VideoEntity(String username, String name, String 
    description) {
      this.id = null;
      this.username = username;
      this.description = description;
      this.name = name;
    }
  // getters and setters
}
```

#### SecurityConfig method that initializes the user accounts with pre-defined roles using a CommandLineRunner bean:
```java
@Bean
CommandLineRunner initUsers(UserManagementRepository 
  repository) {
    return args -> {
      repository.save(new UserAccount("alice", "password", 
        "ROLE_USER"));
      repository.save(new UserAccount("bob", "password", 
        "ROLE_USER"));
      repository.save(new UserAccount("admin", "password", 
        "ROLE_ADMIN"));
  };
}
```
- This set of hard-coded users is utilized for authentication and authorization.

### Creating new entries with ownership assigned to the currently logged-in user

#### HomeController method that processes a POST request for creating a new video and redirects to the home page, using authentication to obtain the current user's name for the video creation:
```java
@PostMapping("/new-video")
public String newVideo(@ModelAttribute NewVideo newVideo,
  Authentication authentication) {
    videoService.create(newVideo,authentication.getName());
    return "redirect:/";
  }
```
- The @PostMapping("/new-video") annotation maps the newVideo method to an HTTP POST request with the URL /new-video.
- The newVideo() method takes two parameters, an instance of the NewVideo class that represents the video data submitted in the form, and an instance of the Authentication class that contains information about the current user's authentication status.
- The videoService.create() method is called to create the new video, passing in the newVideo object and the name of the currently authenticated user.
- After the new video has been created, the user is redirected to the home page using the "redirect:/" string.

#### VideoService method that creates a new VideoEntity object with the given username, name, and description, and saves it to the repository.
```java
public VideoEntity create(NewVideo newVideo, String 
  username) {
    return repository.saveAndFlush(new VideoEntity
      (username, newVideo.name(), newVideo.description()));
  }
```
- "create" is a method that takes two parameters: "newVideo" and "username".
- "newVideo" is an instance of the "NewVideo" class and contains information about the video to be created.
- "username" is a String representing the name of the user who is creating the video.
- The method returns an instance of the "VideoEntity" class created by calling the "saveAndFlush" method of the "repository" object.
- "saveAndFlush" saves the new "VideoEntity" instance to the database and returns the saved instance.
- "VideoEntity" instance is created by passing "username", "newVideo.name()", and "newVideo.description()" as parameters to its constructor.

### Rendering each video with a DELETE button that only the owner of the video can access

#### index.mustache template generates an HTML list of videos with a "Delete" button for each one, and includes a hidden CSRF token for security purposes:
```java
{{#videos}}
    <li>
        {{name}}
        <form action="/delete/videos/{{id}}" method="post">
            <input type="hidden" 
                name="{{_csrf.parameterName}}" 
                value="{{_csrf.token}}">
            <button type="submit">Delete</button>
        </form>
    </li>
{{/videos}}
```
- Iterate over videos array and render HTML line for each video with name and a form
- Form has a hidden _csrf input and button to submit POST request to /delete/videos/{{id}}

#### HomeController method that deletes a video with a specific ID and redirects to the home page:
```java
@PostMapping("/delete/videos/{videoId}")
public String deleteVideo(@PathVariable Long videoId) {
  videoService.delete(videoId);
  return "redirect:/";
}
```
- Responds to POST /delete/videos/{{id}} calls
- Extracts videoId from path variable and passes it to VideoService
- Returns "redirect:/" if successful

#### Finds and deletes a VideoEntity object by its ID or throws a runtime exception if it does not exist:
```java
public void delete(Long videoId) {
  repository.findById(videoId) //
    .map(videoEntity -> {
      repository.delete(videoEntity);
      return true;
    }) //
    .orElseThrow(() -> new RuntimeException("No video at " 
                                              + videoId));
}
```
- Looks up VideoEntity object using videoId
- Performs delete(entity) method using VideoEntity object
- Throws RuntimeException if Optional turns out to be empty

### Securing the deletion feature by allowing only the owner of the video to delete it
VideoRepository method that deletes a VideoEntity object:
```java
@PreAuthorize("#entity.username == authentication.name")
@Override
void delete(VideoEntity entity);
```
- Uses the "@PreAuthorize" annotation for method-level security checks.
- The annotation's argument is the expression "#entity.username == authentication.name".
- Spring Security evaluates this expression before executing the delete method.
- Confirms if the current authenticated user has authority to delete the entity.
- The expression checks if the VideoEntity's username matches the current authenticated user's name from the "authentication" object.
- Only a match allows the delete operation to proceed; otherwise, an exception is thrown.
- Restricts VideoEntity deletion to their owners, enforcing security.

### Enabling method-level security
- @EnableMethodSecurity is Spring Security’s annotation to activate method-based security
- Activates Spring Security’s more powerful @PreAuthorize annotation
- Leverages Spring Security’s simplified AuthorizationManager API

### Displaying user details on the site

