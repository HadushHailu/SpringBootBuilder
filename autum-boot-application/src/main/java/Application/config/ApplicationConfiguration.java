package Application.config;
import org.autumframework.annotation.ConfigurationProperties;
import org.autumframework.annotation.Value;

@ConfigurationProperties(prefix = "app")
public class ApplicationConfiguration {

    @Value("name")
    private String name;

    @Value("timeout")
    private int timeout;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "ApplicationConfiguration{" +
                "name='" + name + '\'' +
                ", timeout=" + timeout +
                '}';
    }
}
