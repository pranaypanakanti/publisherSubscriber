package publisherSubscriber;

import java.util.*;
import java.util.concurrent.*;

class SharedResource {

    int size = 5;
    Queue<Integer> queue = new ArrayDeque<>(size);

    private boolean isFull() {
        return queue.size() >= size;
    }

    private boolean isEmpty() {
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

class Producer implements Runnable {

    SharedResource resource;

    public Producer(SharedResource resource) {
        this.resource = resource;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            resource.produce(i);
        }
    }
}

class Consumer implements Runnable {

    SharedResource resource;

    public Consumer(SharedResource resource) {
        this.resource = resource;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            resource.consume();
        }
    }
}

public class ProducerConsumer {

    public static void main(String[] args) throws InterruptedException {

        SharedResource resource = new SharedResource();

        int numberOfProducers = 2;
        int numberOfConsumers = 3;

        ExecutorService executor = Executors.newFixedThreadPool(numberOfProducers + numberOfConsumers);

        for(int i=0; i<numberOfProducers; i++){
            executor.submit(new Producer(resource));
        }

        for(int i=0; i<numberOfConsumers; i++){
            executor.submit(new Consumer(resource));
        }

        executor.shutdown();
    }
}
