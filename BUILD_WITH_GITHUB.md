# 电池监控APP - GitHub Actions自动构建

## 使用步骤

### 1. 创建GitHub仓库
1. 访问 https://github.com/new
2. 创建新仓库（例如：BatteryMonitor）
3. 不要初始化README（保持空仓库）

### 2. 上传项目到GitHub
```bash
cd C:\tmp\BatteryMonitor
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/你的用户名/BatteryMonitor.git
git push -u origin main
```

### 3. 自动构建APK
- 推送代码后，GitHub Actions会自动构建
- 进入仓库 → Actions → 查看构建进度
- 构建完成后，在 Actions → Artifacts 下载APK

### 4. 手动触发构建
- 进入仓库 → Actions → Build Android APK
- 点击 "Run workflow" → "Run workflow"

## 构建产物
- 路径：`app/build/outputs/apk/debug/app-debug.apk`
- 类型：Debug版本（可直接安装测试）

## 注意事项
- 首次构建约需5-10分钟
- APK有效期90天（GitHub限制）
- 如需Release版本，需要配置签名
