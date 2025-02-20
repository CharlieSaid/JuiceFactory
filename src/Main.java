public class Main {
    
    public static final int NUMBER_OF_PLANTS = 2;
    public static final long RUNTIME = 8 * 1000;
    public static int oranges_used = 0;
    public static int bottles_made = 0;
    public static Orange o = null;
    public static boolean running = false;

    public static void main(String[] args) {
        System.out.println("Hello World");

        myPlant[] plant_list = new myPlant[NUMBER_OF_PLANTS];
        BlockingMailbox[] input_mailbox_list = new BlockingMailbox[NUMBER_OF_PLANTS];
        BlockingMailbox[] output_mailbox_list = new BlockingMailbox[NUMBER_OF_PLANTS];

        // Create plants
        for (int i = 0; i < NUMBER_OF_PLANTS; i++) {
            System.out.println("Creating plant " + (i+1) + "...");
            input_mailbox_list[i] = new BlockingMailbox();
            output_mailbox_list[i] = new BlockingMailbox();
            plant_list[i] = new myPlant("P" + (i+1), input_mailbox_list[i], output_mailbox_list[i]);
        }

        // Run the plants
        long endTime = System.currentTimeMillis() + RUNTIME;
        while (System.currentTimeMillis() < endTime) {
            running = true;

            // Loop through each plant
            for (int i = 0; i < NUMBER_OF_PLANTS; i++) {
                
                // Provide oranges for each plant.
                if (input_mailbox_list[i].isEmpty()) {
                    System.out.println("Putting an orange in " + plant_list[i].get_name());
                    input_mailbox_list[i].put(new Orange());
                    oranges_used++;
                } else {
                    // System.out.println("Plant " + i + " is not empty");
                }

                // Get bottles from each plant.
                if (!output_mailbox_list[i].isEmpty()) {
                    o = output_mailbox_list[i].get();
                    bottles_made++;
                    System.out.println("Got an orange from " + plant_list[i].get_name());
                } else {
                    // System.out.println("Plant " + i + " is not empty");
                }
            }
        }

        setRunning(false);
        System.out.println("Stopping all plants...");
        // Sleep for 1 second
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Summarize the results
        for (int i = 0; i < NUMBER_OF_PLANTS; i++) {
            System.out.println(plant_list[i].get_name() + " holding " + input_mailbox_list[i].size() + " oranges.");
        }
        System.out.println("Total oranges used: " + oranges_used);
        System.out.println("Total bottles made: " + bottles_made);
        System.out.println("Goodbye World");
    }

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        Main.running = running;
    }

}
