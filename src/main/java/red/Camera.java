package red;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix;
    private Vector3f position;
    private static Camera instance = null;
    public Camera(Vector3f position){
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }
    public static Camera get(){
        if(Camera.instance == null){
            instance = new Camera(new Vector3f(0,0,0));
        }
        return Camera.instance;
    }
    public void adjustProjection(){
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f,32.0f * 60.0f,0.0f,32.0f*34.0f,0.0f,100.0f);
    }
    public Matrix4f getViewMatrix(){
        Vector3f cameraFront = new Vector3f(0.0f,0.0f,-1.0f);
        Vector3f cameraUp = new Vector3f(0.0f,1.0f,0.0f);
        this.viewMatrix.identity();
        this.viewMatrix = viewMatrix.lookAt(new Vector3f(position.x,position.y,position.z),
                                            cameraFront.add(position.x,position.y,position.z),
                                            cameraUp);
        return this.viewMatrix;


    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
}
