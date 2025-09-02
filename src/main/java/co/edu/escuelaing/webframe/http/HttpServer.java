package co.edu.escuelaing.webframe.http;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import co.edu.escuelaing.webframe.ioc.SimpleIoCContainer;

/**
 * HTTP server implementation.
 */
public class HttpServer {

    private final int port;
    @SuppressWarnings("unused")
    private final SimpleIoCContainer container;
    private ServerSocket serverSocket;
    private RequestDispatcher dispatcher;
    private volatile boolean running = false;

    private static final String STATIC_RESOURCES_PATH = "src/main/resources/static";

    public HttpServer(int port, SimpleIoCContainer container) {
        this.port = port;
        this.container = container;
        this.dispatcher = new RequestDispatcher(container);
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;

        System.out.println("Server started on port: " + port);
        System.out.println("Server running on: http://localhost:" + port);

        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                handleConnection(clientSocket);
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error accepting connection: " + e.getMessage());
                }
            }
        }
    }

    private void handleConnection(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedOutputStream dataOut = new BufferedOutputStream(clientSocket.getOutputStream())) {

            String requestLine = in.readLine();
            if (requestLine == null || requestLine.trim().isEmpty()) {
                return;
            }

            System.out.println("Request: " + requestLine);

            String[] requestParts = requestLine.split(" ");
            if (requestParts.length != 3) {
                sendErrorResponse(out, 400, "Bad Request");
                return;
            }

            String method = requestParts[0];
            String path = requestParts[1];

            String headerLine;
            while ((headerLine = in.readLine()) != null && !headerLine.trim().isEmpty()) {
            }

            if (isStaticFile(path)) {
                handleStaticFile(path, out, dataOut);
                return;
            }

            String response = dispatcher.processRequest(method, path);

            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html; charset=UTF-8");
            out.println("Connection: close");
            out.println();
            out.println(response);
            out.flush();

        } catch (IOException e) {
            System.err.println("Error handling connection: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    private boolean isStaticFile(String path) {
        return path.contains(".") &&
                (path.endsWith(".html") || path.endsWith(".css") || path.endsWith(".js") ||
                        path.endsWith(".png") || path.endsWith(".jpg"));
    }

    private void handleStaticFile(String path, PrintWriter out, BufferedOutputStream dataOut) {
        try {
            String filePath = path.startsWith("/") ? path.substring(1) : path;
            Path resourcePath = Paths.get(STATIC_RESOURCES_PATH, filePath);

            if (Files.exists(resourcePath) && Files.isRegularFile(resourcePath)) {
                byte[] fileContent = Files.readAllBytes(resourcePath);
                String mimeType = getMimeType(filePath);

                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: " + mimeType);
                out.println("Content-Length: " + fileContent.length);
                out.println("Connection: close");
                out.println();
                out.flush();

                dataOut.write(fileContent);
                dataOut.flush();

                System.out.println("Served static file: " + filePath);
            } else {
                sendErrorResponse(out, 404, "Not Found");
            }
        } catch (IOException e) {
            System.err.println("Error serving static file: " + e.getMessage());
            sendErrorResponse(out, 500, "Internal Server Error");
        }
    }

    private String getMimeType(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "js":
                return "application/javascript";
            case "png":
                return "image/png";
            case "jpg":
                return "image/jpeg";
            default:
                return "application/octet-stream";
        }
    }

    private void sendErrorResponse(PrintWriter out, int statusCode, String statusMessage) {
        out.println("HTTP/1.1 " + statusCode + " " + statusMessage);
        out.println("Content-Type: text/html");
        out.println("Connection: close");
        out.println();
        out.println("<html><head><title>" + statusCode + " " + statusMessage + "</title></head>");
        out.println("<body><h1>" + statusCode + " " + statusMessage + "</h1></body></html>");
        out.flush();
    }

    public void stop() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Server stopped");
            } catch (IOException e) {
                System.err.println("Error stopping server: " + e.getMessage());
            }
        }
    }

    public RequestDispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(RequestDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
}
