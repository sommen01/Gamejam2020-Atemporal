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

uniform float RADIUS;
uniform float SOFTNESS;

void main() {
	vec4 texColor = texture2D(u_texture, vTexCoord);
	
	vec2 position = resolution.xy*0.5;
	float len = length(gl_FragCoord.xy - position);
	float radi = min(resolution.x, resolution.y) * RADIUS;
	float vignette = smoothstep(radi, -SOFTNESS, len);
	texColor.rgb = mix(texColor.rgb, texColor.rgb * vignette, 1.0);
	
	gl_FragColor = vColor * texColor;
}