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
public class PageST32 implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page6.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" +
                "<title>Change over time</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + """
            <link href='nouislider.css' rel='stylesheet'>
            <script src='nouislider.js'></script>
            <script src='wnumb.js'></script>
            
                """;

        //https://refreshless.com/nouislider/
        //https://www.claudiokuenzler.com/blog/1028/first-steps-with-nouislider-integration-html-forms-examples                     
                    
        
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
                <h1>Change over time</h1>
            </div>
        """;

        // Add Div for page Content
        

        // Look up some information from JDBC
        // First we need to use your JDBCConnection class
        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<String> states = jdbc.getStates();
        html = html + """
            <div class ='container'>
                <div class = 'center-div'> <center>
                <div class='firstOFThree'>
                    <form action='/page6.html' method='post'>
                        <div>
                            <label for='select_LGA'><h3>Select a State: </h3></label>
                            <select id='select_LGA' name='select_LGA'>
                                <option>All</option>""";
                                for (String state : states) {
                                    html += "<option>" + state + "</option>";
                                }
    html = html
            + """
                            </select>
                        </div>
                </div>
                        <div class='secondOFThree'>
                            <div>
                                
                                <label for='gender'><h3>Select a Gender value</h3></label><p>
                                <select id='gender' name='gender'>
                                        <option>Both</option>
                                        <option>Male</option>
                                        <option>Female</option>
                                </select>
                            </div>
                                </p>
                        </div>
                        
                        <div class='thirdOFThree'>
                            <div>
                            <center>          
                            <h3>Age Range (years) </h3>
                                <div class='paddingpls'>
                                <input type='hidden' id='weight' name='weight'>
                                <input type='hidden' id='weights' name='weights'>
                                <p>&nbsp;</p>
                                <div class = 'slider-pad'><div id='slider'></div></div>
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
            <br>
                <div class='firstOFThree'>
                        <label for='selectSort'><h3>Select a sort LGA Value: </h3></label>
                        <select id='selectSort' name='selectSort'>
                            <option value = 'h'>Homeless</option>
                            <option value = 'r'>At Risk</option>
                            <option value = 'p'>Population</option>
                            <option value = 'hr'>LastColumn</option>
                        </select>
                </div>
                    <div class='secondOFThree'>
                        <label for='proportional'><h3>Select Value Changes Only: </h3></label>
                            <select id='upDown' name='upDown'>
                                <option>Both</option>
                                <option>Increase</option>
                                <option>Decrease</option>
                            </select>           
                    </div>
                    <div class='thirdOFThree'>
                        <h3>Sort LGA Values by:</h3>
                            <input type='radio' id='asc' name='sort' value='asc' checked = 'checked'>
                            <label class='radio-inline' for='asc'>ASC</label>
                            <br>
                            <input type='radio' id='dec' name='sort' value='desc'>
                            <label class='radio-inline' for='desc'>DESC</label>
                    </div>
                                        
                                        
                                        <div class='thirdOFThree'>
                                        <h3>Graph or Table:</h3>
                                            <input type='radio' id='gr' name='gT' value='graph'>
                                            <label class='radio-inline' for='gOrT'>Graph</label>
                                            <br>
                                            <input type='radio' id='tb' name='gT' value='table' checked = 'checked'>
                                            <label class='radio-inline' for='desc'>Table</label>                               
                                        </div>

                                        <div class = 'firstOFThree'>
                                            <h3>Displayed Value:</h3>
                                                <input type='radio' id='p' name='d' value='percentage'>
                                                <label class='radio-inline' for='display'>Percentage</label>
                                                <br>
                                                <input type='radio' id='v' name='d' value='amount' checked = 'checked'>
                                                <label class='radio-inline' for='desc'>Amount</label>
                                                
                                        </div>
                                        <div class='secondOFThree'>
                                        <p>&nbsp;</p>
                                        </div>
                                        <div class='thirdOFThree'><center>
                                        <p> </h2>
                                        </p>
                                        </center>                                     
                                        </div>
                                        <div><center><h2> <button type='submit'>Generate table</button> <h2></center></div>
                                    </form>
                                    <center></div>
                                
                            </div>
                                """;

        
        // Next we will ask this *class* for the LGAs
        //ArrayList<LGA> lgas = jdbc.getName();
        //must add if statement here, otherwise u r bad
        
        ArrayList<LGA> lgas;
       
       //state = "Victoria";
       
       String state = context.formParam("select_LGA");
       if (state==null) {
           state = "All";
           //lgas = jdbc.getNameFiltered(state);
        }
       /*if (state.equals("All")) {
           lgas = jdbc.getLGAs();
        }
        else {
            lgas = jdbc.getNameFiltered(state);
        }*/
        
        //ArrayList<LGA> lgas = jdbc.getNameFiltered("Victoria");
        // Add HTML for the LGA list
        String gender;
        gender = context.formParam("gender");
        if(gender==null){
            gender = "Both";
        }    
        String sort;
        sort = context.formParam("sort");
        if (sort == null){
            sort = "asc";
        }

        String sortLGAValue;
        sortLGAValue = context.formParam("selectSort");
        if(sortLGAValue==null){
            sortLGAValue = "h";
        }
        String minAgeRange;
        minAgeRange = context.formParam("weight");
        if(minAgeRange==null){
            minAgeRange = "0";
        }

        String maxAgeRange;
        maxAgeRange = context.formParam("weights");
        if(maxAgeRange==null){
            maxAgeRange = "70";
        }

        String changesOnly;
        changesOnly = context.formParam("upDown");
        if(changesOnly == null){
            changesOnly = "Both";
        }
        int min = Integer.parseInt(minAgeRange);
        int max = Integer.parseInt(maxAgeRange);

        String graphOrTable;
        graphOrTable = context.formParam("gT");
        if(graphOrTable == null){
            graphOrTable = "table";
        }

        String display;
        display = context.formParam("d");
        if(display == null){
            display = "amount";
        }

        lgas = jdbc.getThreeTwoInfo(state, gender, sort, sortLGAValue, changesOnly, min, max, display);

        switch (sortLGAValue){
            case "h":
                sortLGAValue = "Homeless";
                break;
            case "r":
                sortLGAValue = "At Risk";
                break;
            case "p":
                sortLGAValue = "Population Change";
                break;
            case "hr":
                sortLGAValue = "Change in Homeless to At Risk";
                break;
            default:
                sortLGAValue = "Homeless";
                break;
        }
        // Finally we can print out all of the LGAs
        if(graphOrTable.equalsIgnoreCase("table")){
        html = html + """
            <br>
            <div class ='container'>
                <div class='center-div'>  <center>""";
                        
                html = html +"<p>State: " + state + "; Age Range: "+minAgeRange + "-" +maxAgeRange+"; Gender: "+gender+"</p>";
                html = html + "<p> You sorted by: "+sortLGAValue+"; In the order: "+sort+"; Chose to view values: " + changesOnly+"; The values provided are in: "+display+"</p>";
                html = html + "<h3> Please note that percentage values do not contain any null values. </h3>";             
                
                html= html + """
                </div>
            </div>
            <br>
                <div class ='container'>
                    <div class = 'center-div'>
                    <center>
                        <table class='customTable'>
                        <thead>
                            <tr>
                            <th>Name</th>
                            <th>Change in Homeless Value</th>
                            <th>Change in At Risk Value</th>
                            <th>Population Change</th>
                            <th>change in homeless to at risk</th>
                            </tr>
                        </thead>
                        <tbody>
                            
                """;
        
        if(display.equalsIgnoreCase("amount")){
        for (LGA lga:lgas){
            html = html + "<tr><td><center><p>" + lga.getName16() + "</p> </center>" +
                    """
                            </td>
                            <td> """;
            //liar ahead
            //very good debug on next line
            //System.out.print(lga.getSumHomelessValue18());
            html = html + "<center><p>" + lga.getChangeHome() + "</p></center>" +
                    """
                            </td>
                            <td> """;
            html = html + "<center><p>" + lga.getChangeRisk() + "</p></center>" +
                    """
                            </td>
                            <td> """;
            html = html + "<center><p>" + lga.getChangePop() + "</p></center>" +
                    """
                            </td>
                            <td> """;
            html = html + "<center><p>" + String.format("%.2f", lga.percentage*100) + "%</p></center>" + "</td></tr>";
        }
        }

        if(display.equalsIgnoreCase("percentage")){
            for (LGA lga:lgas){
                html = html + "<tr><td><center><p>" + lga.getName16() + "</p> </center>" +
                        """
                                </td>
                                <td> """;
                //liar ahead
                //very good debug on next line
                //System.out.print(lga.getSumHomelessValue18());
                html = html + "<center><p>" + String.format("%.2f", lga.cH) + "%</p></center>" +
                        """
                                </td>
                                <td> """;
                html = html + "<center><p>" + String.format("%.2f", lga.cR) + "%</p></center>" +
                        """
                                </td>
                                <td> """;
                html = html + "<center><p>" + String.format("%.2f", lga.cP) + "%</p></center>" +
                        """
                                </td>
                                <td> """;
                html = html + "<center><p>" + String.format("%.2f", lga.percentage*100) + "%</p></center>" + "</td></tr>";
            }
            }

        html = html + """
            
            </tbody>
            </table>
            </center>
        </div>
    </div>
        """;
    }

    else{
        html = html + """
            <br>
            <div class ='container'>
                <div class='center-div'>  <center>""";
                        
                html = html +"<p>State: " + state + "; Age Range: "+minAgeRange + "-" +maxAgeRange+"; Gender: "+gender+"</p>";
                html = html + "<p> You sorted by: "+sortLGAValue+"; In the order: "+sort+"; Chose to view values: " + changesOnly+"; The values provided are in: "+display+"</p>";
                html = html + "<h3> Please note that percentage values do not contain any null values. </h3>";             
                
                html= html + """
                </div>
            </div>
            <br>
        <div class = 'container'>
        <div class = 'center-div-graph'>
            <div>
            <canvas id='myChart'></canvas>
          </div>
        </div>
        </div>
          <script src='https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.8.0/chart.min.js'></script>
          <script>
            const labels = [""";
        if(display.equalsIgnoreCase("amount")){
            
        for (LGA lga:lgas){
            html = html + "'" + lga.getName16() + "',";
        }
        html = html +"""
                ];

            const data = {
                labels: labels,
                datasets: [
                {
                label: 'Change in Homeless Value',
                backgroundColor: 'rgb(238,130,238)',
                borderColor: 'rgb(238,130,238)',
                data: [""";
                for (LGA lga:lgas){
                    html = html + "'" + lga.getChangeHome() + "',";
                }
                html = html +"""
                        ],
                },
                {
                label: 'Change in At Risk Value',
                backgroundColor: 'rgb(51, 255, 255)',
                borderColor: 'rgb(51, 255, 255)',
                data: [""";
                for (LGA lga:lgas){
                    html = html + "'" + lga.getChangeRisk() + "',";
                }
                html = html +"""
                        ]    
                }
                ]
            };

            const config = {
                type: 'line',
                data: data,
                options: {}
            };
          </script>

          <script>
            const myChart = new Chart(
                document.getElementById('myChart'),
                config
            );
          </script>
                """;}
                if(display.equalsIgnoreCase("percentage")){
            
                    for (LGA lga:lgas){
                        html = html + "'" + lga.getName16() + "',";
                    }
                    html = html +"""
                            ];
            
                        const data = {
                            labels: labels,
                            datasets: [
                            {
                            label: 'Change in Homeless Value',
                            backgroundColor: 'rgb(238,130,238)',
                            borderColor: 'rgb(238,130,238)',
                            data: [""";
                            for (LGA lga:lgas){
                                html = html + "'" + lga.cH + "',";
                            }
                            html = html +"""
                                    ],
                            },
                            {
                            label: 'Change in At Risk Value',
                            backgroundColor: 'rgb(51, 255, 255)',
                            borderColor: 'rgb(51, 255, 255)',
                            data: [""";
                            for (LGA lga:lgas){
                                html = html + "'" + lga.cR + "',";
                            }
                            html = html +"""
                                    ]    
                            },
                            {
                            label: 'Change in Population Value',
                            backgroundColor: 'rgb(255, 0, 0)',
                            borderColor: 'rgb(255, 0, 0)',
                            data: [""";
                            for (LGA lga:lgas){
                                html = html + "'" + lga.cP + "',";
                            }
                            html = html +"""
                                    ]    
                            }
                            ]
                        };
            
                        const config = {
                            type: 'line',
                            data: data,
                            options: {}
                        };
                      </script>
            
                      <script>
                        const myChart = new Chart(
                            document.getElementById('myChart'),
                            config
                        );
                      </script>
                            """;}

            
        }
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