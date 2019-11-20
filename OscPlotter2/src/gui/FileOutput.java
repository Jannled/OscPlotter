package gui;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

public class FileOutput 
{
	private static final String fileName = "OscPlotter_Screenshot%03d.png";
	
	static File defaultLocation = new File(String.format(fileName, 1));
	
	public static void writeImage(WritableImage image)
	{
		writeImage(SwingFXUtils.fromFXImage(image, null));
	}
	
	public static void writeImage(RenderedImage image)
	{
		try {
			int i=1;
			while(defaultLocation.exists())
				defaultLocation = new File(String.format(fileName, i++));
			ImageIO.write(image, "png", defaultLocation);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Saved Screenshot to " + defaultLocation.getAbsolutePath());
	}
}
