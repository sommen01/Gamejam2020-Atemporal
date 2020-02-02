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

void main() {
	vec4 texColor = texture2D(u_texture, vTexCoord);
	
	vec2 position = (gl_FragCoord.xy / resolution.xy) - vec2(0.5);
	
	float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
	texColor.rgb = texColor.rgb * gray;
	
	gl_FragColor = vColor * texColor;
}