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

public class ExamplePDFtoPNGConverter implements BaseConverter {
    private static final Logger LOG = LoggerFactory.getLogger(ExamplePDFtoPNGConverter.class);

    @Override
    public void convert(File source, File target) throws IOException {
    long start = System.currentTimeMillis();
        PDDocument document = PDDocument.load(source);
        List<PDPage> list = document.getDocumentCatalog().getAllPages();
        PDPage page = list.get(0);
        String fileName = source.getName().replace(".pdf", "");
        int pageNumber = 1;
        BufferedImage image = page.convertToImage();
        LOG.info("pdf -> png converting");
        ImageIO.write(image, "png", target);
        document.close();
    }
}