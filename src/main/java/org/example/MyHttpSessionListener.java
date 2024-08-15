package org.example;

import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebListener
public class MyHttpSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("Session created with ID: " + se.getSession().getId());
        // Additional logic for session creation can be added here
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("Session destroyed with ID: " + se.getSession().getId());
        // Additional logic for session destruction can be added here
    }
}
