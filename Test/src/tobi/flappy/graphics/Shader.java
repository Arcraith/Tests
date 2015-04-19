package tobi.flappy.graphics;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.util.HashMap;
import java.util.Map;

import tobi.flappy.math.Matrix4f;
import tobi.flappy.math.Vector3f;
import tobi.flappy.utils.ShaderUtils;

public class Shader {

	// class will represent an actual shader
	// --> man muss nicht eingeständig OpenGL calls verwenden, immer, wenn man den Shader aktivieren will
	
	// ATTRIBUTE Locations: Sehr ähnlich zu Uniforms außer, dass sie bei jedem einzelnem Vertex gesetzt werden
	public static final int VERTEX_ATTRIB = 0; 
	public static final int TEXTURE_ATTRIB = 1;
	
	public static Shader BG;
	
	// ID des Shaders
	private final int ID;
	private Map<String, Integer> locationCache = new HashMap<String, Integer>();
	
	public Shader(String vertex, String fragment){
		// Shader werden geladen
		ID = ShaderUtils.load(vertex, fragment);
	}
	
	public static void loadAll(){
		BG = new Shader("shaders/bg.vert", "shaders/bg.frag");
	}
	
	public int getUniform(String name){
		// get the uniform of a certain shader --> location of a certain shader
		// diese checks werden mehrmals pro Frame aufgerufen! --> cachen!
		
		if(locationCache.containsKey(name)){
			return locationCache.get(name);
		}
		
		int result = glGetUniformLocation(ID, name);
		// glGetUniformLocation() kann -1 zurück geben!
		if(result == -1){
			System.err.println("Uniform Variable: " + name + " konnte nicht gefunden werden!");
		} else {
			locationCache.put(name, result);
		}
		
		return result;
	}
	
	public void setUniform1i(String name, int value){
		// uniform variables are ways that we can transmit data, provide our shaders with data from the CPU
		glUniform1i(getUniform(name), value);
	}
	
	public void setUniform1f(String name, float value){
		glUniform1f(getUniform(name), value);
	}
	
	public void setUniform2f(String name, float x, float y){
		glUniform2f(getUniform(name), x, y);
	}
	
	public void setUniform3f(String name, Vector3f vector){
		glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
	}
	
	public void setUniformMat4f(String name, Matrix4f matrix){
		// set our actual matrices, set them into our shaders
		glUniformMatrix4(getUniform(name), false, matrix.toFloatBuffer());
	}
	
	public void enable(){
		glUseProgram(ID);
	}
	
	public void disable(){
		glUseProgram(0);
	}
}
