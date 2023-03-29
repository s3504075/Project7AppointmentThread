package ApptStreamP6;

import java.util.concurrent.ConcurrentLinkedQueue;

public class AppointmentProcessor extends Thread implements ApptProcessor{
    private ConcurrentLinkedQueue<ReminderObj> safeQueue;
    private boolean stopped = false;

    public AppointmentProcessor(ConcurrentLinkedQueue<ReminderObj> safeQueue) {
        this.safeQueue = safeQueue;

        // start polling (invokes run(), below)
        this.start();
    }

    // remove messages from the queue and process them
    public void processAppointments() {
        System.out.println("before processing, queue size is " + safeQueue.size());
        safeQueue.stream().forEach(e -> {
            // Do something with each element
            e = safeQueue.remove();
            System.out.print(e);
        });
        System.out.println("after processing, queue size is now " + safeQueue.size());
    }

    // allow external class to stop us
    public void endProcessing() {
        this.stopped = true;
        interrupt();
    }

    // poll queue for cards
    public void run() {
        final int SLEEP_TIME = 1000; // ms
        while (true) {
            try {
                processAppointments();
                Thread.sleep(SLEEP_TIME);
                System.out.println("polling");
            } catch (InterruptedException ie) {
                // see if we should exit
                if (this.stopped == true) {
                    System.out.println("poll thread received exit signal");
                    break;
                }
            }
        }
    }

}
