/**
 * Original @author: Nate Williams
 * Edited by: Charlie Said
 * 
 * Queue idea credit: Cole Odegard told me that I should use a queue to make things more efficient.
 * Queue implementation credit: https://docs.oracle.com/javase/8/docs/api/java/util/Queue.html, https://www.geeksforgeeks.org/queue-interface-java/
 * 
 */
import java.util.LinkedList;
import java.util.Queue;


public class BlockingMailbox {
    private Queue<Orange> queue = new LinkedList<>();
    
    /**
     * Empty constructor.
     */
    public BlockingMailbox() {}

    /**
     * Put an orange into the mailbox.
     * @param o An Orange you want to put in the mailbox.
     */
    public synchronized void put(Orange o) {
        // No need to wait to see if the mailbox is empty, since a queue is never full.
        queue.add(o);
        notifyAll();
    }

    /**
     * Get an orange from the mailbox.
     * @return The Orange which was at the front of the queue.  The mailbox uses a FIFO queue.
     */
    public synchronized Orange get() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }
        notifyAll();
        return queue.poll(); // We use poll() instead of remove() because poll() returns null if the queue is empty, while remove() throws an exception.
    }

    /**
     * Check if the mailbox is empty.
     * @return True if the mailbox is empty, false otherwise.
     */
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Get the size of the mailbox.
     * @return The number of Oranges in the mailbox, in integer form.
     */
    public synchronized int size() {
        return queue.size();
    }

}
