cmake_minimum_required(VERSION 3.4.1)

set(LINK_DIR /src/main/jni/libfmjni.so)

add_library(
fmjni
SHARED
IMPORTED
)

SET_TARGET_PROPERTIES(
fmjni
PROPERTIES IMPORT_LOCATION
${LINK_DIR}
)

add_subdirectory(src/main/jni/SerialPort)
add_subdirectory(src/main/jni/Sensors)
add_subdirectory(src/main/jni/GPIO)
