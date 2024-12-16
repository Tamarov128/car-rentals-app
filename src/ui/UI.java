package ui;

import domain.Car;
import domain.Reservation;
import repository.MemoryRepository;
import utils.exceptions.CarNotValidException;
import utils.exceptions.IDNotValidException;
import utils.exceptions.ReservationNotValidException;
import service.Service;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UI {

    private Service service;

    public UI (MemoryRepository<Integer,Car> carsRepo, MemoryRepository<Integer,Reservation> reservationsRepo) {
        this.service = new Service(carsRepo, reservationsRepo);
    }

    private void showAll(Iterable<?> elements, String description) {
        if (!elements.iterator().hasNext()) {
            System.out.println("No " + description + " found");
        }
        elements.forEach(System.out::println);
    }

    private Car inputCar() throws InputMismatchException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the car's id: ");
        int carId = scanner.nextInt();
        System.out.println("Enter the car's make: ");
        String carMake = scanner.next();
        System.out.println("Enter the car's model: ");
        String carModel = scanner.next();
        System.out.println("Enter the car's rental price: ");
        float carPrice = scanner.nextFloat();
        System.out.println("Enter the car's manufacturing year: ");
        int carYear = scanner.nextInt();
        return new Car(carId, carMake, carModel, carPrice, carYear);
    }

    private Reservation inputReservation() throws InputMismatchException, ReservationNotValidException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the reservation's id: ");
        int reservationId = scanner.nextInt();
        System.out.println("Enter the name of the person making the reservation: ");
        String reservationName = scanner.next();
        System.out.println("Enter the id of the car to be reserved: ");
        int carId = scanner.nextInt();
        return service.constructReservation(reservationId, reservationName, carId);
    }

    private int inputInt(String description) throws InputMismatchException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the " + description + " (integer): ");
        return scanner.nextInt();
    }

    private float inputFloat(String description) throws InputMismatchException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the " + description + " (real number): ");
        return scanner.nextFloat();
    }

    private boolean inputBoolean(String description) throws InputMismatchException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the " + description + " (true/false): ");
        return scanner.nextBoolean();
    }

    private String inputString(String description) throws InputMismatchException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the " + description + ": ");
        return scanner.next();
    }

    private void displayMenu() {
        System.out.println("\nPlease select an option:");
        System.out.println("-----------------");
        System.out.println("1. Show all cars");
        System.out.println("2. Show all reservations");
        System.out.println("3. Add a new car");
        System.out.println("4. Delete a car");
        System.out.println("5. Update a car");
        System.out.println("6. Create a new reservation");
        System.out.println("7. Cancel a reservation");
        System.out.println("8. Modify a reservation");
        System.out.println("9. Filter cars / reservations");
        System.out.println("10. Generate reports");
        System.out.println("0. Exit");
        System.out.println("-----------------");
    }

    private void displayFilterMenu() {
        System.out.println("Please select an option:");
        System.out.println("1. Filter cars by availability");
        System.out.println("2. Filter cars by make");
        System.out.println("3. Filter cars by manufacturing year");
        System.out.println("4. Filter reservations by name");
        System.out.println("5. Filter reservations by minimum price");
        System.out.println("6. Filter reservations by maximum price");
        System.out.println("-----------------");
    }

    private void displayReportMenu() {
        System.out.println("1. Get the cars manufactured after a given year");
        System.out.println("2. Get the average price of cars in the repository");
        System.out.println("3. Get all available cars sorted by price");
        System.out.println("4. Get reservations made by a certain person");
        System.out.println("5. Get the total price of reservations made by a certain person");
        System.out.println("-----------------");
    }

    private void runFilter() {
        Scanner in = new Scanner(System.in);
        displayFilterMenu();
        int filterOption = in.nextInt();
        switch (filterOption) {
            case 1:
                try {
                    boolean availability = inputBoolean("availability");
                    showAll(service.getCarsByAvailability(availability), "cars");
                }
                catch (InputMismatchException e) {
                    System.out.println("Enter a boolean value!");
                }
                break;
            case 2:
                try {
                    String make = inputString("make");
                    showAll(service.getCarsByMake(make), "cars");
                }
                catch (InputMismatchException e) {
                    System.out.println("Enter a string value!");
                }
                break;
            case 3:
                try {
                    int manufacturingYear = inputInt("manufacturing year");
                    showAll(service.getCarsByManufacturingYear(manufacturingYear), "cars");
                }
                catch (InputMismatchException e) {
                    System.out.println("Enter an integer!");
                }
                break;
            case 4:
                try {
                    String name = inputString("name");
                    showAll(service.getReservationsByName(name), "reservations");
                }
                catch (InputMismatchException e) {
                    System.out.println("Enter a string value!");
                }
                break;
            case 5:
                try {
                    float minPrice = inputFloat("min price");
                    showAll(service.getReservationsByMinPrice(minPrice), "reservations");
                }
                catch (InputMismatchException e) {
                    System.out.println("Enter a float value!");
                }
                break;
            case 6:
                try {
                    float maxPrice = inputFloat("max price");
                    showAll(service.getReservationsByMaxPrice(maxPrice), "reservations");
                }
                catch (InputMismatchException e) {
                    System.out.println("Enter a float value!");
                }
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }
    }

    private void runReport() {
        Scanner in = new Scanner(System.in);
        displayReportMenu();
        int reportOption = in.nextInt();
        switch (reportOption) {
            case 1:
                try {
                    int year = inputInt("year");
                    System.out.println(service.reportCarsManufacturedAfterYear(year));
                }
                catch (InputMismatchException e) {
                    System.out.println("Enter an integer!");
                }
                break;
            case 2:
                System.out.println(service.reportCarsAveragePrice());
                break;
            case 3:
                System.out.println(service.reportCarsAvailableSortedByPrice());
                break;
            case 4:
                try {
                    String name = inputString("name");
                    System.out.println(service.reportReservationsByName(name));
                }
                catch (InputMismatchException e) {
                    System.out.println("Enter a string value!");
                }
                break;
            case 5:
                try {
                    String name = inputString("name");
                    System.out.println(service.reportReservationsTotalPriceByName(name));
                }
                catch (InputMismatchException e) {
                    System.out.println("Enter a string value!");
                }
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }
    }

    public void run() {
        System.out.println("Welcome to CarRentals!!");
        Scanner in = new Scanner(System.in);
        displayMenu();
        int option = in.nextInt();
        while (option != 0) {
            switch (option) {
                case 1:
                    showAll(service.getAllCars(), "cars");
                    break;
                case 2:
                    showAll(service.getAllReservations(), "reservations");
                    break;
                case 3:
                    try {
                        Car newCar = inputCar();
                        service.addCar(newCar);
                    }
                    catch (InputMismatchException e) {
                        System.out.println("Enter valid values!");
                    }
                    catch (CarNotValidException e) {
                        System.out.println(e.getMessage());
                    }
                    catch (IDNotValidException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        int carId = inputInt("car id");
                        service.removeCar(carId);
                    }
                    catch (InputMismatchException e) {
                        System.out.println("Enter an integer!");
                    }
                    catch(IDNotValidException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        int carId2 = inputInt("car id");
                        System.out.println("Enter the new car details: (id must remain the same)");
                        Car updateCar = inputCar();
                        service.updateCar(carId2, updateCar);
                    }
                    catch (InputMismatchException e) {
                        System.out.println("Enter valid values!");
                    }
                    catch (CarNotValidException e) {
                        System.out.println(e.getMessage());
                    }
                    catch(IDNotValidException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6:
                    try {
                        Reservation reservation = inputReservation();
                        service.addReservation(reservation);
                    }
                    catch (ReservationNotValidException e) {
                        System.out.println(e.getMessage());
                    }
                    catch (IDNotValidException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 7:
                    try {
                        int reservationId = inputInt("reservation id");
                        service.removeReservation(reservationId);
                    }
                    catch (InputMismatchException e) {
                        System.out.println("Enter an integer!");
                    }
                    catch(IDNotValidException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 8:
                    try {
                        int reservationId2 = inputInt("reservation id");
                        System.out.println("Enter the new reservation details: (id must remain the same)");
                        Reservation updateReservation = inputReservation();
                        service.updateReservation(reservationId2, updateReservation);
                    }
                    catch (InputMismatchException e) {
                        System.out.println("Enter an integer!");
                    }
                    catch(ReservationNotValidException e) {
                        System.out.println(e.getMessage());
                    }
                    catch (IDNotValidException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 9:
                    runFilter();
                    break;
                case 10:
                    runReport();
                    break;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
            displayMenu();
            option = in.nextInt();
        }
        System.out.println("Bye!");
    }
}
