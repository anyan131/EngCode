ADD_LIBRARY(com_zte_engineer_jni SHARED
com_zte_engineer_AlspsCali.cpp;com_zte_engineer_LcdPowerControl.cpp;
gSensorCaliJni.cpp
)

ADD_LIBRARY(nativehelper SHARED IMPORTED)
set_target_properties(com_zte_engineer_jni PROPERTIES IMPORT_LOCATION
${CMAKE_SOURCE_DIR}/SOs/libnativehelper.so)

ADD_LIBRARY(android_runtime SHARED IMPORTED)
set_target_properties(com_zte_engineer_jni PROPERTIES IMPORT_LOCATION
${CMAKE_SOURCE_DIR}/SOs/libandroid_runtime.so)

ADD_LIBRARY(utils SHARED IMPORTED)
set_target_properties(com_zte_engineer_jni PROPERTIES IMPORT_LOCATION
${CMAKE_SOURCE_DIR}/SOs/libutils.so)

ADD_LIBRARY(media SHARED IMPORTED)
set_target_properties(com_zte_engineer_jni PROPERTIES IMPORT_LOCATION
${CMAKE_SOURCE_DIR}/SOs/libmedia.so)

ADD_LIBRARY(log SHARED IMPORTED)
set_target_properties(com_zte_engineer_jni PROPERTIES IMPORT_LOCATION
${CMAKE_SOURCE_DIR}/SOs/liblog.so)

ADD_LIBRARY(hwm SHARED IMPORTED)
set_target_properties(com_zte_engineer_jni PROPERTIES IMPORT_LOCATION
${CMAKE_SOURCE_DIR}/SOs/libhwm.so)

ADD_LIBRARY(nvram SHARED IMPORTED)
set_target_properties(com_zte_engineer_jni PROPERTIES IMPORT_LOCATION
${CMAKE_SOURCE_DIR}/SOs/libnvram.so)

ADD_LIBRARY(file_op SHARED IMPORTED)
set_target_properties(com_zte_engineer_jni PROPERTIES IMPORT_LOCATION
${CMAKE_SOURCE_DIR}/SOs/libfile_op.so)





