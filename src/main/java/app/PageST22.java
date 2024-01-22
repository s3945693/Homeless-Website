package app;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.plaf.synth.SynthEditorPaneUI;

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
public class PageST22 implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page4.html";

    private double round(double amount, int places) {
        return Math.round(amount * Math.pow(10, places)) / Math.pow(10, places);
    }

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Head information
        html = html + "<head>" +
                "<title>View One LGA</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + """
                <style>
                table.customTable {
                  width: 90%;
                  background-color: #FFFFFF;
                  border-collapse: collapse;
                  border-width: thin;
                  border-color: #7EA8F8;
                  border-style: solid;
                  color: #000000;
                }

                table.customTable td, table.customTable th {
                  border-width: thin;
                  border-color: #7EA8F8;
                  border-style: solid;
                  padding: 3px;
                }

                table.customTable thead {
                  background-color: rgb(14, 249, 120);
                }
                </style>
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
                        <h1>View One LGA</h1>
                    </div>
                """;

        // Add Div for page Content

        // Look up some information from JDBC
        // First we need to use your JDBCConnection class
        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<LGA> lgas = jdbc.getLGAs();

        html = html + """
                <div class ='container'>
                    <div class = 'center-div'><center>
                    <div class='firstOFThree'>
                        <form action='/page4.html' method='post'>
                            <div>
                                <label for='select_LGA'><h3>Select a LGA: </h3></label>
                                <select id='select_LGA' name='select_LGA'>
                                    <option>Random</option>""";
        for (LGA lga : lgas) {
            html = html + "<option>" + lga.getName16() + "</option>";
        }
        html = html
                + """
                                    </select>
                                </div>
                        </div>
                        <div class='secondOFThree'>
                                <div>
                                <label for='proportional'><h3>Select a proportional value: </h3></label>
                                <select id='proportional' name='proportional'>
                                        <option>None</option>
                                        <option>Selected LGA</option>
                                        <option>State</option>
                                        <option>Australia</option>
                                        </select>
                                            </div>
                                    </div>
                                    <div class='thirdOFThree'>
                                            <div><center>
                                            <h3> Show info for year: </h3><p>
                                                <input type='radio' id='yr16' name='year' value='16' checked = 'checked'>
                                                <label class='radio-inline' for='yr16'>2016</label>
                                                <br>
                                                <input type='radio' id='yr18' name='year' value='18'></h2>
                                                <label class='radio-inline' for='desc'>2018</label>
                                                
                                            </p>
                                            </center>
                                            </div>
                                            <div class = 'center-div'><center><h2> <button type='submit'>Generate table</button> <h2></center></div>
                                        </form>
                                    </div>                                    </center>
                                    </div>

                                </div>
                                
                                    """;

        String sLGA = context.formParam("select_LGA");
        String year = context.formParam("year");
        String prop = context.formParam("proportional");
        Random rand = new Random();

        if (sLGA == null || sLGA.equalsIgnoreCase("random")) {
            LGA forRand;
            forRand = lgas.get(rand.nextInt(lgas.size()));
            sLGA = forRand.getName16();
        }
        if (prop == null || prop.equalsIgnoreCase("none")) {
            prop = "none";
        }
        String prop2 = "";
        if (prop.equalsIgnoreCase("Selected LGA")) {
                prop2 = "Selected LGA";
                prop = sLGA;
        }

        if (year == null) {
            year = "16";
        }
        // System.out.print(LGA);
        LGA lga = jdbc.getInfoForTwoTwoPrimary(sLGA, year);

        html = html + """
                <br>
                <div class ='container'>
                    <div class = 'center-div'>
                    <center>
                        <table class='customTable'>
                        <thead>
                            <tr>
                            <th>Name</th>
                            <th>State</th>
                            <th>Type</th>
                            <th>Area</th>
                            <th>Total Population</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                            <td> """;
        html = html + "<center><p>" + lga.getName16() + "</p> </center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + lga.getState() + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + lga.getType() + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + lga.getArea() + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + lga.getPop() + "</p></center>" +
                """
                                    </td>
                                 </tr>
                                </tbody>
                                </table>
                                </center>
                            </div>
                        </div>

                        """;
        
        Integer[] test;
        String yr = "16";
        // System.out.print(year);
        if (year.equals(yr)) {
            test = jdbc.getHomAndRiskValue16(sLGA);
        }

        else {
            test = jdbc.getHomAndRiskValue18(sLGA);
        }
        // start of the majestic, beautiful, magnificent tables
        // i love making tables, i love making tables, i love making tables
        if ((prop.equalsIgnoreCase("none"))) {
        html = html + """
                <div class ='container'>
                <div class = 'center-div'>
                <center>
                <div class - çontainer'>
                 <h2> Homeless Values </h2>
                </div>
                    <table class='customTable'>
                    <thead>
                        <tr>
                        <th>Age Range</th>
                        <th>Male Values</th>
                        <th>Female Values</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                        <td> """;
        html = html + "<center><p> 0-9 </p> </center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[2] + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[0] + "</p></center>" +
                """
                            </td>
                         </tr>
                        </tbody>
                        <tbody>
                            <tr>
                            <td> """;
        html = html + "<center><p> 10-19 </p> </center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[6] + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[4] + "</p></center>" +
                """
                            </td>
                         </tr>
                        </tbody>
                        <tbody>
                            <tr>
                            <td> """;
        html = html + "<center><p> 20-29 </p> </center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[10] + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[8] + "</p></center>" +
                """
                            </td>
                         </tr>
                        </tbody>
                        <tbody>
                            <tr>
                            <td> """;
        html = html + "<center><p> 30-39 </p> </center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[14] + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[12] + "</p></center>" +
                """
                            </td>
                         </tr>
                        </tbody>
                        <tbody>
                            <tr>
                            <td> """;
        html = html + "<center><p> 40-49 </p> </center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[18] + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[16] + "</p></center>" +
                """
                            </td>
                         </tr>
                        </tbody>
                        <tbody>
                            <tr>
                            <td> """;
        html = html + "<center><p> 50-59 </p> </center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[22] + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[20] + "</p></center>" +
                """
                            </td>
                         </tr>
                        </tbody>
                        <tbody>
                            <tr>
                            <td> """;
        html = html + "<center><p> 60+ </p> </center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[26] + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[24] + "</p></center>" +
                """
                            </td>
                         </tr>
                        </tbody>
                        <tbody>
                            <tr>
                            <td> """;
        html = html + "<center><p> No Age Data </p> </center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[29] + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[28] + "</p></center>" +
                """
                            </td>
                         </tr>
                        </tbody>""";
        // start of at riskk values
        html = html + """
                </table>
                <br>
                <div class - 'container'>
                <h2> At Risk Values </h2>
                </div>
                <table class='customTable'>
                <thead>
                    <tr>
                    <th>Age Range</th>
                    <th>Male Values</th>
                    <th>Female Values</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                    <td> """;
        html = html + "<center><p> 0-9 </p> </center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[3] + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[1] + "</p></center>" +
                """
                            </td>
                        </tr>
                        </tbody>
                        <tbody>
                            <tr>
                            <td> """;
        html = html + "<center><p> 10-19 </p> </center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[7] + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[5] + "</p></center>" +
                """
                            </td>
                         </tr>
                        </tbody>
                        <tbody>
                            <tr>
                            <td> """;
        html = html + "<center><p> 20-29 </p> </center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[11] + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[9] + "</p></center>" +
                """
                            </td>
                         </tr>
                        </tbody>
                        <tbody>
                            <tr>
                            <td> """;
        html = html + "<center><p> 30-39 </p> </center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[15] + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[13] + "</p></center>" +
                """
                            </td>
                         </tr>
                        </tbody>
                        <tbody>
                            <tr>
                            <td> """;
        html = html + "<center><p> 40-49 </p> </center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[19] + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[17] + "</p></center>" +
                """
                            </td>
                         </tr>
                        </tbody>
                        <tbody>
                            <tr>
                            <td> """;
        html = html + "<center><p> 50-59 </p> </center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[23] + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[21] + "</p></center>" +
                """
                            </td>
                         </tr>
                        </tbody>
                        <tbody>
                            <tr>
                            <td> """;
        html = html + "<center><p> 60+ </p> </center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[27] + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[25] + "</p></center>" +
                """
                            </td>
                         </tr>
                        </tbody>
                        <tbody>
                            <tr>
                            <td> """;
        html = html + "<center><p> No Age Data </p> </center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[29] + "</p></center>" +
                """
                        </td>
                        <td> """;
        html = html + "<center><p>" + test[28] + "</p></center>" +
                """
                                                </td>
                                        </tr>
                                        </tbody>

                                </table>
                                </center>
                                <br>
                        </div>
                        </div>
                        <br>
                        """;
        }
        //System.out.println("This should be a null value from testing: " +testing[0]);
        //System.out.print("This should be a null value: " +test[0]);
        Integer[] testp = new Integer[30];
        if (!(prop.equalsIgnoreCase("none"))) {
            // add table here
            if (prop.equalsIgnoreCase("state")) {
                if (year.equals(yr)) {
                    testp = jdbc.getAllStateHomAndRiskValue(year, sLGA);
                    // System.out.print("this ran");
                }

                else {
                    testp = jdbc.getAllStateHomAndRiskValue(year, sLGA);
                }
            }
            if (prop.equalsIgnoreCase(sLGA)) {
                if (year.equals(yr)) {
                    testp = jdbc.getHomAndRiskValue16(sLGA);
                    // System.out.print("THis ran "+ prop);
                }

                else {
                    testp = jdbc.getHomAndRiskValue18(sLGA);
                }
            }
            if (prop.equalsIgnoreCase("Australia")) {
                if (year.equals(yr)) {
                    testp = jdbc.getNationHomAndRiskValue(year);
                    // System.out.print("THis ran "+ prop);
                }

                else {
                    testp = jdbc.getNationHomAndRiskValue(year);
                }
            }

            // testp hold the values of the proportianl value selected.
            double y = 0;
            for (int z = 0; z < testp.length; z = z + 2) {
                y = y + testp[z];
            }
            y = y + testp[29];
            // Start of Propotional Table
            html = html + """
                    <div class ='container'>
                    <div class = 'center-div'>
                    <center>
                    <div class - çontainer'>
                        <h1> Proportional Values </h1>

                     <h2> Homeless Values </h2>
                    </div>
                        <table class='customTable'>
                        <thead>
                            <tr>
                            <th>Age Range</th>
                            <th>Male Values</th>
                            <th>Proportional Male Values</th>
                            <th>Female Values</th>
                            <th>Proportional Female Values</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                            <td> """;
            html = html + "<center><p> 0-9 </p> </center>" +
                    """
                            </td>
                            <td> """;
                            html = html + "<center><p>" + test[2] + "</p></center>" +
                                    """
                                            </td>
                                            <td> """;
            double x = (((test[2]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "% </p></center>" +
                    """
                            </td>
                            <td> """;
                            html = html + "<center><p>" + test[0] + "</p></center>" +
                                    """
                                            </td>
                                            <td> """;
            x = (((test[0]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                                </td>
                             </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                <td> """;
            html = html + "<center><p> 10-19 </p> </center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[6] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[6]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[4] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[4]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                                </td>
                             </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                <td> """;
            html = html + "<center><p> 20-29 </p> </center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[10] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[10]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[8] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[8]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                                </td>
                             </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                <td> """; 
            html = html + "<center><p> 30-39 </p> </center>" +
                    """
                            </td>
                            <td> """;html = html + "<center><p>" + test[14] + "</p></center>" +
                                """
                                        </td>
                                        <td> """;
            x = (((test[14]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[12] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[12]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                                </td>
                             </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                <td> """;
            html = html + "<center><p> 40-49 </p> </center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[18] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[18]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[16] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[16]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                                </td>
                             </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                <td> """; 
            html = html + "<center><p> 50-59 </p> </center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[22] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[22]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[20] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[20]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                                </td>
                             </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                <td> """;
            html = html + "<center><p> 60+ </p> </center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[26] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[26]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[24] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[24]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                                </td>
                             </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                <td> """;
            html = html + "<center><p> No Age Data </p> </center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[29] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[29]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[28] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[28]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                                </td>
                             </tr>
                            </tbody>""";
            // Start of Proportional At Risk Values
            y = 0;
            for (int z = 1; z < testp.length; z = z + 2) {
                y = y + testp[z];
            }
            y = y + testp[28];
            html = html + """
                    </table>
                    </center>
                    <br>
                    <center>
                    <h2> At Risk Values </h2>
                    <table class='customTable'>
                    <thead>
                        <tr>
                        <th>Age Range</th>
                        <th>Male Values</th>
                        <th>Proportional Male Values</th>
                        <th>Female Values</th>
                        <th>Proportional Female Values</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                        <td> """;
            html = html + "<center><p> 0-9 </p> </center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[3] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[3]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "% </p></center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[1] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[1]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                                </td>
                             </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                <td> """;
            html = html + "<center><p> 10-19 </p> </center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[7] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[7]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[5] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[5]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                                </td>
                             </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                <td> """;
            html = html + "<center><p> 20-29 </p> </center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[11] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[11]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[9] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[9]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                                </td>
                             </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                <td> """;
            html = html + "<center><p> 30-39 </p> </center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[15] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[15]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[13] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[13]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                                </td>
                             </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                <td> """;
            html = html + "<center><p> 40-49 </p> </center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[19] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[19]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[17] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[17]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                                </td>
                             </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                <td> """;
            html = html + "<center><p> 50-59 </p> </center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[23] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[23]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[21] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[21]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                                </td>
                             </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                <td> """;
            html = html + "<center><p> 60+ </p> </center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[27] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[27]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[25] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[25]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                                </td>
                             </tr>
                            </tbody>
                            <tbody>
                                <tr>
                                <td> """;
            html = html + "<center><p> No Age Data </p> </center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[29] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[29]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                            </td>
                            <td> """; html = html + "<center><p>" + test[28] + "</p></center>" +
                            """
                                    </td>
                                    <td> """;
            x = (((test[28]) * 1.00) / (y * 1.00) * 100);
            html = html + "<center><p>" + round(x, 2) + "%</p></center>" +
                    """
                                </td>
                             </tr>
                            </tbody>""";

            html = html + """
                                </table>
                                </center>
                            </div>
                            </div>
                            <br>
                    """;
        }
        System.out.println(prop2);
        if (prop2.equalsIgnoreCase("Selected LGA")) {
                html += """
                        <div class='container'>
                        
                        <div id='HomPiechart' class='left-div'>
                        

                            <script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script>
                            <script type='text/javascript'>
                            // Load google charts
                            google.charts.load('current', {'packages':['corechart']});
                            // Draw the chart and set the chart values
                            google.charts.setOnLoadCallback(drawChart);
                            function drawChart() {
                                var data = google.visualization.arrayToDataTable([
                        
                                    ['Group', 'Amount'],
                                    """;
                                    System.out.println("poop");
                                    for (int i = 0; i < 14; i++) {
                                        html += "['" + LGA.PIEGUIDE[i] + "', " + test[i * 2] + "],";
                                        //System.out.println("['" + LGA.PIEGUIDE[i] + "', " + test[i * 2] + "],");
                                    }
                                    html += "['" + LGA.PIEGUIDE[14] + "', " + test[28] + "],";
                                    html += "['" + LGA.PIEGUIDE[15] + "', " + test[29] + "],";
                                    html +="""
                                ]);
                                // Optional; add a title and set the width and height of the chart
                                 var options = {legendTextStyle: { color: '#FFF' }, titleTextStyle: { color: '#FFF' }, backgroundColor: '#0ea7f9', 'title': 'Homeless Proportional Pie Chart', 'height' : '400', 'chartArea': {'height': '75%'}};
                            
                                // Display the chart inside the <div> element with id='piechart'
                                var chart = new google.visualization.PieChart(document.getElementById('HomPiechart'));
                                chart.draw(data, options);
                            }
                            </script>
                        </div>
                        
                        <div id='RiskPiechart' class='right-div'>

                            <script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script>
                            <script type='text/javascript'>
                            // Load google charts
                            google.charts.load('current', {'packages':['corechart']});
                            // Draw the chart and set the chart values
                            google.charts.setOnLoadCallback(drawChart);
                            function drawChart() {
                                var data = google.visualization.arrayToDataTable([
                        
                                    ['Group', 'Amount'],
                                    """;
                                    System.out.println("poop");
                                    for (int i = 0; i < 14; i++) {
                                        html += "['" + LGA.PIEGUIDE[i] + "', " + test[i * 2 + 1] + "],";
                                        //System.out.println("['" + LGA.PIEGUIDE[i] + "', " + test[i * 2] + "],");
                                    }
                                    html += "['" + LGA.PIEGUIDE[14] + "', " + test[28] + "],";
                                    html += "['" + LGA.PIEGUIDE[15] + "', " + test[29] + "],";
                                    html +="""
                                ]);
                                // Optional; add a title and set the width and height of the chart
                                 var options = {legendTextStyle: { color: '#FFF' }, titleTextStyle: { color: '#FFF' }, backgroundColor: '#0d8ace', 'title': 'At Risk Proportional Pie Chart', 'height' : '400', 'chartArea': {'height': '75%'}};
                            
                                // Display the chart inside the <div> element with id='piechart'
                                var chart = new google.visualization.PieChart(document.getElementById('RiskPiechart'));
                                chart.draw(data, options);
                            }
                            </script>
                        </div>
                        </div>
                        </div>
                """;

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
