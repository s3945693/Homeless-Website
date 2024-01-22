package app;

import java.util.ArrayList;

import javax.lang.model.util.ElementScanner14;

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
public class PageST21 implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page3.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" + 
               "<title>View All LGAs</title>";

        // Add some CSS (external file)
        html = html + """
            <link rel='stylesheet' type='text/css' href='common.css' />
            <link href='nouislider.css' rel='stylesheet'>
            <script src='nouislider.js'></script>
            <script src='wnumb.js'></script>
                """;
        
        html = html + "</head>";

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
                <h1>View All LGAs</h1>
            </div>
        """;

        // Add Div for page Content
        //html = html + "<div class='content'>";

        // Look up some information from JDBC
        // First we need to use your JDBCConnection class
        JDBCConnection jdbc = new JDBCConnection();

        // Next we will ask this *class* for the LGAs
        
        ArrayList<String> states = jdbc.getStates();

        html += """
            <div class='container'>
            <div class = 'center-div' id='form'>
            <center>
            <div class = 'firstOFThree'>
                <form action='/page3.html' method='post'>
                    <div class='form-group'>
                        <h3>Gender</h3>
                        <input type='radio' id='female' name='gender' value='female'>
                        <label for='female'>Female</label>
                        <br>
                        <input type='radio' id='male' name='gender' value='male'>
                        <label for='male'>Male</label>
                        <br>
                        <input type='radio' id='both' name='gender' value='both' checked='checked'>
                        <label for='both'>Both</label>
                        
                        
                    </div>
                    <br>
                    <div class='form-group'>
                        <h3>State</h3>
                        <label for='select_state'>Select a State</label>
                        <select id='select_state' name='select_state' value='All'> 
                                <option>All</option>""";
                                for (String state : states) {
                                    html += "<option>" + state + "</option>";
                                }
        html += """
                        </select>
                    </div>
                    </div>
                    
                    <div class = 'thirdOFThree'>
                    <div class='form-group'>
                        <h3>Group Shown</h3>
                        <input type='radio' id='Homeless' name='GroupShown' value='Homeless' checked='checked'>
                        <label for='Homeless'>Homeless</label>
                        <br>
                        <input type='radio' id='AtRisk' name='GroupShown' value='At Risk'>
                        <label for='AtRisk'>At Risk</label>
                        
                    </div>
                    <br>
                    <div class='form-group'>
                    <div>
                    <center>          
                        <p>Age Range (years) </p>
                        <div class='paddingpls'>
                        <input type='hidden' id='weight' name='weight'>
                                <input type='hidden' id='weights' name='weights'>
                                <p>&nbsp;</p>
                                <div id='slider'></div>
                                <p>&nbsp;</p>
                                <script>
                                
                                var slider = document.getElementById('slider');
                                noUiSlider.create(slider, {
                                    start: [0, 70],
                                    tooltips: true,
                                    step: 10,
                                    margin: 10,
                                    connect: true,
                                    range: {
                                        'min': 0,
                                        'max': 70
                                    },
                                    format: wNumb( { decimals: 0 })
                                });

                                var weightInput = document.getElementById('weight');
                                slider.noUiSlider.on('update', function (values, handle, unencoded) {
                                var weightValue = values[0];
                                weightInput.value = weightValue;
                                });

                                weightInput.addEventListener('change', function () {
                                slider.noUiSlider.set([null, this.value]);
                                });

                                var weightsInput = document.getElementById('weights');
                                slider.noUiSlider.on('update', function (values, handle, unencoded) {
                                var weightsValue = values[1];
                                weightsInput.value = weightsValue;
                                });

                                weightsInput.addEventListener('change', function () {
                                slider.noUiSlider.set([null, this.value]);
                                });
                        
                        </script>
                        </div>
                        </center>
                    </div>
                    </div>
                    </div>
                    
                    <div class = 'secondOFThree'>
                    <div class='form-group'>
                        <h3>Order by</h3>
                        <input type='radio' id='asc' name='orderby' value='asc'>
                        <label for='asc'>Ascending</label>
                        <br>
                        <input type='radio' id='desc' name='orderby' value='desc' checked='checked'>
                        <label for='desc'>Descending</label>
                    </div>
                    <br>
                    <div class='form-group'>
                        <h3>Displayed Value</h3>
                        <input type='radio' id='Percentage' name='display' value='Percentage'>
                        <label for='Percentage'>Percentage</label>
                        <br>
                        <input type='radio' id='Amount' name='display' value='Amount' checked='checked'>
                        <label for='Amount'>Amount</label>
                        
                    </div>
                    </div>
                   
                    <br>
                    <br>

                    <button type='submit' class='btn btn-primary'>Generate graph</button>
                </form>
                </center>
                </div>
                </div>
                <br>
                """;
                        
                       
        String select_state = context.formParam("select_state");
        select_state = (select_state == null) ? "All" : select_state;
        //String orderby = context.formParam("orderby");
        System.out.println(select_state);
        
        String display = context.formParam("display");
        display = (display == null) ? "Amount" : display;

        String gender = context.formParam("gender");
        gender = (gender == null) ? "both" : gender;
        int GenderIncre = (gender.compareTo("both") == 0) ? 2 : 4;
        int GenderStart = (gender.compareTo("male") == 0) ? 2 : 0;

        String order = context.formParam("orderby");
        order = (order == null) ? "desc" : order;
//i gave up and used weight cause i couldnt get minage/maxage to work
        String min_age = context.formParam("weight");
        System.out.print(min_age);
        min_age = (min_age == null) ? "0" : min_age;
        int IntMin_age = Integer.valueOf(min_age)/10 * 4;

        String max_age = context.formParam("weights");
        max_age = (max_age == null) ? "inf" : max_age;
        int IntMax_age = (max_age.compareTo("inf") == 0) ? 28 : Integer.valueOf(max_age)/10 * 4;


        String GroupShown = context.formParam("GroupShown");
        GroupShown = (GroupShown == null) ? "Homeless" : GroupShown;
        int intGroupShown = (GroupShown.compareTo("Homeless") == 0) ? 0 : 1;

        html += (IntMax_age <= IntMin_age)? "<h1>Please Select a max value that is more than the min value</h1>" : "" ;                        

        html += "<div class='container'>";
        html += "<div class = 'center-div' id='stuff'>";

        ArrayList<LGA> lgas = jdbc.getByLGAs(select_state, display, intGroupShown, GenderIncre, GenderStart, IntMin_age, IntMax_age, order);
        // Add HTML for the LGA list
        html = html + "<h1>" + display + " of " + GroupShown + " " + gender + " people in " + select_state + " LGAs aged " + min_age + " to " + max_age + "</h1><div class = 'left-div-small'>" + "<ul>";

        // Finally we can print out all of the LGAs
        for (LGA lga : lgas) {
            html = html + "<li>" + lga.name + " - ";
            if (display.compareTo("Amount") == 0) {
                html += lga.amount;
            } else {
                String StringPercentage = String.format("%.2f", lga.percentage);
                html += StringPercentage + "%";
            }
            
            html += "</li>";
        }

        // Finish the List HTML
        html = html + "</ul>";

        // Close Content div
        html = html + " </div>";
        html += """
                
            <div class = 'right-div-big'>   
                <div id='barchart'>

                    <script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script>
                    <script type='text/javascript'>
                    // Load google charts
                    google.charts.load('current', {'packages':['corechart']});
                    // Draw the chart and set the chart values
                    google.charts.setOnLoadCallback(drawChart);
                    function drawChart() {
                        var data = google.visualization.arrayToDataTable([

                            ['LGA', 'Homeless', { role: 'annotation' }],
                            """;
                            for (LGA lga : lgas) {
                                html += "['" + lga.name + "', ";
                                html += (display.compareTo("Amount") == 0 ) ? lga.amount + ", " : lga.percentage + ", ";
                                html += (display.compareTo("Amount") == 0 ) ? lga.amount + "]," : lga.percentage + "],";
                            } 
                            
                            
                            html += "]);" + "var options = {vAxis: {textStyle:{color: '#FFF'}}, hAxis: {textStyle:{color: '#FFF'}}, legendTextStyle: { color: '#FFF' }, titleTextStyle: { color: '#FFF' }, backgroundColor: '#0ea7f9', 'overflow':'enable', 'height' : '" + 20 * lgas.size() + "', bar: { groupWidth: '95%' }, 'chartArea': {'height': '99%'}, fontSize: '10' };";
                            html += """
                        
                        // Optional; add a title and set the width and height of the chart
                         
                        
                        // Display the chart inside the <div> element with id='piechart'
                        var chart = new google.visualization.BarChart(document.getElementById('barchart'));
                        chart.draw(data, options);
                    }
                    </script>
                </div>
            </div>
        </div>
        </div>
        """;

        // Footer
        html = html + """
            <div class='footer'>
                <p>COSC2803 - Studio Project Team 71 &#3486;</p>
            </div>
        """;

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

}
