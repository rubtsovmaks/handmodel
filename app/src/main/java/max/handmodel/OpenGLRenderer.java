package max.handmodel;

/**
 * Created by ACER-PC on 21.01.2017.
 */
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.SystemClock;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glLineWidth;

public class OpenGLRenderer implements Renderer {

    private final static int POSITION_COUNT = 3;

    private final static long TIME = 10000;

    private Context context;

    private FloatBuffer vertexData;
    private int uColorLocation;
    private int aPositionLocation;
    private int uMatrixLocation;
    private int programId;

    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMatrix = new float[16];


    float centerX;
    float centerY;
    float centerZ;

    float upX;
    float upY;
    float upZ;

    public OpenGLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        glClearColor(0f, 0f, 0f, 1f);
        glEnable(GL_DEPTH_TEST);
        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader);
        programId = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);
        glUseProgram(programId);
        createViewMatrix();
        prepareData();
        bindData();
    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        glViewport(0, 0, width, height);
        createProjectionMatrix(width, height);
        bindMatrix();
    }

    private void prepareData() {

        float s = 0.4f;
        float d = 0.9f;
        float l = 3;

        // средний палец
        float falanga_1_lenght = 0.7f;
        float falanga_2_delta = 0.5f;
        float falanga_3_delta = 0.4f;

        // безымянный палец
        float noname_falanga_1_lenght = 0.5f;
        float noname_falanga_2_delta = 0.5f;
        float noname_falanga_3_delta = 0.4f;

        // мизинец палец
        float small_falanga_1_lenght = 0.4f;
        float small_falanga_2_delta = 0.4f;
        float small_falanga_3_delta = 0.3f;

        // указательный палец
        float show_falanga_1_lenght = 0.7f;
        float show_falanga_2_delta = 0.4f;
        float show_falanga_3_delta = 0.3f;

        // большой палец
        float big_falanga_1_lenght = 0.3f;
        float big_falanga_2_delta = 0.4f;


        float[] vertices = {


                // срелний палец
                0.0f, 0.0f, 0.0f,
                0.0f, falanga_1_lenght,0.0f,

                0.0f, 0.0f, 0.0f,

                0.0f, falanga_1_lenght,0.0f,

                0.0f, falanga_1_lenght, 0.0f,
                0.0f, falanga_1_lenght + falanga_2_delta,0.0f,

                0.0f, falanga_1_lenght + falanga_2_delta,0.0f,

                0.0f, falanga_1_lenght + falanga_2_delta, 0.0f,
                0.0f, falanga_1_lenght + falanga_2_delta + falanga_3_delta,0.0f,

                // безымянный палец
                -0.3f, -0.2f, 0.0f,
                -0.3f, noname_falanga_1_lenght,0.0f,

                -0.3f, -0.2f, 0.0f,

                -0.3f, noname_falanga_1_lenght,0.0f,

                -0.3f, noname_falanga_1_lenght, 0.0f,
                -0.3f, noname_falanga_1_lenght + noname_falanga_2_delta,0.0f,

                -0.3f, noname_falanga_1_lenght + noname_falanga_2_delta,0.0f,

                -0.3f, noname_falanga_1_lenght + noname_falanga_2_delta, 0.0f,
                -0.3f, noname_falanga_1_lenght + noname_falanga_2_delta + noname_falanga_3_delta,0.0f,

                // мизенец
                -0.6f, -0.4f, 0.0f,
                -0.6f, small_falanga_1_lenght,0.0f,

                -0.6f, -0.4f, 0.0f,

                -0.6f, small_falanga_1_lenght,0.0f,

                -0.6f, small_falanga_1_lenght, 0.0f,
                -0.6f, small_falanga_1_lenght + small_falanga_2_delta,0.0f,

                -0.6f, small_falanga_1_lenght + small_falanga_2_delta,0.0f,

                -0.6f, -small_falanga_1_lenght + small_falanga_2_delta, 0.0f,
                -0.6f, small_falanga_1_lenght + small_falanga_2_delta + small_falanga_3_delta,0.0f,

                // указательный
                0.3f, 0.0f, 0.0f,
                0.3f, show_falanga_1_lenght,0.0f,

                0.3f, 0.0f, 0.0f,

                0.3f, show_falanga_1_lenght,0.0f,

                0.3f, show_falanga_1_lenght, 0.0f,
                0.3f, show_falanga_1_lenght+show_falanga_2_delta,0.0f,

                0.3f, show_falanga_1_lenght+show_falanga_2_delta,0.0f,

                0.3f, show_falanga_1_lenght+show_falanga_2_delta, 0.0f,
                0.3f, show_falanga_1_lenght+show_falanga_2_delta + show_falanga_3_delta,0.0f,

                // большой палец
                0.6f, -0.5f, 0.0f,
                0.6f, big_falanga_1_lenght,0.0f,

                0.6f, -0.5f, 0.0f,

                0.6f, big_falanga_1_lenght,0.0f,

                0.6f, big_falanga_1_lenght,0.0f,
                0.6f, big_falanga_1_lenght + big_falanga_2_delta,0.0f,

                // кости ладони
                0.0f, 0.0f,0.0f,
                0.0f, -1.4f,0.0f,

                0.3f, 0.0f,0.0f,
                0.0f, -1.4f,0.0f,

                -0.3f, -0.2f, 0.0f,
                0.0f, -1.4f,0.0f,

                -0.6f, -0.4f, 0.0f,
                0.0f, -1.4f,0.0f,

                0.6f, -0.5f, 0.0f,
                0.0f, -1.4f,0.0f,

                // ось X
                -l, 0, 0,
                l, 0, 0,

                // ось Y
                0, -l, 0,
                0, l, 0,

                // ось Z
                0, 0, -l,
                0, 0, l,

                // up-вектор
                centerX, centerY, centerZ,
                centerX + upX, centerY + upY, centerZ + upZ,
        };

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);
    }

    private void bindData() {
        // координаты
        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COUNT, GL_FLOAT,
                false, 0, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        // цвет
        uColorLocation = glGetUniformLocation(programId, "u_Color");

        // матрица
        uMatrixLocation = glGetUniformLocation(programId, "u_Matrix");
    }

    private void createProjectionMatrix(int width, int height) {
        float ratio = 1;
        float left = -1;
        float right = 1;
        float bottom = -1;
        float top = 1;
        float near = 2;
        float far = 8;
        if (width > height) {
            ratio = (float) width / height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = (float) height / width;
            bottom *= ratio;
            top *= ratio;
        }

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    private void createViewMatrix() {
        float time = (float)(SystemClock.uptimeMillis() % TIME) / TIME;
        float angle = time  *  2 * 3.1415926f;

        // точка положения камеры
        float eyeX = (float) (Math.cos(angle) * 4f);
        float eyeY = 2f;
        float eyeZ = 4f;

        // точка направления камеры
        float centerX = 0;
        float centerY = 0;
        float centerZ = 0;

        // up-вектор
        float upX = 0;
        float upY = 1;
        float upZ = 0;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }


    private void bindMatrix() {
        Matrix.multiplyMM(mMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 arg0) {
        createViewMatrix();
        bindMatrix();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);



        glLineWidth(20);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 0, 2);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 2, 1);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 3, 1);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 4, 2);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 6, 1);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 7, 2);

        // безымянный
        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 9, 2);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 10, 1);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 11, 1);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 13, 2);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 15, 1);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 16, 2);

        // мизинец
        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 18, 2);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 20, 1);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 21, 1);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 22, 2);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 24, 1);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 25, 2);

        // указательный
        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 27, 2);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 29, 1);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 30, 1);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 31, 2);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 33, 1);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 34, 2);

        // большой палец
        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 36, 2);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 38, 1);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 39, 1);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 40, 2);

        // bones
        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 42, 2);

        // bones
        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 44, 2);

        // bones
        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 46, 2);

        // bones
        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 48, 2);

        // bones
        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 50, 2);

        // оси
        glLineWidth(3);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 1.0f, 1.0f);
        glDrawArrays(GL_LINES, 52, 2);

        glUniform4f(uColorLocation, 1.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_LINES, 54, 2);

        glUniform4f(uColorLocation, 1.0f, 0.5f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 56, 2);

    }


}

