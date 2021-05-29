package mts.teta.resizer;

import marvin.image.MarvinImage;
import marvinplugins.MarvinPluginCollection;
import mts.teta.resizer.imageprocessor.BadAttributesException;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ImageProcessor {

    private static final int MAX_QUALITY = 100;
    private static final int MIN_QUALITY = 1;
    private static final int ACTUAL_SCALE = 1;
    private static final List<String> formats = Arrays.asList("png", "jpg");
    private static final String ERROR_MESSAGE = "Please check params!";

    public void processImage(BufferedImage read, ResizerApp resizerApp) throws IOException, BadAttributesException {
        if (resizerApp.getCropWidth() != null && resizerApp.getCropHeight() != null &&
                resizerApp.getCropX() != null && resizerApp.getCropY() != null) {
            read = crop(read, resizerApp.getCropWidth(), resizerApp.getResizeHeight(), resizerApp.getCropX(),
                    resizerApp.getCropY());
        }
        if (resizerApp.getRadius() != null) {
            read = blur(read, resizerApp.getRadius());
        }
        saveFileWithParams(read, resizerApp.getOutputFormat(), resizerApp.getOutputFile(),
                resizerApp.getResizeWidth(), resizerApp.getResizeHeight(), resizerApp.getQuality());
    }

    private BufferedImage crop(BufferedImage image, Integer width, Integer height, Integer x, Integer y)
            throws BadAttributesException {
        if (width <= 0 || height <= 0) {
            throw new BadAttributesException(ERROR_MESSAGE);
        }
        MarvinImage outImage = new MarvinImage();
        MarvinPluginCollection.crop(new MarvinImage(image), outImage, x, y, width, height);
        return outImage.getBufferedImage();
    }

    private BufferedImage blur(BufferedImage image, Integer radius) throws BadAttributesException {
        if (radius < 0) {
            throw new BadAttributesException(ERROR_MESSAGE);
        }
        MarvinImage outImage = new MarvinImage();
        MarvinPluginCollection.gaussianBlur(new MarvinImage(image), outImage, radius);
        return outImage.getBufferedImage();
    }

    private void saveFileWithParams(BufferedImage image, String outputFormat, File to, Integer width, Integer height,
                                    Integer quality) throws IOException, BadAttributesException {
        Thumbnails.Builder<java.awt.image.BufferedImage> builder = Thumbnails.of(image);

        if (width == null && height == null) {
            builder.scale(ACTUAL_SCALE);
        } else {
            builder.forceSize(width, height);
        }
        if (quality != null) {
            if (quality < MIN_QUALITY || quality > MAX_QUALITY) {
                throw new BadAttributesException(ERROR_MESSAGE);
            }
            builder.outputQuality(quality.doubleValue() / 100);
        }
        if (outputFormat != null) {
            outputFormat = outputFormat.toLowerCase(Locale.ROOT);
            if (formats.contains(outputFormat)) {
                builder.outputFormat(outputFormat);
            } else {
                throw new BadAttributesException(ERROR_MESSAGE);
            }
        }
        builder.toFile(to);
    }
}
