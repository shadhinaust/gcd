# Google Calendar Data
## Simply read the google calendar events
#### Tech Spec
1. Java 11
2. Spring Boot 2.3.4
3. Maven 
4. Docker

#### Google Calendar API
You need to add your own OAuth credential from google cloud console enabling Calendar permission in resource directory. Update the redirect url accordingly.

Note: You can ignore as there is already a test credential available in the directory.
#### Run
```
docker image build -t gcd-app .
docker run -p 8080:8080 gcd-app:latest
```
Or

Run throw IDE or cmd

###### What you will find
1. Google authorization.
2. Upcoming current day events from your google calendar.
3. Available current time slots.
4. Test cases.

###### References
1. https://developers.google.com/calendar/quickstart/java
2. https://github.com/kriscfoster/SpringOAuth2Demo
3. https://libs.garden/java/a2cart/google-calendar-api
4. https://github.com/RameshMF/todo-application-jsp-servlet-jdbc-mysql
5. https://developers.google.com/calendar/v3/reference/events/list
6. https://dzone.com/articles/build-package-and-run-spring-boot-apps-with-docker
7. https://stackoverflow.com/questions/27767264/how-to-dockerize-maven-project-and-how-many-ways-to-accomplish-it

