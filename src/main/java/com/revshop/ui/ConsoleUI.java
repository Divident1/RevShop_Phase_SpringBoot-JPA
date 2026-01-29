package com.revshop.ui;

import com.revshop.model.*;
import com.revshop.repository.FavoriteRepository;
import com.revshop.repository.NotificationRepository;
import com.revshop.repository.ProductRepository;
import com.revshop.repository.ReviewRepository;
import com.revshop.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class ConsoleUI {

    private static final Logger log = LoggerFactory.getLogger(ConsoleUI.class);
    private Scanner scanner;

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ProductRepository productRepository;

    private User currentUser = null;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        log.info("RevShop started");
        System.out.println("\n==================================================");
        System.out.println("   Welcome to RevShop - Your Online Marketplace!");
        System.out.println("==================================================");

        while (true) {
            try {
                if (currentUser == null) {
                    showMainMenu();
                } else if ("BUYER".equalsIgnoreCase(currentUser.getRole())) {
                    showBuyerMenu();
                } else if ("SELLER".equalsIgnoreCase(currentUser.getRole())) {
                    showSellerMenu();
                }
            } catch (java.util.NoSuchElementException e) {
                log.info("Input stream closed. Exiting.");
                break;
            } catch (Exception e) {
                log.error("Error", e);
                System.out.println("Something went wrong. Try again.");
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Forgot Password");
        System.out.println("4. Exit");
        System.out.print("Choice: ");

        int choice = getInt();
        switch (choice) {
            case 1:
                register();
                break;
            case 2:
                login();
                break;
            case 3:
                forgotPassword();
                break;
            case 4:
                System.out.println("Goodbye!");
                System.exit(0);
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void forgotPassword() {
        System.out.println("\n--- Password Recovery ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("New Password: ");
        String newPass = scanner.nextLine();

        if (userService.resetPassword(email, newPass)) {
            System.out.println("Password reset. Please login.");
        } else {
            System.out.println("Failed. Email might not exist.");
        }
    }

    private void register() {
        System.out.println("\n--- Register ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Role (BUYER/SELLER): ");
        String role = scanner.nextLine().toUpperCase();

        if (!role.equals("BUYER") && !role.equals("SELLER")) {
            System.out.println("Invalid role.");
            return;
        }

        User user = new User(email, password, role, name, phone);

        if (role.equals("SELLER")) {
            System.out.print("Business Name: ");
            user.setBusinessName(scanner.nextLine());
            System.out.print("GSTIN: ");
            user.setGstin(scanner.nextLine());
        }

        if (userService.registerUser(user)) {
            System.out.println("Registration successful! Please login.");
        } else {
            System.out.println("Registration failed. Email might be taken.");
        }
    }

    private void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = userService.loginUser(email, password);
        if (user != null) {
            currentUser = user;
            System.out.println("Welcome, " + user.getName() + "!");

            List<Notification> notifs = notificationRepository.findByUserIdAndIsReadFalse(user.getUserId());
            if (!notifs.isEmpty()) {
                System.out.println("\n🔔 " + notifs.size() + " new notification(s):");
                for (Notification n : notifs) {
                    System.out.println(" - " + n.getMessage());
                    n.setRead(true);
                }
                notificationRepository.saveAll(notifs);
            }
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private void logout() {
        log.info("Logout: {}", currentUser.getEmail());
        currentUser = null;
        System.out.println("Logged out.");
    }

    private void showBuyerMenu() {
        System.out.println("\n--- Buyer Menu ---");
        System.out.println("1. Browse Products");
        System.out.println("2. Search Products");
        System.out.println("3. View Cart");
        System.out.println("4. Add to Cart");
        System.out.println("5. Checkout");
        System.out.println("6. Order History");
        System.out.println("7. My Favorites");
        System.out.println("8. Change Password");
        System.out.println("9. Logout");
        System.out.print("Choice: ");

        int c = getInt();
        switch (c) {
            case 1:
                browseProducts();
                break;
            case 2:
                searchProducts();
                break;
            case 3:
                viewCart();
                break;
            case 4:
                addToCart();
                break;
            case 5:
                checkout();
                break;
            case 6:
                viewOrderHistory();
                break;
            case 7:
                viewFavorites();
                break;
            case 8:
                changePassword();
                break;
            case 9:
                logout();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void browseProducts() {
        System.out.println("\n--- Products ---");
        List<Product> products = productService.getAllProducts();
        showProducts(products);
    }

    private void searchProducts() {
        System.out.println("\n--- Search ---");
        System.out.println("1. By Keyword");
        System.out.println("2. By Category");
        System.out.print("Choice: ");
        int c = getInt();

        List<Product> products;
        if (c == 1) {
            System.out.print("Keyword: ");
            products = productService.searchProducts(scanner.nextLine());
        } else if (c == 2) {
            List<Category> cats = categoryService.getAllCategories();
            for (Category cat : cats)
                System.out.println(cat.getCategoryId() + ". " + cat.getName());
            System.out.print("Category ID: ");
            products = productService.searchByCategory(getInt());
        } else {
            System.out.println("Invalid.");
            return;
        }
        showProducts(products);
    }

    private void showProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        for (Product p : products) {
            System.out.printf("ID:%d | %s | Rs.%.2f | Stock:%d\n",
                    p.getProductId(), p.getName(), p.getDiscountedPrice(), p.getQuantity());
        }
        System.out.print("\nProduct ID for details (0 to skip): ");
        int pid = getInt();
        if (pid > 0) {
            Product sel = products.stream().filter(p -> p.getProductId() == pid).findFirst().orElse(null);
            if (sel != null)
                showProductDetails(sel);
            else
                System.out.println("Invalid ID.");
        }
    }

    private void showProductDetails(Product p) {
        System.out.println("\n--- " + p.getName() + " ---");
        System.out.println("Description: " + p.getDescription());
        System.out.println("Price: Rs." + p.getDiscountedPrice() + " (MRP: " + p.getMrp() + ")");
        System.out.println("Stock: " + p.getQuantity());

        List<Review> reviews = reviewRepository.findByProductId(p.getProductId());
        System.out.println("\nReviews (" + reviews.size() + "):");
        for (Review r : reviews)
            System.out.println("- " + r);

        System.out.println("\n1.Add to Cart  2.Write Review  3.Favorite  4.Back");
        System.out.print("Choice: ");
        int c = getInt();

        if (c == 1) {
            System.out.print("Qty: ");
            cartService.addToCart(currentUser.getUserId(), p.getProductId(), getInt());
        } else if (c == 2) {
            System.out.print("Rating (1-5): ");
            int rating = getInt();
            System.out.print("Comment: ");
            Review review = new Review(p.getProductId(), currentUser.getUserId(), rating, scanner.nextLine());
            review.setReviewDate(new Timestamp(System.currentTimeMillis()));
            reviewRepository.save(review);
            System.out.println("Review added!");
        } else if (c == 3) {
            if (!favoriteRepository.existsByUserIdAndProductId(currentUser.getUserId(), p.getProductId())) {
                favoriteRepository.save(new Favorite(currentUser.getUserId(), p.getProductId()));
                System.out.println("Added to favorites!");
            } else {
                System.out.println("Already in favorites!");
            }
        }
    }

    private void viewCart() {
        System.out.println("\n--- Cart ---");
        while (true) {
            List<CartItem> items = cartService.getCartItems(currentUser.getUserId());
            if (items.isEmpty()) {
                System.out.println("Cart is empty.");
                return;
            }
            double total = 0;
            // Fetch product logic if needed, but CartItem entity might have loaded it if we
            // fetched correctly
            // CartService calls cartItemRepo.findByCartId.
            // My CartItem mapped Product with @ManyToOne.
            // Assuming JPA fetches it.

            for (CartItem item : items) {
                // If product is null, we might need to fetch manually, but let's assume JPA
                // mapping works or we patch it.
                // To be safe, let's fetch product details if product object is null (which
                // happens if I used @Transient or simple mapping).
                // I used @ManyToOne... private Product product;
                // If items are returned from JPA repo, product should be populated.

                String pName = "Unknown";
                double pPrice = 0;

                if (item.getProduct() != null) {
                    pName = item.getProduct().getName();
                    pPrice = item.getProduct().getDiscountedPrice();
                } else {
                    // Fallback if mapping failed/lazy load issue outside transaction
                    Product p = productRepository.findById(item.getProductId()).orElse(null);
                    if (p != null) {
                        pName = p.getName();
                        pPrice = p.getDiscountedPrice();
                    }
                }

                System.out.printf("PID:%d | %s | Qty:%d | Rs.%.2f\n",
                        item.getProductId(), pName, item.getQuantity(), pPrice);
                total += pPrice * item.getQuantity();
            }
            System.out.printf("Total: Rs.%.2f\n", total);

            System.out.println("\n1.Checkout 2.Remove 3.Update Qty 4.Back");
            System.out.print("Choice: ");
            int c = getInt();

            if (c == 1) {
                checkout();
                return;
            } else if (c == 2) {
                System.out.print("Product ID to remove: ");
                cartService.removeFromCart(currentUser.getUserId(), getInt());
            } else if (c == 3) {
                System.out.print("Product ID: ");
                int pid = getInt();
                System.out.print("New Qty: ");
                cartService.updateCartQuantity(currentUser.getUserId(), pid, getInt());
            } else
                return;
        }
    }

    private void addToCart() {
        System.out.print("\nProduct ID: ");
        int pid = getInt();
        System.out.print("Qty: ");
        cartService.addToCart(currentUser.getUserId(), pid, getInt());
    }

    private void checkout() {
        System.out.println("\n--- Checkout ---");
        List<CartItem> items = cartService.getCartItems(currentUser.getUserId());
        if (items.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        System.out.print("Shipping Address: ");
        String address = scanner.nextLine();

        System.out.println("\nPayment: 1.Credit 2.Debit 3.UPI");
        System.out.print("Choice: ");
        scanner.nextLine(); // consume leftover

        System.out.println("Processing payment...");
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        System.out.println("Payment successful!");

        if (orderService.placeOrder(currentUser.getUserId(), address)) {
            System.out.println("Order placed! Shipping to: " + address);
        } else {
            System.out.println("Order failed.");
        }
    }

    private void viewOrderHistory() {
        System.out.println("\n--- Orders ---");
        List<Order> orders = orderService.getOrdersByBuyer(currentUser.getUserId());
        if (orders.isEmpty())
            System.out.println("No orders.");
        else
            for (Order o : orders)
                System.out.println(o);
    }

    private void viewFavorites() {
        System.out.println("\n--- My Favorites ---");
        List<Favorite> favs = favoriteRepository.findByUserId(currentUser.getUserId());
        if (favs.isEmpty()) {
            System.out.println("No favorites found.");
            return;
        }

        List<Integer> pids = favs.stream().map(Favorite::getProductId).collect(Collectors.toList());
        List<Product> products = productRepository.findAllById(pids);

        for (Product p : products) {
            System.out.printf("ID:%d | %s | Rs.%.2f\n",
                    p.getProductId(), p.getName(), p.getDiscountedPrice());
        }

        System.out.println("\n1.Remove 2.Back");
        System.out.print("Choice: ");
        int c = getInt();
        if (c == 1) {
            System.out.print("Product ID to remove: ");
            int pid = getInt();
            if (favoriteRepository.existsByUserIdAndProductId(currentUser.getUserId(), pid)) {
                favoriteRepository.deleteById(new FavoriteId(currentUser.getUserId(), pid));
                System.out.println("Removed!");
            } else {
                System.out.println("Failed/Not found.");
            }
        }
    }

    private void showSellerMenu() {
        checkLowStock();

        System.out.println("\n--- Seller Menu ---");
        System.out.println("1. Add Product");
        System.out.println("2. My Products");
        System.out.println("3. Customer Orders");
        System.out.println("4. Product Reviews");
        System.out.println("5. Change Password");
        System.out.println("6. Logout");
        System.out.print("Choice: ");

        int c = getInt();
        switch (c) {
            case 1:
                addProduct();
                break;
            case 2:
                viewMyProducts();
                break;
            case 3:
                viewMyOrders();
                break;
            case 4:
                viewMyReviews();
                break;
            case 5:
                changePassword();
                break;
            case 6:
                logout();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void checkLowStock() {
        List<Product> prods = productService.getProductsBySeller(currentUser.getUserId());
        boolean hasAlert = false;
        for (Product p : prods) {
            if (p.getQuantity() <= p.getThreshold()) {
                if (!hasAlert) {
                    System.out.println("\n⚠️ LOW STOCK:");
                    hasAlert = true;
                }
                System.out.printf("  %s (ID:%d) - %d left\n", p.getName(), p.getProductId(), p.getQuantity());
            }
        }
    }

    private void addProduct() {
        System.out.println("\n--- Add Product ---");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        System.out.print("MRP: ");
        double mrp = getDouble();
        System.out.print("Discounted Price: ");
        double price = getDouble();
        System.out.print("Quantity: ");
        int qty = getInt();
        System.out.print("Low Stock Threshold: ");
        int threshold = getInt();

        List<Category> cats = categoryService.getAllCategories();
        for (Category c : cats)
            System.out.println(c.getCategoryId() + ". " + c.getName());
        System.out.print("Category ID: ");
        int catId = getInt();

        Product p = new Product(currentUser.getUserId(), catId, name, desc, mrp, price, qty, threshold);
        if (productService.addProduct(p))
            System.out.println("Product added!");
        else
            System.out.println("Failed to add product.");
    }

    private void viewMyProducts() {
        System.out.println("\n--- My Products ---");
        List<Product> prods = productService.getProductsBySeller(currentUser.getUserId());
        if (prods.isEmpty()) {
            System.out.println("No products.");
            return;
        }
        for (Product p : prods) {
            System.out.printf("ID:%d | %s | Rs.%.2f | Stock:%d\n",
                    p.getProductId(), p.getName(), p.getDiscountedPrice(), p.getQuantity());
        }
        System.out.println("\n1.Update 2.Delete 3.Back");
        System.out.print("Choice: ");
        int c = getInt();
        if (c == 1)
            updateProduct();
        else if (c == 2)
            deleteProduct();
    }

    private void updateProduct() {
        System.out.print("Product ID: ");
        int pid = getInt();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        System.out.print("MRP: ");
        double mrp = getDouble();
        System.out.print("Discounted Price: ");
        double price = getDouble();
        System.out.print("Quantity: ");
        int qty = getInt();
        System.out.print("Threshold: ");
        int threshold = getInt();

        List<Category> cats = categoryService.getAllCategories();
        for (Category c : cats)
            System.out.println(c.getCategoryId() + ". " + c.getName());
        System.out.print("Category ID: ");
        int catId = getInt();

        Product p = new Product(pid, currentUser.getUserId(), catId, name, desc, mrp, price, qty, threshold);
        if (productService.updateProduct(p))
            System.out.println("Updated!");
        else
            System.out.println("Update failed.");
    }

    private void deleteProduct() {
        System.out.print("Product ID: ");
        int pid = getInt();
        if (productService.deleteProduct(pid, currentUser.getUserId()))
            System.out.println("Deleted!");
        else
            System.out.println("Delete failed.");
    }

    private void viewMyOrders() {
        System.out.println("\n--- Customer Orders ---");
        List<Order> orders = orderService.getOrdersForSeller(currentUser.getUserId());
        if (orders.isEmpty())
            System.out.println("No orders.");
        else
            for (Order o : orders)
                System.out.println(o);
    }

    private void viewMyReviews() {
        System.out.println("\n--- Reviews ---");
        List<Product> prods = productService.getProductsBySeller(currentUser.getUserId());
        if (prods.isEmpty()) {
            System.out.println("No products.");
            return;
        }
        for (Product p : prods)
            System.out.printf("ID:%d | %s\n", p.getProductId(), p.getName());
        System.out.print("Product ID: ");
        int pid = getInt();

        Product sel = prods.stream().filter(p -> p.getProductId() == pid).findFirst().orElse(null);
        if (sel == null) {
            System.out.println("Invalid ID.");
            return;
        }
        List<Review> reviews = reviewRepository.findByProductId(pid);
        if (reviews.isEmpty())
            System.out.println("No reviews.");
        else {
            System.out.println("\n--- " + sel.getName() + " Reviews ---");
            for (Review r : reviews) {
                System.out.println(r.getRating() + "/5 - " + r.getComment());
            }
        }
    }

    private void changePassword() {
        System.out.println("\n--- Change Password ---");
        System.out.print("Old Password: ");
        String oldPass = scanner.nextLine();
        System.out.print("New Password: ");
        String newPass = scanner.nextLine();

        if (userService.changePassword(currentUser.getEmail(), oldPass, newPass)) {
            System.out.println("Password changed!");
        } else {
            System.out.println("Failed. Wrong old password?");
        }
    }

    private int getInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private double getDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }
}
