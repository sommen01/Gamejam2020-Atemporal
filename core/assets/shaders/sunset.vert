//"in" attributes from our SpriteBatch
attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;

//"out" varyings to our fragment shader
varying vec4 vColor;
varying vec2 vTexCoord;
varying vec4 vPosition;
 
void main() {
	vColor = a_color;
	vPosition = a_position;
	vTexCoord = a_texCoord0;
	gl_Position = u_projTrans * a_position;
}