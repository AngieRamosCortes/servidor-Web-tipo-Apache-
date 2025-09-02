package co.edu.escuelaing.webframe.examples;

import java.util.concurrent.atomic.AtomicLong;

import co.edu.escuelaing.webframe.annotations.GetMapping;
import co.edu.escuelaing.webframe.annotations.RequestParam;
import co.edu.escuelaing.webframe.annotations.RestController;

/**
 * Simple greeting controller demonstrating basic functionality.
 */
@RestController
public class GreetingController {

    private static final String TEMPLATE = "Hello, %s!";

    public static String getTemplate() {
        return TEMPLATE;
    }

    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/")
    public String index() {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>Web Framework - ECI</title>" +
                "<link rel='icon' type='image/jpeg' href='Logo.jpg'>" +
                "<link rel='stylesheet' href='styles.css'>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div style='text-align: center; margin-bottom: 20px;'>" +
                "<img src='Logo.jpg' alt='Escuela Colombiana de Ingeniería Julio Garavito' style='max-width: 300px; height: auto; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);'/>"
                +
                "</div>" +
                "<h1>Web Framework</h1>" +
                "<p>Simple web framework with IoC container</p>" +
                "<ul>" +
                "<li><a href='/name'>Greeting with Name</a></li>" +
                "<li><a href='/greeting'>Greeting Service</a></li>" +
                "<li><a href='/counter'>Request Counter</a></li>" +
                "<li><a href='/home.html'>Static Page</a></li>" +
                "</ul>" +
                "</div>" +
                "<script src='script.js'></script>" +
                "</body>" +
                "</html>";
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        long requestNumber = counter.incrementAndGet();
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>Greeting Service</title>" +
                "<link rel='stylesheet' href='styles.css'>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1>Greeting Service</h1>" +
                "<p>" + String.format(TEMPLATE, name) + "</p>" +
                "<p>Request #" + requestNumber + "</p>" +
                "<a href='/'>Back to Home</a>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    @GetMapping("/counter")
    public String counter() {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>Request Counter</title>" +
                "<link rel='stylesheet' href='styles.css'>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1>Request Counter</h1>" +
                "<p>Total requests processed: " + counter.get() + "</p>" +
                "<a href='/'>Back to Home</a>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    @GetMapping("/name")
    public String nameForm() {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>Enter Your Name</title>" +
                "<link rel='stylesheet' href='styles.css'>" +
                "<style>" +
                ".form-container { max-width: 400px; margin: 0 auto; padding: 20px; }" +
                ".form-group { margin-bottom: 15px; }" +
                "label { display: block; margin-bottom: 5px; font-weight: bold; }" +
                "input[type='text'] { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }" +
                "button { background-color: #007bff; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; }"
                +
                "button:hover { background-color: #0056b3; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='form-container'>" +
                "<h1>Enter Your Name</h1>" +
                "<form method='get' action='/greeting'>" +
                "<div class='form-group'>" +
                "<label for='name'>Your Name:</label>" +
                "<input type='text' id='name' name='name' placeholder='Enter your name here...' required>" +
                "</div>" +
                "<button type='submit'>Get Greeting</button>" +
                "</form>" +
                "<br>" +
                "<a href='/'>Back to Home</a>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
