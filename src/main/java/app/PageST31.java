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
public class PageST31 implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page5.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" + 
               "<title>Advanced View of LGAs</title>";

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
                <h1>Advanced View of LGAs</h1>
            </div>
        """;


        // Add Div for page Content
        //html = html + "<div class='content'>";

        // Look up some information from JDBC
        // First we need to use your JDBCConnection class
        JDBCConnection jdbc = new JDBCConnection();

        // Next we will ask this *class* for the LGAs
        
        ArrayList<String> states = jdbc.getStates();

        int MAXWeekIncome = jdbc.getMax("MedWeekIncome");
        int MAXMonMort = jdbc.getMax("MedMonMort");
        int MAXWeekRent = jdbc.getMax("MedWeekRent");
        int MAXMedAge = jdbc.getMax("MedAge");

        html += """
            <div class='container'>
            <div class = 'center-div' id='form'>
            <center>
            <form action='/page5.html' method='post'>
                <div class = 'firstOFThree'>

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
                        
                    <div class='form-group'>
                            <h3>Order by</h3>
                            <input type='radio' id='asc' name='orderby' value='asc'>
                            <label for='asc'>Ascending</label>
                            <br>
                            <input type='radio' id='desc' name='orderby' value='desc' checked='checked'>
                            <label for='desc'>Descending</label>
                            
                            <br>
                            <select id='order_column' name='order_column' value='Homeless/AtRisk'> 
                                    <option>Homeless/AtRisk</option>
                                    <option>Median Weekly Income</option>
                                    <option>Median Monthly Mortgage</option>
                                    <option>Median Weekly Rent</option>
                                    <option>Median Age</option>
                                    
                            </select>
                            <p style='font-size:29px;'>&nbsp</p>
                    </div>

                    <div class='form-group'>
                            <h3>Median Monthly Mortgage Range</h3>
                            <div class='paddingpls'>
                                <input type='hidden' id='minMMMR' name='minMMMR'>
                                        <input type='hidden' id='MaxMMMR' name='MaxMMMR'>
                                        
                                        <div class = 'thirdOFThree'><p>&nbsp;</p>
                                            <div class = 'slider-pad'>
                                            <div id='sliderThree'></div></div></div>
                                        
                                        <script>
                                        
                                        var slider = document.getElementById('sliderThree');
                                        noUiSlider.create(slider, {
                                            start: [0, 3250],
                                            tooltips: true,
                                            step: 50,
                                            margin: 51,
                                            connect: true,
                                            range: {
                                                'min': 0,
                                                'max': 3250
                                            },
                                            format: wNumb( { decimals: 0 })
                                        });
                                        var minMMMRInput = document.getElementById('minMMMR');
                                        slider.noUiSlider.on('update', function (values, handle, unencoded) {
                                        var minMMMRValue = values[0];
                                        minMMMRInput.value = minMMMRValue;
                                        });
                                        minMMMRInput.addEventListener('change', function () {
                                        slider.noUiSlider.set([null, this.value]);
                                        });
                                        var MaxMMMRInput = document.getElementById('MaxMMMR');
                                        slider.noUiSlider.on('update', function (values, handle, unencoded) {
                                        var MaxMMMRValue = values[1];
                                        MaxMMMRInput.value = MaxMMMRValue;
                                        });
                                        MaxMMMRInput.addEventListener('change', function () {
                                        slider.noUiSlider.set([null, this.value]);
                                        });
                                
                                </script>
                                </div>
                    </div>
                </div>

                <div class = 'thirdOfThree'>

                    <div class='form-group'>
                        <h3>State</h3>
                        <label for='select_state'>Select a State</label>
                        <br>
                        <select id='select_state' name='select_state' value='All'> 
                            <option>All</option>""";
                            for (String state : states) {
                                html += "<option>" + state + "</option>";
                            }
                            html += """
                        </select>
                        
                    </div>
                    <br>
                    <div class='form-group'>
                        <h3>Displayed Value</h3>
                        <input type='radio' id='Percentage' name='display' value='Percentage'>
                        <label for='Percentage'>Percentage</label>
                        <br>
                        <input type='radio' id='Amount' name='display' value='Amount' checked='checked'>
                        <label for='Amount'>Amount</label>
                        <p style='font-size:22px;'>&nbsp</p>
                    </div>
                    <br>
                    <div class='form-group'>
                        <h3>Median Weekly Rent Range</h3>
                        <div class='paddingpls'>
                                <input type='hidden' id='minMWR' name='minMWR'>
                                        <input type='hidden' id='MaxMWR' name='MaxMWR'>
                                        
                                        <div class = 'thirdOFThree'><p>&nbsp;</p>
                                            <div class = 'slider-pad'>
                                            <div id='sliderFour'></div></div></div>
                                        
                                        <script>
                                        
                                        var slider = document.getElementById('sliderFour');
                                        noUiSlider.create(slider, {
                                            start: [0, 3250],
                                            tooltips: true,
                                            step: 5,
                                            margin: 5,
                                            connect: true,
                                            range: {
                                                'min': 0,
                                                'max': 650
                                            },
                                            format: wNumb( { decimals: 0 })
                                        });
                                        var minMWRInput = document.getElementById('minMWR');
                                        slider.noUiSlider.on('update', function (values, handle, unencoded) {
                                        var minMWRValue = values[0];
                                        minMWRInput.value = minMWRValue;
                                        });
                                        minMWRInput.addEventListener('change', function () {
                                        slider.noUiSlider.set([null, this.value]);
                                        });
                                        var MaxMWRInput = document.getElementById('MaxMWR');
                                        slider.noUiSlider.on('update', function (values, handle, unencoded) {
                                        var MaxMWRValue = values[1];
                                        MaxMWRInput.value = MaxMWRValue;
                                        });
                                        MaxMWRInput.addEventListener('change', function () {
                                        slider.noUiSlider.set([null, this.value]);
                                        });
                                
                                </script>
                                </div>
                    </div>

                </div>

                <div class = 'secondOFThree'>
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
                        <h3>Median Weekly Income Range</h3>
                        <div class='paddingpls'>
                                <input type='hidden' id='minMWI' name='minMWI'><p>&nbsp;</p>
                                        <input type='hidden' id='MaxMWI' name='MaxMWI'>
                                        
                                        <div class = 'thirdOFThree'>
                                            <div class = 'slider-pad'>
                                            <div id='sliderFive'></div></div></div>
                                        
                                        <script>
                                        
                                        var slider = document.getElementById('sliderFive');
                                        noUiSlider.create(slider, {
                                            start: [0, 3500],
                                            tooltips: true,
                                            step: 50,
                                            margin: 51,
                                            connect: true,
                                            range: {
                                                'min': 0,
                                                'max': 3500
                                            },
                                            format: wNumb( { decimals: 0 })
                                        });
                                        var minMWIInput = document.getElementById('minMWI');
                                        slider.noUiSlider.on('update', function (values, handle, unencoded) {
                                        var minMWIValue = values[0];
                                        minMWIInput.value = minMWIValue;
                                        });
                                        minMWIInput.addEventListener('change', function () {
                                        slider.noUiSlider.set([null, this.value]);
                                        });
                                        var MaxMWIInput = document.getElementById('MaxMWI');
                                        slider.noUiSlider.on('update', function (values, handle, unencoded) {
                                        var MaxMWIValue = values[1];
                                        MaxMWIInput.value = MaxMWIValue;
                                        });
                                        MaxMWIInput.addEventListener('change', function () {
                                        slider.noUiSlider.set([null, this.value]);
                                        });
                                
                                </script>
                                </div>
                    </div>
                    <br><br>
                    <div class='form-group'>
                    <div>
                    <center>          
                        <h3>Median Age Range (years) </h3>
                        <div class='paddingpls'>
                        <input type='hidden' id='weightT' name='weightT'>
                        <input type='hidden' id='weightsT' name='weightsT'>
                        
                        <div class = 'thirdOFThree'>
                            <div class = 'slider-pad'><p>&nbsp;</p>
                            <div id='sliderT'></div></div></div>
                        
                        <script>
                        
                        var slider = document.getElementById('sliderT');
                        noUiSlider.create(slider, {
                            start: [0, 60],
                            tooltips: true,
                            step: 1,
                            margin: 1,
                            connect: true,
                            range: {
                                'min': 0,
                                'max': 60
                            },
                            format: wNumb( { decimals: 0 })
                        });
                        var weightTInput = document.getElementById('weightT');
                        slider.noUiSlider.on('update', function (values, handle, unencoded) {
                        var weightTValue = values[0];
                        weightTInput.value = weightTValue;
                        });
                        weightTInput.addEventListener('change', function () {
                        slider.noUiSlider.set([null, this.value]);
                        });
                        var weightsTInput = document.getElementById('weightsT');
                        slider.noUiSlider.on('update', function (values, handle, unencoded) {
                        var weightsTValue = values[1];
                        weightsTInput.value = weightsTValue;
                        });
                        weightsTInput.addEventListener('change', function () {
                        slider.noUiSlider.set([null, this.value]);
                        });
                
                </script>
                        </div>
                        </center>
                    </div>
                    </div>
                </div>
                        
                    <div class 'center-div'>
                        <p>&nbsp;</p>
                        <div class='form-group'>
                            <div>
                            <center>          
                                <div class='paddingpls'>
                                <input type='hidden' id='weight' name='weight'>
                                        <input type='hidden' id='weights' name='weights'>
                                        <div class = 'secondOFThree'><p>&nbsp;</p></div>
                                        <div class = 'firstOFThree'><p>&nbsp;</p></div>
                                        <div class = 'thirdOFThree'>
                                        <h3>Age Range for Homesless/At Risk (years) </h3>
                                            <div class = 'slider-pad'><p>&nbsp;</p>
                                            <div id='slider'></div></div></div>
                                        
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
                        
                    
                        <br>
                        

                        <div><button type='submit' class='btn btn-primary'>Generate graph</button></div>  
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

        String min_age = context.formParam("weight");
        min_age = (min_age == null) ? "0" : min_age;
        int IntMin_age = Integer.valueOf(min_age)/10 * 4;

        String max_age = context.formParam("weights");
        max_age = (max_age == null) ? "inf" : max_age;
        int IntMax_age = (max_age.compareTo("inf") == 0) ? 28 : Integer.valueOf(max_age)/10 * 4;


        String GroupShown = context.formParam("GroupShown");
        GroupShown = (GroupShown == null) ? "Homeless" : GroupShown;
        int intGroupShown = (GroupShown.compareTo("Homeless") == 0) ? 0 : 1;

        String order_column = context.formParam("order_column");
        order_column = (order_column == null) ? "amount" : order_column;
        switch (order_column) {
            
            case "Median Weekly Income":
                order_column = "MedWeekIncome";
                break;
            case "Median Monthly Mortgage":
                order_column = "MedMonMort";
                break;
            case "Median Weekly Rent":
                order_column = "MedWeekRent";
                break;
            case "Median Age":
                order_column = "MedAge";
                break;
            case "Homeless/AtRisk":
            default:
                order_column = "amount";
                break;
            
        }
        

        String min_inc = context.formParam("minMWI");
        min_inc = (min_inc == null) ? "0" : min_inc;
        int IntMin_inc = Integer.valueOf(min_inc);

        String max_inc = context.formParam("MaxMWI");
        max_inc = (max_inc == null) ? String.valueOf(MAXWeekIncome) : max_inc;
        int IntMax_inc = Integer.valueOf(max_inc);

        html += (IntMax_inc <= IntMin_inc)? "<h1>Please Select a max weekly income value that is more than the min value</h1>" : "" ;

        String min_mort = context.formParam("minMMMR");
        min_mort = (min_mort == null) ? "0" : min_mort;
        int IntMin_mort = Integer.valueOf(min_mort);

        String max_mort = context.formParam("MaxMMMR");
        max_mort = (max_mort == null) ? String.valueOf(MAXMonMort) : max_mort;
        int IntMax_mort = Integer.valueOf(max_mort);

        html += (IntMax_mort <= IntMin_mort)? "<h1>Please Select a max monthly mortgage value that is more than the min value</h1>" : "" ;

        String min_rent = context.formParam("minMWR");
        min_rent = (min_rent == null) ? "0" : min_rent;
        int IntMin_rent = Integer.valueOf(min_rent);

        String max_rent = context.formParam("MaxMWR");
        max_rent = (max_rent == null) ? String.valueOf(MAXWeekRent) : max_rent;
        int IntMax_rent = Integer.valueOf(max_rent);

        html += (IntMax_rent <= IntMin_rent)? "<h1>Please Select a max weekly rent value that is more than the min value</h1>" : "" ;
        
        String min_medage = context.formParam("weightT");
        min_medage = (min_medage == null) ? "0" : min_medage;
        int IntMin_medage = Integer.valueOf(min_medage);

        String max_medage = context.formParam("weightsT");
        max_medage = (max_medage == null) ? String.valueOf(MAXMedAge) : max_medage;
        int IntMax_medage = Integer.valueOf(max_medage);

        html += (IntMax_medage <= IntMin_medage)? "<h1>Please Select a max median age value that is more than the min value</h1>" : "" ;
        
        
        
    
        String medage = context.formParam("weightT");
        String Mmedage = context.formParam("weightsT");
        System.out.println(min_age+"This is the min and max age"+max_age);
        System.out.println("MedAge: "+ medage+" This should work please: "+Mmedage);

        html += (IntMax_age <= IntMin_age)? "<h1>Please Select a max age value that is more than the min value</h1>" : "" ;                        

        html += "<br><div class='container'>";
        html += "<div class = 'center-div' id='stuff'><center>";

        ArrayList<LGA> lgas = jdbc.AdvgetByLGAs(select_state, display, intGroupShown, GenderIncre, GenderStart, IntMin_age, IntMax_age, order, order_column, IntMin_inc, IntMax_inc, IntMin_mort, IntMax_mort, IntMin_rent,IntMax_rent,IntMin_medage,IntMax_medage);
        // Add HTML for the LGA list
        html = html + "<h1>" + display + " of " + GroupShown + " " + gender + " people in " + select_state + " LGAs aged " + min_age + " to " + max_age + " ordered by " + order_column + " " + order + "</h1>";
        html += "<p>Median Weekly Income range from " + min_inc + " to " + max_inc + "</p>";
        html += "<p>Median Monthly Mortgage range from " + min_mort + " to " + max_mort + "</p>";
        html += "<p>Median Weekly Rent range from " + min_rent + " to " + max_rent + "</p>";
        html += "<p>Median Age range from " + min_medage + " to " + IntMax_medage + "</p>";

        html += """
            <table class='customTable'>
            <thead>
                <tr>
                <th>Name</th>
                <th>Homeless/AtRisk Value</th>
                <th>Median Weekly Income</th>
                <th>Median Monthly Mortgage</th>
                <th>Median Weekly Rent</th>
                <th>Median Age</th>
                </tr>
            </thead>
            <tbody>
                """;
        // Finally we can print out all of the LGAs
        for (LGA lga : lgas) {
            html += "<tr>";
            html += "<td><center>" + lga.name + "</center></td><td><center>";
            html += (display.compareTo("Amount") == 0) ? lga.amount : String.format("%.2f", lga.percentage) + "%";
            html += "</center></td>";
            html += "<td><center>" + lga.MedWeekIncome + "</center></td>";
            html += "<td><center>" + lga.MedMonMort + "</center></td>";
            html += "<td><center>" + lga.MedWeekRent + "</center></td>";
            html += "<td><center>" + lga.MedAge + "</center></td>";
            html += "</tr>";
        }

        // Finish the List HTML
        html = html + "</tbody></table>";

        // Close Content div
        html = html + "</div> </center></div> </div>";

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
