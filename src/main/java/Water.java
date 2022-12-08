import org.joml.Matrix4f;
import org.joml.Vector3f;
import renderer.Shader;
import renderer.Surface;
import renderer.Texture;
import renderer.Utils;

import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL32.*;

public class Water implements GameObject {
    private final Shader shader;
    private final Texture texture;
    private final Surface surface;
    private final Matrix4f model;

    private final int VAO;
    private final int VBO;
    private final int EBO;

    public Water() {
        shader = new Shader("src/assets/shaders", "water");
        texture = new Texture("src/assets/images/WaterDiffuse.png", GL_TEXTURE0);
        surface = new Surface(20, 20, 1);
        model = new Matrix4f().identity();

        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();

        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, surface.getVertices(), GL_STATIC_DRAW);

        // position
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Utils.STRIDE, 0);
        glEnableVertexAttribArray(0);

        // texture
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Utils.STRIDE, Utils.POSITION_DATA_SIZE_IN_ELEMENTS * 4);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, surface.getIndices(), GL_STATIC_DRAW);

        shader.bind();
        shader.setTexture("waterTexture", 0);
    }

    private float timePassed = 0;

    @Override
    public void update(float deltaTime) {
        timePassed += deltaTime;
    }

    @Override
    public void render() {
        shader.bind();

        glBindVertexArray(VAO);

        shader.setMatrix("model", model);
        shader.setMatrix("view", Demo.instance.getScene().getCamera().getView());
        shader.setMatrix("projection", Demo.instance.getScene().getCamera().getProjection());
        shader.setFloat("timePassed", timePassed / 100);

        glDrawElements(GL_TRIANGLE_STRIP, surface.getIndices().length, GL_UNSIGNED_INT, 0);
    }

    @Override
    public void dispose() {
        glDeleteBuffers(VAO);
        glDeleteBuffers(VBO);
        glDeleteBuffers(EBO);

        shader.unbind();
    }

    public void translate(float tx, float ty, float tz) {
        model.translate(new Vector3f(tx, ty, tz));
    }
}
