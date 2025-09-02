package co.edu.escuelaing.webframe;

import java.io.IOException;

import co.edu.escuelaing.webframe.http.HttpServer;
import co.edu.escuelaing.webframe.ioc.SimpleIoCContainer;

/**
 * Main class for the Web Framework.
 * web server with IoC capabilities.
 */
public class WebFrameworkApp {

    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws IOException {
        try {
            SimpleIoCContainer container = new SimpleIoCContainer();

            if (args.length > 0) {
                container.registerController(args[0]);
                System.out.println("Registered controller: " + args[0]);
            } else {
                container.scanAndRegisterControllers("co.edu.escuelaing.webframe.examples");
                System.out.println("Scanned and registered controllers from examples package");
            }

            HttpServer server = new HttpServer(DEFAULT_PORT, container);

            System.out.println("Starting Web Framework on port " + DEFAULT_PORT);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nShutting down server...");
                server.stop();
            }));

            server.start();

        } catch (Exception e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }
}
