# TechFieldTool
An Android application that allows field technicians to connect to devices over wifi and perform a oneM2M node Retrieve.

## Support Android SDK Versions.
* Min: 15
* Target: 28

## Required Permissions
1. BLUETOOTH: Needed to perform an BT communication (requesting a connection, accepting a connection
and transferring data).

2. BLUETOOTH_ADMIN: Needed to initiate BT device discovery or manipulate BT settings.

3. ACCESS_FINE_LOCATION: Required because a BT scan can be used to acquire information about the
location of a the user via the device itself or BT beacons in use in the immediate area.
Alternatively, as we are targeting API level 28 and lower, the ACCESS_COARSE_LOCATION permission can
also be used.  On devices running API level 26 and higher, the CompanionDeviceManager class can be
used to perform a scan on nearby companion devices without requiring location permissions.

## MipMap Scaling

44x36 (0.75x) for low-density (ldpi)
58x48 (1.0x baseline) for medium-density (mdpi)
87x72 (1.5x) for high-density (hdpi)
166x96 (2.0x) for extra-high-density (xhdpi)
174x144 (3.0x) for extra-extra-high-density (xxhdpi)
232x192 (4.0x) for extra-extra-extra-high-density (xxxhdpi)

## Splash Screen
The splash screen is implement as an activity without a view, just a style set in AndroidManifest.
The splash screen determines if the device has BT enabled and opens to the main activity after the
SPLASH_DELAY has been reached.
