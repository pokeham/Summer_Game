//---------------//
///vertex shader///
//---------------//
#type vertex
#version 330 core

//attributes for vertex shader
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;

uniform mat4 uProj;
uniform mat4 uView;

//attribute to be sent to fragment shader
out vec4 fColor;

void main(){
    //pass color to fragment shader
    fColor = aColor;

    //vector of size four where aPos is the first 3 elements and a 1 is the last element
    //to be sent
    gl_Position = uProj*uView*vec4(aPos, 1.0);
}
//-----------------//
///fragment shader///
//-----------------//
#type fragment
#version 330 core

//input from vertex shader
in vec4 fColor;
//output vector to GL
out vec4 color;

void main(){
    color = fColor;
}