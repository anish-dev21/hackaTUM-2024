package test_codes;
import java.util.Scanner; // Unused import
import java.util.ArrayList;

public class main {
    public main() {
        System.out.println("Constructor called");
        System.out.println("Constructor called"); // Duplicate
    }

    public void PrintName() { // Method name should follow camelCase
        System.out.println("Hello, World!");
        System.out.println("Hello, World!"); // Duplicate
    }

    public static void main(String[] args) {
        main main = new main();
        main.PrintName();
        main.PrintName(); // Duplicate call
    }
}
