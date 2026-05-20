package AnimationStates;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for loading animation frame sequences from the resources folder.
 * <p>
 * This handles the whole "load every frame until we run out" pattern,
 * plus scaling the images down so the pet isn't massive on screen.
 */
public class loadImages {

    private static final double SCALE = 0.25;

    /**
     * Loads a sequence of animation frames from a folder.
     * <p>
     * The method expects files named like:
     * <pre>
     *     /folder/prefix0001.png
     *     ...
     * </pre>
     * It keeps loading frames until it hits a missing file.
     *
     * @param folderPath The resource folder containing the animation frames.
     * @return An array of scaled {@link Image} objects.
     */
    public static Image[] loadSequence(String folderPath) {

        List<Image> frames = new ArrayList<>();
        int index = 1;

        String prefix = extractPrefix(folderPath);

        while (true) {
            String fileName = buildFramePath(folderPath, prefix, index);

            Image scaled = loadAndScale(fileName);
            if (scaled == null) {
                System.out.println("Missing frame: " + fileName);
                break;
            }

            frames.add(scaled);
            index++;
        }

        System.out.println("Loaded " + frames.size() + " frames from " + folderPath);
        return frames.toArray(new Image[0]);
    }

    /**
     * Pulls the last part of the folder path to use as the filename prefix.
     * <p>
     * Example: "/ImageSequences/idle" → "idle"
     */
    private static String extractPrefix(String folderPath) {
        return folderPath.substring(folderPath.lastIndexOf('/') + 1);
    }

    /**
    * Builds a full resource path for a specific frame number.
    */
    private static String buildFramePath(String folderPath, String prefix, int index) {
        return String.format("%s/%s%04d.png", folderPath, prefix, index);
    }

    /**
     * Loads an image from the classpath and scales it down.
     *
     * @param filePath The resource path to the image.
     * @return A scaled {@link Image}, or null if the file doesn't exist.
     */
    private static Image loadAndScale(String filePath) {
        InputStream stream = loadImages.class.getResourceAsStream(filePath);
        if (stream == null) return null;

        Image original = new Image(stream);
        double width = original.getWidth() * SCALE;
        double height = original.getHeight() * SCALE;

        // Reload for scaling
        InputStream stream2 = loadImages.class.getResourceAsStream(filePath);
        return new Image(stream2, width, height, true, true);
    }
}
