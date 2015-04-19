package tobi.flappy.level;

import tobi.flappy.graphics.Shader;
import tobi.flappy.graphics.VertexArray;

public class Level {

	private VertexArray background;
	
	public Level(){
		float[] vertices = new float[]{
			/*
			-10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
			-10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
			  0.0f,  10.0f * 9.0f / 16.0f, 0.0f,
			  0.0f, -10.0f * 9.0f / 16.0f, 0.0f
			 */
				
			-1, -1, 0, 
			-1, 1, 0, 
			 1, 1, 0, 
			 1, -1, 0
		};
		
		// um redundates Nutzen von vertices zu vermeiden, werden indices genutzt
		byte[] indices = new byte[] {
			0, 1, 2,
			2, 3, 0,
			
		};
		
		float[] textureCoordinates = new float[] {
			0, 1, 
			0, 0, 
			1, 0, 
			1, 1
		};
		
		background = new VertexArray(vertices, indices, textureCoordinates);
		
		
	}
	
	public void render(){
		Shader.BG.enable();
		background.render();
		Shader.BG.disable();
	}
}
