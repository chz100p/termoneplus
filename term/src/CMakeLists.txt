##
# Copyright (C) 2019 Roumen Petrov.  All rights reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
##

cmake_minimum_required(VERSION 3.0)
project(term-c)

add_executable(
        libexec-t1plus.so
        main/jni/appcmd.c
        main/jni/socket.c
        main/jni/atomicio.c
)
target_compile_definitions(
        libexec-t1plus.so
        PRIVATE
        PACKAGE_NAME="${APPLICATION_ID}"
)
target_compile_options(
        libexec-t1plus.so
        PRIVATE
        -fPIC
)
target_link_libraries(
        libexec-t1plus.so
        -pie
)

# used if ABI < 16
add_executable(
        libexeo-t1plus.so
        main/jni/appcmd.c
        main/jni/socket.c
        main/jni/atomicio.c
)
target_compile_definitions(
        libexeo-t1plus.so
        PRIVATE
        PACKAGE_NAME="${APPLICATION_ID}"
)
target_compile_options(
        libexeo-t1plus.so
        PRIVATE
)
target_link_libraries(
        libexeo-t1plus.so
        -nopie
)
