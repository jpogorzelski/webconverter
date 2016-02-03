package pl.pogorzelski.webconverter.queue;

import org.springframework.stereotype.Component;
import pl.pogorzelski.webconverter.util.MailSendService;

import javax.inject.Inject;
import java.util.concurrent.*;

/**
 * @author Kuba
 */
@Component
public class MyExecutorService {
    private LinkedBlockingQueue<Runnable> converterQueue = new LinkedBlockingQueue<>();
    private ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 10,0L, TimeUnit.MILLISECONDS, converterQueue);

    @Inject
    MailSendService mailSendService;

    public void add(ConvertTask task) throws InterruptedException, ExecutionException {
        /*if (converterQueue.isEmpty()) {
            converterQueue.put(task);
            execute();
        } else {
            converterQueue.put(task);
        }*/
        executorService.submit(task);
        executorService.submit(task);
        executorService.submit(task);

        for (Runnable r : executorService.getQueue()) {
            System.out.println("## :: " + JobDiscover.findRealTask(r).toString() );
        }


    }

    /*public void execute() throws InterruptedException, ExecutionException {

        while (!converterQueue.isEmpty()) {


            Future<String> statusFuture = executorService.submit(task);
            String status = statusFuture.get();
            if ("success".equals(status)){
                System.out.print("## " + status + " :: "+ task.getTarget().getName());
                try {
                    mailSendService.send(task.getTarget().getName());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                converterQueue.take();
            }
        }
      //  executorService.shutdown();
    }*/
}
