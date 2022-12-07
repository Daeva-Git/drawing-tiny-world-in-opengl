import org.joml.Matrix4f;
import org.joml.Vector3f;
import renderer.Shader;
import renderer.Surface;
import renderer.Texture;

import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Terrain implements GameObject {
    private final Shader shader;
    private final Texture texture;
    private final Surface surface;
    private final Matrix4f model;

    private final int VAO;
    private final int VBO;
    private final int EBO;

    public Terrain () {
        shader = new Shader("src/assets/shaders/terrain.vert", "src/assets/shaders/terrain.frag");
        texture = new Texture("src/assets/images/TerrainDiffuse.png");
        surface = new Surface(5, 5, 1);
        model = new Matrix4f().identity();

        VAO = glGenBuffers();
        VBO = glGenVertexArrays();
        EBO = glGenBuffers();

        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, surface.getVertices(), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, surface.getIndices(), GL_STATIC_DRAW);

        shader.setTexture("texture", texture.getID());
    }

    @Override
    public void update (float deltaTime) {
        shader.use();
        shader.setMatrix("model", model);
        shader.setMatrix("view", Demo.instance.getScene().getCamera().getView());
        shader.setMatrix("projection", Demo.instance.getScene().getCamera().getProjection());
        shader.dispose();
    }

    @Override
    public void render () {
        glBindVertexArray(VAO);
        glDrawElements(GL_TRIANGLE_STRIP, surface.getIndices().length, GL_UNSIGNED_INT, 0);
    }

    @Override
    public void dispose () {
        glDeleteBuffers(VAO);
        glDeleteBuffers(VBO);
        glDeleteBuffers(EBO);
    }

    public void translate (float tx, float ty, float tz) {
        model.translate(new Vector3f(tx, ty, tz));
    }
}
