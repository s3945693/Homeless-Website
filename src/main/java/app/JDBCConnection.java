package app;

import java.util.ArrayList;
import java.util.jar.Attributes.Name;

import javax.management.Query;

import javassist.runtime.Desc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for Managing the JDBC Connection to a SQLLite Database.
 * Allows SQL queries to be used with the SQLLite Databse in Java.
 *
 * @author Timothy Wiley, 2022. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class JDBCConnection {

    // Name of database file (contained in database folder)
    private static final String DATABASE = "jdbc:sqlite:database/homelessness.db";

    // If you are using the Homelessness data set replace this with the below
    // private static final String DATABASE =
    // "jdbc:sqlite:database/homelessness.db";

    /**
     * This creates a JDBC Object so we can keep talking to the database
     */
    public JDBCConnection() {
        //System.out.println("Created JDBC Connection Object");
    }

    /**
     * Get all of the LGAs in the database.
     * 
     * @return
     *         Returns an ArrayList of LGA objects
     */
    public ArrayList<LGA> getLGAs() {
        // Create the ArrayList of LGA objects to return
        ArrayList<LGA> lgas = new ArrayList<LGA>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM LGAs order by lga_name";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                int code = results.getInt("lga_code");
                String name = results.getString("lga_name");

                // Create a LGA Object
                LGA lga = new LGA(code, name);

                lga.a18[0] = results.getInt("HF0_16");

                // Add the lga object to the array
                lgas.add(lga);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return lgas;
    }

    
    public int getTotalPopulation(int year) {
        int population = 0;

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "select sum(p" + year + ") as pop from lgas";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                // int code = results.getInt("lga_code");
                // String name = results.getString("lga_name");
                population = results.getInt("pop");
                // Create a LGA Object
                // LGA lga = new LGA(code, name);

                // Add the lga object to the array
                // lgas.add(lga);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return population;
    }

    public ArrayList<String> getStates() {
        ArrayList<String> States = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "select state_name from state";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need

                States.add(results.getString("state_name"));

            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return States;
    }

    /*select lga_code, sum(HF0_16 + RF0_16) 
    from lgas group by lga_code*/
    //ArrayList<LGA> lgas = jdbc.getByLGAs(select_state, display, intGroupShown, GenderIncre, GenderStart, IntMin_age, IntMax_age, order);
    public ArrayList<LGA> getByLGAs(String state, String display, int intGroupShown, int GenderIncre, int GenderStart, int IntMin_age, int IntMax_age, String order) {
        //input should have three ints
        // Create the ArrayList of LGA objects to return
        ArrayList<LGA> lgas = new ArrayList<LGA>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            // LGA guide = new LGA();
            int j = 0;
            String query = "select lga_name, sum(";
            for (int i = intGroupShown + GenderStart + IntMin_age; i < IntMax_age; i += GenderIncre) {
                query += (j == 0) ? "" : " + ";
                j++;
                query += LGA.GUIDE18[i];
            }
            
            
            query += (display.compareTo("Amount") == 0) ? ") as amount" : ")/CAST (p2018 as FLOAT) * 100 as percentage"; 
            query += " from lgas natural join state where HF0_18 is not null ";
            query += (state == null || state.compareTo("All") == 0) ? "" : "and state_name = '" + state + "'" ; 
            query += " group by lga_code order by";

            query += (display.compareTo("Amount") == 0) ? " amount " : " percentage ";
            query += order;
            //System.out.println(query);
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                // int code = results.getInt("lga_code");
                LGA lga = new LGA();
                lga.name  = results.getString("lga_name");
                
                if (display.compareTo("Amount") == 0) {
                    lga.amount = results.getInt("amount");
                } else {
                    lga.percentage = results.getFloat("percentage");
                }

                // Add the lga object to the array
                lgas.add(lga);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return lgas;
    }

    public int getLGACount() {
        int LGACount = 0;

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "select count(lga_name) as deez from lgas;";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need

                LGACount = results.getInt("deez");

            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return LGACount;
    }

    public LGA getInfoForTwoTwoPrimary(String nutz, String mike) {
        // Create the ArrayList of LGA objects to return
        LGA lga = new LGA();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "select lga_code,lga_name,type_name,area, state_name, p20" + mike
                    + " from lgas natural join state natural join Type where lga_name = \"" + nutz + "\"";
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                int code = results.getInt("lga_code");
                String name = results.getString("lga_name");
                String type = results.getString("type_name");
                double area = results.getDouble("area");
                String state = results.getString("state_name");
                Integer p = results.getInt("p20" + mike);
                if (results.wasNull()) {
                    // handle NULL field value
                    p = null;
                }
                

                // Create a LGA Object
                LGA lgas = new LGA(code, name, type, area, state, p);
                lga = lgas;
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return lga;
    }

    public LGA getInfoForHomeless(String hunt, String oxlong, String phillip) {
        // Create the ArrayList of LGA objects to return
        // oxlong is year, hunt is the column value for homeless
        // phillip is the LGA name
        LGA lga = new LGA();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "select HF" + hunt + "_" + oxlong + ", HM" + hunt + "_" + oxlong
                    + " from lgas where lga_name = \"" + phillip + "\"";
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                lga.homRolFValue = results.getInt("HF" + hunt + "_" + oxlong);
                lga.homRolMValue = results.getInt("HM" + hunt + "_" + oxlong);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return lga;
    }

    public Integer[] getHomAndRiskValue16(String deez) {
        // input should have three ints
        // Create the ArrayList of LGA objects to return
        Integer[] work = new Integer[30];

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            // LGA guide = new LGA();
            String query = "select * from lgas where lga_name = \"" + deez + "\";";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                // int code = results.getInt("lga_code");
                // LGA valueLga = new LGA();
                for (int i = 0; i < 30; i++) {
                    work[i] = results.getInt(app.LGA.GUIDE16[i]);
                    if (results.wasNull()) {
                        // handle NULL field value
                        work[i] = null;
                    }
                }

                // Add the lga object to the array

            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return work;
    }

    public Integer[] getHomAndRiskValue18(String deez) {
        // input should have three ints
        // Create the ArrayList of LGA objects to return
        Integer[] work = new Integer[30];

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            // LGA guide = new LGA();
            String query = "select * from lgas where lga_name = \"" + deez + "\";";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                // int code = results.getInt("lga_code");
                // LGA valueLga = new LGA();
                for (int i = 0; i < 30; i++) {
                    work[i] = results.getInt(app.LGA.GUIDE18[i]);
                    if (results.wasNull()) {
                        // handle NULL field value
                        work[i] = null;
                    }
                }

                // Add the lga object to the array

            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return work;
    }

    public Integer[] getAllStateHomAndRiskValue(String year, String crush) {
        // input should have three ints
        // Create the ArrayList of LGA objects to return
        Integer[] work = new Integer[30];

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            // LGA guide = new LGA();
            String query;
            // System.out.print("Print 1 ran ");
            // query = "Select * from lgas where lga_name = \"Bellingen\"";
            if (year.equalsIgnoreCase("16")) {
                query = "select ";
                for (int i = 0; i < 29; ++i) {
                    query = query + "sum(" + app.LGA.GUIDE16[i] + ") as " + app.LGA.GUIDE16[i] + ", ";
                    // System.out.print("sum(" + app.LGA.GUIDE16[i] + "), ");
                }
                query = query + "sum(" + app.LGA.GUIDE16[29] + ") as " + app.LGA.GUIDE16[29] + " ";
                query = query + "from lgas where state_code in (select state_code from lgas where lga_name = \"" + crush
                        + "\");";
                // System.out.print(query);

            }

            else {
                query = "select ";
                // .out.print("Print 3 ran ");
                for (int i = 0; i < 30; ++i) {
                    query = query + "sum(" + app.LGA.GUIDE18[i] + ")as " + app.LGA.GUIDE18[i] + ", ";
                }
                query = query + "sum(" + app.LGA.GUIDE18[29] + ")as " + app.LGA.GUIDE18[29] + " ";
                query = query + "from lgas where state_code in (select state_code from lgas where lga_name = \"" + crush
                        + "\");";
            }

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                // int code = results.getInt("lga_code");
                // LGA valueLga = new LGA();
                if (year.equalsIgnoreCase("16")) {
                    for (int i = 0; i < 30; i++) {
                        work[i] = results.getInt(app.LGA.GUIDE16[i]);
                        if (results.wasNull()) {
                            // handle NULL field value
                            work[i] = null;
                        }
                    }
                    
                }

                else {
                    for (int i = 0; i < 30; i++) {
                        work[i] = results.getInt(app.LGA.GUIDE18[i]);
                        if (results.wasNull()) {
                            // handle NULL field value
                            work[i] = null;
                        }
                    }
                }

                // Add the lga object to the array

            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return work;
    }

    public Integer[] getNationHomAndRiskValue(String year) {
        // input should have three ints
        // Create the ArrayList of LGA objects to return
        Integer[] work = new Integer[30];

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            // LGA guide = new LGA();
            String query;
            // System.out.print("Print 1 ran ");
            // query = "Select * from lgas where lga_name = \"Bellingen\"";
            if (year.equalsIgnoreCase("16")) {
                query = "select ";
                for (int i = 0; i < 29; ++i) {
                    query = query + "sum(" + app.LGA.GUIDE16[i] + ") as " + app.LGA.GUIDE16[i] + ", ";
                    // System.out.print("sum(" + app.LGA.GUIDE16[i] + "), ");
                }
                query = query + "sum(" + app.LGA.GUIDE16[29] + ") as " + app.LGA.GUIDE16[29] + " ";
                query = query + "from lgas;";
                // System.out.print(query);

            }

            else {
                query = "select ";
                // .out.print("Print 3 ran ");
                for (int i = 0; i < 30; ++i) {
                    query = query + "sum(" + app.LGA.GUIDE18[i] + ")as " + app.LGA.GUIDE18[i] + ", ";
                }
                query = query + "sum(" + app.LGA.GUIDE18[29] + ")as " + app.LGA.GUIDE18[29] + " ";
                query = query + "from lgas;";
            }

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                // int code = results.getInt("lga_code");
                // LGA valueLga = new LGA();
                if (year.equalsIgnoreCase("16")) {
                    for (int i = 0; i < 30; i++) {
                        work[i] = results.getInt(app.LGA.GUIDE16[i]);
                        if (results.wasNull()) {
                            // handle NULL field value
                            work[i] = null;
                        }
                    }
                }

                else {
                    for (int i = 0; i < 30; i++) {
                        work[i] = results.getInt(app.LGA.GUIDE18[i]);
                        if (results.wasNull()) {
                            // handle NULL field value
                            work[i] = null;
                        }
                    }
                }

                // Add the lga object to the array

            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return work;
    }

    public Integer[] getThoseOtherValues(String deez) {
        // input should have three ints
        // Create the ArrayList of LGA objects to return
        Integer[] work = new Integer[4];

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            // LGA guide = new LGA();
            String query = "select * from lgas where lga_name = \"" + deez + "\";";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                // int code = results.getInt("lga_code");
                // LGA valueLga = new LGA();

                work[0] = results.getInt("MedWeekIncome");
                if (results.wasNull()) {
                    // handle NULL field value
                    work[0] = null;
                }
                work[1] = results.getInt("MedWeekRent");
                if (results.wasNull()) {
                    // handle NULL field value
                    work[1] = null;
                }
                work[2] = results.getInt("MedMonMort");
                if (results.wasNull()) {
                    // handle NULL field value
                    work[2] = null;
                }
                work[3] = results.getInt("MedAge");
                if (results.wasNull()) {
                    // handle NULL field value
                    work[3] = null;
                }

                // Add the lga object to the array

            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return work;
    }

    public ArrayList<LGA> getName() {
        // Create the ArrayList of LGA objects to return
        ArrayList<LGA> lgas = new ArrayList<LGA>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM LGAs";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                String name = results.getString("lga_name");

                // Create a LGA Object
                LGA lga = new LGA(name);
                // Add the lga object to the array
                lgas.add(lga);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return lgas;
    }

    public ArrayList<LGA> getNameFiltered(String state) {
        // Create the ArrayList of LGA objects to return
        ArrayList<LGA> lgas = new ArrayList<LGA>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM LGAs natural join state where state_name = \"" + state + "\";";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                String name = results.getString("lga_name");

                // Create a LGA Object
                LGA lga = new LGA(name);
                // Add the lga object to the array
                lgas.add(lga);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return lgas;
    }

    public int getSinglePopulation16(String lga) {
        int p = 0;

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "select sum(p2016) as p16 from lgas where lga_name = \"" + lga + "\";";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                p = results.getInt("p16");
                
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return p;
    }
    public int getSinglePopulation18(String lga) {
        int p = 0;

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "select sum(p2018) as p18 from lgas where lga_name = \"" + lga + "\";";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                p = results.getInt("p18");
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return p;
    }
    public int getFullLGAs() {
        int fullLgas = 0;

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            // LGA guide = new LGA();
            String query = "select count(*) from lgas where HF0_16 is not null and HF0_18 is not null and p2018 is not null and MedWeekIncome is not null";
            // Get Result
            //System.out.println(query);
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                // int code = results.getInt("lga_code");
                fullLgas = results.getInt("count(*)");
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return fullLgas;
    }

    public ArrayList<LGA> AdvgetByLGAs(String state, String display, int intGroupShown, int GenderIncre, int GenderStart, int IntMin_age, int IntMax_age, String order, String order_column, int IntMin_inc, int IntMax_inc, int IntMin_mort, int IntMax_mort, int IntMin_rent, int IntMax_rent, int IntMin_medage, int IntMax_medage) {
        //input should have three ints
        // Create the ArrayList of LGA objects to return
        ArrayList<LGA> lgas = new ArrayList<LGA>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            // LGA guide = new LGA();
            int j = 0;
            String query = "select lga_name, sum(";
            for (int i = intGroupShown + GenderStart + IntMin_age; i < IntMax_age; i += GenderIncre) {
                query += (j == 0) ? "" : " + ";
                j++;
                query += LGA.GUIDE18[i];
            }
            
            
            query += (display.compareTo("Amount") == 0) ? ") as amount" : ")/CAST (p2018 as FLOAT) * 100 as percentage"; 
            query += ", MedWeekIncome, MedMonMort, MedWeekRent, MedAge from lgas natural join state where HF0_18 is not null and MedWeekIncome is not null ";
            query += " and MedWeekIncome > " + (IntMin_inc-1) + " ";
            query += " and MedWeekIncome < " + (IntMax_inc+1) + " ";
            query += " and MedMonMort > " + (IntMin_mort-1) + " ";
            query += " and MedMonMort < " + (IntMax_mort+1) + " ";
            query += " and MedWeekRent > " + (IntMin_rent-1) + " ";
            query += " and MedWeekRent < " + (IntMax_rent+1) + " ";
            query += " and MedAge > " + IntMin_medage + " ";
            query += " and MedAge < " + (IntMax_medage+1) + " ";
            query += (state == null || state.compareTo("All") == 0) ? "" : "and state_name = '" + state + "'" ; 
            query += " group by lga_code order by";

            query += (order_column.compareTo("amount") == 0) ? 
            (display.compareTo("Amount") == 0) ? " amount " : " percentage " 
            : " " + order_column + " ";
            query += order;
            System.out.println(query);
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                // int code = results.getInt("lga_code");
                LGA lga = new LGA();
                lga.name  = results.getString("lga_name");
                
                if (display.compareTo("Amount") == 0) {
                    lga.amount = results.getInt("amount");
                } else {
                    lga.percentage = results.getFloat("percentage");
                }
                lga.MedWeekIncome = results.getInt("MedWeekIncome");
                lga.MedMonMort = results.getInt("MedMonMort");
                lga.MedWeekRent = results.getInt("MedWeekRent");
                lga.MedAge = results.getInt("MedAge");

                // Add the lga object to the array
                lgas.add(lga);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return lgas;
    }

    public int getMax(String column) {
        int max = 0;

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            // LGA guide = new LGA();
            String query = "select max(" + column + ") as result from lgas";
            // Get Result
            System.out.println(query);
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                // int code = results.getInt("lga_code");
                max = results.getInt("result");
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return max;
    }

    public ArrayList<LGA> getThreeTwoInfo(String state, String gender, String sort, String sortedValueBy, String changes, int minAge, int maxAge, String display) {
        // Create the ArrayList of LGA objects to return
        ArrayList<LGA> lgas = new ArrayList<LGA>();

        // Setup the variable for the JDBC connection
        Connection connection = null;
        int x = 2;

        minAge = (minAge/10)*4;
        if (gender.equalsIgnoreCase("male")){
            minAge = minAge +2;
            x=4;
        }
        if (gender.equalsIgnoreCase("female")){
            x=4;
        }
        String mirage = "";
        String lifeLine = "";
        String octane = "";
        String rez = "";
        String oneHealth = "";
        String runningOutOfNames="";
        
        if(display.equalsIgnoreCase("amount")){
            oneHealth = " where h is not null or r is not null or p is not null";
            if(!(changes.equalsIgnoreCase("both"))||(!(state.equalsIgnoreCase("all")))){
                oneHealth = "and (h is not null or r is not null or p is not null)"; 
            }
            
        }
        if(display.equals("percentage")){
            oneHealth = " where h is not null and r is not null";
            if(!(changes.equalsIgnoreCase("both"))||(!(state.equalsIgnoreCase("all")))){
                oneHealth = "and h is not null and r is not null"; 
            }
            
        }
        if(changes.equalsIgnoreCase("increase")){
            lifeLine = "where "+sortedValueBy+" >0 ";
            rez = "and "+sortedValueBy+" >0 ";
        }

        if(changes.equalsIgnoreCase("decrease")){
            lifeLine = "where "+sortedValueBy+" <0 ";
            rez = "and "+sortedValueBy+" <0 ";
        }

        

        

        maxAge = (maxAge/10)*4;
        //CHANGES AS IN INCERASE OR DECREASE
        String needThisH16 ="("+app.LGA.GUIDE16[minAge]+"+"+app.LGA.GUIDE16[minAge+2];
        String needThisH18 ="("+app.LGA.GUIDE18[minAge]+"+"+app.LGA.GUIDE18[minAge+2];

        String needThisR16 ="("+app.LGA.GUIDE16[minAge+1]+"+"+app.LGA.GUIDE16[minAge+3];
        String needThisR18 ="("+app.LGA.GUIDE18[minAge+1]+"+"+app.LGA.GUIDE18[minAge+3] ;

        for(int i = minAge+4;i<maxAge;i+=x){
            needThisH16 = needThisH16 +  "+" + app.LGA.GUIDE16[i];
            needThisR16 = needThisR16 +  "+" + app.LGA.GUIDE16[i+1];
        }
        needThisH16 = needThisH16 + ")";
        needThisR16 = needThisR16 + ")";

        for(int i = minAge+4;i<maxAge;i+=x){
            needThisH18 = needThisH18+ "+" + app.LGA.GUIDE18[i];
            needThisR18 = needThisR18+ "+" + app.LGA.GUIDE18[i+1];
        }
        needThisH18 = needThisH18+ ")";
        needThisR18 = needThisR18+ ")";

        if(display.equalsIgnoreCase("percentage")){
            mirage = "*100.00/("+needThisH16+"*1.00)";
            octane = "*100.00/("+needThisR16+"*1.00)";
            runningOutOfNames = "*100.00/(p2016*1.00)";
        }
        
        // eg homeless/atrist/populationChange
        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT (" + needThisH18+"-"+needThisH16+")"+mirage+" as h, "+ needThisR18+"-" + needThisH16+octane+" as r, (p2018-p2016) as p, ((" + needThisH18+"-"+needThisH16+")*1.00)/ (("+ needThisR18+"-" + needThisH16+")*1.00) as hr FROM LGAs "+lifeLine+oneHealth+" order by "+ sortedValueBy+" "+ sort +";";
            

            if(state.equalsIgnoreCase("all")){
            query= "SELECT lga_name, (" + needThisH18+"-"+needThisH16+")"+mirage+" as h, ("+ needThisR18+"-" + needThisH16+")"+octane+" as r, (p2018-p2016)"+runningOutOfNames+" as p, ((" + needThisH18+"-"+needThisH16+")*1.00)/ (("+ needThisR18+"-" + needThisH16+")*1.00) as hr FROM LGAs "+lifeLine+ oneHealth+" order by "+ sortedValueBy+" "+ sort +";";
            }
            else{
            query= "SELECT lga_name, " + needThisH18+"-"+needThisH16+mirage+" as h, "+ needThisR18+"-" + needThisH16+octane+" as r, (p2018-p2016)"+runningOutOfNames+" as p, ((" + needThisH18+"-"+needThisH16+")*1.00)/ (("+ needThisR18+"-" + needThisH16+")*1.00) as hr FROM LGAs natural join state where state_name = \"" + state + "\" "+rez+oneHealth+" order by "+ sortedValueBy+" "+ sort +";";
            }        
            // Get Result
            System.out.println(query + "\n");
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                String name = results.getString("lga_name");
                LGA lga = new LGA(name);

                if (display.equalsIgnoreCase("percentage")){
                    Float h = results.getFloat("h");
                    if(results.wasNull()){
                        h = null;
                    }
                    Float r = results.getFloat("r");
                    if(results.wasNull()){
                        r = null;
                    }
                    Float p = results.getFloat("p");
                    if(results.wasNull()){
                        p = null;
                    }
                    lga.cH = h;
                    lga.cR = r;
                    lga.cP = p;
                }
                else{
                    Integer h = results.getInt("h");
                    if (results.wasNull()) {
                        // handle NULL field value
                        h = null;
                    }
                    Integer r = results.getInt("r");
                    if (results.wasNull()) {
                        // handle NULL field value
                        r = null;
                    }
                    Integer p = results.getInt("p");
                    if (results.wasNull()) {
                        // handle NULL field value
                        p = null;
                    }
                    // Create a LGA Object                    
                    lga.setChangeHome(h);
                    lga.setChangeRisk(r);
                    lga.setChangePop(p);
                }   
                lga.percentage = results.getFloat("hr");
                // Add the lga object to the array
                lgas.add(lga);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return lgas;
    }

    public float Fact1(int intGroupShown, int IntMin_age, int IntMax_age) {
        //input should have three ints
        // Create the ArrayList of LGA objects to return
        float Fact1 = 0;

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            // LGA guide = new LGA();
            int j = 0;
            String query = "select sum(";
            for (int i = intGroupShown + IntMin_age + 2; i < IntMax_age; i += 4) {
                query += (j == 0) ? "" : " + ";
                j++;
                query += LGA.GUIDE18[i];
            }
            
            
            query +=  ") as male, sum(";
            j = 0;
           
            for (int i = intGroupShown + IntMin_age; i < IntMax_age; i += 4) {
                query += (j == 0) ? "" : " + ";
                j++;
                query += LGA.GUIDE18[i];
            }
            
            
            query +=  ") as female";
            query += " from lgas where HF0_18 is not null ";
            System.out.println(query);
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                // int code = results.getInt("lga_code");
                int male = results.getInt("male");
                int female  = results.getInt("female");
                Fact1 = (float)female / (female + male) * 100;

                // Add the lga object to the array
                
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return Fact1;
    }

    public LGA Fact2(String aggr) {
        //input should have three ints
        // Create the ArrayList of LGA objects to return
        
        LGA lga = new LGA();
        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            // LGA guide = new LGA();
            int j = 0;
            String query = "select lga_name, " + aggr + "((";
            for (int i = 0; i < 28; i += 2) {
                query += (j == 0) ? "" : " + ";
                j++;
                query += LGA.GUIDE18[i];
            }
            query += " + " + LGA.GUIDE18[28];
            query += " + " + LGA.GUIDE18[29];
            
            
            query +=  ")/CAST (p2018 as FLOAT) * 100) as percentage"; 
            query += ", state_name as sstate from lgas natural join state where HF0_18 is not null and p2018 is not null ";
            //query += " group by lga_code";
            System.out.println(query);
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                // int code = results.getInt("lga_code");
                
                lga.name  = results.getString("lga_name");
                
                lga.percentage = results.getFloat("percentage");
                lga.state = results.getString("sstate");

                // Add the lga object to the array
                
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return lga;
    }

    public float Fact3() {
        //input should have three ints
        // Create the ArrayList of LGA objects to return
        
        float times = 0;
        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            // LGA guide = new LGA();
            String query = "select sum(RM30_18 + RM40_18) as male, sum(RF30_18 + RF40_18) as female from LGAs where HF0_18 is not null";
            
            //System.out.println(query);
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                // int code = results.getInt("lga_code");
                
                int male = results.getInt("male");
                int female = results.getInt("female");
                System.out.println(male);
                System.out.println(female);
                times = (float)female / male;
                System.out.println(times);

                // Add the lga object to the array
                
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return times;
    }

    public ArrayList<Student> getStudents() {
        //input should have three ints
        // Create the ArrayList of LGA objects to return
        
        ArrayList<Student> Students = new ArrayList<Student>(); 
        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            // LGA guide = new LGA();
            String query = "select * from TeamMembers";
            
            //System.out.println(query);
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                // int code = results.getInt("lga_code");
                
                String ID = results.getString("sID");
                String Name = results.getString("sName");
                
                Student student = new Student(ID, Name);
                Students.add(student);

                // Add the lga object to the array
                
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return Students;
    }

    public ArrayList<Persona> getPersonas() {
        //input should have three ints
        // Create the ArrayList of LGA objects to return
        
        ArrayList<Persona> Personas = new ArrayList<Persona>(); 
        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            // LGA guide = new LGA();
            String query = "select * from Personas";
            
            //System.out.println(query);
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                // int code = results.getInt("lga_code");
                
                int ID = results.getInt("pID");
                String Name = results.getString("Name");
                int Age = results.getInt("Age");
                String Gender = results.getString("Gender");
                String Description = results.getString("Description");
                String Needs = results.getString("Needs");
                String Goals = results.getString("Goals");
                String Skills = results.getString("Skills");

                Persona persona = new Persona(ID, Name, Age, Gender, Description, Needs, Goals, Skills);
                Personas.add(persona);

                // Add the lga object to the array
                
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the lga
        return Personas;
    }

}
