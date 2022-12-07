import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Scene {
    private final Camera camera;
    private final ArrayList<GameObject> gameObjects;

    public Scene () {
        gameObjects = new ArrayList<>();

        camera = new Camera();
        camera.setPosition(new Vector3f(-9.964E-3f,  1.180E+0f,  1.908E+0f));
        camera.setFront(new Vector3f(-1.160E-2f, -5.478E-1f, -8.365E-1f));
        camera.setPitch(-27);

        final Water water = new Water();
        final Terrain terrain = new Terrain();
        terrain.translate(1, 0, 0);

        gameObjects.add(camera);
        gameObjects.add(water);
        gameObjects.add(terrain);
    }

    public void update (float deltaTime) {
        for (GameObject gameObject : gameObjects) {
            gameObject.update(deltaTime);
        }
    }

    public void render () {
        for (GameObject gameObject : gameObjects) {
            gameObject.render();
        }
    }

    public void dispose () {
        for (GameObject gameObject : gameObjects) {
            gameObject.dispose();
        }
    }

    public Camera getCamera() {
        return camera;
    }
}
