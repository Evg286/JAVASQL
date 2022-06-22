package com.company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.PreparedStatement;

public class RidesController {
    static MySQL sql;

    static {
        try {
            sql = MySQL.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static String showRides() throws SQLException {

        try {
            ResultSet rs = sql.query("SELECT * FROM public_rides");

            int count = 0;
                while (rs.next()) {
                    count++;
                    System.out.println("\nRide ID: " + rs.getLong("rideID") +
                            "\nComments: " + rs.getString("comments") +
                            "\nFrom: " + rs.getString("startPointName") +
                            "\nTo: " + rs.getString("destination") +
                            "\nTime of Departure: " + rs.getString("rideTime") +
                            "\nTime of Arrival: " + rs.getString("arrivalTime") +
                            "\nNumber of Passengers: " + rs.getString("numOfPassengers") +
                            "\nDate: " + rs.getString("date")
                    );
                }
                if (count == 0)
                    System.out.println("List empty.\n\n");

            return Main.selectionMenu();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Main.selectionMenu();
        }
    }


    public static String addRide() throws SQLException {

        try {
            String query = "INSERT IGNORE INTO public_rides(comments, startPointName,destination, rideTime, numOfPassengers, date) " +
                    "VALUES(?,?,?,?,?,?)";

            PreparedStatement stmt = sql.getConnection().prepareStatement(query);

            String select_comment = getInputFromUser("Enter Comment: ", String.class);

            String select_origin = getInputFromUser("Select Origin: ", String.class);
            while (!validFrom(select_origin))
                select_origin = getInputFromUser("Invalid input. Please select a valid point of departure: ", String.class);

            String select_destination = getInputFromUser("Select Destination: ", String.class);
            while (!validTo(select_destination) || select_destination.equals(select_origin))
                select_destination = getInputFromUser("Invalid input. Please select a valid destination: ", String.class);

            String select_departure_time = getInputFromUser("Select Departure Time (hh:mm): ", String.class);
            while (!validTimeOfDeparture(select_departure_time))
                select_departure_time = getInputFromUser("Invalid input. Please use the hh:mm format and select a valid time: ", String.class);

            String select_date = getInputFromUser("Select Date (dd/mm/yyyy): ", String.class);
            while(!validDate(select_date))
                select_date = getInputFromUser("Invalid input. Please use the dd/mm/yyyy format and select a valid date: ", String.class);

            String select_no_passengers = getInputFromUser("Select the number of passengers (1-20): ", String.class);
            while(!validNumOfPassengers(String.valueOf(select_no_passengers)))
                select_no_passengers = getInputFromUser("Invalid selection. Please select 1-20: ", String.class);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Add the ride? Y/N");
            String add_ride = scanner.nextLine();

            if(add_ride.equals("Y")){
                stmt.setString(1, select_comment);
                stmt.setString(2, select_origin);
                stmt.setString(3, select_destination);
                stmt.setString(4, select_departure_time);
                stmt.setString(5, select_no_passengers);
                stmt.setString(6, select_date);

                stmt.execute();
                System.out.println("\nRide added.\n\n");
                showRides();
                return Main.selectionMenu();
            }
            else if(add_ride.equals("N")){
                System.out.println("Operation Aborted. Start over.\n");
                return addRide();
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            return addRide();
        }
        return Main.selectionMenu();
    }


    public static String deleteRide() throws SQLException {

        try {
            Integer select_ride = getInputFromUser("Enter ride ID to delete: ", Integer.class);
            String check = "SELECT * FROM public_rides WHERE rideID=?";
            PreparedStatement stmt = sql.getConnection().prepareStatement(check);
            stmt.setLong(1, select_ride);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next()) {
                System.out.println("Invalid ride ID. Please re-enter a valid ride ID.\n");
                return deleteRide();
            } else {
                String query = "DELETE FROM public_rides WHERE rideID=?";
                stmt = sql.getConnection().prepareStatement(query);
                stmt.setLong(1, select_ride);
                stmt.execute();
                System.out.println("\nRide deleted.\n\n");
                return Main.selectionMenu();

            }
        } catch (Exception e){
            System.out.println("Invalid input. Please try again.");
            return deleteRide();
        }
    }


    public static String searchRide() throws SQLException {

        try {
            Integer select_ride = getInputFromUser("Enter ride ID to display the ride: ", Integer.class);
            String query = "SELECT * FROM public_rides WHERE rideID=?";
            PreparedStatement stmt = sql.getConnection().prepareStatement(query);
            stmt.setLong(1, select_ride);
            ResultSet rs = stmt.executeQuery();

            if(!rs.next()) {
                System.out.println("Invalid ride ID. Please re-enter a valid ride ID.\n");
                return searchRide();
            }
            System.out.println("\nRide ID: " + rs.getLong("rideID") +
                    "\nComments: " + rs.getString("comments") +
                    "\nFrom: " + rs.getString("startPointName") +
                    "\nTo: " + rs.getString("destination") +
                    "\nTime of Departure: " + rs.getString("rideTime") +
                    "\nTime of Arrival: N/A" +
                    "\nNumber of Passengers: " + rs.getString("numOfPassengers") +
                    "\nDate: " + rs.getString("date")
            );

            return Main.selectionMenu();

        } catch (Exception e){
            System.out.println("Invalid input. Please try again.");
            return searchRide();
        }
    }


    public static String updateRide() throws SQLException {

        try {
            Integer select_ride = getInputFromUser("Enter ride ID to edit the ride: ", Integer.class);
            String check = "SELECT * FROM public_rides WHERE rideID=?";
            PreparedStatement stmt = sql.getConnection().prepareStatement(check);
            stmt.setLong(1, select_ride);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Invalid ride ID. Please re-enter a valid ride ID.\n");
                return updateRide();
            } else {
                String query = "UPDATE public_rides SET comments=?, startPointName=?, destination=?,rideTime=?, numOfPassengers=?, date=? WHERE rideID=?";

                stmt = sql.getConnection().prepareStatement(query);

                String select_comment = getInputFromUser("Enter Comment: ", String.class);

                String select_origin = getInputFromUser("Select Origin: ", String.class);
                while (!validFrom(select_origin))
                    select_origin = getInputFromUser("Invalid input. Please select a valid point of departure: ", String.class);

                String select_destination = getInputFromUser("Select Destination: ", String.class);
                while (!validTo(select_destination) || select_destination.equals(select_origin))
                    select_destination = getInputFromUser("Invalid input. Please select a valid destination: ", String.class);

                String select_departure_time = getInputFromUser("Select Departure Time (hh:mm): ", String.class);
                while (!validTimeOfDeparture(select_departure_time))
                    select_departure_time = getInputFromUser("Invalid input. Please use the hh:mm format and select a valid time: ", String.class);

                String select_date = getInputFromUser("Select Date (dd/mm/yyyy): ", String.class);
                while(!validDate(select_date))
                    select_date = getInputFromUser("Invalid input. Please use the dd/mm/yyyy format and select a valid date: ", String.class);

                Integer select_no_passengers = getInputFromUser("Select the number of passengers (1-20): ", Integer.class);
                while(!validNumOfPassengers(String.valueOf(select_no_passengers)))
                    select_no_passengers = getInputFromUser("Invalid selection. Please select 1-20: ", Integer.class);

                stmt.setString(1, select_comment);
                stmt.setString(2, select_origin);
                stmt.setString(3, select_destination);
                stmt.setString(4, select_departure_time);
                stmt.setDouble(5, select_no_passengers);
                stmt.setString(6, select_date);
                stmt.setLong(7, select_ride);
                stmt.execute();

                System.out.println("\nUpdated ride details:");
                System.out.println("\nRide ID: " + rs.getLong("rideID") +
                        "\nComments: " + rs.getString("comments") +
                        "\nFrom: " + rs.getString("startPointName") +
                        "\nTo: " + rs.getString("destination") +
                        "\nTime of Departure: " + rs.getString("rideTime") +
                        "\nTime of Arrival: N/A" +
                        "\nNumber of Passengers: " + rs.getString("numOfPassengers") +
                        "\nDate: " + rs.getString("date")
                );

                System.out.println("Ride updated.\n\n");
                return Main.selectionMenu();
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
            return searchRide();
        }
    }


    public static void showRidesAsc() throws SQLException {

        try {
            ResultSet rs = sql.query("SELECT * FROM public_rides ORDER BY date ASC");
            System.out.println("List of rides by ascending order of dates:");
            while (rs.next())
                System.out.println("\nRide ID: " + rs.getLong("rideID") +
                        "\nComments: " + rs.getString("comments") +
                        "\nFrom: " + rs.getString("startPointName") +
                        "\nTo: " + rs.getString("destination") +
                        "\nTime of Departure: " + rs.getString("rideTime") +
                        "\nTime of Arrival: " + rs.getString("arrivalTime") +
                        "\nNumber of Passengers: " + rs.getString("numOfPassengers") +
                        "\nDate: " + rs.getString("date")
                );
            System.out.println(Main.selectionMenu());
        } catch (Exception e){
            System.out.println(e.getMessage());
        } finally {
            Main.selectionMenu();
        }
    }

    public static void showRidesByDate() throws SQLException {

        try {
            String select_date = getInputFromUser("Enter date to display rides: ", String.class);
            while(!validDate(select_date))
                select_date = getInputFromUser("Invalid input. Please use the dd/mm/yyyy format and select a valid date: ", String.class);

            String query = "SELECT * FROM public_rides WHERE date=?";

            PreparedStatement stmt = sql.getConnection().prepareStatement(query);
            stmt.setString(1, select_date);
            stmt.execute();
            ResultSet rs = stmt.executeQuery();

            if(!rs.next()){
                System.out.println("No rides on the date selected.\n\n");
                Main.selectionMenu();
            } else {
                //if there's only one ride on a given date, rs.next() skips it
                System.out.println("\nRide ID: " + rs.getLong("rideID") +
                        "\nComments: " + rs.getString("comments") +
                        "\nFrom: " + rs.getString("startPointName") +
                        "\nTo: " + rs.getString("destination") +
                        "\nTime of Departure: " + rs.getString("rideTime") +
                        "\nTime of Arrival: N/A" +
                        "\nNumber of Passengers: " + rs.getString("numOfPassengers") +
                        "\nDate: " + rs.getString("date")
                );
                while (rs.next())
                    System.out.println("\nRide ID: " + rs.getLong("rideID") +
                            "\nComments: " + rs.getString("comments") +
                            "\nFrom: " + rs.getString("startPointName") +
                            "\nTo: " + rs.getString("destination") +
                            "\nTime of Departure: " + rs.getString("rideTime") +
                            "\nTime of Arrival: N/A" +
                            "\nNumber of Passengers: " + rs.getString("numOfPassengers") +
                            "\nDate: " + rs.getString("date")
                    );
                Main.selectionMenu();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            showRidesByDate();
        }
    }


    private static boolean validDate(String select_date){
        return select_date.matches("^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](2022|2023)$");
    }

    private static boolean validNumOfPassengers(String select_no_passengers){
        return select_no_passengers.matches("^(?:1?\\d(?:\\.\\d{1,2})?|20(?:\\.0?0?)?)$");
    }

    private static boolean validFrom(String select_origin){
        return select_origin.matches("[a-zA-Z]+");
    }

    private static boolean validTo(String select_destination){
        return select_destination.matches("[a-zA-Z]+");
    }

    private static boolean validTimeOfDeparture(String select_departure_time){
        return select_departure_time.matches("[0-2][0-3]:[0-5][0-9]");
    }


    private static <T> T getInputFromUser(String message, Class<?> cls){
        Scanner scanner = new Scanner(System.in);

        System.out.println(message);

        if (cls == Integer.class)
            return (T)Integer.valueOf(scanner.nextInt());
        else if (cls == String.class)
            return (T) String.valueOf(scanner.nextLine());

        return null;

    }
}
