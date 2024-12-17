package service;

import domain.Car;
import domain.Reservation;
import utils.filters.AbstractFilter;
import repository.MemoryRepository;
import utils.exceptions.CarNotValidException;
import utils.exceptions.IDNotValidException;
import utils.exceptions.ReservationNotValidException;
import utils.validators.CarValidator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Service {
    private MemoryRepository<Integer,Car> carsRepo;
    private MemoryRepository<Integer,Reservation> reservationsRepo;
    private CarValidator carValidator;

    public Service(MemoryRepository<Integer,Car> carsRepo, MemoryRepository<Integer,Reservation> reservationsRepo ) {
        this.carsRepo = carsRepo;
        this.reservationsRepo = reservationsRepo;
        this.carValidator = new CarValidator();
    }

    public Iterable<Car> getAllCars() {
        return carsRepo.getAll();
    }

    public Iterable<Reservation> getAllReservations(){
        return reservationsRepo.getAll();
    }

    public void addCar(Car car) throws IDNotValidException, CarNotValidException {
        try {
            if (carValidator.isValid(car)) {
                carsRepo.add(car.getId(),car);
            }
            else {
                throw new CarNotValidException("Rental price must be positive and manufacturing year must be a valid year!");
            }
        }
        catch (IDNotValidException e) {
            throw new IDNotValidException(e.getMessage());
        }
    }

    public void removeCar(int id) throws IDNotValidException {
        try {
            if (carsRepo.findById(id).isEmpty()) {
                throw new IDNotValidException("Car with given ID does not exist in the repository");
            }
            else {
                // remove car
                carsRepo.delete(id);
                // remove reservation containing the car
                for (Reservation reservation : reservationsRepo.getAll()) {
                    if (reservation.getRentedCar().getId() == id) {
                        reservationsRepo.delete(reservation.getId());
                        break;
                    }
                }
            }
        }
        catch (IDNotValidException e) {
            throw new IDNotValidException(e.getMessage());
        }
    }

    public void updateCar(int id, Car car) throws IDNotValidException, CarNotValidException {
        if (carValidator.isValid(car)) {
            if (carsRepo.findById(id).isEmpty()) {
                throw new IDNotValidException("Car with given ID does not exist in the repository");
            }
            else {
                // keep the availability attribute of the car to be modified
                Car oldCar = carsRepo.findById(id).get();
                car.setAvailable(oldCar.isAvailable());
                // modify car
                carsRepo.modify(id, car);
                // modify reservation containing the car
                for (Reservation reservation : reservationsRepo.getAll()) {
                    if (reservation.getRentedCar().getId() == id) {
                        Reservation newReservation = new Reservation(reservation.getId(), reservation.getName(), car);
                        reservationsRepo.modify(reservation.getId(), newReservation);
                        break;
                    }
                }
            }
        } else {
            throw new CarNotValidException("Rental price must be positive and manufacturing year must be a valid year!");
        }
    }

    public Reservation constructReservation(int id, String name, int carId) throws ReservationNotValidException {
        // if the car id given corresponds to a car in the car repo,
        // a reservation is created and returned, otherwise an exception is raised
        // ! only checks for the validity of the car id and not the car's availability
        //   does not mark the car as not available
        if (carsRepo.findById(carId).isEmpty()) {
            throw new ReservationNotValidException("The given ID does not correspond to a car ID from the repository!");
        }
        else {
            Car car = carsRepo.findById(carId).get();
            return new Reservation(id, name, car);
        }
    }

    public void addReservation(Reservation reservation) throws IDNotValidException, ReservationNotValidException {
        // checks the car's availability
        // marks car as not available
        try {
            Car car = reservation.getRentedCar();
            if (!car.isAvailable()) {
                throw new ReservationNotValidException("The car is not available!");
            }
            // set car in the reservation as not available
            car.setAvailable(false);
            // add reservation
            reservationsRepo.add(reservation.getId(),reservation);
            // set car in the car repo as not available (via copy)
            Car carCopy = new Car(car);
            carsRepo.modify(car.getId(), carCopy);
        }
        catch (IDNotValidException e) {
            // reservation id already exists in the reservation repo
            throw new IDNotValidException(e.getMessage());
        }
    }

    public void removeReservation(int id) throws IDNotValidException {
        // marks car in reservation as available
        // and removes reservation from repository
        if (reservationsRepo.findById(id).isEmpty()) {
            throw new IDNotValidException("Reservation with given ID does not exist in the repository");
        }
        else {
            try {
                Reservation reservation = reservationsRepo.findById(id).get();
                Car car = reservation.getRentedCar();
                // set car in the car repo as available (via copy)
                car.setAvailable(true);
                Car carCopy = new Car(car);
                carsRepo.modify(car.getId(),carCopy);
                // delete reservation
                reservationsRepo.delete(id);
            }
            catch (IDNotValidException e) {
                throw new IDNotValidException(e.getMessage());
            }
        }
    }

    public void updateReservation(int id, Reservation reservation) throws IDNotValidException, ReservationNotValidException {
        // checks the new car's availability
        // it has to either be in the current reservation or available
        if (reservationsRepo.findById(id).isEmpty()) {
            throw new IDNotValidException("Reservation with given ID does not exist in the repository");
        }
        else {
            try {
                Reservation currentReservation = reservationsRepo.findById(id).get();
                Car currentCar = currentReservation.getRentedCar();
                Car newCar = reservation.getRentedCar();

                if (!newCar.isAvailable() && !newCar.equals(currentCar)) {
                    throw new ReservationNotValidException("Car in the new reservation is not available!");
                }
                // modify reservation
                reservationsRepo.modify(id,reservation);
                // set currentCar in the car repo as available (via copy)
                currentCar.setAvailable(true);
                Car currentCarCopy = new Car(currentCar);
                carsRepo.modify(currentCar.getId(),currentCarCopy);
                // set car in the reservation as not available
                newCar.setAvailable(false);
                // set new car in the car repo as not available (via copy)
                Car newCarCopy = new Car(newCar);
                carsRepo.modify(newCar.getId(), newCarCopy);
            }
            catch (IDNotValidException e) {
                throw new IDNotValidException(e.getMessage());
            }
        }
    }

    public ArrayList<Car> getCarsByAvailability(boolean availability) {
        AbstractFilter<Car> filter = car -> car.isAvailable() == availability;
        ArrayList<Car> filteredCars = new ArrayList<>();
        Iterable<Car> cars = carsRepo.getAll();
        for (Car car : cars) {
            if (filter.accept(car)) {
                filteredCars.add(car);
            }
        }
        return filteredCars;
    }

    public ArrayList<Car> getCarsByMake(String make) {
        AbstractFilter<Car> filter = car -> car.getMake().equals(make);
        ArrayList<Car> filteredCars = new ArrayList<>();
        Iterable<Car> cars = carsRepo.getAll();
        for (Car car : cars) {
            if (filter.accept(car)) {
                filteredCars.add(car);
            }
        }
        return filteredCars;
    }

    public ArrayList<Car> getCarsByManufacturingYear(int manufacturingYear) {
        AbstractFilter<Car> filter = car -> car.getManufacturingYear() == manufacturingYear;
        ArrayList<Car> filteredCars = new ArrayList<>();
        Iterable<Car> cars = carsRepo.getAll();
        for (Car car : cars) {
            if (filter.accept(car)) {
                filteredCars.add(car);
            }
        }
        return filteredCars;
    }

    public ArrayList<Reservation> getReservationsByName(String name) {
        AbstractFilter<Reservation> filter = reservation -> reservation.getName().equals(name);
        ArrayList<Reservation> filteredReservations = new ArrayList<>();
        Iterable<Reservation> reservations = reservationsRepo.getAll();
        for (Reservation reservation : reservations) {
            if (filter.accept(reservation)) {
                filteredReservations.add(reservation);
            }
        }
        return filteredReservations;
    }

    public ArrayList<Reservation> getReservationsByMinPrice(float price) {
        AbstractFilter<Reservation> filter = reservation -> reservation.getPrice() >= price;
        ArrayList<Reservation> filteredReservations = new ArrayList<>();
        Iterable<Reservation> reservations = reservationsRepo.getAll();
        for (Reservation reservation : reservations) {
            if (filter.accept(reservation)) {
                filteredReservations.add(reservation);
            }
        }
        return filteredReservations;
    }

    public ArrayList<Reservation> getReservationsByMaxPrice(float price) {
        AbstractFilter<Reservation> filter = reservation -> reservation.getPrice() <= price;
        ArrayList<Reservation> filteredReservations = new ArrayList<>();
        Iterable<Reservation> reservations = reservationsRepo.getAll();
        for (Reservation reservation : reservations) {
            if (filter.accept(reservation)) {
                filteredReservations.add(reservation);
            }
        }
        return filteredReservations;
    }

    public String reportCarsManufacturedAfterYear(int year) {
        Iterable<Car> carsIterable = carsRepo.getAll();
        ArrayList<Car> cars = new ArrayList<>();
        carsIterable.forEach(cars::add);
        return cars.stream()
                .filter(car -> car.getManufacturingYear() >= year)
                .map(Car::toString)
                .collect(Collectors.joining("\n"));
    }

    public String reportCarsAveragePrice() {
        Iterable<Car> carsIterable = carsRepo.getAll();
        ArrayList<Car> cars = new ArrayList<>();
        carsIterable.forEach(cars::add);
        double averagePrice = cars.stream()
                .mapToDouble(Car::getRentalPrice)
                .average()
                .orElse(0.0);
        return "Average rental price: " + averagePrice;
    }

    public String reportCarsAvailableSortedByPrice() {
        Iterable<Car> carsIterable = carsRepo.getAll();
        ArrayList<Car> cars = new ArrayList<>();
        carsIterable.forEach(cars::add);
        return cars.stream()
                .filter(Car::isAvailable)
                .sorted(Comparator.comparing(Car::getRentalPrice))
                .map(Car::toString)
                .collect(Collectors.joining("\n"));
    }

    public String reportReservationsByName(String name) {
        Iterable<Reservation> reservationsIterable = reservationsRepo.getAll();
        ArrayList<Reservation> reservations = new ArrayList<>();
        reservationsIterable.forEach(reservations::add);
        return reservations.stream()
                .filter(r -> r.getName().equals(name))
                .map(Reservation::toString)
                .collect(Collectors.joining("\n"));
    }

    public String reportReservationsTotalPriceByName(String name) {
        Iterable<Reservation> reservationsIterable = reservationsRepo.getAll();
        ArrayList<Reservation> reservations = new ArrayList<>();
        reservationsIterable.forEach(reservations::add);
        double totalPrice = reservations.stream()
                .filter(r -> r.getName().equals(name))
                .mapToDouble(Reservation::getPrice)
                .sum();
        return "Total rental price: " + totalPrice;
    }

    public void populateCars(int entitiesNumber) throws IDNotValidException, CarNotValidException  {
        int currentEntitiesNumber = 0;
        for (Car car : carsRepo.getAll()) {
            currentEntitiesNumber++;
        }
        for (int i = 1; i <= entitiesNumber; i++) {
            int id = currentEntitiesNumber + i;
            String make = "Make" + id;
            String model = "Model" + id;
            float rentalPrice = 100 + (i % 200);
            int manufacturingYear = 2000 + (i % 24);
            Car newCar = new Car(id, make, model, rentalPrice, manufacturingYear);
            addCar(newCar);
        }
    }

    public void increasePriceTraditionalThreads(double percentage, int year, int threadsNumber) {
        int carsNumber = carsRepo.size();
        int chunkSize = Math.ceilDiv(carsNumber, threadsNumber);

        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 0; i < threadsNumber; i++) {

            int start = i * chunkSize + 1;
            int end = (i == threadsNumber - 1) ? carsNumber + 1 : start + chunkSize;

            Thread thread = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    Car car = carsRepo.findById(j).get();
                    if (car.getManufacturingYear() >= year) {
                        Car newCar = new Car(car);
                        newCar.setRentalPrice((float) (car.getRentalPrice() * (1 + percentage / 100)));
                        try {
                            updateCar(j, newCar);
                        } catch (IDNotValidException | CarNotValidException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void increasePriceExecutorService(double percentage, int year, int threadsNumber) {
        int carsNumber = carsRepo.size();
        int chunkSize = Math.ceilDiv(carsNumber, threadsNumber);

        ExecutorService executorService = Executors.newFixedThreadPool(threadsNumber);

        for (int i = 0; i < threadsNumber; i++) {

            int start = i * chunkSize + 1;
            int end = (i == threadsNumber - 1) ? carsNumber + 1 : start + chunkSize;

            executorService.submit(() -> {
                for (int j = start; j < end; j++) {
                    Car car = carsRepo.findById(j).get();
                    if (car.getManufacturingYear() >= year) {
                        Car newCar = new Car(car);
                        newCar.setRentalPrice((float) (car.getRentalPrice() * (1 + percentage / 100)));
                        try {
                            updateCar(j, newCar);
                        } catch (IDNotValidException | CarNotValidException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
        executorService.shutdown();
    }
}
