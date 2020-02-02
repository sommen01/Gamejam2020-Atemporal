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

uniform float u_ticks;
uniform vec3 color;

void main() {
	float center = mod((u_ticks/60.0)*4.0, 8.0)-1.5;
	vec4 texColor = texture2D(u_texture, vTexCoord);
	vec4 fc = mix(texColor, vec4(texColor.xyz*5.0, texColor.a), (texColor.xyz!=vec3(0.0)&&(abs(vTexCoord.y-vTexCoord.x+center)<=0.2)?1.0:0.0));
	
	if(texColor.rgb == vec3(1.0, 0.0, 1.0))
	{
		gl_FragColor.rgb = color;
		gl_FragColor.a = vColor.a * texColor.a;
	}
	else
	{
		gl_FragColor.rgb = vColor * fc;
	}
}