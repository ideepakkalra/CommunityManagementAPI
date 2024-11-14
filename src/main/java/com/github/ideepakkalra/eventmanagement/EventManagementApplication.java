package com.github.ideepakkalra.eventmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventManagementApplication {
    public static void main(String[] args) {
        // Let's do some ascii art ;)
        System.out.println();
        System.out.println("  _   _   _   _   _     _   _   _   _   _   _   _   _   _   _     _   _   _ ");
        System.out.println(" / \\ / \\ / \\ / \\ / \\   / \\ / \\ / \\ / \\ / \\ / \\ / \\ / \\ / \\ / \\   / \\ / \\ / \\");
        System.out.println("( E | v | e | n | t ) ( M | a | n | a | g | e | m | e | n | t ) ( A | P | I )");
        System.out.println(" \\_/ \\_/ \\_/ \\_/ \\_/   \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/   \\_/ \\_/ \\_/");
        System.out.println();
        System.out.println("+-+-+-+-+-+-+ +-+-+-+-+-+");
        System.out.println("|D|E|E|P|A|K| |K|A|L|R|A|");
        System.out.println("+-+-+-+-+-+-+ +-+-+-+-+-+");
        SpringApplication.run(EventManagementApplication.class, args);
    }
}
