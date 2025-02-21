public class Worker implements Runnable {

    private String name;
    private BlockingMailbox inputMailbox;
    public BlockingMailbox outputMailbox;
    private String behaviors;
    private Orange o = null;


    /**
     * Constructor for the Worker class.
     * @param name A name for the worker.  Primarily relevant for debugging/logging, not critical for functionality.
     * @param inputMailbox The mailbox from which the worker will get Oranges.  Either the plant's fresh orange supply mailbox, or a transitory mailbox for moving semi-processed oranges between workers.
     * @param outputMailbox The mailbox the worker puts the orange in when he is done processing it.
     * @param behaviors A list of behaviors the worker will perform.  Possible behaviors include "fetch, peel, juice, bottle, ship, send, recieve"."
     */
    public Worker(String name, BlockingMailbox inputMailbox, BlockingMailbox outputMailbox, String behaviors) {
        this.name = name;
        this.inputMailbox = inputMailbox;
        this.outputMailbox = outputMailbox;
        this.behaviors = behaviors;
        Thread t = new Thread(this);
        t.start();
    }

    /**
     * Run method for the Worker.  This is where the worker does work.
     */
    public void run() {
        System.out.println("Worker " + name + " is running with I/O mailboxes " + inputMailbox + " and " + outputMailbox + " and behaviors " + behaviors);

        // Wait for the main thread to tell the worker to stop running.
        while (Main.isRunning()) {

            /**
             * If this worker is a grunt
             */
            if (behaviors.contains("fetch")) {
                o = inputMailbox.get(); 
                if (o.getState() == Orange.State.Fetched) {
                }
            }
            if (behaviors.contains("peel") && o.getState() == Orange.State.Fetched) {
                o.runProcess();
            }
            if (behaviors.contains("juice") && o.getState() == Orange.State.Peeled) {
                o.runProcess();
            }
            if (behaviors.contains("send") && o.getState() == Orange.State.Squeezed) {
                outputMailbox.put(o);
            }

            /**
             * If this worker is a bottler
             */
            if (behaviors.contains("recieve")) {
                o = inputMailbox.get();
            }
            if (behaviors.contains("bottle") && o.getState() == Orange.State.Squeezed) {
                o.runProcess();
            }
            if (behaviors.contains("ship") && o.getState() == Orange.State.Bottled) {
                o.runProcess(); // The "Processed" step.
                outputMailbox.put(o);
            }
        }
        System.out.println("Worker " + name + " is stopping...");
    }
}