package tobi.flappy.graphics;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import tobi.flappy.utils.BufferUtils;

public class Texture {

	/*
	 * 1. Load Image
	 * 2. Extract Pixels
	 * 3. Create OpenGL Texture
	 */
	
	private int width, height;
	// Texture-ID
	private int texture;
	
	public Texture(String path){
		texture = load(path);
	}
	
	public int load(String path){
		int[] pixels = null;
		
		try{
			BufferedImage image = ImageIO.read(new FileInputStream(path));
			width = image.getWidth();
			height = image.getHeight();
			
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch(IOException e){
			e.printStackTrace();
		}
		
		/* 
		/ Wechsel von Format ARGB nach RGBA
		/ Alpha	Red		Green	Blue
		/ Red		Green	Blue	Alpha
		*/
		
		
		int[] data = new int[width * height];
		for(int i = 0; i < width * height; i++){
			// Isolieren der einzelnen Farbwerte über Bitshiften 
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			
			// neu zusammensetzen
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		int result = glGenTextures();
		// select texture
		glBindTexture(GL_TEXTURE_2D, result);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
		// unselect texture
		glBindTexture(GL_TEXTURE_2D, 0);
		
		return result;
	}
	
	public void bind(){
		glBindTexture(GL_TEXTURE_2D, texture);
	}
	
	public void unbind(){
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public int getWidth(){
		return width; 
	}
	
	public int getHeight(){
		return height;
	}
}
