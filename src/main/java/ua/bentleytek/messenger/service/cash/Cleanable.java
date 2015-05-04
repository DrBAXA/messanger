package ua.bentleytek.messenger.service.cash;


import javax.annotation.PostConstruct;

public abstract class Cleanable {
    private Thread cleaner;

    protected abstract void clean();

    @PostConstruct
    private void runCleaner(){
        cleaner = new Cleaner(this);
        cleaner.start();
    }
}
