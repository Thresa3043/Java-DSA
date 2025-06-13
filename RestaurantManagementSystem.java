import java.util.*;
public class RestaurantManagementSystem {
    static class MenuItem {
        String name;
        double price;
        boolean available;
        MenuItem(String name, double price) {
            this.name = name;
            this.price = price;
            this.available = true;
        }
        public String toString() {
            return name + " - rs " + price + (available ? " (Available)" : " (Not Available)");
        }
    }
    static class Inventory {
        Map<String, Integer> stock = new HashMap<>();
        void addStock(String item, int quantity) {
            stock.put(item, stock.getOrDefault(item, 0) + quantity);
        }
        boolean useStock(String item, int quantity) {
            int current = stock.getOrDefault(item, 0);
            if (current >= quantity) {
                stock.put(item, current - quantity);
                return true;
            }
            return false;
        }
        void displayStock() {
            System.out.println("\n--- Inventory Status ---");
            for (Map.Entry<String, Integer> entry : stock.entrySet()) {
                System.out.println("- " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }
    static class Order {
        static int counter = 1;
        int orderId;
        List<MenuItem> items = new ArrayList<>();
        double total = 0.0;
        boolean isPaid = false;
        Order() {
            this.orderId = counter++;
        }
        void addItem(MenuItem item) {
            if (item.available) {
                items.add(item);
                total += item.price;
            } else {
                System.out.println("Item " + item.name + " is not available.");
            }
        }
        void printReceipt() {
            System.out.println("\n--- Order Receipt ---");
            System.out.println("Order ID: " + orderId);
            for (MenuItem item : items) {
                System.out.println("- " + item.name + ": rs " + item.price);
            }
            System.out.println("---------------");
            System.out.printf("TOTAL: rs %.2f\n", total);
            System.out.println("---------------");
        }
        void payBill(Scanner scanner) {
            System.out.printf("\nYour total bill is: rs %.2f\n", total);
            while (!isPaid) {
                System.out.print("Pay the bill(Ok): ");
                String pay = scanner.next().toLowerCase();
                if (pay.equals("ok")) {
                    System.out.println("Payment successful. Thank you for visiting MLA Restaurant!");
                    isPaid = true;
                } else {
                    System.out.println("You must pay the bill to complete your order.");
                }
            }
        }
    }
    static class Table {
        int tableNumber;
        boolean reserved;
        Table(int tableNumber) {
            this.tableNumber = tableNumber;
            this.reserved = false;
        }
        public String toString() {
            return "Table " + tableNumber + (reserved ? " (Reserved)" : " (Available)");
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<MenuItem> menu = new ArrayList<>();
        Inventory inventory = new Inventory();
        List<Table> tables = new ArrayList<>();
        int totalTables = 5;
        for (int i = 1; i <= totalTables; i++) {
            tables.add(new Table(i));
        }
        String[] itemNames = {"Chicken Wings", "Chilli Chicken", "Butter Naan", "Roti",
                "Butter Chicken", "Kadai Chicken Curry", "Chicken Biryani",
                "MLA Special Chicken Biryani", "Soft Drinks", "Ice cream"};
        double[] prices = {200, 175, 25, 25, 250, 250, 200, 300, 25, 40};
        int[] initialStock = {10, 10, 15, 15, 5, 5, 15, 10, 20, 30};
        for (int i = 0; i < itemNames.length; i++) {
            menu.add(new MenuItem(itemNames[i], prices[i]));
            inventory.addStock(itemNames[i], initialStock[i]);
        }
        System.out.println("WELCOME TO MLA RESTAURANT...!");
        while (true) {
            System.out.println("\nLogin as:");
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int roleChoice = scanner.nextInt();
            if (roleChoice == 1) {
                boolean adminExit = false;
                while (!adminExit) {
                    System.out.println("\n--- Admin Panel ---");
                    System.out.println("1. View Inventory");
                    System.out.println("2. Add Stock");
                    System.out.println("3. Set Item Availability");
                    System.out.println("4. View Menu");
                    System.out.println("5. View Table Status");
                    System.out.println("6. Logout");
                    System.out.print("Choose option: ");
                    int adminChoice = scanner.nextInt();
                    scanner.nextLine();
                    switch (adminChoice) {
                        case 1:
                            inventory.displayStock();
                            break;
                        case 2:
                            System.out.print("Enter item name: ");
                            String itemToAdd = scanner.nextLine();
                            System.out.print("Enter quantity to add: ");
                            int qty = scanner.nextInt();
                            inventory.addStock(itemToAdd, qty);
                            System.out.println("Stock updated.");
                            break;
                        case 3:
                            System.out.print("Enter item name to update availability: ");
                            String itemName = scanner.nextLine();
                            MenuItem item = null;
                            for (MenuItem m : menu) {
                                if (m.name.equalsIgnoreCase(itemName)) {
                                    item = m;
                                    break;
                                }
                            }
                            if (item != null) {
                                System.out.print("Set as available? (yes/no): ");
                                String avail = scanner.nextLine();
                                item.available = avail.equalsIgnoreCase("yes");
                                System.out.println("Availability updated.");
                            } else {
                                System.out.println("Item not found.");
                            }
                            break;
                        case 4:
                            System.out.println("\n--- Menu ---");
                            for (MenuItem m : menu) {
                                System.out.println(m);
                            }
                            break;
                        case 5:
                            System.out.println("\n--- Table Status ---");
                            for (Table t : tables) {
                                System.out.println(t);
                            }
                            break;
                        case 6:
                            adminExit = true;
                            break;
                        default:
                            System.out.println("Invalid choice.");
                    }
                }

            } else if (roleChoice == 2) {
                System.out.println("\n--- Customer Panel ---");
                boolean tableReserved = false;
                int reservedTableNumber = -1;
                System.out.println("\n--- Table Reservation ---");
                for (Table t : tables) {
                    if (!t.reserved) {
                        System.out.println(t);
                    }
                }
                System.out.print("Enter table number to reserve (0 to skip): ");
                int tableChoice = scanner.nextInt();
                if (tableChoice > 0 && tableChoice <= tables.size()) {
                    Table selectedTable = tables.get(tableChoice - 1);
                    if (!selectedTable.reserved) {
                        selectedTable.reserved = true;
                        tableReserved = true;
                        reservedTableNumber = selectedTable.tableNumber;
                        System.out.println("Table " + reservedTableNumber + " reserved successfully.");
                    } else {
                        System.out.println("That table is already reserved.");
                    }
                } else if (tableChoice != 0) {
                    System.out.println("Invalid table number.");
                }
                boolean exit = false;
                while (!exit) {
                    Order order = new Order();
                    while (true) {
                        System.out.println("\nMenu:");
                        for (int i = 0; i < menu.size(); i++) {
                            System.out.println((i + 1) + ". " + menu.get(i));
                        }
                        System.out.print("Select item number to order (0 to finish): ");
                        int choice = scanner.nextInt();
                        if (choice == 0) break;
                        if (choice > 0 && choice <= menu.size()) {
                            MenuItem selected = menu.get(choice - 1);
                            if (inventory.useStock(selected.name, 1)) {
                                order.addItem(selected);
                                System.out.println("---------------");
                                System.out.println(selected.name + " added to your order.");
                            } else {
                                System.out.println("Sorry, " + selected.name + " is out of stock.");
                            }
                        } else {
                            System.out.println("Invalid choice.");
                        }
                    }
                    if (!order.items.isEmpty()) {
                        order.printReceipt();
                        if (tableReserved) {
                            System.out.println("Reserved Table Number: " + reservedTableNumber);
                        }
                        order.payBill(scanner);
                    } else {
                        System.out.println("No items were ordered.");
                    }
                    System.out.print("\nWould you like to place another order? (yes/no): ");
                    String again = scanner.next().toLowerCase();
                    if (!order.isPaid) {
                        System.out.println("You must pay your bill before placing another order or exiting.");
                        order.payBill(scanner);
                    }
                    if (!again.equals("yes")) {
                        exit = true;
                        if (tableReserved) {
                            tables.get(reservedTableNumber - 1).reserved = false;
                            System.out.println("Table " + reservedTableNumber + " is now free.");
                        }
                    }
                }

            } else if (roleChoice == 3) {
                System.out.println("Thank you! Visit again.");
                break;
            } else {
                System.out.println("Invalid selection.");
            }
        }
    }
}2
