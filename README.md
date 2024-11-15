### 인증 서버 구성 주의 사항
1. Session을 DB/Redis에 저장하여, 여러개의 인증서버가 핸들링 하도록 한다.
2. JWT 검증 방법
    1. Resource 서버 자체 검증 (자체 생산한 RSA PUB/PRI KEY을 jwt-set-uri을 통해 authorization 서버에 저장)
    2. Authorization 서버의 /oauth2/jwk을 통해, Authorization 서버가 생산한 키로 검증
3. Authorization Server가 재시작 되어도, jwk Set을 DB에 유지하고 있기 때문에, 기존에 발행한 AcessToken 검증이 가능하도록 한다.
4. 그럼 Resource 서버에서 발행하는 Key도 DB에 저장해서 관리?  

### 서버 구성

| port  | name                | desc            | 
|-------|---------------------|-----------------|
| :8082 | Client              | /index(UI)      |
| :8091 | ResourceServer      |                 | 
| :8092 | ResourceServer2     |                 | 
| :9000 | AuthorizationServer |                 |
| :8090 | GatewayServer       |                 |
| -     | -                   | -               |
| :8000 | JdbcClient          | UserDB(MYSQL)   | 
| :8081 | RestClient          | WebFlux Example |

### MYSQL

[oauth2-registered-client-schema.sql](https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/resources/org/springframework/security/oauth2/server/authorization/client/)

```mysql
CREATE TABLE oauth2_registered_client
(
    id                            varchar(100)                            NOT NULL,
    client_id                     varchar(100)                            NOT NULL,
    client_id_issued_at           timestamp     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret                 varchar(200)  DEFAULT NULL,
    client_secret_expires_at      timestamp     DEFAULT NULL,
    client_name                   varchar(200)                            NOT NULL,
    client_authentication_methods varchar(1000)                           NOT NULL,
    authorization_grant_types     varchar(1000)                           NOT NULL,
    redirect_uris                 varchar(1000) DEFAULT NULL,
    post_logout_redirect_uris     varchar(1000) DEFAULT NULL,
    scopes                        varchar(1000)                           NOT NULL,
    client_settings               varchar(2000)                           NOT NULL,
    token_settings                varchar(2000)                           NOT NULL,
    PRIMARY KEY (id)
);
```

[oauth2-authorization-consent-schema.sql](https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/resources/org/springframework/security/oauth2/server/authorization/oauth2-authorization-consent-schema.sql)

```mysql
CREATE TABLE oauth2_authorization_consent
(
    registered_client_id varchar(100)  NOT NULL,
    principal_name       varchar(200)  NOT NULL,
    authorities          varchar(1000) NOT NULL,
    PRIMARY KEY (registered_client_id, principal_name)
);
```

[](https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/resources/org/springframework/security/oauth2/server/authorization/oauth2-authorization-schema.sql)

```mysql
CREATE TABLE oauth2_authorization
(
    id                            varchar(100) NOT NULL,
    registered_client_id          varchar(100) NOT NULL,
    principal_name                varchar(200) NOT NULL,
    authorization_grant_type      varchar(100) NOT NULL,
    authorized_scopes             varchar(1000) DEFAULT NULL,
    attributes                    blob          DEFAULT NULL,
    state                         varchar(500)  DEFAULT NULL,
    authorization_code_value      blob          DEFAULT NULL,
    authorization_code_issued_at  timestamp     DEFAULT NULL,
    authorization_code_expires_at timestamp     DEFAULT NULL,
    authorization_code_metadata   blob          DEFAULT NULL,
    access_token_value            blob          DEFAULT NULL,
    access_token_issued_at        timestamp     DEFAULT NULL,
    access_token_expires_at       timestamp     DEFAULT NULL,
    access_token_metadata         blob          DEFAULT NULL,
    access_token_type             varchar(100)  DEFAULT NULL,
    access_token_scopes           varchar(1000) DEFAULT NULL,
    oidc_id_token_value           blob          DEFAULT NULL,
    oidc_id_token_issued_at       timestamp     DEFAULT NULL,
    oidc_id_token_expires_at      timestamp     DEFAULT NULL,
    oidc_id_token_metadata        blob          DEFAULT NULL,
    refresh_token_value           blob          DEFAULT NULL,
    refresh_token_issued_at       timestamp     DEFAULT NULL,
    refresh_token_expires_at      timestamp     DEFAULT NULL,
    refresh_token_metadata        blob          DEFAULT NULL,
    user_code_value               blob          DEFAULT NULL,
    user_code_issued_at           timestamp     DEFAULT NULL,
    user_code_expires_at          timestamp     DEFAULT NULL,
    user_code_metadata            blob          DEFAULT NULL,
    device_code_value             blob          DEFAULT NULL,
    device_code_issued_at         timestamp     DEFAULT NULL,
    device_code_expires_at        timestamp     DEFAULT NULL,
    device_code_metadata          blob          DEFAULT NULL,
    PRIMARY KEY (id)
);
```

![img.png](resource/img.png)
![login.png](resource/login.png)
![consent.png](resource/consent.png)
![users.png](resource/users.png)
![createUsers.png](resource/createUsers.png)
![authorities.png](resource/authorities.png)

## DB

## REF

### Basic
- [Enterprise Security with Spring Authorization Server 1.0 by Rob Winch @ Spring I/O](https://www.youtube.com/watch?v=ELz8wNt_Rys&pp=ygUdU3ByaW5nLmlvIGVudGVycHJpc2Ugc2VjdXJpdHk%3D)
- [Implementing an OAuth 2 authorization server with Spring Security - the new way! by Laurentiu Spilca](https://www.youtube.com/watch?v=DaUGKnA7aro)

### Chapter01 Authorization Server Custom User

- [Spring Boot 3.2.0 - Spring Authorization Server - Create, Update & Delete Users](https://www.youtube.com/watch?v=h609GfjOdfI)
  [🦊 spring_boot_client_04_cleanup_database](https://github.com/wdkeyser02/SpringBootSpringAuthorizationServer/tree/spring_boot_client_04_cleanup_database)

### Chapter02 Authorization Server & Gateway / SpringCloudGatewayAngularTutorial

- [Spring Boot 3 - Spring Cloud Gateway - Angular App - Spring Authorization Server OAuth2](https://www.youtube.com/watch?v=lGVjQxghx9w)
  [🦊 authorization_server ](https://github.com/wdkeyser02/SpringCloudGatewayAngularTutorial/tree/authorization_server)
- [Spring Boot 3 - Spring Cloud Gateway - Angular App](https://www.youtube.com/watch?v=NYNIoegNggg)
  [🦊 gateway ](https://github.com/wdkeyser02/SpringCloudGatewayAngularTutorial/tree/gateway)

### Chapter03-1 Rotating JWK Key / Spring Authorization Server - Spring Cloud Gateway - Angular App

- [🐻‍❄️ Angular Repository](https://github.com/wdkeyser02/AngularApp)
```
# angular app
$ ng serve --port 4200
```

- [ - Spring Boot 3.1.3](https://www.youtube.com/watch?v=0AhK6o39Cvc)
  [🦊 cloud_gateway](https://github.com/wdkeyser02/SpringSecurityCloudGatewayAngularTutorial/tree/cloud_gateway)

- [ Setup - Login & Logout](https://www.youtube.com/watch?v=wddvIxi-cCc)
  [🦊 resource_server](https://github.com/wdkeyser02/SpringSecurityCloudGatewayAngularTutorial/tree/resource_servers)
  [🦊 logout](https://github.com/wdkeyser02/SpringSecurityCloudGatewayAngularTutorial/tree/logout)

- [ - Setup JdbcUserDetailsManager](https://www.youtube.com/watch?v=HLPr7srzILI)
  [🦊 jdbc_user_details_manager](https://github.com/wdkeyser02/SpringSecurityCloudGatewayAngularTutorial/tree/jdbc_user_details_manager)

- [ - Setup JdbcRegisteredClientRepository](https://www.youtube.com/watch?v=ROlvCxH-q04)
  [🦊 jdbc_registered_client_repository](https://github.com/wdkeyser02/SpringSecurityCloudGatewayAngularTutorial/tree/jdbc_registered_client_repository)

- [ - Custom Login & Consent Screen](https://www.youtube.com/watch?v=zp0Ee3Gu0aA)
  [🦊 custom_consent](https://github.com/wdkeyser02/SpringSecurityCloudGatewayAngularTutorial/tree/custom_consent)

- [ Setup - Enable PKCE](https://www.youtube.com/watch?v=qcj-z7HjqbI)
  [🦊 jdbc_registered_client_repository_pkce](https://github.com/wdkeyser02/SpringSecurityCloudGatewayAngularTutorial/tree/jdbc_registered_client_repository_pkce)

- [ Setup - Enable JDBC Session](https://www.youtube.com/watch?v=KB3P8Y6XeKY)
  [🦊 jdbc_session](https://github.com/wdkeyser02/SpringSecurityCloudGatewayAngularTutorial/tree/jdbc_session)

- [ - Save Authorization & Consents to a Database](https://www.youtube.com/watch?v=rcNBKzQ2VYk)
  [🦊 jdbc_authorization](https://github.com/wdkeyser02/SpringSecurityCloudGatewayAngularTutorial/tree/jdbc_authorization)

- [ Setup - Keys Config](https://www.youtube.com/watch?v=tcIDW-9bM58)
  [🦊 keys](https://github.com/wdkeyser02/SpringSecurityCloudGatewayAngularTutorial/tree/keys)

- [ Setup - Rotating Keys (1)](https://www.youtube.com/watch?v=UPjIR4xFIkA)
  [🦊 rotate_keys](https://github.com/wdkeyser02/SpringSecurityCloudGatewayAngularTutorial/tree/rotate_keys)
- [ Setup - Rotating Keys (2)](https://www.youtube.com/watch?v=YlibRJj3KuU)
  [🦊 jdbc_rotate_keys](https://github.com/wdkeyser02/SpringSecurityCloudGatewayAngularTutorial/tree/jdbc_rotate_keys)
- [ Setup - Rotating Keys (3)](https://www.youtube.com/watch?v=2l9-flGO-5A)
  [🦊 jdbc_rotating_keys_encryptor](https://github.com/wdkeyser02/SpringSecurityCloudGatewayAngularTutorial/tree/jdbc_rotating_keys_encryptor)

### Chapter04-1 Google Authenticator (2Factor) / Spring Authorization Server - Spring Cloud Gateway - Angular App

- [ Setup - Two-Factor authentication 1](https://www.youtube.com/watch?v=EuQyNYRiYhk)
  [🫎 part01](https://github.com/wdkeyser02/SpringMfaAuthorizationServer/tree/part01)

- [ Setup - Two-Factor authentication 2](https://www.youtube.com/watch?v=mcDcrP2cZNU)
  [🫎 part02](https://github.com/wdkeyser02/SpringMfaAuthorizationServer/tree/part02)

- [Setup -Multi-Factor authentication](https://www.youtube.com/watch?v=kFiUmByi9DU)
  [🫎 part03](https://github.com/wdkeyser02/SpringMfaAuthorizationServer/tree/part03)
  [🦊 multi_factor_authentication](https://github.com/wdkeyser02/SpringSecurityCloudGatewayAngularTutorial/tree/multi_factor_authentication)

- [Spring Authorization Server - Multi-Factor authentication - Custom User Details](https://www.youtube.com/watch?v=NH8ihAxyflg)
  [🫎 part04](https://github.com/wdkeyser02/SpringMfaAuthorizationServer/tree/part04)

- [Spring Authorization Server - Multi-Factor authentication - Google Authenticator](https://www.youtube.com/watch?v=0dSgrhv2nrE)
  [🫎 part05](https://github.com/wdkeyser02/SpringMfaAuthorizationServer/tree/part05)

### Chapter03-2 Rotating JWK Key / Spring Authorization Server - Spring Cloud Gateway - Angular App
- [ - Custom User Details](https://www.youtube.com/watch?v=Z1y16K04joA)
  [🦊 custom_user_details_service](https://github.com/wdkeyser02/SpringSecurityCloudGatewayAngularTutorial/tree/custom_user_details_service)

- [ - Multi-Factor authentication - Google Authenticator](https://www.youtube.com/watch?v=RQzbow3ErZs)
  [🦊 multi_factor_authenticator_part_02](https://github.com/wdkeyser02/SpringSecurityCloudGatewayAngularTutorial/tree/multi_factor_authenticator_part_02)

### Chapter04-2 2Factor / Spring Authorization Server - Spring Cloud Gateway - Angular App

- [ - Multi-Factor authentication - Google Authenticator - Encrypted Secrets](https://www.youtube.com/watch?v=UNAsWf3tNpA)
- [🫎 Encrypted Secrets](https://github.com/wdkeyser02/SpringMfaAuthorizationServer/tree/part06)


### Trouble01

-[X] [TokenRelay Exception Due To ReadOnlyHttpHeaders #3568](https://github.com/spring-cloud/spring-cloud-gateway/issues/3568)
  - REF: https://github.com/spring-projects/spring-security/issues/15974
  - build.gradle  

-[X] [No AuthenticationProvider found for org.springframework.security.authentication.UsernamePasswordAuthenticationToken](https://github.com/spring-projects/spring-security/issues/13652)
 ```
     @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService customUserDetailsService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(customUserDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
 ```

-[X] Failed to serialize object using DefaultSerializer
-[X] The class with com.example.authorizationserver.user.CustomUserDetails and name of com.example.authorizationserver.user.CustomUserDetails is not in the allowlist. If you believe this class is safe to deserialize, please provide an explicit mapping using Jackson annotations or by providing a Mixin. If the serialization is only done by a trusted source, you can also enable default typing. See https://github.com/spring-projects/spring-security/issues/4370 for details
```
@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
public record User(
  . . .
) implements UserDetails {
```