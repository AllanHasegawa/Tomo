#pragma version(1)
#pragma rs_fp_relaxed
#pragma rs java_package_name(tomo.rs)

#include "rs_debug.rsh"
#include "rand_utils.rsh"

#define ALPHA_0 0.01f
#define ALPHA_1 0.03f
#define ALPHA_2 0.07f

uint32_t randomSeed = 1;

uchar4 RS_KERNEL pixel(uchar4 in, uint32_t x, uint32_t y) {
    randomSeed += x * 27644437 + y * 16769023;
    uint8_t grayAlphaValueIdx = (uint8_t)(randUint32(&randomSeed) % 3);

    float grayAlphaValue = ALPHA_0;
    if (grayAlphaValueIdx == 0) {
        return in;
    } else if (grayAlphaValueIdx == 1) {
        grayAlphaValue = ALPHA_1;
    } else {
        grayAlphaValue = ALPHA_2;
    }

    int grayValue = (int)(randUint32(&randomSeed) % 255);
    grayValue = (int)((float)(grayValue) * grayAlphaValue);

    uchar4 out;
    out.r = (uchar)clamp((int)in.r - grayValue, 0, 255);
    out.g = (uchar)clamp((int)in.g - grayValue, 0, 255);
    out.b = (uchar)clamp((int)in.b - grayValue, 0, 255);
    out.a = in.a;

    return out;
}
