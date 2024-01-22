package app;



/**
 * Class represeting a LGA from the Studio Project database
 * In the template, this only uses the code and name for 2016
 *
 * @author Julius Costa
 */
public class Student {
   JDBCConnection jdbc = new JDBCConnection();
   
   // LGA 2016 Code
   public String ID;
   public String Name;
   
   public Student() {}

   public Student(String ID, String Name) {
      this.Name = Name;
      this.ID = ID;
   }

}