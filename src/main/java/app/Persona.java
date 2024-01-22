package app;



/**
 * Class represeting a LGA from the Studio Project database
 * In the template, this only uses the code and name for 2016
 *
 * @author Julius Costa
 */
public class Persona {
   JDBCConnection jdbc = new JDBCConnection();
   
   // LGA 2016 Code
   public int ID, Age;
   public String Name, Gender, Description, Needs, Goals, Skills;
   
   public Persona() {}

   public Persona(int ID, String Name, int Age, String Gender, String Description, String Needs, String Goals, String Skills) {
      this.Name = Name;
      this.ID = ID;
      this.Age = Age;
      this.Gender = Gender;
      this.Description = Description;
      this.Needs = Needs;
      this.Goals = Goals;
      this.Skills = Skills;
   }

}