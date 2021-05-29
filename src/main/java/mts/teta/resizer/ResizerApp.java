package mts.teta.resizer;

import picocli.CommandLine;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "resizer", mixinStandardHelpOptions = true, version = "resizer 0.0.1",
        description = "https://github.com/artem24630/MTS_task")
public class ResizerApp implements Callable<Integer> {

    public static final int INDEX_CROP_WIDTH = 0;
    public static final int INDEX_CROP_HEIGHT = 1;
    public static final int INDEX_CROP_X = 2;
    public static final int INDEX_CROP_Y = 3;

    public static void main(String... args) {
        int exitCode = runConsole(args);
        System.exit(exitCode);
    }

    protected static int runConsole(String[] args) {
        return new CommandLine(new ResizerApp()).execute(args);
    }

    @CommandLine.Parameters(index = "0", description = "Input file")
    private File inputFile = null;

    @CommandLine.Parameters(index = "1..*", description = "Output file")
    private File outputFile = null;

    @CommandLine.Option(names = "--resize", arity = "2")
    private List<Integer> resizeSize = Arrays.asList(null, null);

    @CommandLine.Option(names = "--quality", arity = "1")
    private Integer quality = null;

    @CommandLine.Option(names = "--crop", description = "width height x y", arity = "4")
    private List<Integer> cropParams = Arrays.asList(null, null, null, null);

    @CommandLine.Option(names = "--blur", arity = "1")
    private Integer radius = null;

    @CommandLine.Option(names = "--format", arity = "1")
    private String outputFormat = null;

    @Override
    public Integer call() throws Exception {
        ImageProcessor imageProcessor = new ImageProcessor();
        imageProcessor.processImage(ImageIO.read(inputFile), this);
        return 0;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public Integer getResizeWidth() {
        return resizeSize.get(0);
    }

    public Integer getResizeHeight() {
        return resizeSize.get(1);
    }

    public Integer getQuality() {
        return quality;
    }

    public Integer getCropWidth() {
        return cropParams.get(INDEX_CROP_WIDTH);
    }

    public Integer getCropHeight() {
        return cropParams.get(INDEX_CROP_HEIGHT);
    }

    public Integer getCropX() {
        return cropParams.get(INDEX_CROP_X);
    }

    public Integer getCropY() {
        return cropParams.get(INDEX_CROP_Y);
    }

    public Integer getRadius() {
        return radius;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public void setResizeWidth(int resizeWidth) {
        resizeSize.set(0, resizeWidth);
    }

    public void setResizeHeight(int resizeHeight) {
        resizeSize.set(1, resizeHeight);
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }
}
