package publisherSubscriber;

import java.util.*;

public class pubSub {
    public static void main(String arg[]) throws InterruptedException {
        publisher obj1 = new publisher();
        subscriber obj2 = new subscriber();
        obj1.start();
        obj2.start();
    }
}

class QueueClass {
    static Queue<Integer> queue = new LinkedList<>();
    static int size = 10;
    static Random random = new Random();

    public static boolean findIsFull() {
        return queue.size() >= size;
    }

    public static boolean findIsEmpty() {
        return queue.isEmpty();
    }

    public static void setValue() {
        queue.offer(random.nextInt(1000));
    }

    public static int getValue() {
        return queue.poll();
    }
}

class publisher extends Thread {
    public void run() {
        while (true) {
            synchronized (QueueClass.queue) {
                while (QueueClass.findIsFull()) {
                    try {
                        QueueClass.queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                QueueClass.setValue();
                QueueClass.queue.notify();
            }
            try {
                Thread.sleep(100); // sleep 100ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

class subscriber extends Thread {
    public void run() {
        while (true) {
            synchronized (QueueClass.queue) {
                while (QueueClass.findIsEmpty()) {
                    try {
                        QueueClass.queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int val = QueueClass.getValue();
                System.out.println("Subscriber got: " + val);
                QueueClass.queue.notify();
            }
            try {
                Thread.sleep(100); // sleep 100ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
