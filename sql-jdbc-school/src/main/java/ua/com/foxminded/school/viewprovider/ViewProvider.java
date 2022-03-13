package ua.com.foxminded.school.viewprovider;

import java.util.Scanner;

public class ViewProvider {   
    private final Scanner scanner;

    public ViewProvider(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public void printMessage(String message) {
        System.out.println(message);
    }
    
    public String readString() {
        return scanner.next();
    }

    public int readInt() {
        return scanner.nextInt();
    }
}
