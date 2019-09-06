import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskScheduler {

    private DelayQueue<TimerTask> delayQueue;
    private ExecutorService workerThreadPool;
    private boolean start = false;

    public TaskScheduler(int size) {
        delayQueue = new DelayQueue<>();
        workerThreadPool = Executors.newFixedThreadPool(size);
    }

    public void start() {
        if (start) {
            throw new UnsupportedOperationException("Can't start again");
        }
        start = true;
        workerThreadPool.submit(() -> {
            TimerTask task;
            while (true) {
                task = delayQueue.take();
                workerThreadPool.submit(task);
                if (task.isRepeat() && task.refreshExpiration()) {
                    delayQueue.add(task);
                }
            }
        });
    }

    public boolean addTask(TimerTask task) {
        return delayQueue.offer(task);
    }
}