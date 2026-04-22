package AnimationStates;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.ArrayList;

public class loadImages {

    private static final double SCALE = 0.25;

    public static Image[] loadSequence(String folderPath) {

        ArrayList<Image> frames = new ArrayList<>();
        int index = 1;

        String prefix = folderPath.substring(folderPath.lastIndexOf('/') + 1);

        while (true) {
            String fileName = String.format("%s/%s%04d.png", folderPath, prefix, index);

            InputStream stream = loadImages.class.getResourceAsStream(fileName);

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

            frames.add(scaled);
            index++;
        }

        System.out.println("Loaded " + frames.size() + " frames from " + folderPath);
        return frames.toArray(new Image[0]);
    }
}
