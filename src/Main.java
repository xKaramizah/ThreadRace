import java.util.ArrayList;
import java.util.List;

class NumberProcessor extends Thread {
    private List<Integer> numbers;
    private List<Integer> evenNumbers;
    private List<Integer> oddNumbers;

    public NumberProcessor(List<Integer> numbers, List<Integer> evenNumbers, List<Integer> oddNumbers) {
        this.numbers = numbers;
        this.evenNumbers = evenNumbers;
        this.oddNumbers = oddNumbers;
    }

    @Override
    public void run() {
        for (int number : numbers) {
            if (number % 2 == 0) {
                synchronized (evenNumbers) {
                    evenNumbers.add(number);
                }
            } else {
                synchronized (oddNumbers) {
                    oddNumbers.add(number);
                }
            }
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> allNumbers = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
            allNumbers.add(i);
        }

        List<Integer> evenNumbers = new ArrayList<>();
        List<Integer> oddNumbers = new ArrayList<>();

        List<NumberProcessor> threads = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            List<Integer> subList = allNumbers.subList(i * 2500, (i + 1) * 2500);
            NumberProcessor thread = new NumberProcessor(subList, evenNumbers, oddNumbers);
            threads.add(thread);
            thread.start();
        }

        for (NumberProcessor thread : threads) {
            thread.join();
        }

        System.out.println("Even numbers: " + evenNumbers.size());
        System.out.println("Odd numbers: " + oddNumbers.size());
    }
}