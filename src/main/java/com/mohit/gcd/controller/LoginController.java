package com.mohit.gcd.controller;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.http.HttpClient;
import java.security.GeneralSecurityException;
import java.util.Collections;


@RestController
public class LoginController {
    private final static Log logger = LogFactory.getLog(LoginController.class);
    private static final String APPLICATION_NAME = "gcd-app";
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static Calendar calendar;
    GoogleClientSecrets clientSecrets;
    GoogleAuthorizationCodeFlow flow;
    Credential credential;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectURI;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public RedirectView googleConnectionStatus() throws Exception {
        return new RedirectView(getAuthorization());
    }

    private String getAuthorization() throws GeneralSecurityException, IOException {
        AuthorizationCodeRequestUrl authorizationUrl;
        if (flow == null) {
            InputStream in = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
            if (in == null) {
                throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
            }
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
                    Collections.singleton(CalendarScopes.CALENDAR_READONLY))
                    .build();
        }
        authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI);
        System.out.println("cal authorizationUrl->" + authorizationUrl);
        return authorizationUrl.build();
    }

    @RequestMapping(value = "/login/oauth2/code/google", method = RequestMethod.GET, params = "code")
    public String oauth2SuccessCallback(RedirectAttributes redirectAttributes, @RequestParam(value = "code") String code) {
        try {
            TokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
            credential = flow.createAndStoreCredential(tokenResponse, "userID");
            calendar = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
            Events eventList = calendar.events().list("primary").setMaxResults(10).setOrderBy("startTime").setSingleEvents(true).execute();
            redirectAttributes.addAttribute("status", "success");
            redirectAttributes.addAttribute("eventList", eventList);
        } catch (Exception exception) {
            logger.info(exception.getMessage());
        }

        return "redirect:/events";
    }

    @RequestMapping(value = "login/oauth2/code/google", method = RequestMethod.GET, params = "error")
    public String oauth2ErrorCallback(RedirectAttributes redirectAttributes, @RequestParam(value = "error") String error) {
        redirectAttributes.addAttribute("status", error);
        redirectAttributes.addAttribute("eventList", null);
        return "redirect:/events";
    }

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public String result(@RequestParam(value = "status") String status, @RequestParam(value = "eventList") Events eventList) {
        String response = "";
        try {
            if(status.equals("success")){
                response = eventList.toString();
            }else{
                response = "Unauthorized";
            }
        } catch (Exception exception) {
            logger.info(exception.getMessage());
        }
        return response;
    }
}
