import org.joml.Matrix4f;
import org.joml.Vector3f;
import renderer.Shader;
import renderer.Surface;
import renderer.Texture;
import renderer.Utils;

import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL32.*;

public class Terrain implements GameObject {
    private final Shader shader;
    private final Shader grassShader;

    private final Texture texture;
    private final Texture heightMapTexture;
    private final Texture grassTexture;
    private final Texture grassDistributionTexture;

    private final Surface surface;
    private final Matrix4f model;

    private final int VAO;
    private final int VBO;
    private final int EBO;

    public Terrain () {
        shader = new Shader("src/assets/shaders", "terrain");
        grassShader = new Shader("src/assets/shaders", "grass");

        texture = new Texture("src/assets/images/TerrainDiffuse.png", GL_TEXTURE1);
        heightMapTexture = new Texture("src/assets/images/TerrainHeightMap.png", GL_TEXTURE2);

        grassTexture = new Texture("src/assets/images/GrassDiffuse.png", GL_TEXTURE4, true);
        grassDistributionTexture = new Texture("src/assets/images/GrassDistribution.png", GL_TEXTURE5);

        surface = new Surface(50, 50, 1);
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
        shader.setTexture("Texture", 1);
        shader.setTexture("heightmap", 2);

        grassShader.bind();
        grassShader.setTexture("heightmap", 2);
        grassShader.setTexture("Texture", 4);
        grassShader.setTexture("distribution", 5);
    }

    @Override
    public void update (float deltaTime) {

    }

    @Override
    public void render () {
        glBindVertexArray(VAO);
        renderTerrain();
        renderGrass();
    }

    public void renderTerrain () {
        shader.bind();
        shader.setMatrix("model", model);
        shader.setMatrix("view", Demo.instance.getScene().getCamera().getView());
        shader.setMatrix("projection", Demo.instance.getScene().getCamera().getProjection());
        shader.setVec3("lightDir", Demo.instance.getScene().getDirectionalLight().getDirection());
        shader.setVec3("lightColor", Demo.instance.getScene().getDirectionalLight().getColor());
        shader.setVec3("viewPos",  Demo.instance.getScene().getCamera().getFront());
        glDrawElements(GL_TRIANGLE_STRIP, surface.getIndices().length, GL_UNSIGNED_INT, 0);
    }

    public void renderGrass () {
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        grassShader.bind();
        grassShader.setMatrix("model", model);
        grassShader.setMatrix("view", Demo.instance.getScene().getCamera().getView());
        grassShader.setMatrix("projection", Demo.instance.getScene().getCamera().getProjection());
        grassShader.setVec3("lightDir", Demo.instance.getScene().getDirectionalLight().getDirection());
        grassShader.setVec3("lightColor", Demo.instance.getScene().getDirectionalLight().getColor());
        grassShader.setVec3("viewPos",  Demo.instance.getScene().getCamera().getFront());
        grassShader.setFloat("size",  0.1f);
        glDrawElements(GL_TRIANGLE_STRIP, surface.getIndices().length, GL_UNSIGNED_INT, 0);
    }

    @Override
    public void dispose () {
        glDeleteBuffers(VAO);
        glDeleteBuffers(VBO);
        glDeleteBuffers(EBO);

        shader.unbind();
        grassShader.unbind();
    }

    public void translate (float tx, float ty, float tz) {
        model.translate(tx, ty, tz);
    }
}
