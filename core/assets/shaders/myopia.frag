#ifdef GL_ES 
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif

#define SIMPLIFY 8

uniform vec2 resolution;

uniform sampler2D u_texture;
varying LOWP vec4 vColor;
varying vec2 vTexCoord;
varying vec4 vPosition;

void main() {
    vec2 vTexCoord2 = vec2(vTexCoord.x*resolution.x, vTexCoord.y*resolution.y);
	vTexCoord2.x = floor(vTexCoord2.x/SIMPLIFY)*SIMPLIFY;
	vTexCoord2.y = floor(vTexCoord2.y/SIMPLIFY)*SIMPLIFY;
	vTexCoord2.x /= resolution.x;
	vTexCoord2.y /= resolution.y;
	vec4 texColor = texture2D(u_texture, vTexCoord2);
	
	gl_FragColor = texColor;
}