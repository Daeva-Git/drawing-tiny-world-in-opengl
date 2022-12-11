package renderer;

public class Surface {
    private final int[] indices;
    private final float[] vertices;
    private int size;

    public Surface(int verticalVerticesCount, int horizontalVerticesCount, int size) {
        this.size = size;

        this.indices = generateIndices(verticalVerticesCount, horizontalVerticesCount);
        this.vertices = generateVertices(verticalVerticesCount, horizontalVerticesCount);
    }

    private float[] generateVertices(int verticalVerticesCount, int horizontalVerticesCount) {
        final int floatsPerVertex = Utils.POSITION_DATA_SIZE_IN_ELEMENTS + Utils.TEXTURE_DATA_SIZE_IN_ELEMENTS;
        final float[] vertices = new float[verticalVerticesCount * horizontalVerticesCount * floatsPerVertex];
        int offset = 0;

        for (int y = 0; y < horizontalVerticesCount; y++) {
            for (int x = 0; x < verticalVerticesCount; x++) {
                final float xRatio = x / (float) (verticalVerticesCount - 1);
                final float zRatio = 1f - (y / (float) (horizontalVerticesCount - 1));

                final float xPosition = xRatio * size - size * 0.5f;
                final float zPosition = zRatio * size - size * 0.5f;

                // position
                vertices[offset++] = xPosition;
                vertices[offset++] = 0;
                vertices[offset++] = zPosition;

                // texture
                vertices[offset++] = xRatio;
                vertices[offset++] = zRatio;
            }
        }
        return vertices;
    }

    private int[] generateIndices(int verticalVerticesCount, int horizontalVerticesCount) {
        final int numStripsRequired = horizontalVerticesCount - 1;
        final int numDegensRequired = 2 * (numStripsRequired - 1);
        final int verticesPerStrip = 2 * verticalVerticesCount;

        final int[] indices = new int[(verticesPerStrip * numStripsRequired) + numDegensRequired];
        int offset = 0;

        for (int y = 0; y < horizontalVerticesCount - 1; y++) {
            if (y > 0) {
                // degenerate begin: repeat first vertex
                indices[offset++] = y * verticalVerticesCount;
            }

            for (int x = 0; x < verticalVerticesCount; x++) {
                // one part of the strip
                indices[offset++] = y * verticalVerticesCount + x;
                indices[offset++] = (y + 1) * verticalVerticesCount + x;
            }

            if (y < horizontalVerticesCount - 2) {
                // degenerate end: repeat last vertex
                indices[offset++] = (y + 1) * verticalVerticesCount + verticalVerticesCount - 1;
            }
        }
        return indices;
    }

    public int[] getIndices() {
        return indices;
    }

    public float[] getVertices() {
        return vertices;
    }
}
