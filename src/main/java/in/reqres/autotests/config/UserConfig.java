package in.reqres.autotests.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:user.properties"
})
public interface UserConfig extends Config {

    @Key("email")
    String email();

    @Key("firstName")
    String firstName();

    @Key("lastName")
    String lastName();

    @Key("avatar")
    String avatar();
}
