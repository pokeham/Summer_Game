package red;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance = null;
    private boolean keyPressed[] = new boolean[350];

    private KeyListener(){

    }
    private static KeyListener get(){
        if(KeyListener.instance == null){
            KeyListener.instance = new KeyListener();
        }
        return  KeyListener.instance;
    }
    public static void keyCallBack(long window,int key,int scancode,int action,int mods){
        if(action == GLFW_PRESS){
            if(key<350){
                get().keyPressed[key] = true;
            }
        } else if(action == GLFW_RELEASE){
            get().keyPressed[key] = false;
        }
    }
    public static boolean isKeyPressed(int keycode){
        return get().keyPressed[keycode];
    }
}
