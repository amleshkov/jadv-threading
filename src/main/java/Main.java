import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        long startTs = System.currentTimeMillis(); // start time
        final ExecutorService threadPool = Executors.newFixedThreadPool(4);
        ArrayList<Future<Integer>> tasks = new ArrayList<>();
        for (String text : texts) {
            Callable<Integer> taskCallable = new RepetitionsCalculator(text);
            Future<Integer> task = threadPool.submit(taskCallable);
            tasks.add(task);
        }
        ArrayList<Integer> results = new ArrayList<>();
        for (Future<Integer> integerFuture : tasks) {
            Integer result = integerFuture.get();
            results.add(result);
        }
        threadPool.shutdown();
        System.out.println("Максимальное значение " + Collections.max(results));
        long endTs = System.currentTimeMillis(); // end time
        System.out.println("Time: " + (endTs - startTs) + "ms");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}