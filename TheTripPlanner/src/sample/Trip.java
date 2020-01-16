package sample;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;

public class Trip {
    public ArrayList<Stop> stopList = new ArrayList<Stop>();
    public static ArrayList<Stop> possibleStops = new ArrayList<Stop>();

    public Trip(){

    }


    public void append(Stop newStop){
        stopList.add(newStop);
    }


    public void remove(String name){
        for(int i = 0; i < stopList.size(); i++){
            if(stopList.get(i).getCity().equalsIgnoreCase(name))
                stopList.remove(stopList.get(i));
        }
    }


    public void insert(Stop newStop, int index){
        stopList.add(index, newStop);
    }


    public void readTripFromFile(String fileName){
        File file = new File(fileName);
        try {
            Scanner reader = new Scanner(file);

            while (reader.hasNext()) {

                String[] line = reader.nextLine().split(",");

                String city = line[0];
                String state = line[1];
                int latDeg = Integer.parseInt(line[2]);
                int latMin = Integer.parseInt(line[3]);
                int longDeg = Integer.parseInt(line[4]);
                int longMin = Integer.parseInt(line[5]);
                stopList.add(new Stop(city, state, latDeg, latMin, longDeg, longMin));
            }

        }catch(IOException ioe){
            ioe.printStackTrace();
        }

    }

    public static void readPossibleStopsFromFile(String fileName) {
        File file = new File(fileName);
        try {
            Scanner reader = new Scanner(file);

            while (reader.hasNext()) {

                String[] line = reader.nextLine().split(",");

                String city = line[0];
                String state = line[1];
                int latDeg = Integer.parseInt(line[2]);
                int latMin = Integer.parseInt(line[3]);
                int longDeg = Integer.parseInt(line[4]);
                int longMin = Integer.parseInt(line[5]);
                possibleStops.add(new Stop(city, state, latDeg, latMin, longDeg, longMin));

            }

        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }


    public void writeToFile(String fileName){
        File file = new File(fileName);
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(fileName));

            for(int i = 0; i < stopList.size(); i++){

                writer.write(stopList.get(i).getCity());
                writer.append(",");
                writer.append(stopList.get(i).getState());
                writer.append(",");
                writer.print(stopList.get(i).getLatDeg());
                writer.append(",");
                writer.print(stopList.get(i).getLatMin());
                writer.append(",");
                writer.print(stopList.get(i).getLongDeg());
                writer.append(",");
                writer.print(stopList.get(i).getLongMin());
                writer.println();

            }

            writer.close();

        } catch(IOException ioe){
            ioe.printStackTrace();
        }

    }

    public static void writePossibleTripsToFile(String fileName){
        File file = new File(fileName);
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(fileName));

            for(int i = 0; i < possibleStops.size(); i++){

                writer.write(possibleStops.get(i).getCity());
                writer.append(",");
                writer.append(possibleStops.get(i).getState());
                writer.append(",");
                writer.print(possibleStops.get(i).getLatDeg());
                writer.append(",");
                writer.print(possibleStops.get(i).getLatMin());
                writer.append(",");
                writer.print(possibleStops.get(i).getLongDeg());
                writer.append(",");
                writer.print(possibleStops.get(i).getLongMin());
                writer.println();

            }

            writer.close();

        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    public double calculateDistance(){
        int EARTH_RADIUS = 6371;
        double RADIAN_FACTOR = 180.0/(Math.PI);
        double x = 0;
        double distance = 0;
        for(int i = 0; i < this.stopList.size() - 1; i++) {
            int j = i + 1;
            x = (Math.sin(stopList.get(i).getLatDeg()/ RADIAN_FACTOR) * Math.sin(stopList.get(j).getLatDeg() / RADIAN_FACTOR))
                    + (Math.cos(stopList.get(i).getLatDeg() / RADIAN_FACTOR)
                    * Math.cos(stopList.get(j).getLatDeg() / RADIAN_FACTOR)
                    * Math.cos(stopList.get(j).getLongDeg() / RADIAN_FACTOR - stopList.get(i).getLongDeg() / RADIAN_FACTOR));

            distance += EARTH_RADIUS * Math.atan( (Math.sqrt(1-Math.pow(x,2)))/x );
        }

        return Math.round(distance);
    }

    @Override
    public String toString(){
        String s = "";
        for(int i = 0; i < stopList.size(); i++){
            s += stopList.get(i).toString() + "\n";
        }
        return s;
    }

}
