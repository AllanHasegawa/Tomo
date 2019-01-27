#pragma version(1)
#pragma rs_fp_relaxed
#pragma rs java_package_name(tomo.rs)

#include "color_utils.rsh"
#include "math_utils.rsh"

float lowValue;
float highValue;
float saturationMultiplier;
float saturationLowValue;
float saturationHighValue;

uchar4 RS_KERNEL pixel(uchar4 in, uint32_t x, uint32_t y) {
    float4 hsv = rgbToHSV(in);

    hsv.y = clamp(hsv.y * saturationMultiplier, saturationLowValue, saturationHighValue);
    hsv.z = clamp(hsv.z, lowValue, highValue);

    float4 out = hsvToRGB(hsv);

    return rsPackColorTo8888(out.r, out.g, out.b, out.a);
}
