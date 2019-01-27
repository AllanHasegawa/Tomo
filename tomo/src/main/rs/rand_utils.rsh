#ifndef __RAND_UTILS_RSH__
#define __RAND_UTILS_RSH__

// Computes a random float between [0,1) given a 2D coordinate.
static float rand2D(uint32_t x, uint32_t y) {
    // mix around the bits in x
    x = x * 3266489917 + 374761393;
    x = (x << 17) | (x >> 15);

    // mix around the bits in y and mix those into x
    x += y * 3266489917;

    // Give x a good stir
    x *= 668265263;
    x ^= x >> 15;
    x *= 2246822519;
    x ^= x >> 13;
    x *= 3266489917;
    x ^= x >> 16;

    // trim the result and scale it to a float in [0,1)
    return (x & 0x00ffffff) * (1.0f / 0x1000000);
}

// Based on: https://gist.github.com/Marc-B-Reynolds/0b5f1db5ad7a3e453596
static uint32_t randUint32(uint32_t *state) {
    uint32_t rndState = *state;
    rndState ^= (rndState << 13);
    rndState ^= (rndState >> 17);
    rndState ^= (rndState <<  5);
    rndState = rndState*1597334677;
    *state = rndState;
    return rndState;
}

// Computes a random float between [0, 1) given a certain state.
static float randFloat(uint32_t *state) {
    union {
        unsigned  int i;
        float f;
    } myrand;
    uint32_t rnd = randUint32(state);
    myrand.i = (rnd & 0x007fffff) | 0x40000000;
    return myrand.f-3.0;
}

#endif
