
## Create a Google OAuth2 client, Register it with Spring Boot 3, and serve up YouTube data

### Add OAuth Client to a Spring Boot project

The web we are building, which speaks to Google, can be described as an OAuth2-authorized client. 
This is represented in Spring Security OAuth2 using OAuth2AuthorizedClient. 
Spring boot autoconfigures ClientRegistrationRepository 
as well as OAuth2AuthorizedClientRepository to
- parse necessary properties in the application.yaml file
- facilitate the flow between our application and one or more OAuth2 providers(Google, GitHub, Facebook, Okta, etc..)

#### Define properties: client credentials and OAuth2 scope in Application.yml
- clientID and clientSecret to authenticate with Google.
- scope to leverage the YouTube Data API .

#### SecurityConfig contains a Bean Definition to Broker Requests
- The clientManager() bean definition will request the two autoconfigured Oauth2 beans and blend them together into DefaultOAuth2AuthorizedClientManager. This bean will do the legwork of pulling the necessary properties from application.yaml and using them in the context of an incoming servlet request.
- When Spring Security OAuth2 is put on the class-path, Spring Boot auto-configures policies that will automatically combine the OAuth 2 beans with OAuth2AuthorizationClientManager

### Invoking an OAuth2 API remotely (Invoking Google's YouTube Data API)

#### YouTubeConfig contains Bean Definitions to create a client proxy that implements our YouTube Http service interface
  - Hooking OAuth 2 support into an HTTP remote service invoker
    - Initialize a WebClient with our OAuth2 exchange filter function
    - Wrap the WebClient in a HttpServiceProxyFactory
    - Create client proxy with type: YouTube
    
### Creating an OAuth2-powered web app
