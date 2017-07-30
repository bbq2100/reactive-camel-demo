package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class App {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(App.class);
        new CountDownLatch(1).await();
    }
}
