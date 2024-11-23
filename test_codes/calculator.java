public class calculator {
    public int add(int a, int b) {
        if (a < 0 || b < 0) { // Validation done redundantly
            throw new IllegalArgumentException("Negative numbers are not allowed");
        }
        return a + b;
    }

    public int subtract(int a, int b) {
        if (a < 0 || b < 0) { // Validation done redundantly
            throw new IllegalArgumentException("Negative numbers are not allowed");
        }
        return a - b;
    }

    public static void main(String[] args) {
        calculator calc = new calculator();
        try {
            System.out.println("Addition: " + calc.add(10, 20));
            System.out.println("Subtraction: " + calc.subtract(20, 10));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
