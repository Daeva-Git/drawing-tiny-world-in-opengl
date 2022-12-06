import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

public class World {
    public void render () {
        renderTerrain();
        renderGrass();
        renderWater();
    }

    private void renderTerrain () {
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }

    private void renderGrass () {

    }

    private void renderWater () {

    }
}
