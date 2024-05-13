package org.example.servlet;

import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    /**
     * on every call adds new session attribute and prints all session attributes
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String randomString = getRandomString();
        session.setAttribute(randomString, randomString);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head></head><body>");
        out.println("<p>" + "sessionId: " + session.getId() + "</p>");
        for (Enumeration<String> e = session.getAttributeNames(); e.hasMoreElements(); ) {
            String attributeName = e.nextElement();
            out.println("<p>" + attributeName + " " + session.getAttribute(attributeName) + "</p>");
        }
        out.println("</body></html>");
    }

    private String getRandomString() {
        var length = 10;
        var useLetters = true;
        var useNumbers = true;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

}
