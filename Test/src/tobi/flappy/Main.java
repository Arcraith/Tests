package tobi.flappy;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;

import tobi.flappy.graphics.Shader;
import tobi.flappy.input.Input;
import tobi.flappy.level.Level;
import tobi.flappy.math.Matrix4f;

public class Main implements Runnable{

	private int width = 1200;
	private int height = 720;

	private Thread thread;
	private boolean running = false;
	
	// Identifier for the window (weil C....)
	private long window;
	
	private Level level;
	
	public void start() {
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
	}

	private void init(){
		if(glfwInit() != GL_TRUE){
			// TODO: Handle it
			return;
		}
		
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		window = glfwCreateWindow(width, height, "Flappy", NULL, NULL );
		if(window == NULL){
			// TODO: Handle it
			return;
		}
		
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - width) / 2, (GLFWvidmode.height(vidmode) -height) / 2);
		
		glfwSetKeyCallback(window, new Input());
		
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GLContext.createFromCurrent();
		
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		
		Shader.loadAll();
		
		Shader.BG.enable();
		Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
		Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.BG.disable();
		
		level = new Level();
	}
	
	public void run() {
		init();
		while(running){
			update();
			render();
			
			if(glfwWindowShouldClose(window) == GL_TRUE){
				running = false;
			}
		}
	}
	
	private void update(){
		glfwPollEvents();
		
	}
	
	private void render(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		level.render();
		
		glfwSwapBuffers(window);
	}

	public static void main(String[] args) {
		new Main().start();
	}
}
