#ifndef PI
#define PI 3.14159265359
#endif

// returns the animation type from marker pixels in the corners of textures
int sampleAnimationType(sampler2D tex, vec2 uv) {
    #ifdef WAVY_CORE__HAS_LINEAR_SAMPLER
    vec4 rawCol = texelFetch(tex, ivec2(uv * textureSize(tex, 0)), 0);
    #else
    vec4 rawCol = texture(tex, uv);
    #endif
    ivec4 col = ivec4(rawCol * 255);

    int animType = 0;
    switch(col.a) {
        // Alpha 1 or 253, generic wavy animation
        case 1:
        case 253:
            animType = 1;
        break;
        // Alpha 2, higher intensity generic wavy animation. For edges of multipart blocks
        case 2:
            animType = 2;
        break;
        // Alpha 3 or 251, lower intensity generic wavy animation. For 'carpet' flowers like pink petals
        case 3:
        case 521:
            animType = 3;
        break;
        // Alpha 4, special animation for vines
        case 4:
            animType = 4;
        break;
        // Alpha 5 or 252, wavy leaves animation
        case 5:
        case 252:
            animType = 5;
        break;
        // Alpha 6, higher intensity wavy leaves animation
        case 6:
            animType = 6;
        break;
        // Alpha 7, special animation for lily pads
        case 7:
            animType = 7;
        break;
        // Alpha 8, special animation for spore blossoms
        case 8:
            animType = 8;
        break;
        // Alpha 18, special animation for water
        case 18:
            animType = 18;
        break;
        // Alpha 19, special animation for clear water
        case 19:
            animType = 19;
        break;
    }

    return animType;
}

// applies vertex offsets based on an animation type, if 0 then the original positions are returned
// vertPos - Position vertex attribute
// modelOffset - ModelOffset or ChunkOffset uniform
// time - GameTime uniform
// animType - result of sampleAnimationType function
// out vertexAlphaMultiplier - used to adjust the alpha of water so it looks correct in game
vec3 applyVertexOffsets(vec3 vertPos, vec3 modelOffset, float time, int animType, out float vertexAlphaMultiplier) {
    vertexAlphaMultiplier = 1.0;
    if(animType == 0) {
        return vertPos + modelOffset;
    }

    float xOffset = 0.0;
    float yOffset = 0.0;
    float zOffset = 0.0;

    vec3 repeatPos = vertPos / 2.0 * PI;
    float animTime = time * 4000.0;

    switch(animType) {
        // Generic waving animation
        case 1:
        case 2:
        case 3:
            xOffset = sin(repeatPos.x + animTime) / 32.0;
            zOffset = cos(repeatPos.z + repeatPos.y + animTime) / 32.0;
            if(animType == 2) {
                // higher intensity
                xOffset *= 2;
                zOffset *= 2;
            }
            if(animType == 3) {
                // lower intensity
                xOffset /= 2;
                zOffset /= 2;
            }
        break;

        // Vines animation
        case 4:
            xOffset = sin(repeatPos.x + (repeatPos.y / 2.0) + animTime) / 32.0;
            zOffset = cos(repeatPos.z + (repeatPos.y / 2.0) + animTime) / 32.0;
        break;

        // Leaves animation
        case 5:
        case 6:
            xOffset = sin(repeatPos.x + (repeatPos.y / 2.0) + animTime) / 32.0;
            yOffset = cos(repeatPos.z + (repeatPos.y / 2.0) + animTime) / 32.0;
            if(animType == 6) {
                // higher intensity
                xOffset *= 2;
                zOffset *= 2;
            }
        break;

        // Spore blossom animation
        case 8:
            xOffset = sin(repeatPos.x + repeatPos.y + animTime) / 50.0;
            zOffset = cos(repeatPos.z + repeatPos.y + animTime) / 50.0;
            yOffset = sin(repeatPos.y + (animTime / 3.0)) / 30.0;
        break;

        // Water / lily pad animation
        case 7:
        case 18:
        case 19:
            if(animType != 7) {
                // adjust the alpha value for water
                vertexAlphaMultiplier = animType == 18 ? 10.0 : 4.7368;
            }
            float wavePeriod = distance(vertPos.xz, vec2(8.0, 8.0)) * 2.0;
            yOffset = sin(wavePeriod + animTime) * 0.04;
        break;
    }

    return vertPos + modelOffset + vec3(xOffset, yOffset, zOffset);
}