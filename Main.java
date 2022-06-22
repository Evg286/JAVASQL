package com.company;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {


    public static String selectionMenu() throws SQLException {
        System.out.println("""
               
               MAIN MENU
               
               1 - DISPLAY ALL RIDES
               2 - ADD A NEW RIDE
               3 - DELETE A RIDE
               4 - SEARCH A RIDE
               5 - UPDATE A RIDE
               6 - DISPLAY ALL RIDES BY ASCENDING DATES ORDER
               7 - DISPLAY ALL RIDES ON A CERTAIN DATE
               
               SELECT ACTION:
               """);

        Scanner scanner = new Scanner(System.in);
        int selection = scanner.nextInt();
            switch (selection) {
                case 1 -> RidesController.showRides();
                case 2 -> RidesController.addRide();
                case 3 -> RidesController.deleteRide();
                case 4 -> RidesController.searchRide();
                case 5 -> RidesController.updateRide();
                case 6 -> RidesController.showRidesAsc();
                case 7 -> RidesController.showRidesByDate();
            }

        return null;
    }


    public static void main(String[] args) throws SQLException {

        MySQL sql = MySQL.getInstance();

        sql.perform("CREATE TABLE IF NOT EXISTS public_rides(" +
                "rideID bigint PRIMARY KEY AUTO_INCREMENT," +
                "comments varchar(200)," +
                "startPointName varchar(100)," +
                "destination varchar(100)," +
                "rideTime varchar(100)," +
                "arrivalTime varchar(100)," +
                "numOfPassengers int not null," +
                "date varchar(50) not null)"
        );

//      //Uncomment to populate table
//        sql.perform("INSERT IGNORE INTO public_rides" +
//                "(comments, startPointName, destination, rideTime, arrivalTime, numOfPassengers, date) VALUES " +
//                "(\"Rainy\", \"Tel-Aviv\", \"Jerusalem\", \"14:00\",\"15:00\", 20, \"23/10/2022\")," +
//                "(\"Sunny\", \"Jerusalem\", \"Tel-Aviv\", \"14:00\",\"15:00\", 20, \"24/10/2022\"), " +
//                "(\"Rainy\", \"Eilat\", \"Metula\", \"08:00\",\"18:00\", 20, \"25/10/2022\")"
//        );


        selectionMenu();







    }
}


