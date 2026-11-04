@echo off
REM ========================================
REM Android APK 本地构建脚本
REM 需要先安装 Android SDK 和 JDK 17
REM ========================================

echo ========================================
echo 电池监控APP - APK构建脚本
echo ========================================
echo.

REM 检查Java
echo [1/5] 检查Java环境...
java -version >nul 2>&1
if errorlevel 1 (
    echo [错误] 未找到Java，请先安装JDK 17
    echo 下载地址：https://adoptium.net/
    pause
    exit /b 1
)
echo [√] Java环境正常

REM 检查Android SDK
echo [2/5] 检查Android SDK...
if "%ANDROID_HOME%"=="" (
    echo [错误] 未设置ANDROID_HOME环境变量
    echo 请设置ANDROID_HOME指向Android SDK路径
    pause
    exit /b 1
)
echo [√] Android SDK路径: %ANDROID_HOME%

REM 检查Gradle Wrapper
echo [3/5] 检查Gradle Wrapper...
if not exist "gradlew.bat" (
    echo [错误] 未找到gradlew.bat
    pause
    exit /b 1
)
echo [√] Gradle Wrapper存在

REM 构建APK
echo [4/5] 开始构建APK...
echo 这可能需要几分钟时间...
echo.
call gradlew.bat assembleDebug

if errorlevel 1 (
    echo.
    echo [错误] 构建失败，请检查错误信息
    pause
    exit /b 1
)

REM 查找APK文件
echo [5/5] 构建完成！
echo.
echo APK文件位置：
dir /b app\build\outputs\apk\debug\*.apk 2>nul
echo.
echo 按任意键退出...
pause >nul
