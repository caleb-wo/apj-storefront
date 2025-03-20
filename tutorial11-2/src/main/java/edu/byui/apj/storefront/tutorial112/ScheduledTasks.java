package edu.byui.apj.storefront.tutorial112;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }


    @Scheduled(cron = "0 26 15 * 3 4")
    public void executorServiceExample() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<String> names = Arrays.asList(
                "Alice", "Bob", "Charlie", "David", "Emma",
                "Frank", "Grace", "Henry", "Ivy", "Jack",
                "Karen", "Liam", "Mia", "Noah", "Olivia",
                "Paul", "Quinn", "Ryan", "Sophia", "Thomas"
        );
        int splitIndex = names.size() / 2;
        List<String> lOne = names.subList( 0, splitIndex );
        List<String> lTwo = names.subList( splitIndex, names.size() );
        List<Future<String>> futures = new ArrayList<>();
        List<Callable<String>> tasks = Arrays.asList(
                () -> {
                    lOne.stream().forEach(System.out::println);
                    return "First half complete";
                },
                () -> {
                    lTwo.stream().forEach(System.out::println);
                    return "Second half complete";
                }
        );

        // Submit tasks
        for (Callable<String> task : tasks) {
            futures.add(executor.submit(task));
        }
        // Polling loop to check if tasks are done
        while (!futures.isEmpty()) {
            Iterator<Future<String>> iterator = futures.iterator();

            while (iterator.hasNext()) {
                Future<String> future = iterator.next();


                if (future.isDone()) {
                    try {
                        String result = future.get(); // Retrieve completed result
                        System.out.println("Processed result: " + result.toUpperCase());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    iterator.remove(); // Remove completed future
                }
            }

            try {
                Thread.sleep(500); // Polling interval
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        executor.shutdown();
        System.out.println("All tasks completed!");

        }


    }
