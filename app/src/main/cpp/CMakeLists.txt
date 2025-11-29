# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.

# Sets the minimum CMake version required for this project.
cmake_minimum_required(VERSION 3.22.1)

project("synesthesia-ai")

set(CMAKE_VERBOSE_MAKEFILE TRUE)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -v -stdlib=libc++")

include(edge-impulse-sdk/cmake/utils.cmake)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

set(EI_SDK_FOLDER edge-impulse-sdk)

# Set TensorFlow Lite library path and run the download script
set(TFLITE_LIB_DIR ${CMAKE_SOURCE_DIR}/tflite/lib/android_arm64)

# --- ROBUST TFLITE DOWNLOAD --- #
add_custom_command(
    OUTPUT ${TFLITE_LIB_DIR}/libtensorflow-lite.a
    COMMAND ${CMAKE_COMMAND} -E env bash ${CMAKE_SOURCE_DIR}/tflite/download_tflite_libs.sh
    WORKING_DIRECTORY ${CMAKE_SOURCE_DIR}/tflite
    COMMENT "Downloading and setting up TFLite library..."
    VERBATIM
)

# Create a custom target to run the download command
add_custom_target(download_tflite ALL DEPENDS ${TFLITE_LIB_DIR}/libtensorflow-lite.a)

add_definitions(-DEI_CLASSIFIER_ENABLE_DETECTION_POSTPROCESS_OP=1
        -DEI_CLASSIFIER_USE_FULL_TFLITE=1
        -DEI_CLASSIFIER_TFLITE_ENABLE_NNAPI=1
        -DNDEBUG
)

link_directories(${TFLITE_LIB_DIR})

add_library(${CMAKE_PROJECT_NAME} SHARED
        synesthesia-ai.cpp)

# Ensure the download completes before the library is built
add_dependencies(${CMAKE_PROJECT_NAME} download_tflite)

target_include_directories(${CMAKE_PROJECT_NAME} PRIVATE
        .
)

# Collect C source files (CMSIS-DSP)
file(GLOB EI_C_SOURCE_FILES
        "${CMAKE_SOURCE_DIR}/edge-impulse-sdk/CMSIS/DSP/Source/TransformFunctions/*.c"
        "${CMAKE_SOURCE_DIR}/edge-impulse-sdk/CMSIS/DSP/Source/CommonTables/*.c"
        "${CMAKE_SOURCE_DIR}/edge-impulse-sdk/CMSIS/DSP/Source/BasicMathFunctions/*.c"
        "${CMAKE_SOURCE_DIR}/edge-impulse-sdk/CMSIS/DSP/Source/ComplexMathFunctions/*.c"
        "${CMAKE_SOURCE_DIR}/edge-impulse-sdk/CMSIS/DSP/Source/FastMathFunctions/*.c"
        "${CMAKE_SOURCE_DIR}/edge-impulse-sdk/CMSIS/DSP/Source/SupportFunctions/*.c"
        "${CMAKE_SOURCE_DIR}/edge-impulse-sdk/CMSIS/DSP/Source/MatrixFunctions/*.c"
        "${CMAKE_SOURCE_DIR}/edge-impulse-sdk/CMSIS/DSP/Source/StatisticsFunctions/*.c"
)

# Collect C++ source files (Edge Impulse SDK & Model)
file(GLOB EI_CPP_SOURCE_FILES
        "${CMAKE_SOURCE_DIR}/tflite-model/*.cpp"
        "${CMAKE_SOURCE_DIR}/edge-impulse-sdk/dsp/kissfft/*.cpp"
        "${CMAKE_SOURCE_DIR}/edge-impulse-sdk/dsp/image/*.cpp"
        "${CMAKE_SOURCE_DIR}/edge-impulse-sdk/dsp/dct/*.cpp"
        "${CMAKE_SOURCE_DIR}/edge-impulse-sdk/dsp/memory.cpp"
        "${CMAKE_SOURCE_DIR}/edge-impulse-sdk/porting/posix/*.c*"
        "${CMAKE_SOURCE_DIR}/edge-impulse-sdk/porting/mingw32/*.c*"
)

LIST(APPEND EI_C_SOURCE_FILES "${EI_SDK_FOLDER}/tensorflow/lite/c/common.c")

target_sources(${CMAKE_PROJECT_NAME} PRIVATE ${EI_C_SOURCE_FILES} ${EI_CPP_SOURCE_FILES})

# Link libraries
target_link_libraries(${CMAKE_PROJECT_NAME}
        tensorflow-lite
        android
        log
        jnigraphics
        farmhash
        fft2d_fftsg
        fft2d_fftsg2d
        ruy
        XNNPACK
        cpuinfo
        pthreadpool)