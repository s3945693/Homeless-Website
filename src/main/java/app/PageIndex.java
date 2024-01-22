package app;

import java.util.ArrayList;

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
public class PageIndex implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/";
    JDBCConnection jdbc = new JDBCConnection();

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Header information
        html = html + "<head>" + 
               "<title>Homepage</title>";

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
                <h1>
                    <img src='logo.png' class='top-image' alt='RMIT logo' height='75'>
                    Homepage
                </h1>
            </div>
        """;
        LGA max = jdbc.Fact2("max");
        LGA min = jdbc.Fact2("min");
        html = html + """
            <div class='container'>
                <div class='center-div'>
                    <h1><center> Homesless Facts </center></h1> 
                    <div class = 'firstOfThree'><p><center> <strong>""";
        html += String.format("%.2f", jdbc.Fact1(0, 0, 28)) + """
                    %</strong> of all homeless people are female with <strong>""";
        html += " " + String.format("%.2f", jdbc.Fact1(0, 0, 16)) + """
                    %</strong> of homeless people under 40 being female yet only <strong>""";
        html += " " + String.format("%.2f", jdbc.Fact1(0, 16, 28)) + """
                    %</strong> of over 40s are female</center> </p> </div>
                    
                    <div class = 'secondOfThree'><p><center>There are <strong>""";
        html += " " + String.format("%.2f", jdbc.Fact3()) + """ 
                    </strong> times as females between the ages 20 - 40 are at risk compared to males in the same age range</center> </p> </div>
                    

                    <div class = 'thirdOfThree'><p><center>The county with the highest homeless rate is <strong>""";
        html += max.name+ ", " + max.state + "</strong> with a homelessness rate of <strong>" + String.format("%.2f", max.percentage) + "%</strong>. ";
        html += " While the lowest is <strong>" + min.name + ", " + min.state + "</strong> with <strong>" + String.format("%.2f", min.percentage) + "%</strong> homeless </center></p> </div>" + """
                            
                            
                    
                    <h3><center>
                    From data of  
                    """;
                    html += jdbc.getTotalPopulation(2016) + " people";
                    html += " scattererd through " +jdbc.getLGACount()+" LGA's please note only " + jdbc.getFullLGAs() + " of these are full do to the imperfections of using real world data" + """
                    </center>
                    </h3>

                </div>
            </div>
            """;
        html = html +"""
                <div class>
                <h1>   </h1>
                </div>
                """;
                
            html = html + """
                <div class='container'>
                    <div class='center-div'>
                    <div class = 'firstOfThree'><p><center><a href='mission.html'><h2>Our Mission</h2></a></center> </p> </div>
                    <div class = 'secondOfThree'><p><center><a href='page3.html'><h2>View All LGAs</h2></a></center> </p> </div>
                    <div class = 'thirdOfThree'><p><center><a href='page4.html'><h2>View One LGA</h2></a></center></p> </div>
                    <div class = 'left-div'><center><a href='page5.html'><h2>Advanced View of LGAs</h2> </a></center></div>
                    <div class = 'right-div-home'><center><a href='page6.html'><h2>Change over time<h2></a></center></div>
                    </div>
                </div>
                """;
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
