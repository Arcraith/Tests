package tobi.flappy.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import tobi.flappy.utils.BufferUtils;

public class VertexArray {

	/*
	 * Informationen über das Nutzen von VertexArrayObject:
	 * 
	 * After that when you bind the VAO again, all the those attributes and
	 * pointers also become current. So one glBindVertexArray call is equivalent
	 * to all the code previously needed to set up all the attributes. It's
	 * handy for passing geometry around between functions or methods without
	 * having to create your own structs or objects.
	 * 
	 * (One time setup, multiple use is the easiest way to use VAOs, but you can
	 * also change attributes just by binding it and doing more enable/pointer
	 * calls. VAOs are not constants.)
	 * 
	 * ------------------------------------------------------------------------
	 * 
	 * Vertex Array Objects are like macros in word processing programs and the
	 * like. A good description is found here.
	 * 
	 * Macros just remember the actions you did, such as activate this
	 * attribute, bind that buffer, etc. When you call glBindVertexArray(
	 * yourVAOId ), it simply replays those attribute pointer bindings and
	 * buffer bindings.
	 * 
	 * So your next call to draw uses whatever was bound by the VAO.
	 * 
	 * VAO's don't store vertex data. No. The vertex data is stored in a vertex
	 * buffer or in an array of client memory.
	 * 
	 * http://stackoverflow.com/questions/11821336/what-are-vertex-array-objects
	 */

	// amount of vertices, welche überhaupt gerendert werden || specifies the
	// number of elements to be rendered
	private int count;

	// vao = VertexArrayObject, vbo = VertexBufferObject, ibo =
	// IndexBufferObject, tbo = TextureBufferObject
	private int vao, vbo, ibo, tbo;

	public VertexArray(float[] vertices, byte[] indices,
			float[] textureCoordinates) {
		// weil wir die indices zum Beschreiben der Vertices benutzen, gilt:
		count = indices.length;

		vao = glGenVertexArrays();

		// bind VertexArray, "group of buffers", Connecten von vertices und
		// textureCoordinates
		glBindVertexArray(vao);

		vbo = glGenBuffers();
		// Erstellen eines Array-Float-Buffers für Vertices
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices),
				GL_STATIC_DRAW);

		// size = 3, weil xyz
		glVertexAttribPointer(Shader.VERTEX_ATTRIB, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(Shader.VERTEX_ATTRIB);

		tbo = glGenBuffers();
		// Erstellen eines Array-Float-Buffers für TextureCoordinates
		glBindBuffer(GL_ARRAY_BUFFER, tbo);
		glBufferData(GL_ARRAY_BUFFER,
				BufferUtils.createFloatBuffer(textureCoordinates),
				GL_STATIC_DRAW);

		// size = 2, weil xy, weil es sich um 2D Texturen handelt
		glVertexAttribPointer(Shader.TEXTURE_ATTRIB, 2, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(Shader.TEXTURE_ATTRIB);

		ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER,
				BufferUtils.createByteBuffer(indices), GL_STATIC_DRAW);

		// unbinden der Buffer
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public void bind() {
		glBindVertexArray(vao);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
	}

	public void unbind() {
		glBindVertexArray(0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public void draw() {
		// render primitives from array data
		glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0);
	}

	public void render() {
		bind();
		draw();
	}

}
