package AnimationStates;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Utility class for loading and scaling animation frame sequences from the application's
 * resource directory. Each animation sequence is expected to follow a strict naming pattern:
 *
 * <p><b>Example folder structure:</b>
 * <pre>
 * /ImageSequences/walkLeft/
 *     walkLeft0001.png
 *     walkLeft0002.png
 *     walkLeft0003.png
 *     ...
 * </pre>
 *
 * <p>The loader automatically:
 * <ul>
 *     <li>Builds filenames using a 4‑digit index (e.g., {@code walkLeft0001.png})</li>
 *     <li>Loads each frame until a missing file is encountered</li>
 *     <li>Scales each frame using a global scale factor</li>
 * </ul>
 *
 * <p>This class is used by the animation system to load all pet animations at startup.</p>
 */

public class loadImages {

    // Scale factor applied to every loaded frame, was set when renders where 4k might need adjusting to fit your windows
    private static final double SCALE = 0.25;

    public static Image[] loadSequence(String folderPath) {

        /**
         * Loads a sequence of images from a resource folder. The method assumes that the folder
         * contains files named using the pattern:
         *
         * <p>{@code <prefix><index>.png}</p>
         *
         * where:
         * <ul>
         *     <li><b>prefix</b> is the folder name (e.g., {@code walkLeft})</li>
         *     <li><b>index</b> is a 4‑digit number starting at 0001</li>
         * </ul>
         *
         * <p>The method continues loading frames until a file is missing, which signals the end
         * of the sequence.</p>
         *
         * @param folderPath the resource path to the animation folder
         *                   (e.g., {@code "/ImageSequences/walkLeft"})
         *
         * @return an array of scaled {@link Image} objects representing the animation frames
         */

        ArrayList<Image> frames = new ArrayList<>();
        int index = 1;

        String prefix = folderPath.substring(folderPath.lastIndexOf('/') + 1);

        // Builds the expected filename using a fixed 4‑digit pattern: "%s/%s%04d.png"
        while (true) {
            String fileName = String.format("%s/%s%04d.png", folderPath, prefix, index);

            // Try to load the frame from resources
            InputStream stream = loadImages.class.getResourceAsStream(fileName);

            // If the frame doesn't exist, the sequence is finished
            if (stream == null) {
                System.out.println("Missing frame: " + fileName);
                break;
            }

            // Load and scale
            Image original = new Image(stream);
            double targetWidth = original.getWidth() * SCALE;
            double targetHeight = original.getHeight() * SCALE;

            // Reload for scaling
            InputStream stream2 = loadImages.class.getResourceAsStream(fileName);
            Image scaled = new Image(stream2, targetWidth, targetHeight, true, true);

            // Store the scaled frame
            frames.add(scaled);
            index++;
        }

        System.out.println("Loaded " + frames.size() + " frames from " + folderPath);
        return frames.toArray(new Image[0]);
    }
}
