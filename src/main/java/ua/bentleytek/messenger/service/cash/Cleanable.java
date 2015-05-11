package ua.bentleytek.messenger.service.cash;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

public abstract class Cleanable {

    @Autowired
    Environment env;

    protected abstract void clean();

    @PostConstruct
    private void runCleaner(){
        Thread cleaner = new Cleaner(this, env.getProperty("timeout.cash.clean", Integer.class));
        cleaner.start();
    }
}
