package co.edu.escuelaing.webframe.ioc;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import co.edu.escuelaing.webframe.annotations.GetMapping;
import co.edu.escuelaing.webframe.annotations.RestController;

/**
 * Unit tests for the SimpleIoCContainer class.
 */
public class SimpleIoCContainerTest {
    
    private SimpleIoCContainer container;
    
    @Before
    public void setUp() {
        container = new SimpleIoCContainer();
    }
    
    @Test
    public void testRegisterController() throws Exception {
        container.registerController(TestController.class.getName());
        
        Object controller = container.getController("TestController");
        assertNotNull("Controller should be registered", controller);
        assertTrue("Controller should be instance of TestController", 
                   controller instanceof TestController);
        
        assertEquals("Should have 1 controller registered", 1, container.getControllerCount());
    }
    
    @Test
    public void testRouteHandlerDiscovery() throws Exception {
        container.registerController(TestController.class.getName());
        
        Object[] handlerInfo = container.findRouteHandler("/test", "GET");
        assertNotNull("Route handler should be found", handlerInfo);
        assertEquals("Should return 2 elements", 2, handlerInfo.length);
        
        Object controllerInstance = handlerInfo[0];
        Method handlerMethod = (Method) handlerInfo[1];
        
        assertNotNull("Controller instance should not be null", controllerInstance);
        assertNotNull("Handler method should not be null", handlerMethod);
        assertEquals("Method name should be 'testMethod'", "testMethod", handlerMethod.getName());
        
        assertTrue("Should have at least 1 route registered", container.getRouteCount() >= 1);
    }
    
    @Test
    public void testRouteNotFound() throws Exception {
        container.registerController(TestController.class.getName());
        
        Object[] handlerInfo = container.findRouteHandler("/nonexistent", "GET");
        assertNull("Non-existent route should return null", handlerInfo);
    }
    
    @Test(expected = ClassNotFoundException.class)
    public void testRegisterInvalidController() throws Exception {
        container.registerController("co.invalid.NonExistentController");
    }
    
    @RestController
    public static class TestController {
        
        @GetMapping("/test")
        public String testMethod() {
            return "Test response";
        }
    }
}
