
import java.util.ArrayList;
import java.util.Scanner;

class Stock {
    private String symbol;
    private String name;
    private double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void displayStock() {
        System.out.printf("%-10s %-20s Rs. %.2f%n",
                symbol, name, price);
    }
}

class Holding {
    private Stock stock;
    private int quantity;
    private double averageBuyPrice;

    public Holding(Stock stock, int quantity, double buyPrice) {
        this.stock = stock;
        this.quantity = quantity;
        this.averageBuyPrice = buyPrice;
    }

    public Stock getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAverageBuyPrice() {
        return averageBuyPrice;
    }

    public void buyMore(int amount, double price) {
        double totalCost =
                averageBuyPrice * quantity + price * amount;

        quantity += amount;
        averageBuyPrice = totalCost / quantity;
    }

    public void sell(int amount) {
        quantity -= amount;
    }

    public double getCurrentValue() {
        return quantity * stock.getPrice();
    }

    public double getProfitLoss() {
        return quantity *
                (stock.getPrice() - averageBuyPrice);
    }
}

public class StockTradingPlatform {

    private static Scanner sc = new Scanner(System.in);
    private static ArrayList<Stock> market = new ArrayList<>();
    private static ArrayList<Holding> portfolio = new ArrayList<>();

    private static double cashBalance = 100000.00;

    public static void main(String[] args) {

        initializeMarket();

        int choice;

        System.out.println("======================================");
        System.out.println("       STOCK TRADING PLATFORM");
        System.out.println("======================================");
        System.out.println("Starting cash balance: Rs. 100000.00");

        do {
            System.out.println("\n1. View Market Stocks");
            System.out.println("2. Buy Stocks");
            System.out.println("3. Sell Stocks");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Account Balance");
            System.out.println("6. Update Simulated Market Prices");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            choice = readInt();

            switch (choice) {
                case 1:
                    displayMarket();
                    break;

                case 2:
                    buyStock();
                    break;

                case 3:
                    sellStock();
                    break;

                case 4:
                    displayPortfolio();
                    break;

                case 5:
                    displayBalance();
                    break;

                case 6:
                    updatePrices();
                    break;

                case 7:
                    System.out.println("Thank you for using the platform!");
                    break;

                default:
                    System.out.println("Invalid choice. Select 1-7.");
            }

        } while (choice != 7);

        sc.close();
    }

    private static void initializeMarket() {
        market.add(new Stock("TCS", "Tata Consultancy", 3500));
        market.add(new Stock("INFY", "Infosys", 1800));
        market.add(new Stock("RELIANCE", "Reliance Industries", 2900));
        market.add(new Stock("HDFCBANK", "HDFC Bank", 1700));
        market.add(new Stock("WIPRO", "Wipro", 550));
    }

    private static int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Invalid input. Enter a whole number: ");
            sc.next();
        }
        return sc.nextInt();
    }

    private static void displayMarket() {
        System.out.println("\n----------- STOCK MARKET -----------");
        System.out.printf("%-10s %-20s %s%n",
                "Symbol", "Company", "Price");

        for (Stock stock : market) {
            stock.displayStock();
        }
    }

    private static Stock findStock(String symbol) {
        for (Stock stock : market) {
            if (stock.getSymbol().equalsIgnoreCase(symbol)) {
                return stock;
            }
        }
        return null;
    }

    private static Holding findHolding(Stock stock) {
        for (Holding holding : portfolio) {
            if (holding.getStock() == stock) {
                return holding;
            }
        }
        return null;
    }

    private static void buyStock() {
        displayMarket();

        System.out.print("\nEnter stock symbol to buy: ");
        String symbol = sc.next();

        Stock stock = findStock(symbol);

        if (stock == null) {
            System.out.println("Stock not found.");
            return;
        }

        System.out.print("Enter quantity to buy: ");
        int quantity = readInt();

        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }

        double cost = stock.getPrice() * quantity;

        if (cost > cashBalance) {
            System.out.println("Insufficient cash balance!");
            return;
        }

        cashBalance -= cost;

        Holding holding = findHolding(stock);

        if (holding == null) {
            portfolio.add(new Holding(stock, quantity, stock.getPrice()));
        } else {
            holding.buyMore(quantity, stock.getPrice());
        }

        System.out.printf("Successfully bought %d shares of %s.%n",
                quantity, stock.getSymbol());
        System.out.printf("Total cost: Rs. %.2f%n", cost);
    }

    private static void sellStock() {
        if (portfolio.isEmpty()) {
            System.out.println("Your portfolio is empty.");
            return;
        }

        displayPortfolio();

        System.out.print("\nEnter stock symbol to sell: ");
        String symbol = sc.next();

        Stock stock = findStock(symbol);

        if (stock == null) {
            System.out.println("Stock not found.");
            return;
        }

        Holding holding = findHolding(stock);

        if (holding == null) {
            System.out.println("You do not own this stock.");
            return;
        }

        System.out.print("Enter quantity to sell: ");
        int quantity = readInt();

        if (quantity <= 0 || quantity > holding.getQuantity()) {
            System.out.println("Invalid quantity.");
            return;
        }

        double proceeds = quantity * stock.getPrice();

        holding.sell(quantity);
        cashBalance += proceeds;

        if (holding.getQuantity() == 0) {
            portfolio.remove(holding);
        }

        System.out.printf("Successfully sold %d shares of %s.%n",
                quantity, stock.getSymbol());
        System.out.printf("Amount received: Rs. %.2f%n", proceeds);
    }

    private static void displayPortfolio() {
        System.out.println("\n----------- YOUR PORTFOLIO -----------");

        if (portfolio.isEmpty()) {
            System.out.println("No stocks owned yet.");
            return;
        }

        double totalValue = 0;
        double totalProfitLoss = 0;

        System.out.printf("%-10s %-10s %-15s %-15s%n",
                "Symbol", "Quantity", "Current Value", "Profit/Loss");

        for (Holding holding : portfolio) {
            double value = holding.getCurrentValue();
            double profitLoss = holding.getProfitLoss();

            totalValue += value;
            totalProfitLoss += profitLoss;

            System.out.printf("%-10s %-10d Rs. %-12.2f Rs. %.2f%n",
                    holding.getStock().getSymbol(),
                    holding.getQuantity(),
                    value,
                    profitLoss);
        }

        System.out.printf("%nTotal stock value: Rs. %.2f%n", totalValue);
        System.out.printf("Unrealized profit/loss: Rs. %.2f%n",
                totalProfitLoss);
    }

    private static void displayBalance() {
        System.out.printf("Available cash: Rs. %.2f%n", cashBalance);
    }

    private static void updatePrices() {
        for (Stock stock : market) {
            double change = 0.90 + Math.random() * 0.20;
            double newPrice = stock.getPrice() * change;
            stock.setPrice(Math.round(newPrice * 100.0) / 100.0);
        }

        System.out.println("Simulated market prices updated!");
        displayMarket();
    }
}

