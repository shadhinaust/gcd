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

