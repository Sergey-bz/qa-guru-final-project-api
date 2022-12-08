package in.reqres.autotests.specs;

import in.reqres.autotests.config.EnvConfig;
import org.aeonbits.owner.ConfigFactory;

public class BaseSpec {

    private static final EnvConfig envConfig = ConfigFactory.create(EnvConfig.class);

    public static String getUrl() {
        return envConfig.url();
    }
}
