package com.dofun.safe.deviceinfo.info

import android.content.Context
import android.graphics.Rect
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.StreamConfigurationMap
import android.util.Range
import android.util.SizeF
import androidx.core.util.Pair
import com.dofun.safe.deviceinfo.R
import com.dofun.safe.deviceinfo.utils.CommandUtils
import com.dofun.safe.deviceinfo.utils.Constants
import com.dofun.safe.deviceinfo.utils.DecimalUtils
import org.json.JSONArray

object CameraInfo {
    @JvmStatic
    fun getCameraInfoJson(context: Context): JSONArray? {
        return getCameraInfo(context)
    }

    @JvmStatic
    fun getCameraInfo(context: Context): JSONArray? {
        val jsonArray = JSONArray()
        val manager = try {
            context.getSystemService("camera") as CameraManager
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        if (manager == null) {
            return null
        }
        val cameraIdList = manager.cameraIdList
        var index = 0
        while (index < cameraIdList.size) {
            val cameraId = cameraIdList[index]
            val list = ArrayList<Pair<String, String>>()
            val characteristics = manager.getCameraCharacteristics(cameraId)
            val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
            list.add(Pair(context.getString(R.string.menu_camera), getFacing(facing)))
            val activeArraySize = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE)
            if (activeArraySize != null) {
                val width = activeArraySize.right - activeArraySize.left
                val height = activeArraySize.bottom - activeArraySize.top
                val round = DecimalUtils.round((width * height) / 1000000.0, 1)
                list.add(
                    Pair(
                        context.getString(R.string.camera_resolution),
                        "$round MP (${width}x${height})"
                    )
                )
            }
            val lensInfoAvailableApertures = characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES)
            if (lensInfoAvailableApertures != null && lensInfoAvailableApertures.isNotEmpty()) {
                val sb = StringBuffer()
                for (f in lensInfoAvailableApertures) {
                    sb.append("f/").append(f).append(" ")
                }
                list.add(Pair(context.getString(R.string.camera_aperture), sb.toString().trim()))
            }
            val lensInfoAvailableFocalLengths = characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)
            if (lensInfoAvailableFocalLengths != null && lensInfoAvailableFocalLengths.isNotEmpty()) {
                val sb2 = StringBuffer()
                for (f in lensInfoAvailableFocalLengths) {
                    sb2.append(f).append(" mm ")
                }
                list.add(Pair(context.getString(R.string.camera_focal_length), sb2.toString().trim()))
            }
            val afAvailableModes = characteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES)
            if (afAvailableModes != null && afAvailableModes.isNotEmpty()) {
                val sb3 = StringBuffer()
                for (mode in afAvailableModes) {
                    sb3.append(getAfAvailableModes(mode)).append(",")
                }
                list.add(Pair(context.getString(R.string.camera_af_modes), sb3.deleteCharAt(sb3.length - 1).toString()))
            }
            val physicalSize = characteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE)
            if (physicalSize != null) {
                list.add(Pair(context.getString(R.string.camera_size), "${physicalSize.width}x${physicalSize.height}"))
            }
            if (physicalSize != null && activeArraySize != null) {
                val value = physicalSize.width * physicalSize.height
                val width2 = (activeArraySize.right - activeArraySize.left).toDouble()
                val round2 = DecimalUtils.round(
                    Math.sqrt(((value * 1000.0) / width2) * 1000.0 / (activeArraySize.bottom - activeArraySize.top)),
                    2
                )
                list.add(Pair(context.getString(R.string.camera_pixel_size), "~${round2}µm"))
            }
            val focalLengths = characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)
            if (focalLengths != null && focalLengths.isNotEmpty()) {
                val f3 = focalLengths[0]
                val sizeF = characteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE)
                if (sizeF != null) {
                    val width3 = sizeF.width
                    if (width3 > 0.0f) {
                        val angle = DecimalUtils.round(Math.toDegrees(Math.atan((width3 * 0.5) / f3)) * 2.0, 1)
                        list.add(Pair(context.getString(R.string.camera_view_angle), "$angle°"))
                    }
                }
            }
            val map: StreamConfigurationMap? = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            if (map != null) {
                val formats = map.outputFormats
                val sb4 = StringBuffer()
                for (format in formats) {
                    sb4.append(getFormat(format)).append(",")
                }
                list.add(Pair(context.getString(R.string.camera_formats), sb4.deleteCharAt(sb4.length - 1).toString()))
            }
            val sensorInfoSensitivityRange = characteristics.get(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE)
            if (sensorInfoSensitivityRange != null) {
                list.add(
                    Pair(
                        context.getString(R.string.camera_iso),
                        "${sensorInfoSensitivityRange.lower}-${sensorInfoSensitivityRange.upper}"
                    )
                )
            }
            val sensorInfoColorFilterArrangement =
                characteristics.get(CameraCharacteristics.SENSOR_INFO_COLOR_FILTER_ARRANGEMENT)
            if (sensorInfoColorFilterArrangement != null) {
                list.add(
                    Pair(
                        context.getString(R.string.camera_color_filter),
                        getSensorInfoColorFilterArrangement(sensorInfoColorFilterArrangement)
                    )
                )
            }
            val sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)
            if (sensorOrientation != null) {
                list.add(Pair(context.getString(R.string.camera_orientation), sensorOrientation.toString()))
            }
            val flashInfoAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
            if (flashInfoAvailable != null) {
                list.add(Pair(context.getString(R.string.camera_flash), flashInfoAvailable.toString()))
            }
            val obj = CommandUtils.convertToJson(list)
            jsonArray.put(obj)
            index++
        }
        return jsonArray
    }

    private fun getAvailableToneMapModes(availableToneMapModes: Int): String {
        return when (availableToneMapModes) {
            0 -> "CONTRAST_CURVE"
            1 -> "FAST"
            2 -> "HIGH_QUALITY"
            3 -> "GAMMA_VALUE"
            4 -> "PRESET_CURVE"
            else -> "\$unknown-$availableToneMapModes"
        }
    }

    private fun getSyncMaxLatency(syncMaxLatency: Int): String {
        return when (syncMaxLatency) {
            -1 -> Constants.UNKNOWN
            0 -> "PER_FRAME_CONTROL"
            else -> "\$unknown-$syncMaxLatency"
        }
    }

    private fun getAvailableOisDataModes(availableOisDataModes: Int): String {
        return when (availableOisDataModes) {
            0 -> "OFF"
            1 -> "ON"
            else -> "\$unknown-$availableOisDataModes"
        }
    }

    private fun getAvailableLensShadingMapModes(availableLensShadingMapModes: Int): String {
        return when (availableLensShadingMapModes) {
            0 -> "OFF"
            1 -> "ON"
            else -> "\$unknown-$availableLensShadingMapModes"
        }
    }

    private fun getAvailableFaceDetectModes(availableFaceDetectModes: Int): String {
        return when (availableFaceDetectModes) {
            0 -> "OFF"
            1 -> "SIMPLE"
            2 -> "FULL"
            else -> "\$unknown-$availableFaceDetectModes"
        }
    }

    private fun getShadingAvailableModes(shadingAvailableModes: Int): String {
        return when (shadingAvailableModes) {
            0 -> "OFF"
            1 -> "FAST"
            2 -> "HIGH_QUALITY"
            else -> "\$unknown-$shadingAvailableModes"
        }
    }

    private fun getSensorReferenceIlluminant1(sensorReferenceIlluminant1: Int): String {
        return when (sensorReferenceIlluminant1) {
            1 -> "DAYLIGHT"
            2 -> "FLUORESCENT"
            3 -> "TUNGSTEN"
            4 -> "FLASH"
            9 -> "FINE_WEATHER"
            10 -> "CLOUDY_WEATHER"
            11 -> "SHADE"
            12 -> "DAYLIGHT_FLUORESCENT"
            13 -> "DAY_WHITE_FLUORESCENT"
            14 -> "COOL_WHITE_FLUORESCENT"
            15 -> "WHITE_FLUORESCENT"
            17 -> "STANDARD_LIGHT_A"
            18 -> "STANDARD_LIGHT_B"
            19 -> "STANDARD_LIGHT_C"
            20 -> "D55"
            21 -> "D65"
            22 -> "D75"
            23 -> "D50"
            24 -> "ISO_STUDIO_TUNGSTEN"
            else -> "\$unknown-$sensorReferenceIlluminant1"
        }
    }

    private fun getSensorInfoTimestampSource(sensorInfoTimestampSource: Int): String {
        return when (sensorInfoTimestampSource) {
            0 -> "UNKNOWN"
            1 -> "REALTIME"
            else -> "\$unknown-$sensorInfoTimestampSource"
        }
    }

    private fun getSensorInfoColorFilterArrangement(sensorInfoColorFilterArrangement: Int): String {
        return when (sensorInfoColorFilterArrangement) {
            0 -> "RGGB"
            1 -> "GRBG"
            2 -> "GBRG"
            3 -> "BGGR"
            4 -> "RGB"
            else -> "\$unknown-$sensorInfoColorFilterArrangement"
        }
    }

    private fun getSensorAvailableTestPatternModes(sensorAvailableTestPatternModes: Int): String {
        return when (sensorAvailableTestPatternModes) {
            0 -> "OFF"
            1 -> "SOLID_COLOR"
            2 -> "COLOR_BARS"
            3 -> "COLOR_BARS_FADE_TO_GRAY"
            4 -> "PN9"
            5 -> "CUSTOM1"
            else -> "\$unknown-$sensorAvailableTestPatternModes"
        }
    }

    private fun getScalerCroppingType(scalerCroppingType: Int): String {
        return when (scalerCroppingType) {
            0 -> "CENTER_ONLY"
            1 -> "FREEFORM"
            else -> "\$unknown-$scalerCroppingType"
        }
    }

    private fun getRequestAvailableCapabilities(requestAvailableCapabilities: Int): String {
        return when (requestAvailableCapabilities) {
            0 -> "BACKWARD_COMPATIBLE"
            1 -> "MANUAL_SENSOR"
            2 -> "MANUAL_POST_PROCESSING"
            3 -> "RAW"
            4 -> "PRIVATE_REPROCESSING"
            5 -> "READ_SENSOR_SETTINGS"
            6 -> "BURST_CAPTURE"
            7 -> "YUV_REPROCESSING"
            8 -> "DEPTH_OUTPUT"
            9 -> "CONSTRAINED_HIGH_SPEED_VIDEO"
            10 -> "MOTION_TRACKING"
            else -> "\$unknown-$requestAvailableCapabilities"
        }
    }

    private fun getAvailableNoiseReductionModes(availableNoiseReductionModes: Int): String {
        return when (availableNoiseReductionModes) {
            0 -> "OFF"
            1 -> "FAST"
            2 -> "HIGH_QUALITY"
            3 -> "MINIMAL"
            4 -> "ZERO_SHUTTER_LAG"
            else -> "\$unknown-$availableNoiseReductionModes"
        }
    }

    private fun getCameraSensorSyncType(cameraSensorSyncType: Int?): String {
        return when (cameraSensorSyncType) {
            0 -> "APPROXIMATE"
            1 -> "CALIBRATED"
            else -> "\$unknown-$cameraSensorSyncType"
        }
    }

    private fun getLensPoseReference(lensPoseReference: Int?): String {
        return when (lensPoseReference) {
            0 -> "PRIMARY_CAMERA"
            1 -> "OIS_AXIS_CENTER"
            else -> "\$unknown-$lensPoseReference"
        }
    }

    private fun getFocusDistanceCalibration(focusDistanceCalibration: Int?): String {
        return when (focusDistanceCalibration) {
            0 -> "UNCALIBRATED"
            1 -> "APPROXIMATE"
            2 -> "CALIBRATED"
            else -> "\$unknown-$focusDistanceCalibration"
        }
    }

    private fun getAvailableOpticalStabilization(availableOpticalStabilization: Int): String {
        return when (availableOpticalStabilization) {
            0 -> "OFF"
            1 -> "ON"
            else -> "\$unknown-$availableOpticalStabilization"
        }
    }

    private fun getAvailableHotPixelModes(availableHotPixelModes: Int): String {
        return when (availableHotPixelModes) {
            0 -> "OFF"
            1 -> "FAST"
            2 -> "HIGH_QUALITY"
            else -> "\$unknown-$availableHotPixelModes"
        }
    }

    private fun getAvailableEdgeModes(availableEdgeModes: Int): String {
        return when (availableEdgeModes) {
            0 -> "OFF"
            1 -> "FAST"
            2 -> "HIGH_QUALITY"
            3 -> "ZERO_SHUTTER_LAG"
            else -> "\$unknown-$availableEdgeModes"
        }
    }

    private fun getCorrectionAvailableModes(correctionAvailableModes: Int): String {
        return when (correctionAvailableModes) {
            0 -> "OFF"
            1 -> "FAST"
            2 -> "HIGH_QUALITY"
            else -> "\$unknown-$correctionAvailableModes"
        }
    }

    private fun getAwbAvailableModes(awbAvailableModes: Int): String {
        return when (awbAvailableModes) {
            0 -> "OFF"
            1 -> "AUTO"
            2 -> "INCANDESCENT"
            3 -> "FLUORESCENT"
            4 -> "WARM_FLUORESCENT"
            5 -> "DAYLIGHT"
            6 -> "CLOUDY_DAYLIGHT"
            7 -> "CONTROL_AWB_MODE_TWILIGHT"
            8 -> "SHADE"
            else -> "\$unknown-$awbAvailableModes"
        }
    }

    private fun getVideoStabilizationModes(videoStabilizationModes: Int): String {
        return when (videoStabilizationModes) {
            0 -> "OFF"
            1 -> "ON"
            else -> "\$unknown-$videoStabilizationModes"
        }
    }

    private fun getAvailableSceneModes(availableSceneModes: Int): String {
        return when (availableSceneModes) {
            0 -> "DISABLED"
            1 -> "FACE_PRIORITY"
            2 -> "ACTION"
            3 -> "PORTRAIT"
            4 -> "LANDSCAPE"
            5 -> "NIGHT"
            6 -> "NIGHT_PORTRAIT"
            7 -> "THEATRE"
            8 -> "BEACH"
            9 -> "SNOW"
            10 -> "SUNSET"
            11 -> "STEADYPHOTO"
            12 -> "FIREWORKS"
            13 -> "SPORTS"
            14 -> "PARTY"
            15 -> "CANDLELIGHT"
            16 -> "BARCODE"
            17 -> "HIGH_SPEED_VIDEO"
            18 -> "HDR"
            else -> "\$unknown-$availableSceneModes"
        }
    }

    private fun getAvailableModes(availableModes: Int): String {
        return when (availableModes) {
            0 -> "OFF"
            1 -> "AUTO"
            2 -> "MODE_USE_SCENE_MODE"
            3 -> "OFF_KEEP_STATE"
            else -> "\$unknown-$availableModes"
        }
    }

    private fun getAvailableEffects(availableEffects: Int): String {
        return when (availableEffects) {
            0 -> "OFF"
            1 -> "MONO"
            2 -> "NEGATIVE"
            3 -> "SOLARIZE"
            4 -> "SEPIA"
            5 -> "POSTERIZE"
            6 -> "WHITEBOARD"
            7 -> "BLACKBOARD"
            8 -> "AQUA"
            else -> "\$unknown-$availableEffects"
        }
    }

    private fun getAfAvailableModes(afAvailableModes: Int): String {
        return when (afAvailableModes) {
            0 -> "OFF"
            1 -> "AUTO"
            2 -> "MACRO"
            3 -> "CONTINUOUS_VIDEO"
            4 -> "CONTINUOUS_PICTURE"
            5 -> "EDOF"
            else -> "\$unknown-$afAvailableModes"
        }
    }

    private fun getAeAvailableModes(aeAvailableModes: Int): String {
        return when (aeAvailableModes) {
            0 -> "OFF"
            1 -> "ON"
            2 -> "ON_AUTO_FLASH"
            3 -> "ON_ALWAYS_FLASH"
            4 -> "ON_AUTO_FLASH_REDEYE"
            5 -> "ON_EXTERNAL_FLASH"
            else -> "\$unknown-$aeAvailableModes"
        }
    }

    private fun getAntiBandingModes(antiBandingModes: Int): String {
        return when (antiBandingModes) {
            0 -> "OFF"
            1 -> "50HZ"
            2 -> "60HZ"
            3 -> "AUTO"
            else -> "\$unknown-$antiBandingModes"
        }
    }

    private fun getAberrationModes(aberrationModes: Int): String {
        return when (aberrationModes) {
            0 -> "OFF"
            1 -> "FAST"
            2 -> "HIGH_QUALITY"
            else -> "\$unknown-$aberrationModes"
        }
    }

    private fun getFacing(facing: Int?): String {
        if (facing == null) {
            return Constants.UNKNOWN
        }
        return when (facing) {
            0 -> "FRONT"
            1 -> "BACK"
            2 -> "EXTERNAL"
            else -> "\$unknown-$facing"
        }
    }

    private fun getLevel(level: Int?): String {
        if (level == null) {
            return Constants.UNKNOWN
        }
        return when (level) {
            0 -> "LIMITED"
            1 -> "FULL"
            2 -> "LEGACY"
            3 -> "LEVEL_3"
            4 -> "EXTERNAL"
            else -> "\$unknown-$level"
        }
    }

    private fun getFormat(format: Int): String {
        return when (format) {
            4 -> "RGB_565"
            16 -> "NV16"
            17 -> "NV21"
            20 -> "YUY2"
            32 -> "RAW_SENSOR"
            34 -> "PRIVATE"
            35 -> "YUV_420_888"
            36 -> "RAW_PRIVATE"
            37 -> "RAW10"
            38 -> "RAW12"
            39 -> "YUV_422_888"
            40 -> "YUV_444_888"
            41 -> "FLEX_RGB_888"
            42 -> "FLEX_RGBA_8888"
            256 -> "JPEG"
            257 -> "DEPTH_POINT_CLOUD"
            842094169 -> "YV12"
            1144402265 -> "DEPTH16"
            else -> "\$unknown-$format"
        }
    }
}
