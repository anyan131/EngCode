add_library(
ledhelper
SHARED
LedHelper.cpp
)

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

target_link_libraries( # Specifies the target library.
                       ledhelper

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )