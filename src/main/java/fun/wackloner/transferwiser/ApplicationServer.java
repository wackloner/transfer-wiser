package fun.wackloner.transferwiser;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.ws.rs.ApplicationPath;

public class ApplicationServer {
    private final Server jettyServer;

    private ApplicationServer(Server jettyServer) {
        this.jettyServer = jettyServer;
    }

    public static ApplicationServer newInstance() {
        // TODO: extract constants
        ApplicationConfig config = new ApplicationConfig();
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        Server server = new Server(8080);
        ServletContextHandler contextHandler = new ServletContextHandler(server, "/*");
        contextHandler.addServlet(servlet, "/*");

        return new ApplicationServer(server);
    }

    public void run() {
        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
            // TODO: log exception
        } finally {
            jettyServer.destroy();
        }
    }

    @ApplicationPath("resources")
    private static class ApplicationConfig extends ResourceConfig {
        ApplicationConfig() {
            packages("fun.wackloner.transferwiser");
        }
    }
}