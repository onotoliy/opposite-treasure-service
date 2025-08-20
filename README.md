# opposite-treasure-service

```angular2html
spring.application.name=lenas-client 
server.port=8081  

## keycloak 
spring.security.oauth2.client.provider.lenas-realm.issuer-uri=http://localhost:8085/realms/lenas-realm  
spring.security.oauth2.client.registration.lenas-realm.provider=lenas-realm 
spring.security.oauth2.client.registration.lenas-realm.client-name=lenas-client-name 
spring.security.oauth2.client.registration.lenas-realm.client-id=lenas-client-id 
spring.security.oauth2.client.registration.lenas-realm.client-secret=veS8yqDACC5SDOTwfyX68pnJa81aI2ol 
spring.security.oauth2.client.registration.lenas-realm.scope=openid,offline_access,profile 
spring.security.oauth2.client.registration.lenas-realm.authorization-grant-type=authorization_code
```