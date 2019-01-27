#pragma version(1)
#pragma rs_fp_relaxed
#pragma rs java_package_name(tomo.rs)

#include "rs_debug.rsh"
#include "color_utils.rsh"
#include "rand_utils.rsh"

ushort dulling;
float hDistance;
float sDistance;
float vDistance;

static float randomizeValue(float now, float low, float top, float randMax, uint32_t x, uint32_t y, uint32_t rUses) {
    uint32_t rBase = x * 10000 + y;
    float r = rand2D(rBase, rUses * 10);

    for (ushort i = 0; i < dulling; i++) {
        float tmp = rand2D(rBase, rUses * 10 + i + 1);
        if (tmp < r) {
            r = tmp;
        }
    }

    float flag;

    if (rand2D(rBase, rUses * 10 + dulling + 1) > 0.5) {
        flag = 1.0f;
    } else {
        flag = -1.0f;
    }

    float new = now + flag * r * randMax;

    if (new < low) {
        new = low;
    }

    if (new > top) {
        new = top;
    }

    return new;
}

uchar4 RS_KERNEL pixel(uchar4 in, uint32_t x, uint32_t y) {
    float4 f4 = rgbToHSV(in);

    f4.r = randomizeValue(f4.r, 0, 360, hDistance, x, y, 0);
    f4.g = randomizeValue(f4.g, 0, 1, sDistance, x, y, 1);
    f4.b = randomizeValue(f4.b, 0, 1, vDistance, x, y, 2);

    float4 out = hsvToRGB(f4);

    return rsPackColorTo8888(out.r, out.g, out.b, out.a);
}
