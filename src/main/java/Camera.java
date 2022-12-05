import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private final static float YAW = -90.0f;
    private final static float PITCH = 0.0f;
    private final static float SPEED = 2.5f;
    private final static float SENSITIVITY = 0.1f;
    private final static float ZOOM = 45.0f;

    enum Direction {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT
    };


    // camera Attributes
    public Vector3f position;
    public Vector3f front;
    public Vector3f up;
    public Vector3f right;
    public Vector3f worldUp;

    // euler Angles
    public float yaw;
    public float pitch;

    // camera options
    public float movementSpeed;
    public float mouseSensitivity;
    public float zoom;

    public Camera (Vector3f position, Vector3f up, float yaw, float pitch, Vector3f front, float movementSpeed, float mouseSensitivity, float zoom) {
        this.position = position;
        this.up = up;
        this.yaw = yaw;
        this.pitch = pitch;
        this.front = front;
        this.movementSpeed = movementSpeed;
        this.mouseSensitivity = mouseSensitivity;
        this.zoom = zoom;
        updateCameraVectors();
    }

    public Camera () {
        this(new Vector3f(), new Vector3f(), YAW, PITCH, new Vector3f(), SPEED, SENSITIVITY, ZOOM);
    }

    public Camera (float posX, float posY, float posZ, float upX, float upY, float upZ, float yaw, float pitch, Vector3f front, float movementSpeed, float mouseSensitivity, float zoom) {
        this.position = new Vector3f(posX, posY, posZ);
        this.worldUp = new Vector3f(upX, upY, upZ);
        this.yaw = yaw;
        this.pitch = pitch;
        this.front = front;
        this.movementSpeed = movementSpeed;
        this.mouseSensitivity = mouseSensitivity;
        this.zoom = zoom;
        updateCameraVectors();
    }

    public Matrix4f getViewMatrix () {
        return new Matrix4f().lookAt(position, new Vector3f(position).add(front), up);
    }

    public void processKeyboard (Direction direction, float deltaTime) {
        final float velocity = movementSpeed * deltaTime;
        switch (direction) {
            case FORWARD -> position.add(new Vector3f(front).mul(velocity));
            case BACKWARD -> position.sub(new Vector3f(front).mul(velocity));
            case LEFT -> position.sub(new Vector3f(right).mul(velocity));
            case RIGHT -> position.add(new Vector3f(right).mul(velocity));
        }
    }

    // processes input received from a mouse input system. Expects the offset value in both the x and y direction.
    public void processMouseMovement (float xOffset, float yOffset, boolean constrainPitch) {
        xOffset *= mouseSensitivity;
        yOffset *= mouseSensitivity;

        yaw  += xOffset;
        pitch += yOffset;

        // make sure that when pitch is out of bounds, screen doesn't get flipped
        if (constrainPitch)
        {
            if (pitch > 89.0f)
                pitch = 89.0f;
            if (pitch < -89.0f)
                pitch = -89.0f;
        }

        // update Front, Right and Up Vectors using the updated Euler angles
        updateCameraVectors();
    }

    public void processMouseMovement (float xOffset, float yOffset) {
        processMouseMovement(xOffset, yOffset, true);
    }

    // processes input received from a mouse scroll-wheel event. Only requires input on the vertical wheel-axis
    public void ProcessMouseScroll(float yOffset) {
        zoom -= yOffset;
        if (zoom < 1.0f)
            zoom = 1.0f;
        if (zoom > 45.0f)
            zoom = 45.0f;
    }

    // TODO: 12/5/2022 implement
    public Matrix4f getInvertedCamera(float y) {
        // returns the inverted camera matrix pos
        return new Matrix4f();
    }

    // calculates the front vector from the Camera's (updated) Euler Angles
    private void updateCameraVectors () {
        // calculate the new Front vector
        front.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        front.normalize();

        // also, re-calculate the Right and Up vector
        right.normalize(new Vector3f(front).cross(worldUp)); // normalize the vectors, because their length gets closer to 0 the more you look up or down which results in slower movement.
        up.normalize(new Vector3f(right).cross(front));
    }
}
