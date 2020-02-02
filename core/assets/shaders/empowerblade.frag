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
	
	if(texColor.rgb == vec3(0.0, 0.0, 0.0) || texColor.rgb == vec3(1.0, 1.0, 1.0))
	{
		gl_FragColor.rgb = vec3(1.0, 0.8, 0.22);
		gl_FragColor.a = vColor.a * texColor.a;
	}
	else if(texColor.rgb == vec3(1.0, 0.0, 1.0))
	{
		gl_FragColor.rgb = color;
		gl_FragColor.a = vColor.a * texColor.a;
	}
	else
	{
		gl_FragColor.rgb = vec3(1.0, 1.0, 1.0);
		gl_FragColor.a = vColor.a * texColor.a;
	}
}