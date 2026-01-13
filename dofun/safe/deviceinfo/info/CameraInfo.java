package com.dofun.safe.deviceinfo.info;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Range;
import android.util.SizeF;
import androidx.core.util.Pair;
import com.dofun.safe.deviceinfo.R;
import com.dofun.safe.deviceinfo.utils.CommandUtils;
import com.dofun.safe.deviceinfo.utils.Constants;
import com.dofun.safe.deviceinfo.utils.DecimalUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes8.dex */
public class CameraInfo {
    public static JSONArray getCameraInfoJson(Context context) {
        return getCameraInfo(context);
    }

    public static JSONArray getCameraInfo(Context context) {
        CameraManager manager;
        String[] cameraIdList;
        CameraManager manager2;
        int i;
        int i2;
        JSONArray jsonArray = new JSONArray();
        try {
            manager = (CameraManager) context.getSystemService("camera");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (manager == null) {
            return null;
        }
        String[] cameraIdList2 = manager.getCameraIdList();
        int length = cameraIdList2.length;
        int i3 = 0;
        while (i3 < length) {
            String cameraId = cameraIdList2[i3];
            List<Pair<String, String>> list = new ArrayList<>();
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            Integer facing = (Integer) characteristics.get(CameraCharacteristics.LENS_FACING);
            list.add(new Pair<>(context.getString(R.string.menu_camera), getFacing(facing)));
            Rect activeArraySize = (Rect) characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            if (activeArraySize != null) {
                int width = activeArraySize.right - activeArraySize.left;
                i = 0;
                int height = activeArraySize.bottom - activeArraySize.top;
                double round = DecimalUtils.round((width * height) / 1000000.0d, 1);
                manager2 = manager;
                cameraIdList = cameraIdList2;
                length = length;
                list.add(new Pair<>(context.getString(R.string.camera_resolution), round + " MP (" + width + "x" + height + ")"));
            } else {
                manager2 = manager;
                cameraIdList = cameraIdList2;
                length = length;
                i = 0;
            }
            float[] lensInfoAvailableApertures = (float[]) characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES);
            if (!(lensInfoAvailableApertures == null || lensInfoAvailableApertures.length == 0)) {
                StringBuffer sb = new StringBuffer();
                int length2 = lensInfoAvailableApertures.length;
                for (int i4 = i; i4 < length2; i4++) {
                    float f = lensInfoAvailableApertures[i4];
                    sb.append("f/").append(f).append(" ");
                }
                list.add(new Pair<>(context.getString(R.string.camera_aperture), sb.toString().trim()));
            }
            float[] lensInfoAvailableFocalLengths = (float[]) characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
            if (lensInfoAvailableFocalLengths != null && lensInfoAvailableFocalLengths.length != 0) {
                StringBuffer sb2 = new StringBuffer();
                int length3 = lensInfoAvailableFocalLengths.length;
                int i5 = i;
                while (i5 < length3) {
                    float f2 = lensInfoAvailableFocalLengths[i5];
                    sb2.append(f2).append(" mm").append(" ");
                    i5++;
                    lensInfoAvailableApertures = lensInfoAvailableApertures;
                    lensInfoAvailableFocalLengths = lensInfoAvailableFocalLengths;
                }
                list.add(new Pair<>(context.getString(R.string.camera_focal_length), sb2.toString().trim()));
            }
            int[] afAvailableModes = (int[]) characteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
            if (!(afAvailableModes == null || afAvailableModes.length == 0)) {
                StringBuffer sb3 = new StringBuffer();
                int length4 = afAvailableModes.length;
                for (int i6 = i; i6 < length4; i6++) {
                    int i7 = afAvailableModes[i6];
                    sb3.append(getAfAvailableModes(i7)).append(",");
                }
                list.add(new Pair<>(context.getString(R.string.camera_af_modes), sb3.deleteCharAt(sb3.length() - 1).toString()));
            }
            SizeF physicalSize = (SizeF) characteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE);
            if (physicalSize != null) {
                list.add(new Pair<>(context.getString(R.string.camera_size), physicalSize.getWidth() + "x" + physicalSize.getHeight()));
            }
            if (physicalSize == null || activeArraySize == null) {
                i2 = i3;
            } else {
                double value = physicalSize.getWidth() * physicalSize.getHeight();
                double width2 = activeArraySize.right - activeArraySize.left;
                double round2 = DecimalUtils.round(Math.sqrt((((value * 1000.0d) / width2) * 1000.0d) / (activeArraySize.bottom - activeArraySize.top)), 2);
                i2 = i3;
                list.add(new Pair<>(context.getString(R.string.camera_pixel_size), "~" + round2 + "µm"));
            }
            float[] focalLengths = (float[]) characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
            if (focalLengths != null && focalLengths.length > 0) {
                float f3 = focalLengths[i];
                SizeF sizeF = (SizeF) characteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE);
                if (sizeF != null) {
                    float width3 = sizeF.getWidth();
                    if (width3 > 0.0f) {
                        list.add(new Pair<>(context.getString(R.string.camera_view_angle), DecimalUtils.round(Math.toDegrees(Math.atan((width3 * 0.5d) / f3)) * 2.0d, 1) + "°"));
                    }
                }
            }
            StreamConfigurationMap map = (StreamConfigurationMap) characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            if (map != null) {
                int[] ints = map.getOutputFormats();
                StringBuffer sb4 = new StringBuffer();
                int length5 = ints.length;
                for (int i8 = i; i8 < length5; i8++) {
                    int i9 = ints[i8];
                    sb4.append(getFormat(i9)).append(",");
                }
                list.add(new Pair<>(context.getString(R.string.camera_formats), sb4.deleteCharAt(sb4.length() - 1).toString()));
            }
            Range<Integer> sensorInfoSensitivityRange = (Range) characteristics.get(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE);
            if (sensorInfoSensitivityRange != null) {
                list.add(new Pair<>(context.getString(R.string.camera_iso), sensorInfoSensitivityRange.getLower() + "-" + sensorInfoSensitivityRange.getUpper()));
            }
            Integer sensorInfoColorFilterArrangement = (Integer) characteristics.get(CameraCharacteristics.SENSOR_INFO_COLOR_FILTER_ARRANGEMENT);
            if (sensorInfoColorFilterArrangement != null) {
                list.add(new Pair<>(context.getString(R.string.camera_color_filter), getSensorInfoColorFilterArrangement(sensorInfoColorFilterArrangement.intValue())));
            }
            Integer sensorOrientation = (Integer) characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            if (sensorOrientation != null) {
                list.add(new Pair<>(context.getString(R.string.camera_orientation), sensorOrientation.toString()));
            }
            Boolean flashInfoAvailable = (Boolean) characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            if (flashInfoAvailable != null) {
                list.add(new Pair<>(context.getString(R.string.camera_flash), flashInfoAvailable.toString()));
            }
            JSONObject object = CommandUtils.convertToJson(list);
            jsonArray.put(object);
            i3 = i2 + 1;
            manager = manager2;
            cameraIdList2 = cameraIdList;
        }
        return jsonArray;
    }

    private static String getAvailableToneMapModes(int availableToneMapModes) {
        switch (availableToneMapModes) {
            case 0:
                return "CONTRAST_CURVE";
            case 1:
                return "FAST";
            case 2:
                return "HIGH_QUALITY";
            case 3:
                return "GAMMA_VALUE";
            case 4:
                return "PRESET_CURVE";
            default:
                return "$unknown-" + availableToneMapModes;
        }
    }

    private static String getSyncMaxLatency(int syncMaxLatency) {
        switch (syncMaxLatency) {
            case -1:
                return Constants.UNKNOWN;
            case 0:
                return "PER_FRAME_CONTROL";
            default:
                return "$unknown-" + syncMaxLatency;
        }
    }

    private static String getAvailableOisDataModes(int availableOisDataModes) {
        switch (availableOisDataModes) {
            case 0:
                return "OFF";
            case 1:
                return "ON";
            default:
                return "$unknown-" + availableOisDataModes;
        }
    }

    private static String getAvailableLensShadingMapModes(int availableLensShadingMapModes) {
        switch (availableLensShadingMapModes) {
            case 0:
                return "OFF";
            case 1:
                return "ON";
            default:
                return "$unknown-" + availableLensShadingMapModes;
        }
    }

    private static String getAvailableFaceDetectModes(int availableFaceDetectModes) {
        switch (availableFaceDetectModes) {
            case 0:
                return "OFF";
            case 1:
                return "SIMPLE";
            case 2:
                return "FULL";
            default:
                return "$unknown-" + availableFaceDetectModes;
        }
    }

    private static String getShadingAvailableModes(int shadingAvailableModes) {
        switch (shadingAvailableModes) {
            case 0:
                return "OFF";
            case 1:
                return "FAST";
            case 2:
                return "HIGH_QUALITY";
            default:
                return "$unknown-" + shadingAvailableModes;
        }
    }

    private static String getSensorReferenceIlluminant1(int sensorReferenceIlluminant1) {
        switch (sensorReferenceIlluminant1) {
            case 1:
                return "DAYLIGHT";
            case 2:
                return "FLUORESCENT";
            case 3:
                return "TUNGSTEN";
            case 4:
                return "FLASH";
            case 5:
            case 6:
            case 7:
            case 8:
            case 16:
            default:
                return "$unknown-" + sensorReferenceIlluminant1;
            case 9:
                return "FINE_WEATHER";
            case 10:
                return "CLOUDY_WEATHER";
            case 11:
                return "SHADE";
            case 12:
                return "DAYLIGHT_FLUORESCENT";
            case 13:
                return "DAY_WHITE_FLUORESCENT";
            case 14:
                return "COOL_WHITE_FLUORESCENT";
            case 15:
                return "WHITE_FLUORESCENT";
            case 17:
                return "STANDARD_A";
            case 18:
                return "STANDARD_B";
            case 19:
                return "STANDARD_C";
            case 20:
                return "D55";
            case 21:
                return "D65";
            case 22:
                return "D75";
            case 23:
                return "D50";
            case 24:
                return "ISO_STUDIO_TUNGSTEN";
        }
    }

    private static String getSensorInfoTimestampSource(int sensorInfoTimestampSource) {
        switch (sensorInfoTimestampSource) {
            case 0:
                return "UNKNOWN";
            case 1:
                return "REALTIME";
            default:
                return "$unknown-" + sensorInfoTimestampSource;
        }
    }

    private static String getSensorInfoColorFilterArrangement(int sensorInfoColorFilterArrangement) {
        switch (sensorInfoColorFilterArrangement) {
            case 0:
                return "RGGB";
            case 1:
                return "GRBG";
            case 2:
                return "GBRG";
            case 3:
                return "BGGR";
            case 4:
                return "RGB";
            default:
                return "$unknown-" + sensorInfoColorFilterArrangement;
        }
    }

    private static String getSensorAvailableTestPatternModes(int sensorAvailableTestPatternModes) {
        switch (sensorAvailableTestPatternModes) {
            case 0:
                return "OFF";
            case 1:
                return "SOLID_COLOR";
            case 2:
                return "COLOR_BARS";
            case 3:
                return "COLOR_BARS_FADE_TO_GRAY";
            case 4:
                return "PN9";
            case 256:
                return "CUSTOM1";
            default:
                return "$unknown-" + sensorAvailableTestPatternModes;
        }
    }

    private static String getScalerCroppingType(int scalerCroppingType) {
        switch (scalerCroppingType) {
            case 0:
                return "CENTER_ONLY";
            case 1:
                return "FREEFORM";
            default:
                return "$unknown-" + scalerCroppingType;
        }
    }

    private static String getRequestAvailableCapabilities(int requestAvailableCapabilities) {
        switch (requestAvailableCapabilities) {
            case 0:
                return "BACKWARD_COMPATIBLE";
            case 1:
                return "MANUAL_SENSOR";
            case 2:
                return "MANUAL_POST_PROCESSING";
            case 3:
                return "RAW";
            case 4:
                return "PRIVATE_REPROCESSING";
            case 5:
                return "READ_SENSOR_SETTINGS";
            case 6:
                return "BURST_CAPTURE";
            case 7:
                return "YUV_REPROCESSING";
            case 8:
                return "DEPTH_OUTPUT";
            case 9:
                return "CONSTRAINED_HIGH_SPEED_VIDEO";
            case 10:
                return "MOTION_TRACKING";
            case 11:
                return "LOGICAL_MULTI_CAMERA";
            case 12:
                return "MONOCHROME";
            default:
                return "$unknown-" + requestAvailableCapabilities;
        }
    }

    private static String getAvailableNoiseReductionModes(int availableNoiseReductionModes) {
        switch (availableNoiseReductionModes) {
            case 0:
                return "OFF";
            case 1:
                return "FAST";
            case 2:
                return "HIGH_QUALITY";
            case 3:
                return "MINIMAL";
            case 4:
                return "ZERO_SHUTTER_LAG";
            default:
                return "$unknown-" + availableNoiseReductionModes;
        }
    }

    private static String getCameraSensorSyncType(Integer cameraSensorSyncType) {
        if (cameraSensorSyncType == null) {
            return Constants.UNKNOWN;
        }
        switch (cameraSensorSyncType.intValue()) {
            case 0:
                return "APPROXIMATE";
            case 1:
                return "CALIBRATED";
            default:
                return "$unknown-" + cameraSensorSyncType;
        }
    }

    private static String getLensPoseReference(Integer lensPoseReference) {
        if (lensPoseReference == null) {
            return Constants.UNKNOWN;
        }
        switch (lensPoseReference.intValue()) {
            case 0:
                return "PRIMARY_CAMERA";
            case 1:
                return "GYROSCOPE";
            default:
                return "$unknown-" + lensPoseReference;
        }
    }

    private static String getFocusDistanceCalibration(Integer focusDistanceCalibration) {
        if (focusDistanceCalibration == null) {
            return Constants.UNKNOWN;
        }
        switch (focusDistanceCalibration.intValue()) {
            case 0:
                return "UNCALIBRATED";
            case 1:
                return "APPROXIMATE";
            case 2:
                return "CALIBRATED";
            default:
                return "$unknown-" + focusDistanceCalibration;
        }
    }

    private static String getAvailableOpticalStabilization(int jsonArrayAvailableOpticalStabilization) {
        switch (jsonArrayAvailableOpticalStabilization) {
            case 0:
                return "OFF";
            case 1:
                return "ON";
            default:
                return "$unknown-" + jsonArrayAvailableOpticalStabilization;
        }
    }

    private static String getAvailableHotPixelModes(int availableHotPixelModes) {
        switch (availableHotPixelModes) {
            case 0:
                return "OFF";
            case 1:
                return "FAST";
            case 2:
                return "HIGH_QUALITY";
            default:
                return "$unknown-" + availableHotPixelModes;
        }
    }

    private static String getAvailableEdgeModes(int availableEdgeModes) {
        switch (availableEdgeModes) {
            case 0:
                return "OFF";
            case 1:
                return "FAST";
            case 2:
                return "HIGH_QUALITY";
            case 3:
                return "ZERO_SHUTTER_LAG";
            default:
                return "$unknown-" + availableEdgeModes;
        }
    }

    private static String getCorrectionAvailableModes(int correctionAvailableModes) {
        switch (correctionAvailableModes) {
            case 0:
                return "OFF";
            case 1:
                return "FAST";
            case 2:
                return "HIGH_QUALITY";
            default:
                return "$unknown-" + correctionAvailableModes;
        }
    }

    private static String getAwbAvailableModes(int awbAvailableModes) {
        switch (awbAvailableModes) {
            case 0:
                return "OFF";
            case 1:
                return "AUTO";
            case 2:
                return "INCANDESCENT";
            case 3:
                return "FLUORESCENT";
            case 4:
                return "WARM_FLUORESCENT";
            case 5:
                return "DAYLIGHT";
            case 6:
                return "CLOUDY_DAYLIGHT";
            case 7:
                return "CONTROL_AWB_MODE_TWILIGHT";
            case 8:
                return "SHADE";
            default:
                return "$unknown-" + awbAvailableModes;
        }
    }

    private static String getVideoStabilizationModes(int videoStabilizationModes) {
        switch (videoStabilizationModes) {
            case 0:
                return "OFF";
            case 1:
                return "ON";
            default:
                return "$unknown-" + videoStabilizationModes;
        }
    }

    private static String getAvailableSceneModes(int availableSceneModes) {
        switch (availableSceneModes) {
            case 0:
                return "DISABLED";
            case 1:
                return "FACE_PRIORITY";
            case 2:
                return "ACTION";
            case 3:
                return "PORTRAIT";
            case 4:
                return "LANDSCAPE";
            case 5:
                return "NIGHT";
            case 6:
                return "NIGHT_PORTRAIT";
            case 7:
                return "THEATRE";
            case 8:
                return "BEACH";
            case 9:
                return "SNOW";
            case 10:
                return "SUNSET";
            case 11:
                return "STEADYPHOTO";
            case 12:
                return "FIREWORKS";
            case 13:
                return "SPORTS";
            case 14:
                return "PARTY";
            case 15:
                return "CANDLELIGHT";
            case 16:
                return "BARCODE";
            case 17:
                return "HIGH_SPEED_VIDEO";
            case 18:
                return "HDR";
            default:
                return "$unknown-" + availableSceneModes;
        }
    }

    private static String getAvailableModes(int availableModes) {
        switch (availableModes) {
            case 0:
                return "OFF";
            case 1:
                return "AUTO";
            case 2:
                return "MODE_USE_SCENE_MODE";
            case 3:
                return "OFF_KEEP_STATE";
            default:
                return "$unknown-" + availableModes;
        }
    }

    private static String getAvailableEffects(int availableEffects) {
        switch (availableEffects) {
            case 0:
                return "OFF";
            case 1:
                return "MONO";
            case 2:
                return "NEGATIVE";
            case 3:
                return "SOLARIZE";
            case 4:
                return "SEPIA";
            case 5:
                return "POSTERIZE";
            case 6:
                return "WHITEBOARD";
            case 7:
                return "BLACKBOARD";
            case 8:
                return "AQUA";
            default:
                return "$unknown-" + availableEffects;
        }
    }

    private static String getAfAvailableModes(int afAvailableModes) {
        switch (afAvailableModes) {
            case 0:
                return "OFF";
            case 1:
                return "AUTO";
            case 2:
                return "MACRO";
            case 3:
                return "CONTINUOUS_VIDEO";
            case 4:
                return "CONTINUOUS_PICTURE";
            case 5:
                return "EDOF";
            default:
                return "$unknown-" + afAvailableModes;
        }
    }

    private static String getAeAvailableModes(int aeAvailableModes) {
        switch (aeAvailableModes) {
            case 0:
                return "OFF";
            case 1:
                return "ON";
            case 2:
                return "ON_AUTO_FLASH";
            case 3:
                return "ON_ALWAYS_FLASH";
            case 4:
                return "ON_AUTO_FLASH_REDEYE";
            case 5:
                return "ON_EXTERNAL_FLASH";
            default:
                return "$unknown-" + aeAvailableModes;
        }
    }

    private static String getAntiBandingModes(int antiBandingModes) {
        switch (antiBandingModes) {
            case 0:
                return "OFF";
            case 1:
                return "50HZ";
            case 2:
                return "60HZ";
            case 3:
                return "AUTO";
            default:
                return "$unknown-" + antiBandingModes;
        }
    }

    private static String getAberrationModes(int aberrationModes) {
        switch (aberrationModes) {
            case 0:
                return "OFF";
            case 1:
                return "FAST";
            case 2:
                return "HIGH_QUALITY";
            default:
                return "$unknown-" + aberrationModes;
        }
    }

    private static String getFacing(Integer facing) {
        if (facing == null) {
            return Constants.UNKNOWN;
        }
        switch (facing.intValue()) {
            case 0:
                return "FRONT";
            case 1:
                return "BACK";
            case 2:
                return "EXTERNAL";
            default:
                return "$unknown-" + facing;
        }
    }

    private static String getLevel(Integer level) {
        if (level == null) {
            return Constants.UNKNOWN;
        }
        switch (level.intValue()) {
            case 0:
                return "LIMITED";
            case 1:
                return "FULL";
            case 2:
                return "LEGACY";
            case 3:
                return "LEVEL_3";
            case 4:
                return "EXTERNAL";
            default:
                return "$unknown-" + level;
        }
    }

    private static String getFormat(int format) {
        switch (format) {
            case 4:
                return "RGB_565";
            case 16:
                return "NV16";
            case 17:
                return "NV21";
            case 20:
                return "YUY2";
            case 32:
                return "RAW_SENSOR";
            case 34:
                return "PRIVATE";
            case 35:
                return "YUV_420_888";
            case 36:
                return "RAW_PRIVATE";
            case 37:
                return "RAW10";
            case 38:
                return "RAW12";
            case 39:
                return "YUV_422_888";
            case 40:
                return "YUV_444_888";
            case 41:
                return "FLEX_RGB_888";
            case 42:
                return "FLEX_RGBA_8888";
            case 256:
                return "JPEG";
            case 257:
                return "DEPTH_POINT_CLOUD";
            case 842094169:
                return "YV12";
            case 1144402265:
                return "DEPTH16";
            default:
                return "$unknown-" + format;
        }
    }
}
