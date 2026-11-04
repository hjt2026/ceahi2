#!/bin/bash
# ========================================
# Android APK 本地构建脚本 (Linux/Mac)
# 需要先安装 Android SDK 和 JDK 17
# ========================================

echo "========================================"
echo "电池监控APP - APK构建脚本"
echo "========================================"
echo ""

# 检查Java
echo "[1/5] 检查Java环境..."
if ! command -v java &> /dev/null; then
    echo "[错误] 未找到Java，请先安装JDK 17"
    echo "Ubuntu/Debian: sudo apt install openjdk-17-jdk"
    echo "macOS: brew install openjdk@17"
    exit 1
fi
echo "[√] Java环境正常: $(java -version 2>&1 | head -1)"

# 检查Android SDK
echo "[2/5] 检查Android SDK..."
if [ -z "$ANDROID_HOME" ]; then
    echo "[错误] 未设置ANDROID_HOME环境变量"
    echo "请设置: export ANDROID_HOME=~/Android/Sdk"
    exit 1
fi
echo "[√] Android SDK路径: $ANDROID_HOME"

# 检查Gradle Wrapper
echo "[3/5] 检查Gradle Wrapper..."
if [ ! -f "./gradlew" ]; then
    echo "[错误] 未找到gradlew"
    exit 1
fi
chmod +x gradlew
echo "[√] Gradle Wrapper存在"

# 构建APK
echo "[4/5] 开始构建APK..."
echo "这可能需要几分钟时间..."
echo ""
./gradlew assembleDebug

if [ $? -ne 0 ]; then
    echo ""
    echo "[错误] 构建失败，请检查错误信息"
    exit 1
fi

# 查找APK文件
echo "[5/5] 构建完成！"
echo ""
echo "APK文件位置："
ls -lh app/build/outputs/apk/debug/*.apk 2>/dev/null
echo ""
echo "按回车键退出..."
read
