package co.edu.escuelaing.webframe.http;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.edu.escuelaing.webframe.annotations.RequestParam;
import co.edu.escuelaing.webframe.ioc.SimpleIoCContainer;

/**
 * Simple request dispatcher for handling HTTP requests.
 */
public class RequestDispatcher {
    
    private final SimpleIoCContainer container;
    private static final Pattern QUERY_PARAM_PATTERN = Pattern.compile("[?&]([^=&]+)=([^&]*)");
    
    public RequestDispatcher(SimpleIoCContainer container) {
        this.container = container;
    }
    
    public String processRequest(String method, String fullPath) {
        try {
            String path = fullPath;
            String queryString = "";
            
            int queryIndex = fullPath.indexOf('?');
            if (queryIndex != -1) {
                path = fullPath.substring(0, queryIndex);
                queryString = fullPath.substring(queryIndex + 1);
            }
            
            System.out.println("Processing route: " + path);
            
            Map<String, String> queryParams = parseQueryParameters(queryString);
            
            Object[] handlerInfo = container.findRouteHandler(path, method);
            if (handlerInfo == null) {
                return generateNotFoundResponse(path);
            }
            
            Object controllerInstance = handlerInfo[0];
            Method handlerMethod = (Method) handlerInfo[1];
            
            Object[] methodArgs = prepareMethodArguments(handlerMethod, queryParams);
            
            Object result = handlerMethod.invoke(controllerInstance, methodArgs);
            
            if (result instanceof String) {
                return (String) result;
            } else {
                return "Response generated";
            }
            
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.err.println("Error processing request: " + e.getMessage());
            return generateErrorResponse(e);
        }
    }
    
    private Map<String, String> parseQueryParameters(String queryString) {
        Map<String, String> params = new HashMap<>();
        
        if (queryString == null || queryString.trim().isEmpty()) {
            return params;
        }
        
        if (!queryString.startsWith("?") && !queryString.startsWith("&")) {
            queryString = "?" + queryString;
        }
        
        Matcher matcher = QUERY_PARAM_PATTERN.matcher(queryString);
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            params.put(key, value);
        }
        
        return params;
    }
    
    private Object[] prepareMethodArguments(Method method, Map<String, String> queryParams) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            RequestParam requestParam = param.getAnnotation(RequestParam.class);
            
            if (requestParam != null) {
                String paramName = requestParam.value();
                String paramValue = queryParams.get(paramName);
                
                if (paramValue == null || paramValue.isEmpty()) {
                    String defaultValue = requestParam.defaultValue();
                    paramValue = defaultValue.isEmpty() ? null : defaultValue;
                }
                
                args[i] = paramValue;
            } else {
                args[i] = null;
            }
        }
        
        return args;
    }
    
    private String generateNotFoundResponse(String path) {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head><title>404 - Not Found</title></head>" +
               "<body>" +
               "<h1>404 - Not Found</h1>" +
               "<p>The requested path " + path + " was not found.</p>" +
               "<p><a href='/'>Back to Home</a></p>" +
               "</body>" +
               "</html>";
    }
    
    private String generateErrorResponse(Exception exception) {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head><title>500 - Error</title></head>" +
               "<body>" +
               "<h1>500 - Internal Server Error</h1>" +
               "<p>An error occurred: " + exception.getMessage() + "</p>" +
               "<p><a href='/'>Back to Home</a></p>" +
               "</body>" +
               "</html>";
    }
}
