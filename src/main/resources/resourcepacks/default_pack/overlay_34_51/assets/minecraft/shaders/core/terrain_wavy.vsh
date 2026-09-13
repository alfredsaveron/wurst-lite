#version 150

#moj_import <light.glsl>
#moj_import <fog.glsl>

#moj_import <vt_wavy_core/vert.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler0;
uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

// this is so we dont have to duplicate the whole shader just for 1.21.1
#ifdef VT_WAVY_CORE_MODEL_OFFSET
uniform vec3 ModelOffset;
#endif
#ifndef VT_WAVY_CORE_MODEL_OFFSET
uniform vec3 ChunkOffset;
#endif

uniform int FogShape;
uniform float GameTime;

out float vertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;

void main() {
    vec3 offset = 
    #ifdef VT_WAVY_CORE_MODEL_OFFSET
        ModelOffset;
    #endif
    #ifndef VT_WAVY_CORE_MODEL_OFFSET
        ChunkOffset;
    #endif

    vec3 pos = Position + offset;

    float vertexAlphaMultiplier = 1.0;
    gl_Position = ProjMat * ModelViewMat * vec4(applyVertexOffsets(Position, offset, GameTime, sampleAnimationType(Sampler0, UV0), vertexAlphaMultiplier), 1.0);

    vertexDistance = fog_distance(pos, FogShape);
    vertexColor = Color * minecraft_sample_lightmap(Sampler2, UV2);
    texCoord0 = UV0;

    vertexColor.a *= vertexAlphaMultiplier;
}
