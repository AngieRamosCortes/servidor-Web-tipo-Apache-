@echo off
REM ===================================================================
REM Swift NetFramework - Development Build Script
REM Version: 2.1.0
REM Description: Build script for development and testing
REM ===================================================================

echo 🏗️  Swift NetFramework Build Script v2.1.0
echo.

echo 📦 Cleaning previous builds...
call mvn clean

echo 🔧 Compiling source code...
call mvn compile

echo 🧪 Running unit tests...
call mvn test

echo 📋 Generating documentation...
call mvn javadoc:javadoc

if %errorlevel% equ 0 (
    echo.
    echo ✅ Build completed successfully!
    echo 📊 Build artifacts are available in the target/ directory
    echo 📚 Documentation is available in target/site/apidocs/
    echo.
    echo 🚀 Run start-framework.bat to launch the framework
) else (
    echo.
    echo ❌ Build failed! Please check the error messages above.
)

echo.
pause
