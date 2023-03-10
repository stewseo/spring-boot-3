
## Create a Google OAuth2 client, Register it with Spring Boot 3, and serve up YouTube data

### <span style="color:yellow">**Delegate to an external system such as Google to offload risk for user management**</span>

An OAuth2-authorized client is represented in Spring Security OAuth2 using OAuth2AuthorizedClient.
Spring boot autoconfigures ClientRegistrationRepository as well as OAuth2AuthorizedClientRepository to
- parse necessary properties in the application.yaml file
- facilitate the flow between our application and OAuth2 providers

### <span style="color:green">**Configure properties in Application.yml**</span>
- clientID and clientSecret to authenticate with Google.
- scope to leverage the YouTube Data API.

### <span style="color:green">**Define bean to broker requests**</span>

- The clientManager() bean definition will request the two autoconfigured Oauth2 beans and blend them together into DefaultOAuth2AuthorizedClientManager. This bean will do the legwork of pulling the necessary properties from application.yaml and using them in the context of an incoming servlet request.
- When Spring Security OAuth2 is put on the class-path, Spring Boot auto-configures policies that will automatically combine the OAuth 2 beans with OAuth2AuthorizationClientManager

### <span style="color:yellow">**Invoking an OAuth2 API remotely (Invoking Google's YouTube Data API)**</span>

### <span style="color:green">**Define beans to create a client proxy that implements our Http service interface**</span>
- The idea of HTTP client proxies is to capture all the details needed to interact with a remote service in an interface definition and let Spring Framework, under the hood, marshal the request and response.
- Hooking OAuth 2 support into an HTTP remote service invoker
  - Initialize a WebClient with our OAuth2 exchange filter function
  - Wrap the WebClient in a HttpServiceProxyFactory
  - Create client proxy with type: YouTube

### <span style="color:green">**Define Declarative Http Service Interface**</span>
- channelVideos() method specifies the HTTP verb along with the URL
  - For HTTP remoting, @GetExchange tells Spring Framework to remotely invoke /search?part=snippet &type=video using an HTTP GET call.
  - The path in the @GetExchange call is appended to the configured base URL, https://www.googleapis.com/youtube/v3, forming a complete URL to access this API.
  - This method has three @RequestParam inputs: channelId, maxResults, and order.
    - The order parameter is constrained to what the API considers acceptable values using a Java enum, Sort.
    - The names of the query parameters are lifted from the method's argument names.

### <span style="color:green">**Capture the API Response with Java 17 records**</span>
- https://developers.google.com/youtube/v3/docs/search

### <span style="color:yellow">**Creating an OAuth2-powered web app**</span>

### <span style="color:green">**Define Web Controller**</span>
- @Controller indicates that this is a template-based web controller. Each web method returns the name of a template to render.
- We are injecting the YouTube service through constructor injection
- The index method has a Spring MVC Model object, where we create a channelVideos attribute, It invokes our YouTube service's channelVideos method with
  - a channel ID
  - a page size of 10
  - and uses view counts as the way to sort search results.
  - The name of the template to render is index.

### <span style="color:green">**Define Mustache templating engine**</span>
- The name of the template expands to src/main/resources/templates/index.mustache.
- Mustache directives are wrapped in double curly braces, whether it’s to iterate over an array ({{#channelVideos.items}}) or a single field ({{id.videoId}}).
- A Mustache directive that starts with a pound sign (#) is a signal to iterate, generating a copy of HTML for every entry. Because the SearchListResponse items fields are an array of SearchResult entries, the HTML inside that tag is repeated for each entry.

### <span style="color:green">**Applying CSS to our table**</span>
- Create src/main/resources/static/style.css
- Spring MVC will serve up static resources found in src/main/resources/static automatically

![Screenshot_20230309_110348](https://user-images.githubusercontent.com/54422342/224247018-8d13ded6-cb8e-4e16-95e9-887dbd6d9280.png)

