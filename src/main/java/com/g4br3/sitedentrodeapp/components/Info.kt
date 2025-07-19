package com.g4br3.sitedentrodeapp.components

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File

class Info(applicationContext: Context) {
    val context = applicationContext
    val packageManager = context.packageManager
    val packageInfo = packageManager.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS or PackageManager.GET_SIGNING_CERTIFICATES)
    val appInfo = context.applicationInfo

    @RequiresApi(Build.VERSION_CODES.P)
    public var all = mapOf(
        "packageName" to packageInfo.packageName,
        "versionName" to packageInfo.versionName,
        "versionCode" to packageInfo.longVersionCode,
        "appName" to appInfo.loadLabel(packageManager).toString(),
        "firstInstallTime" to packageInfo.firstInstallTime,
        "lastUpdateTime" to packageInfo.lastUpdateTime,
        "sourceDir" to appInfo.sourceDir,
      //  "isDebug" to BuildConfig.DEBUG,
       // "applicationId" to BuildConfig.APPLICATION_ID,
        "requestedPermissions" to (packageInfo.requestedPermissions?.toList() ?: emptyList()),
        "filesDir" to context.filesDir.absolutePath,
        "cacheDir" to context.cacheDir.absolutePath,
        "externalFilesDir" to context.getExternalFilesDir(null)?.absolutePath,
        "apkSizeBytes" to File(appInfo.sourceDir).length(),
        "signatures" to (packageInfo.signingInfo?.apkContentsSigners?.map { it.toCharsString() })
    )

}