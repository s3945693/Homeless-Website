package app;

import java.util.ArrayList;

import org.sqlite.JDBC;

import io.javalin.http.Context;
import io.javalin.http.Handler;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2021. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class PageMission implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/mission.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" + 
               "<title>Our Mission</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "</head>";
        JDBCConnection jdbc = new JDBCConnection();

        // Add the body
        html = html + "<body>";

        // Add the topnav
        // This uses a Java v15+ Text Block
        html = html + """
            <div class='topnav'>
                <a href='/'>Homepage</a>
                <a href='mission.html'>Our Mission</a>
                <a href='page3.html'>View All LGAs</a>
                <a href='page4.html'>View One LGA</a>
                <a href='page5.html'>Advanced View of LGAs</a>
                <a href='page6.html'>Change over time</a>
            </div>
        """;

        // Add header content block
        html = html + """
            <div class='header'>
                <h1>Our Mission</h1>
            </div>
        """;

        // Add Div for page Content
        //html = html + "<div class='container'>";
 
        html = html + """
            <div class='container'>
                <div class = 'left-div'>
                        <h2>How does our website addersses the challenge?</h2>
                        <p> The website aims to help educate its users on the issue of homelessness, which is prevalent throughout Australia. The website also guides the user in how they could help tackle the issue. </p>                
                        <h2>How can this site be used?</h2>
                        <p> This website can be used to view simple or detailed statistics that are related to homelessnes. The user can find various statistical causes that could be related to homelessness. </p>
                        <h2>The persona's we target?</h2>""";
        ArrayList<Persona> Personas = jdbc.getPersonas();
        for (Persona persona : Personas) {
            html += "<h3>" + persona.Name + "</h3>";
            html += "<p>" + persona.Age + " year old " + persona.Gender + "</p>";
            html += "<p>" + persona.Description + "</p>";
            html += "<p>" + persona.Needs + "</p>";
            html += "<p>" + persona.Goals + "</p>";
            html += "<p>" + persona.Skills + "</p>";

        }  
        html += "<h2>This Website is made by:</h2>";
        ArrayList<Student> students =jdbc.getStudents();
        for (Student student : students) {
            html += "<p>" + student.ID + " - " + student.Name + "</p>"; 
        }
        html += """
                </div>
                <div class = 'right-div'>
                        <img src='homeless2.jpg' alt='homeless man' width='700'>
                        <img src='Homeless3.jpg' alt='homeless girl' width='700'>
                </div>
                
            </div>
                """;


        // Close Content div
        //html = html + "</div>";

        // Footer
        html = html + """
            <div class='footer'>
                <p>COSC2803 - Studio Project Team 71</p>
            </div>
        """;

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

}
