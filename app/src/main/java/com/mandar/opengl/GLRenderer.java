package com.mandar.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer {
    private Context context;
    // texture coordinates
    private FloatBuffer mTexCoordinate;
    private final FloatBuffer mShapeCoordinate;

    private final int mBytesPerFloat = 4; // How many bytes per float.
    private final int mPositionDataSize = 3; // Size of the position data in elements.
    private int mPositionHandle; // This will be used to pass in model position information.

    // token values of texture to pass in shader
    private int mTexUniformHandle;

    // token values of model texture coordinate to pass in shader
    private int mTexCoordinateHandle;

    // size of texture coordinate datasize
    private final int mTexCoordinateDataSize = 2;

    final int[] mTextureDataHandle = new int[1];
    final int[] mTextureDataHandle1 = new int[1];

    /** This is a handle to our shading program. */
    private int mProgramHandle;
    private float CurrentAngle=0;
    private int counter=30;


    private final String vertexShaderCode =
            " attribute vec4 a_Position; " +
                    " attribute vec2 a_TexCoordinate; "
                    + "varying vec3 v_Position; "
                    + "varying vec2 v_TexCoordinate; " +
                    " void main() {" +
                    "  gl_Position = a_Position; "
                    + " v_TexCoordinate = a_TexCoordinate; "
                    + " " +
                    "}";

    private final String fragmentShaderCode =
            " precision mediump float;" +
                    " uniform sampler2D u_Texture; "
                    + "varying vec3 v_Position; "
                    + "varying vec4 v_Color;   "
                    + "varying vec2 v_TexCoordinate;  " +
                    "void main() {" +
                    "    gl_FragColor =( texture2D(u_Texture, v_TexCoordinate)); "+//texture2D(tex, TexCoord);"+// gl_FragColor = vColor;" +
                    "}";
    private float vertices[] = {

//		    		0.0f, 1.0f, 0.0f,
//		            1.0f, 1.0f, 0.0f,
//		            1.0f, 0.0f, 0.0f,
//
//
//		    		1.0f, 0.0f, 0.0f,
//		            0.0f, 0.0f, 0.0f,
//		            0.0f, 1.0f, 0.0f,

            //1 ------------
            0.990f, 0.0f, 0.0f,
            0.9659f, 0.2588f, 0.0f,
            0.0f, 0.0f, 0.0f,

            0.9659f, 0.2588f, 0.0f,
            0.8660f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.0f,

            //2 ------------
            0.8660f, 0.5f, 0.0f,
            0.7071f, 0.7071f, 0.0f,
            0.0f, 0.0f, 0.0f,

            0.7071f, 0.7071f, 0.0f,
            0.5f, 0.8660f, 0.0f,
            0.0f, 0.0f, 0.0f,

            //3 ------------
            0.5f, 0.8660f, 0.0f,
            0.25881f, 0.9659f, 0.0f,
            0.0f, 0.0f, 0.0f,

            0.2588f, 0.9659f, 0.0f,
            0.0f, 0.9999f, 0.0f,
            0.0f, 0.0f, 0.0f,

            //4 ------------
            0.0f, 0.9999f, 0.0f,
            -0.2588f, 0.9659f, 0.0f,
            0.0f, 0.0f, 0.0f,

            -0.2588f, 0.9659f, 0.0f,
            -0.5f, 0.8660f, 0.0f,
            0.0f, 0.0f, 0.0f,

            //5 ----------
            -0.5f, 0.866f, 0.0f,
            -0.707f, 0.707f, 0.0f,
            0.0f, 0.0f, 0.0f,

            -0.707f, 0.707f, 0.0f,
            -0.866f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.0f,

            //6 ----------
            -0.8660f, 0.5f, 0.0f,
            -0.9659f, 0.2584f, 0.0f,
            0.0f, 0.0f, 0.0f,


            -0.9659f, 0.2588f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f,

            //7 ----------
            -1.0f, 0.0f, 0.0f,
            -0.9659f, -0.2588f, 0.0f,
            0.0f, 0.0f, 0.0f,


            -0.9659f, -0.2588f, 0.0f,
            -0.8660f, -0.5f, 0.0f,
            0.0f, 0.0f, 0.0f,
            //8 ----------
            -0.8660f, -0.5f, 0.0f,
            -0.70710f, -0.70710f, 0.0f,
            0.0f, 0.0f, 0.0f,


            -0.70710f, -0.70710f, 0.0f,
            -0.5f, -0.8660f, 0.0f,
            0.0f, 0.0f, 0.0f,
            //9 ----------
            -0.5f, -0.8660f, 0.0f,
            -0.25881f, -0.9659f, 0.0f,
            0.0f, 0.0f, 0.0f,


            -0.25881f, -0.9659f, 0.0f,
            0.00001f, -1.0f, 0.0f,
            0.0f, 0.0f, 0.0f,
            //10 ----------
            0.0001f, -1.0f, 0.0f,
            0.2588f, -0.96f, 0.0f,
            0.0f, 0.0f, 0.0f,


            0.25881f, -0.9659f, 0.0f,
            0.5f, -0.8660f, 0.0f,
            0.0f, 0.0f, 0.0f,
            //11 ----------
            0.5f, -0.8660f, 0.0f,
            0.70710f, -0.70710f, 0.0f,
            0.0f, 0.0f, 0.0f,


            0.70710f, -0.70710f, 0.0f,
            0.8660254f, -0.5f, 0.0f,
            0.0f, 0.0f, 0.0f,
            //12 ----------
            0.8660f, -0.5f, 0.0f,
            0.9659f, -0.25881f, 0.0f,
            0.0f, 0.0f, 0.0f,

            0.965f, -0.2588f, 0.0f,
            0.990f,0.0f,0.0f,
            0.0f, 0.0f, 0.0f,

    };

    private float texture[] = {


            //1 real
            0.241180f, 0.03407f,     // inclination
            0.50f, 0.0f,     // edge
            0.50f, 1.0f,     // angle

            0.50f, 0.0f,     // edge
            0.241180f, 0.03407f,     // inclination
            0.50f, 1.0f,     // angle

            //2 real
            0.241180f, 0.03407f,     // inclination
            0.50f, 0.0f,     // edge
            0.50f, 1.0f,     // angle

            0.50f, 0.0f,     // edge
            0.241180f, 0.03407f,     // inclination
            0.50f, 1.0f,     // angle

            //3 real
            0.241180f, 0.03407f,     // inclination
            0.50f, 0.0f,     // edge
            0.50f, 1.0f,     // angle

            0.50f, 0.0f,     // edge
            0.241180f, 0.03407f,     // inclination
            0.50f, 1.0f,     // angle

            //4 real
            0.241180f, 0.03407f,     // inclination
            0.50f, 0.0f,     // edge
            0.50f, 1.0f,     // angle

            0.50f, 0.0f,     // edge
            0.241180f, 0.03407f,     // inclination
            0.50f, 1.0f,     // angle

            //5 real
            0.241180f, 0.03407f,     // inclination
            0.50f, 0.0f,     // edge
            0.50f, 1.0f,     // angle

            0.50f, 0.0f,     // edge
            0.241180f, 0.03407f,     // inclination
            0.50f, 1.0f,     // angle

            //6 real
            0.241180f, 0.03407f,     // inclination
            0.50f, 0.0f,     // edge
            0.50f, 1.0f,     // angle

            0.50f, 0.0f,     // edge
            0.241180f, 0.03407f,     // inclination
            0.50f, 1.0f,     // angle

            //7 real
            0.241180f, 0.03407f,     // inclination
            0.50f, 0.0f,     // edge
            0.50f, 1.0f,     // angle

            0.50f, 0.0f,     // edge
            0.241180f, 0.03407f,     // inclination
            0.50f, 1.0f,     // angle

            //8 real
            0.241180f, 0.03407f,     // inclination
            0.50f, 0.0f,     // edge
            0.50f, 1.0f,     // angle

            0.50f, 0.0f,     // edge
            0.241180f, 0.03407f,     // inclination
            0.50f, 1.0f,     // angle

            //9 real
            0.241180f, 0.03407f,     // inclination
            0.50f, 0.0f,     // edge
            0.50f, 1.0f,     // angle

            0.50f, 0.0f,     // edge
            0.241180f, 0.03407f,     // inclination
            0.50f, 1.0f,     // angle

            //10 real
            0.241180f, 0.03407f,     // inclination
            0.50f, 0.0f,     // edge
            0.50f, 1.0f,     // angle

            0.50f, 0.0f,     // edge
            0.241180f, 0.03407f,     // inclination
            0.50f, 1.0f,     // angle

            //11 real
            0.241180f, 0.03407f,     // inclination
            0.50f, 0.0f,     // edge
            0.50f, 1.0f,     // angle

            0.50f, 0.0f,     // edge
            0.241180f, 0.03407f,     // inclination
            0.50f, 1.0f,     // angle

            //12 real
            0.241180f, 0.03407f,     // inclination
            0.50f, 0.0f,     // edge
            0.50f, 1.0f,     // angle

            0.50f, 0.0f,     // edge
            0.241180f, 0.03407f,     // inclination
            0.50f, 1.0f,     // angle

    };

    public static int LoadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }


    public GLRenderer(Context context) {
        this.context = context;
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        // use native byte order
        bb.order(ByteOrder.nativeOrder()); // little/big endian
        mShapeCoordinate = bb.asFloatBuffer(); // Make Space
        mShapeCoordinate.put(vertices); // fill Coord
        mShapeCoordinate.position(0); // set first coord
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // m_Shape = new Shape();
        // m_Shape.loadGLTexture(gl, this.context);
        try {

            GLES20.glGenTextures(1, mTextureDataHandle, 0);
            final Bitmap bmpimg = BitmapFactory.decodeStream(context
                    .getAssets().open("test.jpg"));

            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmpimg, 0);


//            GLES20.glGenTextures(1, mTextureDataHandle1, 1);
//            final Bitmap bmpimg1 = BitmapFactory.decodeStream(context
//                    .getAssets().open("OpenGL.png"));
//            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle1[0]);
//            // Load the bitmap into the bound texture.
//            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmpimg1, 0);


            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bmpimg.recycle();
//            bmpimg1.recycle();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // prepare shaders and OpenGL program
        int vertexShader = GLRenderer.LoadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = GLRenderer.LoadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);

        mProgramHandle = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgramHandle, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgramHandle, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgramHandle);                  // create OpenGL program executables

    }

    // dont load always
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        int less = 0;
        int offsetx = 0, offsety = 0;
        if (width > height) {
            less = height;
            // offsetx=width-height;
        } else {
            less = width;
            offsety = height - width;
        }
        GLES20.glViewport(offsetx, offsety, less, less);
    }


    // keep lite as possible
    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear background
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Drawing
        //m_Shape.DrawShape();

        GLES20.glUseProgram(mProgramHandle);
        GLES20.glLinkProgram(mProgramHandle);                  // create OpenGL program executables



        //  if(counter>3)
        // {
        //	  counter=0;

        // current point on circle   CC current center on outer circle
//		   float ccenterx = ((float) (java.lang.Math.cos(CurrentAngle  * Math.PI / 180F)*0.5f)+0.5f );
//		   float ccentery = ((float) (java.lang.Math.sin(CurrentAngle  * Math.PI / 180F)*0.5f)+0.5f);
//
//		   float anticenterx = java.lang.Math.abs((1)-ccenterx);
//		   float anticentery = java.lang.Math.abs((1)-ccentery);
//
//		   float inclix = ((float) (java.lang.Math.cos((15+CurrentAngle)* Math.PI / 180F )*0.5f)+(0.5f));
//		   float incliy = ((float) (java.lang.Math.sin((15+CurrentAngle)* Math.PI / 180F)*0.5f)+(0.5f));
//


        // Algo 2
        float ccenterx = (0.5f );
        float ccentery = (0.5f);

        float anticenterx = ((float) (java.lang.Math.cos(CurrentAngle  * Math.PI / 180F)*0.5f)+0.5f );
        float anticentery = ((float) (java.lang.Math.sin(CurrentAngle  * Math.PI / 180F)*0.5f)+0.5f);

        float inclix = ((float) (java.lang.Math.cos((15+CurrentAngle)* Math.PI / 180F )*0.5f)+(0.5f));
        float incliy = ((float) (java.lang.Math.sin((15+CurrentAngle)* Math.PI / 180F)*0.5f)+(0.5f));


        CurrentAngle=CurrentAngle+0.3f;

        if(CurrentAngle>=361)
            CurrentAngle=1;

        float[] t = new float[12*12];

        float ccx=-0.8660f;
        float ccy=-0.2588f;

        for(int i=0,j=0;i<12;i++)
        {
            t[j]=inclix; j++;
            t[j]=incliy; j++;    // inclination
            t[j]=anticenterx; j++;
            t[j]=anticentery; j++;     // edge
            t[j]=ccenterx; j++;
            t[j]=ccentery;   j++;    // angle

            t[j]=anticenterx; j++;
            t[j]=anticentery;  j++;    // edge
            t[j]=inclix; j++;
            t[j]=incliy;   j++;   // inclination
            t[j]=ccenterx; j++;
            t[j]=ccentery;  j++;     // angle

//				t[j]=0.241180f; j++;
//				t[j]= 0.03407f; j++;    // inclination
//				t[j]=0.50f; j++;
//				t[j]=0.0f; j++;     // edge
//				t[j]=0.50f; j++;
//				t[j]=1.0f;   j++;    // angle
//
//				t[j]=0.50f; j++;
//				t[j]=0.0f; j++;     // edge
//				t[j]=0.241180f; j++;
//				t[j]= 0.03407f; j++;    // inclination
//				t[j]=0.50f; j++;
//				t[j]=1.0f;   j++;    // angle

//				t[j]=-1.0f; j++;
//				t[j]= 0.0f; j++;    // inclination
//				t[j]=ccx; j++;
//				t[j]=ccy; j++;     // edge
//				t[j]=1.0f; j++;
//				t[j]=0.0f;   j++;    // angle
//
//				t[j]=ccx; j++;
//				t[j]=ccy; j++;     // edge
//				t[j]=-1.0f; j++;
//				t[j]= 0.0f; j++;    // inclination
//				t[j]=1.0f; j++;
//				t[j]=0.0f;   j++;    // angle
//


        }

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(t.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        mTexCoordinate = byteBuffer.asFloatBuffer();
        mTexCoordinate.put(t);
        mTexCoordinate.position(0);


        //	  }
        //	  else
        //	  {
        //		  counter++;
        //	  }












        mTexUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");
        mTexCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");
        mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle[0]);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTexUniformHandle, 0);

        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,0, mShapeCoordinate);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the texture coordinate information
        mTexCoordinate.position(0);
        GLES20.glVertexAttribPointer(mTexCoordinateHandle, mTexCoordinateDataSize, GLES20.GL_FLOAT, false,0, mTexCoordinate);

        GLES20.glEnableVertexAttribArray(mTexCoordinateHandle);

        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.length/3);
    }

}
