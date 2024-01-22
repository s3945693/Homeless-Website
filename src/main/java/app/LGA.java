package app;


/**
 * Class represeting a LGA from the Studio Project database
 * In the template, this only uses the code and name for 2016
 *
 * @author Timothy Wiley, 2022. email: timothy.wiley@rmit.edu.au
 */
public class LGA {
   JDBCConnection jdbc = new JDBCConnection();
   
   // LGA 2016 Code
   public int code;
   public int p2016, p2018, MedWeekIncome, MedMonMort, MedWeekRent, MedAge;
   public int pop;
   public int[] a16 = new int[30]; //HF = 0 RF = 1 HM = 2 RM = 3
   public int[] a18 = new int[30]; //0-3 = 0-10, 4-7 = 10+ 8 = 20+, 12 = 30+, 16 = 40+, 20 = 50+ 24-27 = 60+ 28-29 unknown
   static public final String[] GUIDE16 = {"HF0_16", "RF0_16", "HM0_16", "RM0_16", 
   "HF10_16", "RF10_16", "HM10_16", "RM10_16", "HF20_16", "RF20_16", "HM20_16", "RM20_16", 
   "HF30_16", "RF30_16", "HM30_16", "RM30_16", "HF40_16", "RF40_16", "HM40_16", "RM40_16", 
   "HF50_16", "RF50_16", "HM50_16", "RM50_16", "HF60_16", "RF60_16", "HM60_16", "RM60_16", "UF_16", "UM_16"};
   //HERE 18
   static public final String[] GUIDE18 = {"HF0_18", "RF0_18", "HM0_18", "RM0_18", 
   "HF10_18", "RF10_18", "HM10_18", "RM10_18", "HF20_18", "RF20_18", "HM20_18", "RM20_18", 
   "HF30_18", "RF30_18", "HM30_18", "RM30_18", "HF40_18", "RF40_18", "HM40_18", "RM40_18", 
   "HF50_18", "RF50_18", "HM50_18", "RM50_18", "HF60_18", "RF60_18", "HM60_18", "RM60_18", "UF_18", "UM_18"};
   // LGA 2016 Name
   //wrtie mega string!!!!!!
   public int amount;
   public String name;
   public String type;
   public String cType;
   public double area;
   public String state;
   public int iState;
   
   public int homRolFValue;
   public int homRolMValue;
   public float percentage;
   public Integer[] homAndRiskValues16 = new Integer[30];
   public Integer[] homAndRiskValues18 = new Integer[30];
   public Integer[] otherInfo = new Integer[4]; //only contains median values
   public Integer sumHomelessValue16=null;
   public Integer sumHomelessValue18=null;
   public Integer sumAtRiskValue16;
   public Integer sumAtRiskValue18;
   public Integer p16 = 0;
   public Integer p18 = 0;
   public Integer popu = null;

   public Integer ChangeHom;
   public Integer ChangeRisk;
   public Integer ChangePop;
   public Float cH;
   public Float cR;
   public Float cP;
   static public final String[] PIEGUIDE = {"Female 0-9", "Male 0-9", "Female 10-19", "Male 10-19", "Female 20-29", "Male 20-29", "Female 30-39", "Male 30-39", "Female 40-49", "Male 40-49", "Female 50-59", "Male 50-59", "Female 60+", "Male 60+", "Female Unknown", "Male Unknown",};
   
   /**
    * Create an LGA and set the fields
    */
   public LGA() {}

   public LGA(String name){
      this.name = name;
   }

   
   public LGA(int code, String name) {
      this.code = code;
      this.name = name;
   }

   public LGA(int code, String name, String type, double area, String state, Integer pop) {
      this.code = code;
      this.name = name;
      this.type = type;
      this.area = area;
      this.state = state;
      if (pop!=null){
         this.popu = pop;   
      }
      
   }

   public Integer setChangeHome(Integer h){
      this.ChangeHom = h;
      return this.ChangeHom;
   }

   public Integer setChangeRisk(Integer r){
      this.ChangeRisk = r;
      return this.ChangeRisk;
   }

   public Integer setChangePop(Integer p){
      this.ChangePop = p;
      return this.ChangePop;
   }


   public Integer getChangeHome(){
      return ChangeHom;
   }

   public Integer getChangeRisk(){
      return ChangeRisk;
   }

   public Integer getChangePop(){
      return ChangePop;
   }

   public int getCode16() {
      return code;
   }

   public String getName16() {
      return name;
   }

   public String getType(){
      return type;
   }

   public double getArea(){
      return area;
   }

   public String getState(){
      return state;
   }

   public Integer getPop(){
      return popu;
   }

   public int getMaleHomelessValue(){
      return homRolMValue;
   }

   public int getFemaleHomelessValue(){
      return homRolFValue;
   }

   public Integer[] sethomAndRiskValues16(String name){
         homAndRiskValues16 = jdbc.getHomAndRiskValue16(name);
         return homAndRiskValues16;
   }

   public Integer[] sethomAndRiskValues18(String name){
      homAndRiskValues18 = jdbc.getHomAndRiskValue18(name);
      return homAndRiskValues18;
   }

   public Integer[] setOtherInfo(String name){
      otherInfo = jdbc.getThoseOtherValues(name);
      return otherInfo;
   }

   public Integer getSumHomelessValue16(){
      //need the next lines cause otherwise page crashes, cant add objects if null, or maybe there is a way, i dont know
      sumHomelessValue16 = 0;
      for(int i = 0;i<27;i = i+2){
         if (homAndRiskValues16[i]!=null){
         sumHomelessValue16 += homAndRiskValues16[i];
         }
      }
      if (homAndRiskValues16[28]!=null){
         sumHomelessValue16 += homAndRiskValues16[28];
      }
      if (homAndRiskValues16[29]!=null){
         sumHomelessValue16 += homAndRiskValues16[29];
      }
      return sumHomelessValue16;
   }

   public Integer getSumHomelessValue18(){
      sumHomelessValue18 = 0;
      for(int i = 0;i<28;i = i+2){
         if (homAndRiskValues18[i]!=null){
         sumHomelessValue18 += homAndRiskValues18[i];
         }
      }
      if (homAndRiskValues18[28]!=null){
         sumHomelessValue18 += homAndRiskValues18[28];
      }
      if (homAndRiskValues18[29]!=null){
         //there is an error here for page 3.2
         sumHomelessValue18 += homAndRiskValues18[29];
      }
      return sumHomelessValue18;
   }

   public Integer getSumAtRiskValue16(){
      sumAtRiskValue16 = 0;
      for(int i = 1;i<28;i = i+2){
         if (homAndRiskValues16[i]!=null){
         sumAtRiskValue16 += homAndRiskValues16[i];
         }
      }
      if (homAndRiskValues16[28]!=null){
         sumAtRiskValue16 += homAndRiskValues16[28];
      }
      if (homAndRiskValues16[29]!=null){
         sumAtRiskValue16 += homAndRiskValues16[29];
      }
      return sumAtRiskValue16;
   }

   public Integer getSumAtRiskValue18(){
      sumAtRiskValue18 = 0;
      for(int i = 1;i<28;i = i+2){
         if (homAndRiskValues18[i]!=null){
            sumAtRiskValue18 += homAndRiskValues18[i];
         }
      }
      if (homAndRiskValues18[28]!=null){
         sumAtRiskValue18 += homAndRiskValues18[28];
      }
      if (homAndRiskValues18[29]!=null){
         sumAtRiskValue18 += homAndRiskValues18[29];
      }
      return sumAtRiskValue18;
   }

   public Integer setp16(String name){
      p16 = 0;
      p16 = jdbc.getSinglePopulation16(name);
      return p16;
   }

   public Integer setp18(String name){
      p18 = 0;
      p18 = jdbc.getSinglePopulation18(name);
      return p18;
   }

   public Integer getp16(){
      return p16;
   }

}