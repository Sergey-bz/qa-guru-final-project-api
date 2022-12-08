package in.reqres.autotests.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:user.properties"
})
public interface UserConfig extends Config {

    String email();
    String firstName();
    String lastName();
    String avatar();
}
