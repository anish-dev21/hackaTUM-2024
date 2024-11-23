package test_codes;
public class person {
    public String name; // Should be private with a getter/setter
    public int age; // Should be private with a getter/setter

    public person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void displayInfo() { // Violates single responsibility principle
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
    }

    public static void main(String[] args) {
        person person = new person("John Doe", 25);
        person.displayInfo();
    }
}

