package animals;

public class Cat extends Animal {
    public Cat(String name) {
        super(name);
    }

    @Override
    public void sound() {
        System.out.println(name + " meows.");
    }

    public static void main(String[] args) {
        Cat myCat = new Cat("Whiskers");
        myCat.sound(); // Whiskers meows.
    }
}

