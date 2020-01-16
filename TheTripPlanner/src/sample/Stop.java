package sample;

public class Stop implements Comparable<Stop>{
    private String city;
    private String state;
    private int latDeg;
    private int latMin;

    private int longDeg;
    private int longMin;


    public Stop(){}

    public Stop(String name, String state, int latDeg, int latMin, int longDeg, int longMin){
        this.city = name;
        this.latDeg = latDeg;
        this.latMin = latMin;
        this.state = state;
        this.longDeg = longDeg;
        this.longMin = longMin;

    }

    public String getCity(){ return this.city; }

    public void setCity(String city){ this.city = city; }

    public String getState(){ return this.state; }

    public void setState(String state){ this.state = state; }

    public int getLatDeg(){ return this.latDeg; }

    public void setLatDeg(int latDeg){ this.latDeg = latDeg; }

    public int getLatMin(){ return this.latMin; }

    public void setLatMin(int latMin){ this.latMin = latMin; }

//    public char getLatDir(){ return this.latDir; }
//
//    public void setLatDir(char latDir){ this.latDir = latDir; }

    public int getLongDeg(){ return longDeg; }

    public void setLongDeg(int longDeg){ this.longDeg = longDeg; }

    public int getLongMin(){ return this.longMin; }

    public void setLongMin(int longMin){ this.longMin = longMin; }

//    public char getLongDir(){ return this.longDir; }
//
//    public void setLongDir(char longDir){ this.longDir = longDir; }

    public int compareTo(Stop newStop){
        return this.getCity().compareToIgnoreCase(newStop.getCity());
    }

    @Override
    public String toString(){
//        return "City: " + this.getCity() + "\tLat Deg: " + this.getLatDeg() + " \tLat Min: " + this.getLatMin() + " \tLat Dir: " + this.getLatDir()
//                + "  \tLong Deg: " + this.getLongDeg() + " \tLong Min: " + this.getLongMin() + " \tLong Dir: " + this.getLongDir();

        return this.city + ", " + this.state;
    }

}
