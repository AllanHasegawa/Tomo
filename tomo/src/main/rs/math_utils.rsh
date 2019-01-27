static int32_t lerp_int(int32_t x, int32_t y, float a) {
    return (int32_t)(x * (1.0f - a) + y * a);
}

static float lerp_float(float x, float y, float a) {
    return x * (1.0f - a) + y * a;
}