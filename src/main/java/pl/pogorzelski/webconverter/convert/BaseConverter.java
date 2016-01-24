package pl.pogorzelski.webconverter.convert;

import java.io.File;
import java.io.IOException;

/**
 * Created by jakub.pogorzelski on 16.12.15.
 */
public interface BaseConverter {

    File convert(File sourceFile) throws IOException;
}
