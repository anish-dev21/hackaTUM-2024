package test.animals;

public class Dog {
  // Dog-specific method
    public void bark() {
        System.out.println("The dog barks.");
    }

    public static void main(String[] args) {
        // Creating an object of the child class
        Dogx myDog = new Dogx();

        // Calling methods from both the parent and child class
        myDog.eat();   // Inherited method from Animal
        myDog.bark();  // Method specific to Dog
    }
}
