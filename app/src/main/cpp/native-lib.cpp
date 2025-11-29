#include <jni.h>
#include <android/log.h>
#include <string>
#include <vector>
#include <mutex>

// IMPORT YOUR MODEL
#include "edge-impulse-sdk/classifier/ei_run_classifier.h"
#include "edge-impulse-sdk/dsp/image/image.hpp"
#include "tflite-model/tflite_learn_840166_5.h"

// This buffer will hold the converted RGB image data
static std::vector<uint8_t> rgb_buffer_global;
// Mutex to protect access to the global buffer
static std::mutex mtx;

// Callback to get the RGB data for the classifier
static int get_rgb_data(size_t offset, size_t length, float *out_ptr) {
    std::lock_guard<std::mutex> lock(mtx);
    if (rgb_buffer_global.empty()) {
        return -1;
    }

    for (size_t i = 0; i < length; i++) {
        size_t index = (offset + i) * 3;
        uint8_t r = rgb_buffer_global[index];
        uint8_t g = rgb_buffer_global[index + 1];
        uint8_t b = rgb_buffer_global[index + 2];
        out_ptr[i] = (r << 16) | (g << 8) | b;
    }
    return 0;
}

// Clamp utility
int clamp(int val, int min, int max) {
    if (val < min) return min;
    if (val > max) return max;
    return val;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_test_1camera_MainActivity_classify(
        JNIEnv* env,
        jobject, /* this */
        jobject y_buffer, jobject u_buffer, jobject v_buffer,
        jint y_pixel_stride, jint u_pixel_stride, jint v_pixel_stride,
        jint y_row_stride, jint u_row_stride, jint v_row_stride,
        jint src_width, jint src_height) {

    uint8_t* y_data = (uint8_t*)env->GetDirectBufferAddress(y_buffer);
    uint8_t* u_data = (uint8_t*)env->GetDirectBufferAddress(u_buffer);
    uint8_t* v_data = (uint8_t*)env->GetDirectBufferAddress(v_buffer);

    if (y_data == nullptr || u_data == nullptr || v_data == nullptr) {
        return env->NewStringUTF("Error: Could not get buffer addresses.");
    }

    const int dest_width = EI_CLASSIFIER_INPUT_WIDTH;
    const int dest_height = EI_CLASSIFIER_INPUT_HEIGHT;
    std::vector<uint8_t> rgb_buffer_local(dest_width * dest_height * 3);

    // This is a basic nearest-neighbor scaling and YUV to RGB conversion.
    for (int j = 0; j < dest_height; j++) {
        for (int i = 0; i < dest_width; i++) {
            // Map destination pixel to source pixel
            int src_x = i * src_width / dest_width;
            int src_y = j * src_height / dest_height;

            // Y value
            int y_val = y_data[src_y * y_row_stride + src_x * y_pixel_stride];

            // UV values (for YUV420, UV plane is subsampled by 2)
            int uv_x = src_x / 2;
            int uv_y = src_y / 2;
            int u_ix = uv_y * u_row_stride + uv_x * u_pixel_stride;
            int v_ix = uv_y * v_row_stride + uv_x * v_pixel_stride;
            
            int u_val = u_data[u_ix];
            int v_val = v_data[v_ix];

            // YCbCr to RGB conversion
            int c = y_val - 16;
            int d = u_val - 128;
            int e = v_val - 128;

            int r = clamp((298 * c + 409 * e + 128) >> 8, 0, 255);
            int g = clamp((298 * c - 100 * d - 208 * e + 128) >> 8, 0, 255);
            int b = clamp((298 * c + 516 * d + 128) >> 8, 0, 255);

            int dest_index = (j * dest_width + i) * 3;
            rgb_buffer_local[dest_index] = static_cast<uint8_t>(r);
            rgb_buffer_local[dest_index + 1] = static_cast<uint8_t>(g);
            rgb_buffer_local[dest_index + 2] = static_cast<uint8_t>(b);
        }
    }

    {
        std::lock_guard<std::mutex> lock(mtx);
        rgb_buffer_global = std::move(rgb_buffer_local);
    }

    signal_t signal;
    signal.total_length = EI_CLASSIFIER_INPUT_WIDTH * EI_CLASSIFIER_INPUT_HEIGHT;
    signal.get_data = &get_rgb_data;

    ei_impulse_result_t result = { 0 };
    EI_IMPULSE_ERROR res = run_classifier(&signal, &result, false);

    {
        std::lock_guard<std::mutex> lock(mtx);
        rgb_buffer_global.clear();
    }
    
    if (res != EI_IMPULSE_OK) {
        std::string error_msg = "Inference Error: " + std::to_string(res);
        return env->NewStringUTF(error_msg.c_str());
    }

    float max_score = 0.0f;
    std::string best_label = "Uncertain";

    for (size_t i = 0; i < EI_CLASSIFIER_LABEL_COUNT; i++) {
        if (result.classification[i].value > max_score) {
            max_score = result.classification[i].value;
            best_label = result.classification[i].label;
        }
    }

    std::string output = best_label + ":" + std::to_string(max_score);
    return env->NewStringUTF(output.c_str());
}