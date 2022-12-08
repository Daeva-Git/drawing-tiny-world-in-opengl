import org.joml.Vector3f;

import java.util.ArrayList;

public class Scene {
    private final Camera camera;
    private final ArrayList<GameObject> gameObjects;

    public Scene () {
        gameObjects = new ArrayList<>();

        camera = new Camera();
        camera.setPosition(new Vector3f(-9.964E-3f,  1.180E+0f, 1.908E+0f));
        camera.setFront(new Vector3f(-1.160E-2f, -5.478E-1f, -8.365E-1f));
        camera.setPitch(-27);

        final Water water = new Water();
        final Terrain terrain = new Terrain();

//        water.translate(0, -0.02f, 0);

        gameObjects.add(camera);
        gameObjects.add(terrain);
        gameObjects.add(water);
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
