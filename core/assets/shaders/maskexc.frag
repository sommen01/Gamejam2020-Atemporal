//"in" attributes from our vertex shader
varying vec4 vColor;
varying vec2 vTexCoord;

//declare uniforms
uniform sampler2D u_texture;
uniform sampler2D u_mask;
uniform vec2 u_offset;
uniform vec2 u_maskScale;
uniform float u_frames;

void main() {
	vec2 fixPos = vec2(vTexCoord.x/u_maskScale.x, mod(vTexCoord.y, 1.0/u_frames)/u_maskScale.y);
	vec2 pos = fixPos + u_offset;
	pos.y = mod(pos.y*u_frames, 1.0);
	pos.x = min(abs(pos.x), 2 - abs(pos.x));
	pos.y = min(abs(pos.y), 2 - abs(pos.y));
	vec4 texColor = texture2D(u_mask, pos);
	vec4 mask = texture2D(u_texture, vTexCoord);
	if(mask.rgb != vec3(0.0, 0.0, 0.0))
	{
		texColor.a = mask.a;
		gl_FragColor = texColor;
	}
	else
	{
		gl_FragColor = mask;
	}
}