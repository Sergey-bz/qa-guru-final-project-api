package in.reqres.autotests.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:env.properties"
})
public interface EnvConfig extends Config {

    @DefaultValue("https://reqres.in/")
    String url();
}
