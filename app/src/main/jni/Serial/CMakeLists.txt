ADD_LIBRARY(Serial SHARED Serial.cpp)
find_library(
        log-lib
        log
)

target_link_libraries(
        Serial
        ${log-lib}
)