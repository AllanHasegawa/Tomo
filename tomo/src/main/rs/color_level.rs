#pragma version(1)
#pragma rs_fp_relaxed
#pragma rs java_package_name(tomo.rs)

#include "rs_debug.rsh"
#include "color_utils.rsh"
#include "math_utils.rsh"

float shadowValue = 0.f;
float highlightValue = 1.f;

uchar4 RS_KERNEL pixel(uchar4 in, uint32_t x, uint32_t y) {
    float4 hsv = rgbToHSV(in);

    hsv.z = lerp_float(shadowValue, highlightValue, hsv.z);

    float4 out = hsvToRGB(hsv);

    return rsPackColorTo8888(out.r, out.g, out.b, out.a);
}
