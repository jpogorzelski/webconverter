package pl.pogorzelski.webconverter.queue;

import pl.pogorzelski.webconverter.convert.BaseConverter;
import pl.pogorzelski.webconverter.domain.Converter;
import pl.pogorzelski.webconverter.domain.User;
import pl.pogorzelski.webconverter.service.ConversionActionService;
import pl.pogorzelski.webconverter.util.ConverterUtils;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * @author Kuba
 */

public class ConvertTask implements Callable<String> {

    private Converter converter;
    private File source;
    private File target;
    private State state;
    private User user;

    private ConversionActionService conversionActionService;


    public ConvertTask(Converter converter, File source, File target, User user, ConversionActionService
            conversionActionService) {
        this.converter = converter;
        this.source = source;
        this.target = target;
        this.user = user;
        this.state = State.READY;
        this.conversionActionService = conversionActionService;
    }

    public ConvertTask(Converter converter, File source, File target, ConversionActionService conversionActionService) {
        this(converter, source, target, null, conversionActionService);
    }

    public ConvertTask(ConvertTask ct) {
        this(ct.getConverter(), ct.getSource(), ct.getTarget(), ct.getUser(), ct.getConversionActionService());
    }

    @Override
    public String call() throws Exception {
        if (this.state.equals(State.READY)) {
            MyExecutorService.finishedTasks.add(this);
            this.state = State.IN_PROGRESS;
            long start = System.currentTimeMillis();
            BaseConverter converter = ConverterUtils.getConverter(this.converter);
            conversionActionService.convert(converter, this.source, this.target, this.user);

            this.state = State.COMPLETE;
            System.out.println("Took call : " + (System.currentTimeMillis() - start) + " ms");
            return "success";
        }
        return "task not ready";
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public File getSource() {
        return source;
    }

    public void setSource(File source) {
        this.source = source;
    }

    public File getTarget() {
        return target;
    }

    public void setTarget(File target) {
        this.target = target;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ConversionActionService getConversionActionService() {
        return conversionActionService;
    }

    public void setConversionActionService(ConversionActionService conversionActionService) {
        this.conversionActionService = conversionActionService;
    }

    @Override
    public String toString() {
        return "ConvertTask{" +
                "converter=" + converter +
                ", source=" + source +
                ", target=" + target +
                ", state=" + state +
                ", user=" + user +
                '}';
    }
}
