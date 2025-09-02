package co.edu.escuelaing.webframe.ioc;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.escuelaing.webframe.annotations.GetMapping;
import co.edu.escuelaing.webframe.annotations.RestController;

/**
 * IoC container for managing controllers and routes.
 */
public class SimpleIoCContainer {

    private final Map<String, Object> controllers = new HashMap<>();
    private final Map<String, RouteMapping> routes = new HashMap<>();

    private static class RouteMapping {
        final Object controllerInstance;
        final Method handlerMethod;

        RouteMapping(Object controllerInstance, Method handlerMethod) {
            this.controllerInstance = controllerInstance;
            this.handlerMethod = handlerMethod;
        }
    }

    public void registerController(String controllerClassName) throws Exception {
        System.out.println("Registering controller: " + controllerClassName);

        try {
            Class<?> controllerClass = Class.forName(controllerClassName);

            if (!controllerClass.isAnnotationPresent(RestController.class)) {
                System.err.println("Class " + controllerClassName + " is not annotated with @RestController");
                return;
            }

            Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            String controllerKey = controllerClass.getSimpleName();

            controllers.put(controllerKey, controllerInstance);

            scanControllerMethods(controllerInstance, controllerClass);

            System.out.println("Controller registered successfully: " + controllerClassName);

        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException
                | NoSuchMethodException | InvocationTargetException e) {
            System.err.println("Failed to register controller " + controllerClassName + ": " + e.getMessage());
            throw e;
        }
    }

    public void scanAndRegisterControllers(String packageName) throws Exception {
        System.out.println("Scanning package for controllers: " + packageName);

        List<Class<?>> controllerClasses = findControllerClasses(packageName);

        if (controllerClasses.isEmpty()) {
            System.out.println("No controllers found in package: " + packageName);
            return;
        }

        for (Class<?> controllerClass : controllerClasses) {
            try {
                registerController(controllerClass.getName());
            } catch (Exception e) {
                System.err.println("Failed to register controller: " + controllerClass.getName());
            }
        }

        System.out.println("Package scan completed. Registered " + controllerClasses.size() + " controllers.");
    }

    private List<Class<?>> findControllerClasses(String packageName) {
        List<Class<?>> controllerClasses = new ArrayList<>();

        try {
            String packagePath = packageName.replace('.', '/');
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources(packagePath);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());

                if (directory.exists() && directory.isDirectory()) {
                    scanDirectoryForControllers(directory, packageName, controllerClasses);
                }
            }
        } catch (IOException e) {
            System.err.println("Error scanning for controllers: " + e.getMessage());
        }

        return controllerClasses;
    }

    private void scanDirectoryForControllers(File directory, String packageName, List<Class<?>> controllerClasses) {
        File[] files = directory.listFiles();
        if (files == null)
            return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectoryForControllers(file, packageName + "." + file.getName(), controllerClasses);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);

                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(RestController.class)) {
                        controllerClasses.add(clazz);
                        System.out.println("Found controller class: " + className);
                    }
                } catch (ClassNotFoundException e) {
                    System.err.println("Could not load class: " + className);
                }
            }
        }
    }

    private void scanControllerMethods(Object controllerInstance, Class<?> controllerClass) {
        Method[] methods = controllerClass.getDeclaredMethods();

        for (Method method : methods) {
            GetMapping getMapping = method.getAnnotation(GetMapping.class);

            if (getMapping != null) {
                String path = getMapping.value();
                String routeKey = "GET:" + path;

                RouteMapping mapping = new RouteMapping(controllerInstance, method);
                routes.put(routeKey, mapping);

                System.out.println("Registered route: GET " + path + " -> " +
                        controllerClass.getSimpleName() + "." + method.getName());
            }
        }
    }

    public Object[] findRouteHandler(String path, String httpMethod) {
        String routeKey = httpMethod + ":" + path;
        RouteMapping mapping = routes.get(routeKey);

        if (mapping != null) {
            System.out.println("Route handler found: " + routeKey);
            return new Object[] { mapping.controllerInstance, mapping.handlerMethod };
        }

        System.out.println("No route handler found for: " + routeKey);
        return null;
    }

    public Object getController(String controllerName) {
        return controllers.get(controllerName);
    }

    public int getControllerCount() {
        return controllers.size();
    }

    public int getRouteCount() {
        return routes.size();
    }
}
