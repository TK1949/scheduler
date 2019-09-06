import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public abstract class TimerTask implements Delayed, Runnable {

    private boolean repeat;
    private long interval;
    private AtomicLong expiration;

    public TimerTask(long interval) {
        this(false, interval);
    }

    public TimerTask(boolean repeat, long interval) {
        this.repeat = repeat;
        this.interval = interval;
        this.expiration = new AtomicLong(System.currentTimeMillis() + interval);
    }

    public boolean isRepeat() {
        return repeat;
    }

    protected boolean refreshExpiration() {
        return setExpiration(interval);
    }

    protected boolean setExpiration(long expire) {
        expire += System.currentTimeMillis();
        return expiration.getAndSet(expire) != expire;
    }

    @Override
    public int compareTo(Delayed o) {
        if (o instanceof TimerTask) {
            return Long.compare(expiration.get(), ((TimerTask) o).expiration.get());
        }
        return 0;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return Math.max(0, unit.convert(expiration.get() - System.currentTimeMillis(), TimeUnit.MILLISECONDS));
    }
}
