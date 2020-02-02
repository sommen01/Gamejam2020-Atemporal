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

const vec3 HAPPINESS = vec3(1.2, 1.0, 0.8);

void main() {
	//sample the texture
	vec4 texColor = texture2D(u_texture, vTexCoord);
	
	texColor.rgb = texColor.rgb * HAPPINESS;
	
	gl_FragColor = vColor * texColor;
}