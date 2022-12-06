import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    // attributes
    private Matrix4f projection = new Matrix4f();
    private Matrix4f view = new Matrix4f();
    private Matrix4f model = new Matrix4f();

    private Vector3f position = new Vector3f(0.0f, 0.0f, 3.0f);
    private Vector3f front = new Vector3f(0.0f, 0.0f, -1.0f);
    private Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);


    // euler angles
    private float yaw = -90.0f;
    private float pitch = 0.0f;

    // options
    private float movementSpeed = 0.5f;
    private float mouseSensitivity = 0.05f;
    private float fov = 45.0f;
    private float near = 0.1f;
    private float far = 100;

    public Camera (Vector3f up, Vector3f front) {
        this.up = up;
        this.front = front;
        updateCameraVectors();
    }

    public Camera () {
        updateCameraVectors();
    }

    public void input (long window, float deltaTime) {
        final float velocity = movementSpeed * deltaTime;

        // front back movement
        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)
            position.add(new Vector3f(front).mul(velocity));
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS)
            position.sub(new Vector3f(front).mul(velocity));

        // horizontal movement
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS)
            position.sub(new Vector3f(new Vector3f(front).cross(up)).normalize().mul(velocity));
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS)
            position.add(new Vector3f(new Vector3f(front).cross(up)).normalize().mul(velocity));
    }

    // processes input received from a mouse input system. Expects the offset value in both the x and y direction.
    public void mouseDragged (float deltaTime, float xOffset, float yOffset, boolean constrainPitch) {
        xOffset *= mouseSensitivity * deltaTime;
        yOffset *= mouseSensitivity * deltaTime;

        yaw += xOffset;
        pitch += yOffset;

        // make sure that when pitch is out of bounds, screen doesn't get flipped
        if (constrainPitch) {
            if (pitch > 89.0f)
                pitch = 89.0f;
            if (pitch < -89.0f)
                pitch = -89.0f;
        }

        // update Front, Right and Up Vectors using the updated Euler angles
        updateCameraVectors();
    }

    public void mouseDragged (float deltaTime, float xOffset, float yOffset) {
        this.mouseDragged(deltaTime, xOffset, yOffset, true);
    }


    // processes input received from a mouse scroll-wheel event. Only requires input on the vertical wheel-axis
    public void mouseScrolled(float deltaTime, float offset) {
        fov -= offset * deltaTime;
        if (fov < 1.0f) fov = 1.0f;
        if (fov > 45.0f) fov = 45.0f;
    }

    // calculates the front vector from the Camera's (updated) Euler Angles
    private void updateCameraVectors () {
        // calculate the new Front vector
        front.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        // normalize
        front.normalize();
    }

    // TODO: 06.12.22 remove (done for testing)
    public void update (int width, int height) {
        model = new Matrix4f();
        view = new Matrix4f().lookAt(position, new Vector3f(position).add(front), up);

        projection.identity();
        projection.perspective((float) Math.toRadians(fov), (float) width / height, near, far);

        model.identity();
    }

    public Matrix4f getProjection () {
        return this.projection;
    }

    public Matrix4f getView () {
        return this.view;
    }

    public Matrix4f getModel () {
        return this.model;
    }
}
