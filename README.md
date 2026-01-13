# Safe Device Info Demo

## 项目目的

该仓库提供一个 **Android 设备信息采集示例**：

- `SafeDeviceInfoManager` 聚合系统、硬件、电池、存储、网络等信息并输出 `JSONObject`。
- `MainActivity` 提供按钮触发采集，并把格式化后的 JSON 显示在界面与日志中。

项目定位为 **设备信息采集 SDK 的演示工程**，用于验证采集逻辑在真实设备上的表现。

## 已迁移为 Kotlin 可构建项目

本仓库已重构为 **可构建的 Kotlin Android 工程**，保持原有逻辑与组织结构一致：

- Demo 入口（`MainActivity`/`LogUtils`）使用 Kotlin，设备信息采集逻辑保留原 Java 实现以确保行为一致。
- 补齐 AndroidManifest 与布局文件，使 Demo 可直接运行。
- 补齐 `deviceinfo.info.*` 与 `utils` 等源码，使项目可直接编译运行。

## 目录结构

```
./app/src/main/java/com/dofun/safe/deviceinfo
  ├── SafeDeviceInfoManager.kt      # 采集入口：聚合多个信息模块为 JSON
  ├── info/                         # 设备信息模块（Kotlin 实现）
  └── utils/                        # 采集辅助工具（Kotlin 实现）
./app/src/main/java/com/dofun/safe/devicedemo
  ├── MainActivity.kt               # Demo UI：点击按钮触发采集并展示结果
  └── LogUtils.kt                   # 日志输出与 logcat 采集辅助
./app/src/main/res/layout
  └── activity_main.xml             # Demo 布局（ViewBinding）
```

## 构建与运行

在 Android Studio 中打开仓库根目录或直接使用 Gradle：

```bash
./gradlew :app:assembleDebug
```

> 提示：如果环境无法访问 Gradle 官方分发站点，请确保能访问 `services.gradle.org` 以下载 Gradle 发行包。

## 说明

`deviceinfo.info.*` 模块已迁移为 Kotlin，实现逻辑与原 Java 版本保持一致。
