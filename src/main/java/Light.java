import org.joml.Vector3f;

public class Light {
    private Vector3f color;
    private Vector3f direction;

    public Light () {
        color = new Vector3f(1, 1, 1);
        direction = new Vector3f(0.5f, 0.5f, 0).normalize();
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }
}
