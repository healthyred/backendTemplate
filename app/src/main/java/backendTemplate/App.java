/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package backendTemplate;

import backendTemplate.dagger.ApplicationComponent;
import backendTemplate.dagger.DaggerApplicationComponent;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {

        ApplicationComponent applicationComponent = DaggerApplicationComponent.create();

        // Initialize Routing here
        final RoutingHandler routingHandler = new RoutingHandler();
        routingHandler.get("healthcheck", applicationComponent.getHealthHandler());

        Undertow.Builder builder = Undertow.builder();
//        builder.setIoThreads(2);
//        builder.setWorkerThreads(10);
        builder.addHttpListener(8080, "0.0.0.0");
        builder.setHandler(routingHandler);

        Undertow server = builder.build();
        server.start();

    }
}
