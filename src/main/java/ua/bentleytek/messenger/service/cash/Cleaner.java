package ua.bentleytek.messenger.service.cash;

public class Cleaner extends Thread{

    private Cleanable object;

    private long timeout;

    public Cleaner(Cleanable object, long timeout) {
        this.object = object;
        this.timeout = timeout;
    }

    @Override
    public void run(){
        while (! isInterrupted()){
            object.clean();
            try {
                sleep(timeout);
            } catch (InterruptedException ie) {
                //do nothing
            }
        }
    }
}
