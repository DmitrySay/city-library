## Task statement

### Create an enterprise-grade "city list" application (it will stay there for years, will be extended and maintained) which allows the user to do the following:

- browse through the paginated list of cities with the corresponding photos
- search by the name
- edit the city (both name and photo)

### Notes

- initial list of cities should be populated using the attached cities.csv file
- city addition, deletion and sorting are not in the scope of this task

## Technical clarification

- Spring Boot
- any build system
- any database
- any frontend stack
- Usage of Spring Data REST is prohibited

### Bonus points (not mandatory)

- Frontend stack should be one from the list below:
  Angular/VueJS/React
- Editing the city should be only allowed for users with Spring Security's role ROLE_ALLOW_EDIT

### Expected outcome

A repository link to your GitHub account from where you can clone the solution and run it with little-tozero effort.
Should you need any further information, please do not hesitate to contact the sender of this task

## How to get started application in docker:

Execute from root directory:

```
bash startup.sh
```

- Script build backend image and database image then start containers.
- Flyway migration create tables and populate cities from the cities.csv file.
- For local development use `dev` profile. VM OPTIONS: -Dspring.profiles.active=dev

### Default backend API users:

- ADMIN creds email=admin@email.com, password=123456789
- USER creds email=user@email.com, password=123456789

### Link to SWAGGER 
http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config

### Link to ui
- https://github.com/DmitrySay/city-library-ui

### Update application.yml with your Google email credentials

Caused by: javax.mail.AuthenticationFailedException: 534-5.7.9 
Application-specific password required. Learn more at
534 5.7.9  https://support.google.com/mail/?p=InvalidSecondFactor - gsmtp

To fix it, follow this guide to create an App Password
https://support.google.com/accounts/answer/185833?p=InvalidSecondFactor

Read article
https://mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/
