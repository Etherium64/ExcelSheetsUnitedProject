package AnimationStates;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.ArrayList;

// utility class for loading image sequences
// note for team scaling can be adjusted here

public class loadImages {

    // Scale factor applied to every loaded frame, was set when renders where 4k might need adjusting to fit your windows
    private static final double SCALE = 0.25;

    public static Image[] loadSequence(String folderPath) {

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
