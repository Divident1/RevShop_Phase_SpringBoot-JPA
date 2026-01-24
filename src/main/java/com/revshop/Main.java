package com.revshop;

import com.revshop.dao.FavoriteDAO;
import com.revshop.dao.NotificationDAO;
import com.revshop.dao.ReviewDAO;
import com.revshop.service.*;
import com.revshop.ui.ConsoleUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting RevShop.....................");

        try {
            com.revshop.util.DatabaseInitializer.initialize();

            UserService userService = new UserService();

            ProductService productService = new ProductService();

            CartService cartService = new CartService();

               CategoryService categoryService = new CategoryService();
            OrderService orderService = new OrderService();

               ReviewDAO reviewDAO = new ReviewDAO();
            FavoriteDAO favoriteDAO = new FavoriteDAO();
               NotificationDAO notificationDAO = new NotificationDAO();

            ConsoleUI ui = new ConsoleUI(
                    userService,
                    productService,
                    cartService,
                    orderService,
                    reviewDAO,
                    favoriteDAO,
                    notificationDAO,
                    categoryService);

            ui.run();

        } catch (Exception e) {
            logger.error("Fatal error", e);
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
