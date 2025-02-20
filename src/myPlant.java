public class myPlant implements Runnable {

    private String name;

    // Each plant has its own mailbox; this is defined by the main thread, and is used for communication between the main thread and the plant.
    private BlockingMailbox oranges_input_Mailbox;
    private BlockingMailbox bottles_output_Mailbox;

    public static final int NUMBER_OF_GRUNTS = 1;
    public static final int NUMBER_OF_BOTTLERS = 1;

    Worker[] grunt_list = new Worker[NUMBER_OF_GRUNTS];
    Worker[] bottler_list = new Worker[NUMBER_OF_BOTTLERS];

    // Mailbox for moving oranges from Grunts (who peel and juice) to Bottlers (who bottle and ship).
    BlockingMailbox bottler_mailbox = new BlockingMailbox();
    // Mailbox for moving bottles from Bottlers to the main thread.
    BlockingMailbox shipping_mailbox = new BlockingMailbox();

    public myPlant(String name, BlockingMailbox oranges_input_Mailbox, BlockingMailbox bottles_output_Mailbox) {
        this.name = name;
        this.oranges_input_Mailbox = oranges_input_Mailbox;
        this.bottles_output_Mailbox = bottles_output_Mailbox;
        Thread t = new Thread(this);
        t.start();
    }

    public String get_name() {
        return name;
    }

    public void run() {
        System.out.println("Plant " + name + " is running with plant mailbox" + oranges_input_Mailbox);

        // Start the bottle-operator workers (to do bottling)
        System.out.println(name + ": Starting bottler workers...");
        for (int i = 0; i < NUMBER_OF_BOTTLERS; i++) {
            bottler_list[i] = new Worker(name + "B" + (i+1), bottler_mailbox, bottles_output_Mailbox, "recieve, bottle, ship");
        }

        // Start the grunt workers (to do fetching, peeling, and juicing)
        System.out.println(name + ": Starting grunt workers...");

        for (int i = 0; i < NUMBER_OF_GRUNTS; i++) {
            grunt_list[i] = new Worker(name + "G" + (i+1), oranges_input_Mailbox, bottler_mailbox, "fetch, peel, juice, send");
        }

        // Wait the main thread to tell us to stop running.
        while (Main.isRunning()) {
            try {
                Thread.sleep(1000);
                System.out.println("Plant " + name + " is running...");
            } catch (InterruptedException ignored) {}
        }

        System.out.println("Plant " + name + " is stopping...");
        // Stop the workers
    }
    
}
