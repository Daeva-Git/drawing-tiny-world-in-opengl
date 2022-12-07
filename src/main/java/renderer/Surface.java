package renderer;
public class Surface {
    private final int[] indices;
    private final float[] vertices;

    public Surface(int verticalVerticesCount, int horizontalVerticesCount, int size) {
        this.indices = generateIndices(verticalVerticesCount, horizontalVerticesCount);
        this.vertices = generateVertices(verticalVerticesCount, horizontalVerticesCount);
    }

    private float[] generateVertices(int verticalVerticesCount, int horizontalVerticesCount) {
        final int floatsPerVertex = Utils.POSITION_DATA_SIZE_IN_ELEMENTS + Utils.TEXTURE_DATA_SIZE_IN_ELEMENTS;
        final float[] heightMapVertexData = new float[verticalVerticesCount * horizontalVerticesCount * floatsPerVertex];
        int offset = 0;

        for (int y = 0; y < horizontalVerticesCount; y++) {
            for (int x = 0; x < verticalVerticesCount; x++) {
                final float xRatio = x / (float) (verticalVerticesCount - 1) - 0.5f;
                final float zRatio = 1f - (y / (float) (horizontalVerticesCount - 1));

                final float xPosition = xRatio;
                final float zPosition = zRatio;

                // position
                heightMapVertexData[offset++] = xPosition;
                heightMapVertexData[offset++] = 0;
                heightMapVertexData[offset++] = zPosition;

                // normal
//                heightMapVertexData[offset++] = 0;
//                heightMapVertexData[offset++] = 1;
//                heightMapVertexData[offset++] = 0;

                // texture
                heightMapVertexData[offset++] = xRatio;
                heightMapVertexData[offset++] = zRatio;
            }
        }
        return heightMapVertexData;
    }

    private int[] generateIndices(int verticalVerticesCount, int horizontalVerticesCount) {
        final int numStripsRequired = horizontalVerticesCount - 1;
        final int numDegensRequired = 2 * (numStripsRequired - 1);
        final int verticesPerStrip = 2 * verticalVerticesCount;

        final int[] heightMapIndexData = new int[(verticesPerStrip * numStripsRequired) + numDegensRequired];
        int offset = 0;

        for (int y = 0; y < horizontalVerticesCount - 1; y++) {
            if (y > 0) {
                // degenerate begin: repeat first vertex
                heightMapIndexData[offset++] = y * verticalVerticesCount;
            }

            for (int x = 0; x < verticalVerticesCount; x++) {
                // one part of the strip
                heightMapIndexData[offset++] = y * verticalVerticesCount + x;
                heightMapIndexData[offset++] = (y + 1) * verticalVerticesCount + x;
            }

            if (y < horizontalVerticesCount - 2) {
                // degenerate end: repeat last vertex
                heightMapIndexData[offset++] = (y + 1) * verticalVerticesCount + verticalVerticesCount - 1;
            }
        }
        return heightMapIndexData;
    }

    public int[] getIndices() {
        return indices;
    }

    public float[] getVertices() {
        return vertices;
    }
}
