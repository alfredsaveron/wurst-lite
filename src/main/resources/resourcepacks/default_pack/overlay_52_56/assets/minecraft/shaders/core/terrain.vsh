#version 150

#moj_import <minecraft:light.glsl>
#moj_import <minecraft:fog.glsl>

#moj_import <minecraft:vt_wavy_core/vert.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler0;
uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec3 ModelOffset;
uniform int FogShape;
uniform float GameTime;

out float vertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;

void main() {
    vec3 pos = Position + ModelOffset;

    float vertexAlphaMultiplier = 1.0;
    gl_Position = ProjMat * ModelViewMat * vec4(applyVertexOffsets(Position, ModelOffset, GameTime, sampleAnimationType(Sampler0, UV0), vertexAlphaMultiplier), 1.0);

    vertexDistance = fog_distance(pos, FogShape);
    vertexColor = Color * minecraft_sample_lightmap(Sampler2, UV2);
    texCoord0 = UV0;

    vertexColor.a *= vertexAlphaMultiplier;
}
