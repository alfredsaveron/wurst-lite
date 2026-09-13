#version 150

#moj_import <minecraft:light.glsl>
#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>
#moj_import <minecraft:globals.glsl>

#moj_import <minecraft:vt_wavy_core/vert.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler0;
uniform sampler2D Sampler2;

out float sphericalVertexDistance;
out float cylindricalVertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;

#ifndef VT_WARM_GLOW_ENABLED
vec4 minecraft_sample_lightmap(sampler2D lightMap, ivec2 uv) {
    return texture(lightMap, clamp(uv / 256.0, vec2(0.5 / 16.0), vec2(15.5 / 16.0)));
}
#endif

void main() {
    vec3 pos = Position + ModelOffset;

    float vertexAlphaMultiplier = 1.0;
    gl_Position = ProjMat * ModelViewMat * vec4(applyVertexOffsets(Position, ModelOffset, GameTime, sampleAnimationType(Sampler0, UV0), vertexAlphaMultiplier), 1.0);

    sphericalVertexDistance = fog_spherical_distance(pos);
    cylindricalVertexDistance = fog_cylindrical_distance(pos);
    vertexColor = Color * minecraft_sample_lightmap(Sampler2, UV2);
    texCoord0 = UV0;

    vertexColor.a *= vertexAlphaMultiplier;
}
