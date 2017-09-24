package nl.hsac.fitnesse.fixture.util.selenium.driverfactory;

import nl.hsac.fitnesse.fixture.Environment;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.FileDetector;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UselessFileDetector;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.BiFunction;

/**
 * Factory to connect to remote selenium driver.
 */
public class RemoteDriverFactory implements DriverFactory {
    public static final String REMOTE_URL_KEY = "SeleniumRemoteUrl";
    private final BiFunction<URL, Capabilities, ? extends RemoteWebDriver> constr;
    private final URL url;
    private final Capabilities capabilities;

    public RemoteDriverFactory(String url, Capabilities capabilities) {
        this(RemoteWebDriver::new, url, capabilities);
    }

    public RemoteDriverFactory(
            BiFunction<URL, Capabilities, ? extends RemoteWebDriver> constr,
            String url,
            Capabilities capabilities) {
        try {
            this.constr = constr;
            this.url = new URL(url);
            this.capabilities = capabilities;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RemoteWebDriver createDriver() {
        RemoteWebDriver remoteWebDriver = constr.apply(url, capabilities);
        FileDetector fd = remoteWebDriver.getFileDetector();
        if (fd == null || fd instanceof UselessFileDetector) {
            remoteWebDriver.setFileDetector(new LocalFileDetector());
        }
        Environment.getInstance().setSymbol(REMOTE_URL_KEY, url.toString());
        return remoteWebDriver;
    }
}
