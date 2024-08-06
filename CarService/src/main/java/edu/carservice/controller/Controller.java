package edu.carservice.controller;

import edu.carservice.model.Car;
import edu.carservice.model.User;
import edu.carservice.service.*;
import edu.carservice.util.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    static private Stage stage = Stage.AUTH;
    static private User curUser;
    static private Scanner sc = new Scanner(System.in);


    public void start() {
        while (!stage.equals(Stage.EXIT)) {
            if (stage.equals(Stage.AUTH)) auth();
            else if (stage.equals(Stage.MENU)) menu();
            else if (stage.equals(Stage.CARS)) cars();
            else if (stage.equals(Stage.ORDERS)) orders();
            else if (stage.equals(Stage.USERS)) users();
            else if (stage.equals(Stage.SEARCH)) search();
            else if (stage.equals(Stage.AUDIT)) audit();
        }
    }

    private void auth() {
        System.out.print("\nAuthentication:\n1. Sign Up\n2. Sign In\n0. Exit\n> ");
        AuthService auth = new AuthService();
        UsersService usersService = new UsersService();
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
                    auth.signUp(username, password, UserCategory.valueOf(category.toUpperCase()));
                    System.out.println("Success.");
                    break;
                case 2:
                    username = readString("Enter username");
                    password = readString("Enter password");
                    if (usersService.checkPassword(username, password)) {
                        curUser = auth.signIn(username, password);
                        AuditService.addLog(LogType.INFO, curUser, "Log in.");
                        stage = Stage.MENU;
                    } else {
                        AuditService.addLog(LogType.INFO, curUser, "Wrong password.");
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

        CarManageService carManageService = new CarManageService();

        String brand, model, condition;
        int year, price, index;

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
                    AuditService.addLog(LogType.INFO, curUser, "Added new car.");
                    break;
                case 3:
                    if (!isManagerOrHigher()) throw new IOException("You have no access to this.");
                    index = readInteger("Enter car index");
                    Car car = carManageService.getCar(index);
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

                    carManageService.updateCar(index, car);
                    AuditService.addLog(LogType.INFO, curUser, "Update car info.");
                    break;
                case 4:
                    if (!isManagerOrHigher()) throw new IOException("You have no access to this.");
                    index = readInteger("Enter car index");
                    carManageService.removeCar(index);
                    AuditService.addLog(LogType.INFO, curUser, "Remove car from list.");
                    break;

            }
        } catch (IOException e) {
            AuditService.addLog(LogType.WARN, curUser, "Cars menu warning.");
            System.out.println(e.getMessage());
        }
    }

    private void orders() {
        System.out.print("\nOrders:\n1. New buy order\n2. New service order\n3. Display all\n4. Change state\n0. Back\n> ");
        OrderService orderService = new OrderService();
        CarManageService carManageService = new CarManageService();
        int index;
        try {
            int choice = sc.nextInt();
            switch (choice) {
                case 0:
                    stage = Stage.MENU;
                    break;
                case 1:
                    index = readInteger("Enter car index");
                    orderService.addBuyOrder(curUser, carManageService.getCar(index));
                    AuditService.addLog(LogType.INFO, curUser, "New buy order.");
                    break;
                case 2:
                    index = readInteger("Enter car index");
                    orderService.addServiceOrder(curUser, carManageService.getCar(index));
                    AuditService.addLog(LogType.INFO, curUser, "New service order.");
                    break;
                case 3:
                    if (!isManagerOrHigher()) throw new IOException("You have no access to this.");
                    orderService.displayOrders();
                    AuditService.addLog(LogType.INFO, curUser, "Display all orders.");
                    break;
                case 4:
                    if (!isManagerOrHigher()) throw new IOException("You have no access to this.");
                    index = readInteger("Enter order index");
                    String state = readString("Enter order state (created, progress, declined)");
                    orderService.setOrderState(index, OrderState.valueOf(state.toUpperCase()));
                    AuditService.addLog(LogType.INFO, curUser, "Changed order state.");
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            AuditService.addLog(LogType.WARN, curUser, "Orders menu warning.");
            System.out.println(e.getMessage());
        }
    }

    private void users() {
        System.out.print("\nUsers:\n1. Display all\n2. Display ordered by category" +
                "\n3. Display ordered by name\n4. Update user\n0. Back\n> ");
        UsersService usersService = new UsersService();

        try {
            int choice = sc.nextInt();
            switch (choice) {
                case 0:
                    stage = Stage.MENU;
                    break;
                case 1:
                    usersService.displayUsers();
                    AuditService.addLog(LogType.INFO, curUser, "Display all users.");
                    break;
                case 2:
                    usersService.displayOrderedByCategory();
                    AuditService.addLog(LogType.INFO, curUser, "Display all users ordered by category.");
                    break;
                case 3:
                    usersService.displayOrderedByName();
                    AuditService.addLog(LogType.INFO, curUser, "Display all users ordered by name.");
                    break;
                case 4:
                    String name = readString("Enter username");
                    User user = usersService.getUser(name);
                    String password = readString("Enter password or -1 to skip");
                    String category = readString("Enter category or -1 to skip");

                    if (!"-1".equals(password)) user.setPassword(password);
                    if (!"-1".equals(category)) user.setCategory(UserCategory.valueOf(category));

                    usersService.updateUser(user);
                    AuditService.addLog(LogType.INFO, curUser, "Update user.");;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            AuditService.addLog(LogType.WARN, curUser, "Users menu warning.");
            System.out.println(e.getMessage());
        }
    }

    private void search() {
        System.out.print("\nUsers:\n1. Cars by brand\n2. Cars by model" +
                "\n3. Cars by year\n4. Cars by price\n5. Orders by user" +
                "\n6. Orders by car\n7. Orders by state\n0. Back\n> ");
        FilterService filterService = new FilterService();

        try {
            int choice = sc.nextInt();
            switch (choice) {
                case 0:
                    stage = Stage.MENU;
                    break;
                case 1:
                    String brand = readString("Enter brand");
                    filterService.carsByBrand(brand);
                    AuditService.addLog(LogType.INFO, curUser, "Search cars by brand.");
                    break;
                case 2:
                    String model = readString("Enter model");
                    filterService.carsByModel(model);
                    AuditService.addLog(LogType.INFO, curUser, "Search cars by model.");
                    break;
                case 3:
                    int year = readInteger("Enter year");
                    filterService.carsByYear(year);
                    AuditService.addLog(LogType.INFO, curUser, "Search cars by year.");
                    break;
                case 4:
                    int price = readInteger("Enter price");
                    filterService.carsByPrice(price);
                    AuditService.addLog(LogType.INFO, curUser, "Search cars by price.");
                    break;
                case 5:
                    UsersService usersService = new UsersService();
                    String name = readString("Enter username");
                    filterService.ordersByUser(usersService.getUser(name));
                    AuditService.addLog(LogType.INFO, curUser, "Search orders by user.");
                    break;
                case 6:
                    CarManageService carManageService = new CarManageService();
                    int index = readInteger("Enter car index");
                    filterService.ordersByCar(carManageService.getCar(index));
                    AuditService.addLog(LogType.INFO, curUser, "Search orders by car.");
                    break;
                case 7:
                    String state = readString("Enter order state");
                    filterService.orderByState(OrderState.valueOf(state));
                    AuditService.addLog(LogType.INFO, curUser, "Search orders by state.");
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            AuditService.addLog(LogType.WARN, curUser, "Search menu warning.");
            System.out.println(e.getMessage());
        }
    }

    private void audit() {
        System.out.print("\nAudit:\n1. Display all logs\n2. Logs by date\n3. Logs by user\n4. Logs by action\n5. Save to file\n0. Back\n> ");
        AuditService auditService = new AuditService();
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
                    AuditService.addLog(LogType.INFO, curUser, "Display logs by date.");
                    break;
                case 3:
                    String username = readString("Enter username");
                    logs = auditService.logsByUser(username);
                    auditService.display(logs);
                    AuditService.addLog(LogType.INFO, curUser, "Display logs by user.");
                case 4:
                    String action = readString("Enter action");
                    logs = auditService.logsByAction(action.toUpperCase());
                    auditService.display(logs);
                    AuditService.addLog(LogType.INFO, curUser, "Display logs by action.");
                    break;
                case 5:
                    AuditService.addLog(LogType.INFO, curUser, "Saved log to file.");
                    auditService.save();
                default:
                    break;
            }
        } catch (IOException e) {
            AuditService.addLog(LogType.WARN, curUser, "Audit menu warning.");
            System.out.println(e.getMessage());
        }
    }

    private boolean isManagerOrHigher() {
        return curUser.getCategory().equals(UserCategory.ADMIN) || curUser.getCategory().equals(UserCategory.MANAGER);
    }

    private boolean isAdmin() {
        return curUser.getCategory().equals(UserCategory.ADMIN);
    }

    private String readString(String msg) {
        System.out.print(msg + ": ");
        return sc.next();
    }

    private int readInteger(String msg) {
        System.out.print(msg + ": ");
        return sc.nextInt();
    }
}
