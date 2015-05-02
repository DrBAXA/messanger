package ua.bentleytek.messenger.service.cash;

import ua.bentleytek.messenger.service.cash.Cleanable;

public class Cleaner extends Thread{

    public Cleaner(Cleanable object) {
        this.object = object;
    }

    private Cleanable object;
    @Override
    public void run(){
        while (! isInterrupted()){
            object.clean();
            try {
                sleep(60000);
            } catch (InterruptedException ie) {
                //do nothing
            }
        }
    }
}
