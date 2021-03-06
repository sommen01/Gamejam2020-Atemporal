#ifdef GL_ES 
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif

uniform vec2 resolution;

uniform sampler2D u_texture;
varying LOWP vec4 vColor;
varying vec2 vTexCoord;

uniform vec3 color;

void main() {
	vec4 texColor = texture2D(u_texture, vTexCoord);
	texColor.rgb = color;
	gl_FragColor = vColor * texColor;
}