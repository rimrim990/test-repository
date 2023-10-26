package learn.java.testrepository.plain.async;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallbackFutureTask<T> extends FutureTask<T>{

    private final SuccessCallback<T> sc;
    private final ErrorCallback<T> ec;

    public CallbackFutureTask(final Callable<T> callable, final SuccessCallback<T> sc, final ErrorCallback<T> ec) {
        super(callable);
        this.sc = sc;
        this.ec = ec;
    }

    @Override
    protected void done() {
        try {
            sc.onSuccess(get());
        } catch (ExecutionException e) {
            ec.onError(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    interface SuccessCallback<T> {
        void onSuccess(T result);
    }

    interface ErrorCallback<T> {
        void onError(Exception ex);
    }
}
