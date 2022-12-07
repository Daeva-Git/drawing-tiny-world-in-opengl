package renderer;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;

public class Water {
    private Texture texture;

    public Water () {
        texture = new Texture("src/assets/images/WaterDiffuse.png");
    }

    private final float[] vertices = {
            // positions          // normals           // texture coords
            0.5f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, // top right
            0.5f, 0.0f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, // bottom right
            -0.5f, 0.0f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, // bottom left
            -0.5f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f  // top left
    };

    private final int[] indices = {
            0, 1, 3, // first triangle
            1, 2, 3  // second triangle
    };

    public void update (float deltaTime) {

    }

    public void render (float deltaTime) {
        // bind textures on corresponding texture units
        glActiveTexture(GL_TEXTURE0);
//        glBindTexture(GL_TEXTURE_2D, id);
    }


    public float[] getVertices() {
        return this.vertices;
    }

    public int[] getIndices () {
        return this.indices;
    }

    public Texture getTexture() {
        return this.texture;
    }
}
