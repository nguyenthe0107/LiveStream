package olmo.wellness.android.extension

import android.content.Context
import com.amazonaws.ivs.broadcast.BroadcastSession
import com.amazonaws.ivs.broadcast.Device

fun Context.getAvailableCameraSize(): Int =
    BroadcastSession.listAvailableDevices(this).filter { it.type == Device.Descriptor.DeviceType.CAMERA }.size

fun Context.getSelectedCamera(position: Int): Device.Descriptor =
    getSortedCameraList()[position]

fun Context.getSortedCameraList(): List<Device.Descriptor> =
    BroadcastSession.listAvailableDevices(this).sortedBy { it.deviceId }.filter { it.type == Device.Descriptor.DeviceType.CAMERA }
