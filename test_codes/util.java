package test_codes;
public class util {
    public static String appName = "TestApp"; // Unnecessary static variable

    public int number; // Should be private with a getter/setter

    public void addNumber(int value) {
        number = value; // No validation
    }

    public static void displayAppInfo() {
        System.out.println("Application Name: " + appName);
        System.out.println("Application Name: " + appName); // Duplicate logic
    }

    public static void main(String[] args) {
        util.displayAppInfo();
        util utils = new util();
        utils.addNumber(42);
        System.out.println("Number: " + utils.number);
    }
}
