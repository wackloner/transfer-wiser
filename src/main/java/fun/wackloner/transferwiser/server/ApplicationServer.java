package fun.wackloner.transferwiser.server;

import fun.wackloner.transferwiser.repository.AccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;


/**
 * Class which creates and manages a server.
 */
public class ApplicationServer {
    private static final Logger log = LogManager.getLogger(ApplicationServer.class);

    private static final int SERVER_PORT = 8080;
    private final Server jettyServer;

    public static final String BASE_URL = String.format("http://localhost:%d", SERVER_PORT);

    private ApplicationServer(Server jettyServer) {
        this.jettyServer = jettyServer;
    }

    public static ApplicationServer newInstance(AccountRepository accountRepository) {
        var config = new ApplicationConfig(accountRepository);
        var servlet = new ServletHolder(new ServletContainer(config));
        var server = new Server(SERVER_PORT);
        var contextHandler = new ServletContextHandler(server, "/*");
        contextHandler.addServlet(servlet, "/*");

        return new ApplicationServer(server);
    }

    public void start(boolean blocking) {
        try {
            jettyServer.start();
            if (blocking) {
                jettyServer.join();
            }
        } catch (Exception e) {
            log.error("Jetty server failed!", e);
        } finally {
            if (blocking) {
                jettyServer.destroy();
            }
        }
    }

    public void stop() {
        try {
            jettyServer.stop();
        } catch (Exception e) {
            log.error("Failed to stop jetty server!", e);
        }
    }
}
