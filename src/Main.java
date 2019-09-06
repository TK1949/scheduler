public class Main {

    public static void main(String[] args) {
        TaskScheduler scheduler = new TaskScheduler(0xf);
        scheduler.start();
        scheduler.addTask(new TimerTask(true, 3000) {
            @Override
            public void run() {
                System.out.println("多次执行 -> " + System.currentTimeMillis());
            }
        });

        scheduler.addTask(new TimerTask(6000) {
            @Override
            public void run() {
                System.out.println("单次执行 -> " + System.currentTimeMillis());
            }
        });

        scheduler.addTask(new TimerTask(6000) {
            @Override
            public void run() {
                throw new RuntimeException("异常执行");
            }
        });
    }
}