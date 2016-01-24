package pl.pogorzelski.webconverter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pogorzelski.webconverter.controller.CompilationException;
import pl.pogorzelski.webconverter.convert.BaseConverter;
import pl.pogorzelski.webconverter.domain.Converter;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Kuba on 2016-01-24.
 */
public class ConverterUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ConverterUtils.class);

    public static void compileConverterFile(File sourceFile) throws CompilationException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int run = compiler.run(null, null, null, sourceFile.getPath());
        if (run != 0) {
            throw new CompilationException("Compilation Error");
        }
    }


    public static boolean validConverter(Converter c) {
        Class<?> cls = loadClass(c);

        if (cls != null && BaseConverter.class.isAssignableFrom(cls)) {
            return true;
        }
        return false;
    }

    public static Class<?> loadClass(Converter converter) {
        URLClassLoader classLoader = null;
        try {
            classLoader = URLClassLoader.newInstance(new URL[]{Constants.ROOT_FOLDER.toURI().toURL()});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            Class<?> cls = Class.forName(converter.getPackageName() + "." + converter.getClassName(), false,
                    classLoader);
            return cls;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static BaseConverter getConverter(Converter c) {
        Class<?> cls = ConverterUtils.loadClass(c);

        if (cls != null && BaseConverter.class.isAssignableFrom(cls)) {
            try {
                return (BaseConverter) cls.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
