#ifndef __COLOR_UTILS_RSH__
#define __COLOR_UTILS_RSH__

static float4 rgbToHSV(uchar4 in) {
    float4 f4 = rsUnpackColor8888(in);

    float cMax = fmax(fmax(f4.r, f4.g), f4.b);
    float cMin = fmin(fmin(f4.r, f4.g), f4.b);
    float delta = cMax - cMin;

    float h = 0;

    if (cMax == f4.r) {
        h = 60.0 * fmod((f4.g - f4.b)/delta, 6);
    } else if (cMax == f4.g) {
        h = 60.0 * ((f4.b - f4.r)/delta + 2.0);
    } else {
        h = 60.0 * ((f4.r - f4.g)/delta + 4.0);
    }

    float s = 0;

    if (cMax > 1e-6) {
        s = delta/cMax;
    }

    float v = cMax;

    f4.r = clamp(h, 0.f, 359.f);
    f4.g = clamp(s, 0.f, 1.f);
    f4.b = clamp(v, 0.f, 1.f);

    return f4;
}

static float4 hsvToRGB(float4 in) {
    float c = in.b * in.g;
    float x = c * (1.0 - fabs(fmod(in.r / 60.f, 2.f) - 1.f));
    float m = in.b - c;

    float r = 0.0;
    float g = 0.0;
    float b = 0.0;

    if (in.r < 60) {
        r = c;
        g = x;
        b = 0;
    } else if (in.r < 120) {
        r = x;
        g = c;
        b = 0;
    } else if (in.r < 180) {
        r = 0;
        g = c;
        b = x;
    } else if (in.r < 240) {
        r = 0;
        g = x;
        b = c;
    } else if (in.r < 300) {
        r = x;
        g = 0;
        b = c;
    } else {
        r = c;
        g = 0;
        b = x;
    }

    r += m;
    g += m;
    b += m;

    r = clamp(r, 0.f, 1.f);
    g = clamp(g, 0.f, 1.f);
    b = clamp(b, 0.f, 1.f);

    float4 out;
    out.r = r;
    out.g = g;
    out.b = b;
    out.a = in.a;

    return out;
}

#endif
