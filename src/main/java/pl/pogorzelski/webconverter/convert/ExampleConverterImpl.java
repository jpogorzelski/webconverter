package pl.pogorzelski.webconverter.convert;

import java.io.File;
import java.io.IOException;

/**
 * Created by kuba on 12/20/15.
 */
public class ExampleConverterImpl implements BaseConverter {

    @Override
    public File convert(File sourceFile) throws IOException {
        return null;
    }
}


/*
package pl.pogorzelski.webconverter.convert;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MyExampleConverterImpl implements BaseConverter {
    private static final Logger LOG = LoggerFactory.getLogger(MyExampleConverterImpl.class);

    @Override
    public File convert(File sourceFile) throws IOException {
        PDDocument document = PDDocument.load(sourceFile);
        List<PDPage> list = document.getDocumentCatalog().getAllPages();
        PDPage page = list.get(0);
        LOG.info("Total files to be converted -> {}", list.size());

        String fileName = sourceFile.getName().replace(".pdf", "");
        int pageNumber = 1;
        BufferedImage image = page.convertToImage();
        File outputFile = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName + "_" +
                pageNumber + ".png");

        LOG.info("Image Created -> {}", outputFile.getName());
        ImageIO.write(image, "png", outputFile);
        document.close();

        return outputFile;
    }
}

 */