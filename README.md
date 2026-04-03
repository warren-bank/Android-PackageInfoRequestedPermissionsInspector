### [PackageInfo.requestedPermissions Inspector](https://github.com/warren-bank/Android-PackageInfoRequestedPermissionsInspector)

A little Android utility app for the purpose of inspecting the value of [`PackageInfo.requestedPermissions`](https://developer.android.com/reference/android/content/pm/PackageInfo#requestedPermissions)

#### Purpose

* to help debug [this issue](https://github.com/warren-bank/Android-Mock-Location/issues/31)
  - only effects Android 15+ after [increasing the target SDK version](https://github.com/warren-bank/Android-Mock-Location/commit/6c7ec307151b927ed0b222719f2cd7126964be24) from 33 (Android 13) to 35 (Android 15)
  - is first exhibited in release: [v2.4.2](https://github.com/warren-bank/Android-Mock-Location/releases/tag/service%2Fv02.04.02)
  - and appears to trace back to the way that the [list of permissions](https://github.com/warren-bank/Android-Mock-Location/blob/service/v02.04.02/android-studio-project/Mock-my-GPS/src/main/java/com/github/warren_bank/mock_location/security_model/RuntimePermissions.java#L26-L50) to request at runtime is derived from `PackageInfo.requestedPermissions`
    * a fatal crash occurs because the following permissions are no-longer requested at runtime:
      - [`ACCESS_COARSE_LOCATION`](https://github.com/warren-bank/Android-Mock-Location/blob/service/v02.04.02/android-studio-project/Mock-my-GPS/src/main/AndroidManifest.xml#L13)
      - [`ACCESS_FINE_LOCATION`](https://github.com/warren-bank/Android-Mock-Location/blob/service/v02.04.02/android-studio-project/Mock-my-GPS/src/main/AndroidManifest.xml#L14)

#### Configuration

* [`AndroidManifest.xml`](./android-studio-project/PackageInfoRequestedPermissionsInspector/src/main/AndroidManifest.xml)
  - includes the following permissions:
    * [`ACCESS_COARSE_LOCATION`](https://developer.android.com/reference/android/Manifest.permission#ACCESS_COARSE_LOCATION)
    * [`ACCESS_FINE_LOCATION`](https://developer.android.com/reference/android/Manifest.permission#ACCESS_FINE_LOCATION)
    * [`ACCESS_NETWORK_STATE`](https://developer.android.com/reference/android/Manifest.permission#ACCESS_NETWORK_STATE)
    * [`ACCESS_WIFI_STATE`](https://developer.android.com/reference/android/Manifest.permission#ACCESS_WIFI_STATE)
    * [`MANAGE_EXTERNAL_STORAGE`](https://developer.android.com/reference/android/Manifest.permission#MANAGE_EXTERNAL_STORAGE)
    * [`READ_EXTERNAL_STORAGE`](https://developer.android.com/reference/android/Manifest.permission#READ_EXTERNAL_STORAGE)
    * [`WRITE_EXTERNAL_STORAGE`](https://developer.android.com/reference/android/Manifest.permission#WRITE_EXTERNAL_STORAGE)
* [`constants.gradle`](./android-studio-project/constants.gradle)
  - includes the configuration:
    * `targetSdkVersion = 35`

#### Legal

* copyright: [Warren Bank](https://github.com/warren-bank)
* license: [GPL-2.0](https://www.gnu.org/licenses/old-licenses/gpl-2.0.txt)
