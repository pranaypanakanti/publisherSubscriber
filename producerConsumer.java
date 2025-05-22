package publisherSubscriber;

import java.util.*;

class sharedResource {

    int size = 5;
    Queue<Integer> queue = new ArrayDeque<>(size);

    public boolean isFull() {
        return queue.size() >= 5;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized void produce(int i) {
        while (isFull()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        queue.offer(i);
        System.out.println("Produced : " + i);
        notifyAll();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void consume() {
        while (isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Consumed : " + queue.poll());
        notifyAll();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

class producer implements Runnable {

    sharedResource resource;

    public producer(sharedResource resource) {
        this.resource = resource;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            resource.produce(i);
        }
    }
}

class consumer implements Runnable {

    sharedResource resource;

    public consumer(sharedResource resource) {
        this.resource = resource;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            resource.consume();
        }
    }
}

public class producerConsumer {

    public static void main(String[] args) throws InterruptedException {

        sharedResource resource = new sharedResource();
        producer obj1 = new producer(resource);
        consumer obj2 = new consumer(resource);

        Thread t1 = new Thread(obj1);
        Thread t2 = new Thread(obj1);
        Thread t3 = new Thread(obj2);
        Thread t4 = new Thread(obj2);
        Thread t5 = new Thread(obj2);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
    }
}
