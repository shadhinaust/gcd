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
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.mohit.gcd.model.Agenda;
import com.mohit.gcd.service.GcdService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Controller
public class GcdController {
    @Autowired
    private GcdService gcdService;

    private final static Log logger = LogFactory.getLog(GcdController.class);
    private static final String APPLICATION_NAME = "gcd-app";
    private static HttpTransport HTTP_TRANSPORT;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static Calendar calendar;
    GoogleClientSecrets clientSecrets;
    GoogleAuthorizationCodeFlow flow;
    Credential credential;

    private static String REDIRECT_URI = "http://localhost:8080/login/oauth2/code/google";

    @RequestMapping("")
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public RedirectView login() throws GeneralSecurityException, IOException {
        return new RedirectView(getAuthorization());
    }

    @RequestMapping(value = "/agenda", method = RequestMethod.GET)
    public RedirectView agenda() throws GeneralSecurityException, IOException {
        return new RedirectView(getAuthorization());
    }

    @RequestMapping(value = "/login/oauth2/code/google", method = RequestMethod.GET, params = "code")
    public String oauth2SuccessCallback(ModelMap modelMap, @RequestParam(value = "code") String code) {
        String message = "Upcoming events";
        List<Agenda> agendas = new ArrayList<>();
        List<Agenda> freeSlots = new ArrayList<>();
        try {
            TokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
            credential = flow.createAndStoreCredential(tokenResponse, "userID");
            calendar = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
            agendas = gcdService.getAllAgendas(calendar);
            freeSlots = gcdService.getAllFreeTime(agendas);
            if(agendas.isEmpty()){
                message = "No upcoming events";
            }
        } catch (Exception exception) {
            message = "Opps! Internal Server error.";
            logger.info(exception.getMessage());
        }
        modelMap.addAttribute("message", message);
        modelMap.addAttribute("agendas", agendas);
        modelMap.addAttribute("freeSlots", freeSlots);
        return "agenda";
    }

    @RequestMapping(value = "login/oauth2/code/google", method = RequestMethod.GET, params = "error")
    public String oauth2ErrorCallback(ModelMap modelMap, @RequestParam(value = "error") String error) {
        modelMap.addAttribute("message", "Unauthorized");
        return "error";
    }

    @RequestMapping(value = "events", method = RequestMethod.GET, params = "error")
    public String events(@RequestParam(value = "error") Events events, @RequestParam(value = "error") String error) {
        return "index";
    }

    public String getAuthorization() throws IOException, GeneralSecurityException {
        AuthorizationCodeRequestUrl authorizationUrl;
        if (flow == null) {
            InputStream in = GcdController.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
            if (in == null) {
                throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
            }
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
                    Collections.singleton(CalendarScopes.CALENDAR_READONLY))
                    .build();
        }

        authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI);
        logger.debug(authorizationUrl);
        return authorizationUrl.build();
    }
}
