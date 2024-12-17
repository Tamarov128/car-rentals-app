package ui;

import domain.Car;
import domain.Reservation;
import repository.databaseRepository.CarDatabaseRepository;
import repository.databaseRepository.ReservationDatabaseRepository;
import repository.fileRepository.binaryFile.CarBinaryFileRepository;
import repository.fileRepository.binaryFile.ReservationBinaryFileRepository;
import repository.memoryRepository.CarRepository;
import repository.memoryRepository.ReservationRepository;
import repository.fileRepository.textFile.CarTextFileRepository;
import repository.fileRepository.textFile.ReservationTextFileRepository;
import service.Service;
import utils.exceptions.IDNotValidException;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

public class Main {

    //Java solution for managing the reservations for car rentals.

    static class Initialize {
        // provide mock data for repositories
        public static void initializeMemory(CarRepository carRepository, ReservationRepository reservationRepository) {
            // add mock data to memory repos
            Car car1 = new Car(1, "Toyota", "Camry", 120, 2019);
            Car car2 = new Car(2, "Mazda", "MX-30", 250, 2021);
            Car car3 = new Car(3, "Dacia", "Logan", 200, 2013);
            Car car4 = new Car(4, "Mitsubishi", "Eclipse Cross", 240, 2024);
            Car car5 = new Car(5, "Dacia", "Spring", 150, 2022);
            Reservation reservation1 = new Reservation(1, "Elena", car1);
            Reservation reservation2 = new Reservation(2, "Tamara", car3);
            Reservation reservation3 = new Reservation(3, "Tamara", car4);
            try {
                carRepository.add(car1.getId(), car1);
                carRepository.add(car2.getId(), car2);
                carRepository.add(car3.getId(), car3);
                carRepository.add(car4.getId(), car4);
                carRepository.add(car5.getId(), car5);
                reservationRepository.add(reservation1.getId(), reservation1);
                car1.setAvailable(false);
                reservationRepository.add(reservation2.getId(), reservation2);
                car3.setAvailable(false);
                reservationRepository.add(reservation3.getId(), reservation3);
                car4.setAvailable(false);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static void initializeTextFile(String carsPath, String reservationsPath) {
            // add mock data to txt files
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(carsPath))) {
                bw.write("1,Toyota,Camry,120.0,2018,false\n");
                bw.write("2,Mazda,MX-30,250.0,2020,true\n");
                bw.write("3,Dacia,Logan,200.0,2013,false\n");
                bw.write("4,Mitsubishi,Eclipse Cross,240.0,2024,true\n");
                bw.write("5,Dacia,Spring,240.0,2022,true\n");
            } catch (IOException e) {
                //thrown by new FileWriter()
                throw new RuntimeException(e);
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(reservationsPath))) {
                bw.write("1,Elena,1,Toyota,Camry,120.0,2018,false\n");
                bw.write("2,Tamara,3,Dacia,Logan,200.0,2013,false\n");
            } catch (IOException e) {
                //thrown by new FileWriter()
                throw new RuntimeException(e);
            }
        }

        public static void initializeBinaryFile(String carsPath, String reservationsPath) {
            // add mock data to bin files
            Car car1 = new Car(1, "Toyota", "Camry", 120, 2017);
            Car car2 = new Car(2, "Mazda", "MX-30", 250, 2021);
            Car car3 = new Car(3, "Dacia", "Logan", 200, 2013);
            Car car4 = new Car(4, "Mitsubishi", "Eclipse Cross", 240, 2024);
            Car car5 = new Car(5, "Dacia", "Spring", 150, 2022);
            Reservation reservation1 = new Reservation(1, "Elena", car1);
            car1.setAvailable(false);
            Reservation reservation2 = new Reservation(2, "Tamara", car3);
            car3.setAvailable(false);
            try {
                ObjectOutputStream oosCar = new ObjectOutputStream(new FileOutputStream(carsPath));
                HashMap<Integer, Car> initialCarData = new HashMap<>();
                initialCarData.put(car1.getId(), car1);
                initialCarData.put(car2.getId(), car2);
                initialCarData.put(car3.getId(), car3);
                initialCarData.put(car4.getId(), car4);
                initialCarData.put(car5.getId(), car5);
                oosCar.writeObject(initialCarData);
                ObjectOutputStream oosReservation = new ObjectOutputStream(new FileOutputStream(reservationsPath));
                HashMap<Integer, Reservation> initialReservationData = new HashMap<>();
                initialReservationData.put(reservation1.getId(), reservation1);
                initialReservationData.put(reservation2.getId(), reservation2);
                oosReservation.writeObject(initialReservationData);
            } catch (IOException e) {
                //thrown by FileOutputStream() or by writeObject()
                throw new RuntimeException(e);
            }
        }
    }

    static class Start {
        // initialize repositories; create and run UI
        public static void startMemory() {
            CarRepository carRepository = new CarRepository();
            ReservationRepository reservationRepository = new ReservationRepository();
            Initialize.initializeMemory(carRepository, reservationRepository);
            UI ui = new UI(carRepository, reservationRepository);
            ui.run();
        }

        public static void startText(String carsPath, String reservationsPath) {
            Initialize.initializeTextFile(carsPath, reservationsPath);
            CarTextFileRepository carRepository = new CarTextFileRepository(carsPath);
            ReservationTextFileRepository reservationRepository = new ReservationTextFileRepository(reservationsPath);
            UI ui = new UI(carRepository, reservationRepository);
            ui.run();
        }

        public static void startBinary(String carsPath, String reservationsPath) {
            Initialize.initializeBinaryFile(carsPath, reservationsPath);
            CarBinaryFileRepository carRepository = new CarBinaryFileRepository(carsPath);
            ReservationBinaryFileRepository reservationRepository = new ReservationBinaryFileRepository(reservationsPath);
            UI ui = new UI(carRepository, reservationRepository);
            ui.run();
        }

        public static void startDatabase(String path) {
            CarDatabaseRepository carRepository = new CarDatabaseRepository(path);
            ReservationDatabaseRepository reservationRepository = new ReservationDatabaseRepository(path);
            UI ui = new UI(carRepository, reservationRepository);
            ui.run();
        }
    }

    public static void main(String[] args) {

//        Properties prop = new Properties();
//        try {
//            prop.load(new FileReader("settings.properties"));
//            String repoType = (String)prop.get("repo_type");
//
//            if (repoType.equals("memory")) {
//                Start.startMemory();
//            }
//            if (repoType.equals("text")) {
//                String carsPath = (String)prop.get("cars_path");
//                String reservationsPath = (String)prop.get("reservations_path");
//                Start.startText(carsPath, reservationsPath);
//            }
//            if (repoType.equals("binary")) {
//                String carsPath = (String)prop.get("cars_path");
//                String reservationsPath = (String)prop.get("reservations_path");
//                Start.startBinary(carsPath, reservationsPath);
//            }
//            if (repoType.equals("database")) {
//                String path = (String)prop.get("path");
//                Start.startDatabase(path);
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

        Car car1 = new Car(1, "Toyota", "Camry", 120, 2019);
        Car car2 = new Car(2, "Mazda", "MX-30", 250, 2021);
        Car car3 = new Car(3, "Dacia", "Logan", 200, 2013);
        Car car4 = new Car(4, "Mitsubishi", "Eclipse Cross", 240, 2024);
        Car car5 = new Car(5, "Dacia", "Spring", 150, 2022);
        Car car6 = new Car(6, "Toyota", "Camry", 120, 2019);
        Car car7 = new Car(7, "Mazda", "MX-30", 250, 2021);
        Car car8 = new Car(8, "Dacia", "Logan", 200, 2013);
        Car car9 = new Car(9, "Mitsubishi", "Eclipse Cross", 240, 2024);
        Car car10 = new Car(10, "Dacia", "Spring", 150, 2022);
        Car car11 = new Car(11, "Toyota", "Camry", 120, 2019);
        Car car12 = new Car(12, "Mazda", "MX-30", 250, 2021);
        Car car13 = new Car(13, "Dacia", "Logan", 200, 2013);
        Car car14 = new Car(14, "Mitsubishi", "Eclipse Cross", 240, 2024);
        Car car15 = new Car(15, "Dacia", "Spring", 150, 2022);
        Car car16 = new Car(16, "Toyota", "Camry", 120, 2019);
        Car car17 = new Car(17, "Mazda", "MX-30", 250, 2021);
        Car car18 = new Car(18, "Dacia", "Logan", 200, 2013);
        Car car19 = new Car(19, "Mitsubishi", "Eclipse Cross", 240, 2024);
        Car car20 = new Car(20, "Dacia", "Spring", 150, 2022);

        CarRepository carRepository = new CarRepository();
        try {
            carRepository.add(car1.getId(), car1);
            carRepository.add(car2.getId(), car2);
            carRepository.add(car3.getId(), car3);
            carRepository.add(car4.getId(), car4);
            carRepository.add(car5.getId(), car5);
            carRepository.add(car6.getId(), car6);
            carRepository.add(car7.getId(), car7);
            carRepository.add(car8.getId(), car8);
            carRepository.add(car9.getId(), car9);
            carRepository.add(car10.getId(), car10);
            carRepository.add(car11.getId(), car11);
            carRepository.add(car12.getId(), car12);
            carRepository.add(car13.getId(), car13);
            carRepository.add(car14.getId(), car14);
            carRepository.add(car15.getId(), car15);
            carRepository.add(car16.getId(), car16);
            carRepository.add(car17.getId(), car17);
            carRepository.add(car18.getId(), car18);
            carRepository.add(car19.getId(), car19);
            carRepository.add(car20.getId(), car20);
        }
        catch (IDNotValidException e) {
            throw new RuntimeException(e);
        }

        ReservationRepository reservationRepository = new ReservationRepository();
        Service service = new Service(carRepository, reservationRepository);

        service.increasePriceExecutorService(50, 2021, 4);
        System.out.println("Updated prices of cars produced after 2021 to 50% more");
        UI ui = new UI(carRepository, reservationRepository);
        ui.run();
    }
}
