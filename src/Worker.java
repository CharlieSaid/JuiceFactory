public class Worker implements Runnable {

    private String name;
    private BlockingMailbox inputMailbox;
    public BlockingMailbox outputMailbox;
    private String behaviors;
    private Orange o = null;


    public Worker(String name, BlockingMailbox inputMailbox, BlockingMailbox outputMailbox, String behaviors) {
        this.name = name;
        this.inputMailbox = inputMailbox;
        this.outputMailbox = outputMailbox;
        this.behaviors = behaviors;
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        System.out.println("Worker " + name + " is running with I/O mailboxes " + inputMailbox + " and " + outputMailbox + " and behaviors " + behaviors);

        // If we are a grunt

        while (Main.isRunning()) {

            if (behaviors.contains("fetch")) {
                o = inputMailbox.get();
                if (o.getState() == Orange.State.Fetched) {
                    System.out.println("Worker " + name + " fetched a new orange!");
                }
            }
            if (behaviors.contains("peel") && o.getState() == Orange.State.Fetched) {
                o.runProcess();
                System.out.println("Worker " + name + " peeled an orange!");
            }
            if (behaviors.contains("juice") && o.getState() == Orange.State.Peeled) {
                o.runProcess();
                System.out.println("Worker " + name + " juiced an orange!");
            }
            if (behaviors.contains("send") && o.getState() == Orange.State.Squeezed) {
                outputMailbox.put(o);
                System.out.println("Worker " + name + " sent an orange to the Bottling mailbox!");
            }

            // If we are a bottler

            if (behaviors.contains("recieve")) {
                o = inputMailbox.get();
                System.out.println("Worker " + name + " recieved an orange from the Bottling mailbox!");
            }
            if (behaviors.contains("bottle") && o.getState() == Orange.State.Squeezed) {
                o.runProcess();
                System.out.println("Worker " + name + " bottled an orange!");
            }
            if (behaviors.contains("ship") && o.getState() == Orange.State.Bottled) {
                o.runProcess(); // The "Processed" step.
                outputMailbox.put(o);
                System.out.println("Worker " + name + " shipped an orange!");
            }
            System.out.println("Worker " + name + " ran 1 cycle.");
        }
        System.out.println("Worker " + name + " is stopping...");
    }
    
}
