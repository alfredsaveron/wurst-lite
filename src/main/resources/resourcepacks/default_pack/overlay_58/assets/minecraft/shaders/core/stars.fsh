#version 150

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>

in vec3 position;

out vec4 fragColor;

void main() {
	vec4 color = ColorModulator;
    color.a += sin((GameTime * 2000.0) + position.x + position.z);
    fragColor = color;
}
