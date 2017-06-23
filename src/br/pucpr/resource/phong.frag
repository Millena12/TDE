#version 330

uniform sampler2D uTexture;

uniform vec3 uLightDir;

uniform vec3 uDiffuseLight;
uniform vec3 uAmbientLight;

uniform vec3 uSpecularLight;
uniform float uSpecularPower;

uniform vec3 uDiffuseMaterial;
uniform vec3 uSpecularMaterial;
uniform vec3 uAmbientMaterial;


in vec3 vViewPath;
in vec3 vNormal;

in float vertDist;
in vec2 vTexCoord;

out vec4 outColor;

void main() {
    vec3 L = normalize(uLightDir);
    vec3 N = normalize(vNormal);

    // Calculo do componente Ambiente
    vec3 ambient = uAmbientLight * uAmbientMaterial;

    float diffuseIntensity = max(dot(N, -L), 0.0);

    // vertDist distancia da luz ate o obj:  - vertDist * 0.2

    vec3 diffuse = ((diffuseIntensity * uDiffuseLight)/* - vertDist * 0.2 */) * uDiffuseMaterial;

    // Calculo do componente Specular
    float specularIntensity = 0.0;
    if(uSpecularPower > 0.0) {
        vec3 V = normalize (vViewPath);
        vec3 R = reflect(L, N);

        specularIntensity = pow(max(dot(R, V), 0.0), uSpecularPower);
    }
    vec3 specular = specularIntensity * uSpecularLight * uSpecularMaterial;

     vec4 texel = texture(uTexture, vTexCoord);

    vec3 color = clamp(texel.rgb*(ambient + diffuse) + specular, 0.0, 1.0);
    outColor = vec4(color, texel.a);
}