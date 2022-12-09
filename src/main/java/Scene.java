import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class Scene {
    private final Camera camera;
    private final Light directionalLight;
    private final ArrayList<GameObject> gameObjects;

    private final Water water;
    private final Terrain terrain;

    public Scene () {
        gameObjects = new ArrayList<>();

        camera = new Camera();
        camera.setPosition(new Vector3f(-9.964E-3f,  1.180E+0f, 1.908E+0f));
        camera.setFront(new Vector3f(-1.160E-2f, -5.478E-1f, -8.365E-1f));
        camera.setPitch(-27);

        directionalLight = new Light();

        water = new Water();
        terrain = new Terrain();

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
        camera.reflect();

        Demo.instance.getFrameBuffer().bind();
        Demo.instance.clear();

        glEnable(GL_CULL_FACE);
        terrain.render();
        glDisable(GL_CULL_FACE);

        Demo.instance.getFrameBuffer().unbind();
        Demo.instance.getFrameBuffer().bindTexture();

        camera.reflect();

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

    public Light getDirectionalLight() {
        return directionalLight;
    }
}
