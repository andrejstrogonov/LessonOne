package com.example.lessonone;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES10.glClearColor;
import static android.opengl.GLES20.glViewport;

public class LessonOneRenderer implements Renderer {
    private float[] mModelMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    /** Store our model data in a float buffer. */
    private final FloatBuffer mTriangle1Vertices;
    private final FloatBuffer mTriangle2Vertices;
    private final FloatBuffer mTriangle3Vertices;
    private final FloatBuffer mTriangle4Vertices;
    private final FloatBuffer mTriangle5Vertices;
    private final FloatBuffer mTriangle6Vertices;


    private int mColorHandle;

    /** How many bytes per float. */
    private final int mBytesPerFloat = 4;
    private int mPositionHandle;

    /** How many elements per vertex. */
    private final int mStrideBytes = 7 * mBytesPerFloat;

    /** Offset of the position data. */
    private final int mPositionOffset = 0;

    /** Size of the position data in elements. */
    private final int mPositionDataSize = 3;

    /** Offset of the color data. */
    private final int mColorOffset = 3;

    /** Size of the color data in elements. */
    private final int mColorDataSize = 4;
    private float x;
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private int mMVPMatrixHandle;

    public LessonOneRenderer() {
        // Define points for equilateral triangles.

        // This triangle is white_blue.First sail is mainsail
        final float[] triangle1VerticesData = {
                // X, Y, Z,
                // R, G, B, A
                -0.5f, -0.25f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,

                0.0f, -0.25f, 0.0f,
                0.8f, 0.8f, 1.0f, 1.0f,

                0.0f, 0.56f, 0.0f,
                0.8f, 0.8f, 1.0f, 1.0f};

        final float[] triangle2VerticesData = {
                // X, Y, Z,
                // R, G, B, A
                -0.25f, -0.25f, 0.0f,
                0.8f, 0.8f, 1.0f, 1.0f,

                0.03f, -0.25f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,

                -0.25f, 0.4f, 0.0f,
                0.8f, 0.8f, 1.0f, 1.0f};
        final float[] triangle3VerticesData = {
                // X, Y, Z,
                // R, G, B, A
                -1.0f, -1.5f, 0.0f,
                0.0f, 0.0f, 1.0f, 1.0f,

                1.0f, -0.35f, 0.0f,
                0.0f, 0.0f, 1.0f, 1.0f,

                -1.0f, -0.35f, 0.0f,
                0.0f, 0.0f, 1.0f, 1.0f};
        final float[] triangle4VerticesData = {
                // X, Y, Z,
                // R, G, B, A
                -1.0f, -1.5f, 0.0f,
                0.0f, 0.0f, 1.0f, 1.0f,

                1.0f, -1.5f, 0.0f,
                0.0f, 0.0f, 1.0f, 1.0f,

                1.0f, -0.35f, 0.0f,
                0.0f, 0.0f, 1.0f, 1.0f};
        final float[] triangle5VerticesData = {
                // X, Y, Z,
                // R, G, B, A
                -0.4f, -0.3f, 0.0f,
                0.7f, 0.3f, 0.4f, 1.0f,

                -0.4f, -0.4f, 0.0f,
                0.7f, 0.3f, 0.4f, 1.0f,

                0.3f, -0.3f, 0.0f,
                0.7f, 0.3f, 0.4f, 1.0f};
        final float[] triangle6VerticesData = {
                // X, Y, Z,
                // R, G, B, A
                -0.4f, -0.4f, 0.0f,
                0.7f, 0.3f, 0.4f, 1.0f,

                0.22f, -0.4f, 0.0f,
                0.7f, 0.3f, 0.4f, 1.0f,

                0.3f, -0.3f, 0.0f,
                0.7f, 0.3f, 0.4f, 1.0f};

        mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle1Vertices.put(triangle1VerticesData).position(0);

         mTriangle2Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle2Vertices.put(triangle1VerticesData).position(0);

        mTriangle3Vertices = ByteBuffer.allocateDirect(triangle3VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle3Vertices.put(triangle3VerticesData).position(0);

        mTriangle4Vertices = ByteBuffer.allocateDirect(triangle4VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle4Vertices.put(triangle4VerticesData).position(0);

        mTriangle5Vertices = ByteBuffer.allocateDirect(triangle5VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle5Vertices.put(triangle5VerticesData).position(0);

        mTriangle6Vertices = ByteBuffer.allocateDirect(triangle6VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle6Vertices.put(triangle6VerticesData).position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
    {
        glClearColor(0.5f,0.0f,0.9f,1.5f);
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.5f;
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ =-5.0f;
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
        final String vertexShader =

                "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
                        + "attribute vec4 a_Color;        \n"     // Per-vertex color information we will pass in.

                        + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader.

                        + "void main()                    \n"     // The entry point for our vertex shader.
                        + "{                              \n"
                        + "   v_Color = a_Color;          \n"     // Pass the color through to the fragment shader.
                        // It will be interpolated across the triangle.
                        + "   gl_Position =  a_Position; \n"  // gl_Position is a special variable used to store the final position.

                        + "}                              \n";    // normalized screen coordinates.

        final String fragmentShader =
                "precision mediump float;       \n"       // Set the default precision to medium. We don't need as high of a
                        // precision in the fragment shader.
                        + "varying vec4 v_Color;          \n"     // This is the color from the vertex shader interpolated across the
                        // triangle per fragment.
                        + "void main()                    \n"     // The entry point for our fragment shader.
                        + "{                              \n"
                        + "   gl_FragColor = v_Color;     \n"     // Pass the color directly through the pipeline.
                        + "}                              \n";

        // Load in the vertex shader.
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        if (vertexShaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(vertexShaderHandle, vertexShader);

            // Compile the shader.
            GLES20.glCompileShader(vertexShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(vertexShaderHandle);
                vertexShaderHandle = 0;
            }
        }

        if (vertexShaderHandle == 0)
        {
            throw new RuntimeException("Error creating vertex shader.");
        }

        // Load in the fragment shader shader.
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        if (fragmentShaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);

            // Compile the shader.
            GLES20.glCompileShader(fragmentShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(fragmentShaderHandle);
                fragmentShaderHandle = 0;
            }
        }

        if (fragmentShaderHandle == 0)
        {
            throw new RuntimeException("Error creating fragment shader.");
        }

        // Create a program object and store the handle to it.
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0)
        {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color");

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0)
            {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0)
        {
            throw new RuntimeException("Error creating program.");
        }

        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");

        // Tell OpenGL to use this program when rendering.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        GLES20.glUseProgram(programHandle);

    }

    @Override
    // Set the OpenGL viewport to fill the entire surface.
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        glViewport(0, 0, width, height);
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);

    }

    @Override
    // Clear the rendering surface.
    public void onDrawFrame(GL10 glUnused) {
        //   glClear(GL_COLOR_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, x, 0.0f, 0.0f);
        drawTriangle(mTriangle1Vertices);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, x + 0.3f, 0.0f, 0.0f);
        drawTriangle(mTriangle2Vertices);
        if(x<=1){x = (float) (x + 0.001);}
       else {x=0;}

        Matrix.setIdentityM(mModelMatrix, 0);
        drawTriangle(mTriangle3Vertices);

        Matrix.setIdentityM(mModelMatrix, 0);
        drawTriangle(mTriangle4Vertices);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, x, 0.0f, 0.0f);
        drawTriangle(mTriangle5Vertices);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, x, 0.0f, 0.0f);
        drawTriangle(mTriangle6Vertices);
    }
    /**
     * Draws a triangle from the given vertex data.
     *
     * @param aTriangleBuffer The buffer containing the vertex data.
     */
    private void drawTriangle(final FloatBuffer aTriangleBuffer)
    {
        // Pass in the position information
        aTriangleBuffer.position(mPositionOffset);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, aTriangleBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        aTriangleBuffer.position(mColorOffset);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, aTriangleBuffer);

        GLES20.glEnableVertexAttribArray(mColorHandle);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }

}
