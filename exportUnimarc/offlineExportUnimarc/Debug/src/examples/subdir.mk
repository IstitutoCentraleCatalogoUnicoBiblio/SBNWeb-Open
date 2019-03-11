################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/examples/ReadMarcExample.cpp \
../src/examples/WriteMarcExample.cpp 

OBJS += \
./src/examples/ReadMarcExample.o \
./src/examples/WriteMarcExample.o 

CPP_DEPS += \
./src/examples/ReadMarcExample.d \
./src/examples/WriteMarcExample.d 


# Each subdirectory must supply rules for building sources it contributes
src/examples/%.o: ../src/examples/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -DNEW_TAGS -I"/home/export/workspace/cdt/offlineExportUnimarc/include" -I"/home/export/workspace/cdt/offlineExportUnimarc/src" -O0 -g3 -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o"$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


