/*
 * Queue implementation: https://docs.oracle.com/javase/8/docs/api/java/util/Queue.html, https://www.geeksforgeeks.org/queue-interface-java/
 * 
 */
import java.util.LinkedList;
import java.util.Queue;


public class BlockingMailbox {
    private Queue<Orange> queue = new LinkedList<>();
    
    // No null constructor is necessary if we are using a LinkedList, which is initialized to be empty anyways.
    // public BlockingMailbox() {
    //     orange = null;
    // }

    public synchronized void put(Orange o) {
        // Remove waiting to see if the mailbox is empty, since a queue is never full.
        // while (!isEmpty()) {
        //     try {
        //         wait();
        //     } catch (InterruptedException ignored) {}
        // }
        queue.add(o);
        notifyAll();
    }

    public synchronized Orange get() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }
        notifyAll();
        return queue.poll(); // We use poll() instead of remove() because poll() returns null if the queue is empty, while remove() throws an exception.
    }

    // This code looks repetitive, but since the queue is hidden within the Mailbox structure, this method is necessary to ensure that other threads can check the queue status.
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized int size() {
        return queue.size();
    }

}
