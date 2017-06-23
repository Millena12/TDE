#version 330

uniform mat4 uProjection;
uniform mat4 uView;
uniform mat4 uWorld;

uniform vec3 uCameraPosition;

in vec3 aPosition;
in vec3 aNormal;

uniform float uScale;

out vec3 vNormal;
out vec3 vViewPath;

out float vertDist;

out vec2 vTexCoord;

in vec2 aTexCoord;

void main() {
    vNormal = (uWorld * vec4(aNormal, 0.0)).xyz;

    vec4 worldPos = uWorld * vec4(aPosition.x,aPosition.y*uScale,aPosition.z, 1.0f);
    gl_Position =  uProjection * uView * worldPos;

    vNormal = (uWorld * vec4(aNormal, 0.0)).xyz;
    vViewPath = uCameraPosition - worldPos.xyz;

    vTexCoord = aTexCoord;

    vertDist = distance(uCameraPosition, worldPos.xyz);
}