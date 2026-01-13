# Safe Device Info Demo

## 项目目的

该仓库包含一个 **Android 设备信息采集示例**：

- `SafeDeviceInfoManager` 负责聚合系统、硬件、电池、存储、网络等信息并输出为 `JSONObject`。
- `MainActivity` 提供一个简单的按钮触发采集，并将格式化后的 JSON 输出到界面与日志。

整体上它更像是一个**设备信息采集 SDK 的演示工程**，用于验证采集逻辑在真实设备上的表现，而不是完整的可发布应用。

## 目录结构

```
./dofun/safe/deviceinfo
  └── SafeDeviceInfoManager.java   # 采集入口：聚合多个信息模块为 JSON
./dofun/safe/devicedemo
  ├── MainActivity.java            # Demo UI：点击按钮触发采集并展示结果
  └── LogUtils.java                # 日志输出与 logcat 采集辅助
```

> 备注：仓库中仅包含部分源码，`deviceinfo.info.*` 以及部分资源/构建文件未在此仓库出现，因此无法直接构建为完整 APK。

## 运行方式（参考）

该工程是 Android 项目的一部分，通常需要以下条件才能运行：

1. 在 Android Studio 中创建或导入 Android 工程结构。
2. 将本仓库中的源码放入对应的 `app` 或 `library` 模块内。
3. 确保依赖包含：
   - AndroidX AppCompat、Core
   - Gson
4. 补齐缺失的 `deviceinfo.info.*`、布局资源 (`ActivityMainBinding` 对应布局) 等内容。

## 是否需要重构为 Kotlin/Java 项目？

- **当前源码已是 Java 实现**，且逻辑简单直接，短期内**没有必须迁移到 Kotlin 的需求**。
- 若目标是长期维护、与现代 Android 生态对齐，可以考虑逐步迁移到 Kotlin；但现阶段该仓库更像 demo/片段代码，投入全面迁移的收益较低。

**结论：不需要重构为 Kotlin 项目，保持 Java 即可。**

## 后续建议

- 若希望完整构建：补齐 Gradle 构建脚本、AndroidManifest、布局与缺失的 `deviceinfo.info` 模块。
- 若希望发布为 SDK：可将 `deviceinfo` 作为独立 library module，并提供更明确的权限声明与 API 文档。
