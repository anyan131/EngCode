ADD_LIBRARY(SerialPort SHARED AlexTaoSerialPort.cpp)
find_library(
    log-lib
    log
)

target_link_libraries(
SerialPort
${log-lib}
)