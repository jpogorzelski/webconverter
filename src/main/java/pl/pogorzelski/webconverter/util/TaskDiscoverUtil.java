package pl.pogorzelski.webconverter.util;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author Kuba
 */
public class TaskDiscoverUtil {
    private final static Field callableInFutureTask;
    private static final Class<? extends Callable> adapterClass;
    private static final Field runnableInAdapter;
    private static final String CALLABLE = "callable";
    private static final String TASK = "task";
    private static final String NOT_A_FUTURE_TASK = "Not a FutureTask";

    static {
        try {
            adapterClass = Executors.callable(() -> {/**/}).getClass();
            callableInFutureTask = FutureTask.class.getDeclaredField(CALLABLE);
            runnableInAdapter = adapterClass.getDeclaredField(TASK);

            callableInFutureTask.setAccessible(true);
            runnableInAdapter.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Object findRealTask(Runnable task) {
        if (task instanceof FutureTask) {
            try {
                Object callable = callableInFutureTask.get(task);
                return adapterClass.isInstance(callable) ? runnableInAdapter.get(callable) : callable;
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
        throw new ClassCastException(NOT_A_FUTURE_TASK);
    }
}