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
varying vec4 vPosition;

uniform float multR;
uniform float multG;
uniform float multB;

void main() {
	vec4 texColor = texture2D(u_texture, vTexCoord);
	float len = length(vTexCoord);
	float consider = max(1.0-vTexCoord.x, 1.0 - vTexCoord.y);
	float force = (1.0 - consider)*2;
	vec3 forceVec = vec3(1.0 + force, 1.0 + force, 1.0 + force);
	float max = length(texColor.rgb);
	texColor.rgb = mix(texColor.rgb, texColor.rgb * forceVec, 1.0);
	if(max < 0.2)
		max = 1.0;
		
	texColor.rgb = vec3(max(texColor.r, max*0.2), max(texColor.g, max*0.2), max(texColor.b, max*0.2));
	gl_FragColor = vColor * texColor;
}