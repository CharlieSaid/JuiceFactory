public class myPlant implements Runnable {

    private String name;

    // Each plant has its own mailbox; this is defined by the main thread, and is used for communication between the main thread and the plant.
    private BlockingMailbox oranges_input_Mailbox;
    private BlockingMailbox bottles_output_Mailbox;

    // Parameters for number of workers.
    public static final int NUMBER_OF_GRUNTS = 2;
    public static final int NUMBER_OF_BOTTLERS = 2;

    // Creating the workers storage arrays.
    Worker[] grunt_list = new Worker[NUMBER_OF_GRUNTS];
    Worker[] bottler_list = new Worker[NUMBER_OF_BOTTLERS];

    // Mailbox for moving oranges from Grunts (who peel and juice) to Bottlers (who bottle and ship).
    BlockingMailbox bottler_mailbox = new BlockingMailbox();
    // Mailbox for moving bottles from Bottlers to the main thread.
    BlockingMailbox shipping_mailbox = new BlockingMailbox();

    /**
     * Constructor for the plant class.
     * @param name Name of the plant.  Not relevant to functionality, for logging/debugging purposes only.
     * @param oranges_input_Mailbox The mailbox from which the plant recieves unprocessed oranges.
     * @param bottles_output_Mailbox The mailbox to which the plant sends finished oranges.
     */
    public myPlant(String name, BlockingMailbox oranges_input_Mailbox, BlockingMailbox bottles_output_Mailbox) {
        // Set up parameters
        this.name = name;
        this.oranges_input_Mailbox = oranges_input_Mailbox;
        this.bottles_output_Mailbox = bottles_output_Mailbox;
        
        // Start the thread
        Thread t = new Thread(this);
        t.start();
    }

    /**
     * Get the name of the plant.
     * @return The name of the plant.
     */
    public String get_name() {
        return name;
    }

    /**
     * Run method for the plant.
     * Note that the plant does not perform any work itself; it merely starts the workers.  
     * The workers do all the work; the plant is just an artifact of capitalism to justify the pay gap between the wage-slaves and the owners of the means of production.
     */
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
            } catch (InterruptedException ignored) {}
        }
        System.out.println("Plant " + name + " is stopping...");
    }
    
}
