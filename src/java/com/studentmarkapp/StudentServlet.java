package com.studentmarkapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {
    
    // Initialize logger for "Log system activities" requirement
    private static final Logger LOGGER = Logger.getLogger(StudentServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String name = request.getParameter("studentName");
        String markStr = request.getParameter("studentMark");
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // 1. Start the HTML structure with a link to your CSS
        out.println("<html><head><link rel='stylesheet' href='style.css'></head><body>");
        out.println("<div class='card'>"); // This wraps your output in the CSS card

        try {
            // Check for empty inputs
            if (name == null || name.isEmpty() || markStr == null) {
                throw new Exception("Name and Mark are required fields.");
            }

            int mark = Integer.parseInt(markStr);
            LOGGER.info("Processing submission: Name=" + name + ", Mark=" + mark);
            
            // Validate the mark range
            if (mark < 0 || mark > 100) {
                throw new InvalidMarkException("Mark must be between 0 and 100");
            }
            
            String result = (mark >= 50) ? "Pass" : "Fail";
            
            // Display successful result with colors
            out.println("<h2>Result for " + name + ":</h2>");
            if ("Pass".equals(result)) {
                out.println("<h1 style='color:green;'>" + result + "</h1>");
            } else {
                out.println("<h1 style='color:red;'>" + result + "</h1>");
            }
            LOGGER.info("Calculation successful: " + result);
            
        } catch (InvalidMarkException e) {
            LOGGER.warning("Validation Error: " + e.getMessage());
            out.println("<h2 style='color:red;'>Error</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            
        } catch (NumberFormatException e) {
            LOGGER.severe("Number format error: Input was '" + markStr + "'");
            out.println("<h2 style='color:red;'>Error</h2>");
            out.println("<p>Please enter a valid number for the mark.</p>");
            
        } catch (Exception e) {
            LOGGER.severe("System error: " + e.getMessage());
            out.println("<h2 style='color:red;'>Error</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
        }

        // 2. Add a back button and close the tags
        out.println("<br><a href='index.html'>Back to Form</a>");
        out.println("</div></body></html>");
    }
}
