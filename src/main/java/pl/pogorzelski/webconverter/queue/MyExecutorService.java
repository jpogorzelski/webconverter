package pl.pogorzelski.webconverter.queue;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Kuba
 */
@Component(value = "myExecutorService")
public class MyExecutorService {

    protected static List<ConvertTask> finishedTasks = new ArrayList<>();
    private ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new
            LinkedBlockingQueue<>());

    public void add(ConvertTask task) throws InterruptedException, ExecutionException {

        executorService.submit(task);

        /*for (int i = 0; i < 30; i++) {
            executorService.submit(new ConvertTask(task));
        }*/
    }

    public ThreadPoolExecutor getExecutorService() {
        return executorService;
    }

    public List<ConvertTask> getFinishedTasks() {
        return finishedTasks;
    }


}
