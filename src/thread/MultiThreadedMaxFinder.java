package thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Callable 객체를 Executor 에 추가한다.
 * Executor 는 각 Callable 객체에 대해 스레드를 실행한다.
 * 각 스레드에서 Callable 의 call()을 호출한다.
 * 작업에 대한  결과를 Future 를 통해 얻는다.
 * 작업이 완료되지 않은 상태로 Future 를 사용하려하면 폴링 스레드가 블록된다.
 */
public class MultiThreadedMaxFinder {

    public static int max(int[] data) throws InterruptedException, ExecutionException {
        if (data.length == 1) {
            return data[0];
        } else if (data.length == 0) {
            throw new IllegalArgumentException();
        }

        // 범위를 반반으로 하여 작업을 2개로 분할한다.
        FindMaxTask task1 = new FindMaxTask(data, 0, data.length / 2);
        FindMaxTask task2 = new FindMaxTask(data, data.length / 2, data.length);

        // 2개의 스레드를 생성한다.
        ExecutorService service = Executors.newFixedThreadPool(2);

        Future<Integer> future1 = service.submit(task1);
        Future<Integer> future2 = service.submit(task2);

        return Math.max(future1.get(), future2.get());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int result = max(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        System.out.println(Integer.toString(result));
    }
}
