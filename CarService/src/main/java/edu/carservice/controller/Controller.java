package edu.carservice.controller;

import edu.carservice.model.Car;
import edu.carservice.model.User;
import edu.carservice.service.*;
import edu.carservice.util.*;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;

public class Controller {
    private Stage stage;
    private User curUser;
    private Scanner sc;

    private AuthService authService;
    private AuditService auditService;
    private UsersService usersService;
    private CarManageService carManageService;
    private OrderService orderService;

    public Controller() {
        migrate();
        init();
    }

    private void init() {
        stage = Stage.AUTH;
        curUser = null;
        sc = new Scanner(System.in);

        authService = new AuthService();
        auditService = new AuditService();
        usersService = new UsersService();
        carManageService = new CarManageService();
        orderService = new OrderService();
    }

    private void migrate() {
        URL fileUrl = ConnectionPool.class.getClassLoader().getResource("db.properties");

        try (
                FileInputStream fis = new FileInputStream(fileUrl.getFile());
                Connection c = ConnectionPool.getDataSource().getConnection();
                Statement statement = c.createStatement()
        ) {
            Properties properties = new Properties();
            properties.load(fis);
            String defaultSchema = properties.getProperty("liquibase.schema.default");
            String serviceSchema = properties.getProperty("liquibase.schema.service");
            String path = properties.getProperty("liquibase.changelog.path");

            statement.execute("create schema if not exists " + defaultSchema);
            statement.execute("create schema if not exists " + serviceSchema);

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
            database.setDefaultSchemaName(defaultSchema);
            database.setLiquibaseSchemaName(serviceSchema);

            Liquibase liquibase = new Liquibase(path, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (LiquibaseException | SQLException | IOException e) {
            System.err.println("Database migration error: " + e.getMessage());
        }
    }


    public void start() {
        while (!stage.equals(Stage.EXIT)) {
            if (stage.equals(Stage.AUTH)) {
                auth();
            } else if (stage.equals(Stage.MENU)) {
                menu();
            } else if (stage.equals(Stage.CARS)) {
                cars();
            } else if (stage.equals(Stage.ORDERS)) {
                orders();
            } else if (stage.equals(Stage.USERS)) {
                users();
            } else if (stage.equals(Stage.SEARCH)) {
                search();
            } else if (stage.equals(Stage.AUDIT)) {
                audit();
            }
        }
        sc.close();
    }

    private void auth() {
        System.out.print("\nAuthentication:\n1. Sign Up\n2. Sign In\n0. Exit\n> ");
        String username, password;

        try {
            int choice = sc.nextInt();
            switch (choice) {
                case 0:
                    stage = Stage.EXIT;
                    break;
                case 1:
                    username = readString("Enter username");
                    password = readString("Enter password");
                    String category = readString("Enter user category (client, manager, admin)");
                    authService.signUp(username, password, UserCategory.valueOf(category.toUpperCase()));
                    System.out.println("Success.");
                    break;
                case 2:
                    username = readString("Enter username");
                    password = readString("Enter password");
                    if (usersService.checkPassword(username, password)) {
                        curUser = authService.signIn(username, password);
                        auditService.addLog(LogType.INFO, curUser, "Log in.");
                        stage = Stage.MENU;
                    } else {
                        System.out.println("Invalid password.");
                    }
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid user category.");
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("Invalid input.");
        }
    }

    private void menu() {
        System.out.print("\nMenu:\n1. Cars\n2. Orders\n3. Users\n4. Search\n5. Audit\n0. Back\n> ");

        try {
            int choice = sc.nextInt();
            switch (choice) {
                case 0:
                    stage = Stage.AUTH;
                    break;
                case 1:
                    stage = Stage.CARS;
                    break;
                case 2:
                    stage = Stage.ORDERS;
                    break;
                case 3:
                    if (!isAdmin()) throw new IOException("You have no access to this.");
                    stage = Stage.USERS;
                    break;
                case 4:
                    stage = Stage.SEARCH;
                    break;
                case 5:
                    if (!isAdmin()) throw new IOException("You have no access to this.");
                    stage = Stage.AUDIT;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void cars() {
        System.out.print("\nCars:\n1. Display all\n2. Add new car\n3. Update car\n4. Remove car\n0. Back\n> ");

        String brand, model, condition;
        int year, price;
        long id;

        try {
            int choice = sc.nextInt();
            switch (choice) {
                case 0:
                    stage = Stage.MENU;
                    break;
                case 1:
                    carManageService.displayCars();
                    break;
                case 2:
                    if (!isManagerOrHigher()) throw new IOException("You have no access to this.");
                    brand = readString("Enter brand");
                    model = readString("Enter model");
                    year = readInteger("Enter year");
                    price = readInteger("Enter price");
                    condition = readString("Enter condition (new, great, used, bad, crashed)");
                    carManageService.addCar(brand, model, year, price, CarCondition.valueOf(condition.toUpperCase()));
                    auditService.addLog(LogType.INFO, curUser, "Added new car.");
                    break;
                case 3:
                    if (!isManagerOrHigher()) throw new IOException("You have no access to this.");
                    id = readInteger("Enter car id");
                    Car car = carManageService.getCar(id);
                    brand = readString("Enter brand or -1 to skip");
                    model = readString("Enter model or -1 to skip");
                    year = readInteger("Enter year or -1 to skip");
                    price = readInteger("Enter price or -1 to skip");
                    condition = readString("Enter condition (new, great, used, bad, crashed) or -1 to skip");

                    if (!"-1".equals(brand)) car.setBrand(brand);
                    if (!"-1".equals(model)) car.setModel(model);
                    if (year != -1) car.setYear(year);
                    if (price != -1) car.setPrice(price);
                    if (!"-1".equals(condition)) car.setCondition(CarCondition.valueOf(condition.toUpperCase()));

                    carManageService.updateCar(car);
                    auditService.addLog(LogType.INFO, curUser, "Update car info.");
                    break;
                case 4:
                    if (!isManagerOrHigher()) throw new IOException("You have no access to this.");
                    id = readInteger("Enter car id");
                    carManageService.removeCar(id);
                    auditService.addLog(LogType.INFO, curUser, "Remove car from list.");
                    break;

            }
        } catch (IOException e) {
            auditService.addLog(LogType.WARN, curUser, "Cars menu warning.");
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("Invalid input.");
        }
    }

    private void orders() {
        System.out.print("\nOrders:\n1. New buy order\n2. New service order\n3. Display all\n4. Change state\n0. Back\n> ");

        long id;
        try {
            int choice = sc.nextInt();
            switch (choice) {
                case 0:
                    stage = Stage.MENU;
                    break;
                case 1:
                    id = readInteger("Enter car id");
                    orderService.addBuyOrder(curUser.getId(), id);
                    auditService.addLog(LogType.INFO, curUser, "New buy order.");
                    break;
                case 2:
                    id = readInteger("Enter car id");
                    orderService.addServiceOrder(curUser.getId(), id);
                    auditService.addLog(LogType.INFO, curUser, "New service order.");
                    break;
                case 3:
                    if (!isManagerOrHigher()) throw new IOException("You have no access to this.");
                    orderService.displayOrders();
                    auditService.addLog(LogType.INFO, curUser, "Display all orders.");
                    break;
                case 4:
                    if (!isManagerOrHigher()) throw new IOException("You have no access to this.");
                    id = readInteger("Enter order id");
                    String state = readString("Enter order state (created, progress, declined)");
                    orderService.setOrderState(id, OrderState.valueOf(state.toUpperCase()));
                    auditService.addLog(LogType.INFO, curUser, "Changed order state.");
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            auditService.addLog(LogType.WARN, curUser, "Orders menu warning.");
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("Invalid input.");
        }
    }

    private void users() {
        System.out.print("\nUsers:\n1. Display all\n2. Display ordered by category" + "\n3. Display ordered by name\n4. Update user\n0. Back\n> ");

        try {
            int choice = sc.nextInt();
            switch (choice) {
                case 0:
                    stage = Stage.MENU;
                    break;
                case 1:
                    usersService.displayUsers();
                    auditService.addLog(LogType.INFO, curUser, "Display all users.");
                    break;
                case 2:
                    usersService.displayOrderedByCategory();
                    auditService.addLog(LogType.INFO, curUser, "Display all users ordered by category.");
                    break;
                case 3:
                    usersService.displayOrderedByName();
                    auditService.addLog(LogType.INFO, curUser, "Display all users ordered by name.");
                    break;
                case 4:
                    String name = readString("Enter username");
                    User user = usersService.getUser(name);
                    String password = readString("Enter password or -1 to skip");
                    String category = readString("Enter category or -1 to skip");

                    if (!"-1".equals(password)) user.setPassword(password);
                    if (!"-1".equals(category)) user.setCategory(UserCategory.valueOf(category));

                    usersService.updateUser(user);
                    auditService.addLog(LogType.INFO, curUser, "Update user.");
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            auditService.addLog(LogType.WARN, curUser, "Users menu warning.");
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("Invalid input.");
        }
    }

    private void search() {
        System.out.print("\nUsers:\n1. Cars by brand\n2. Cars by model" + "\n3. Cars by year\n4. Cars by price\n5. Filter by user" + "\n6. Filter by car\n7. Filter by state\n0. Back\n> ");

        try {
            int choice = sc.nextInt();
            switch (choice) {
                case 0:
                    stage = Stage.MENU;
                    break;
                case 1:
                    String brand = readString("Enter brand");
                    carManageService.displayCarsByBrand(brand);
                    auditService.addLog(LogType.INFO, curUser, "Search cars by brand.");
                    break;
                case 2:
                    String model = readString("Enter model");
                    carManageService.displayCarsByModel(model);
                    auditService.addLog(LogType.INFO, curUser, "Search cars by model.");
                    break;
                case 3:
                    int year = readInteger("Enter year");
                    carManageService.displayCarsByYear(year);
                    auditService.addLog(LogType.INFO, curUser, "Search cars by year.");
                    break;
                case 4:
                    int price = readInteger("Enter price");
                    carManageService.displayCarsByPrice(price);
                    auditService.addLog(LogType.INFO, curUser, "Search cars by price.");
                    break;
                case 5:
                    UsersService usersService = new UsersService();
                    String name = readString("Enter username");
                    orderService.filterByUser(usersService.getUser(name));
                    auditService.addLog(LogType.INFO, curUser, "Search orders by user.");
                    break;
                case 6:
                    CarManageService carManageService = new CarManageService();
                    int id = readInteger("Enter car id");
                    orderService.filterByCar(carManageService.getCar(id));
                    auditService.addLog(LogType.INFO, curUser, "Search orders by car.");
                    break;
                case 7:
                    String state = readString("Enter order state");
                    orderService.filterByState(OrderState.valueOf(state));
                    auditService.addLog(LogType.INFO, curUser, "Search orders by state.");
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            auditService.addLog(LogType.WARN, curUser, "Search menu warning.");
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("Invalid input.");
        }
    }

    private void audit() {
        System.out.print("\nAudit:\n1. Display all logs\n2. Logs by date\n3. Logs by user\n4. Logs by action\n5. Save to file\n0. Back\n> ");

        ArrayList<String> logs;
        try {
            int choice = sc.nextInt();
            switch (choice) {
                case 0:
                    stage = Stage.MENU;
                    break;
                case 1:
                    auditService.printLogs();
                    break;
                case 2:
                    String date = readString("Enter date");
                    logs = auditService.logsByDate(date);
                    auditService.display(logs);
                    auditService.addLog(LogType.INFO, curUser, "Display logs by date.");
                    break;
                case 3:
                    String username = readString("Enter username");
                    logs = auditService.logsByUser(username);
                    auditService.display(logs);
                    auditService.addLog(LogType.INFO, curUser, "Display logs by user.");
                case 4:
                    String action = readString("Enter action");
                    logs = auditService.logsByAction(action.toUpperCase());
                    auditService.display(logs);
                    auditService.addLog(LogType.INFO, curUser, "Display logs by action.");
                    break;
                case 5:
                    auditService.addLog(LogType.INFO, curUser, "Saved log to file.");
                    auditService.save();
                default:
                    break;
            }
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("Invalid input.");
        }
    }

    private boolean isManagerOrHigher() {
        return curUser != null &&
                (curUser.getCategory().equals(UserCategory.ADMIN) || curUser.getCategory().equals(UserCategory.MANAGER));
    }

    private boolean isAdmin() {
        return curUser != null && curUser.getCategory().equals(UserCategory.ADMIN);
    }

    private String readString(String msg) {
        System.out.print(msg + ": ");
        return sc.next();
    }

    private int readInteger(String msg) {
        System.out.print(msg + ": ");
        return sc.nextInt();
    }

    private long readLong(String msg) {
        System.out.print(msg + ": ");
        return sc.nextLong();
    }
}
