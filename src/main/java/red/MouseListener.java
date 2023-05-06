package red;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {

    private static MouseListener instance = null;
    private double scrollX, scrollY;
    private double posX,posY,lastY,lastX;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;
    //private constructor because we only want one instance to be created and only created within this class
    private MouseListener(){
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.posX = 0.0;
        this.posY = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }
    //get function to ensure one instance of the object
    public static MouseListener get(){
        if (MouseListener.instance == null){
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    /// Call Backs ///
    public static void mousePosCallback(long window,double X,double Y){
        get().lastX = get().posX;
        get().lastY = get().posY;
        get().posX = X;
        get().posY = Y;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2]; //!!CHANGE to ensure its only left click!!
    }
    public static void mouseButtonCallback(long window,int button, int action, int mods){
        if(action == GLFW_PRESS){
            if(get().mouseButtonPressed.length > button) {
                get().mouseButtonPressed[button] = true;
            }
        } else if(action == GLFW_RELEASE){
            if(get().mouseButtonPressed.length > button) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }
    public static void mouseScrollCallback(long window,double xOffset, double yOffset){
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }
    //end frame function
    public static void endFrame(){
        get().scrollY = 0;
        get().scrollX = 0;
        get().lastX = get().posX;
        get().lastY = get().posY;
    }
    //getters
    public static float getX(){return (float)get().posX;}
    public static float getY(){return (float)get().posY;}
    public static float getDx(){return (float)(get().lastX - get().posX);}
    public static float getDy(){return (float)(get().lastY - get().posY);}
    public static float getScrollX(){return  (float) get().scrollX;}
    public static float getScrollY(){return  (float) get().scrollY;}
    public static boolean isDragging(){return get().isDragging;}
    public static boolean mouseButtonDown(int button){
        if(get().mouseButtonPressed.length > button){
            return get().mouseButtonPressed[button];
        }
        return false;

    }



}
