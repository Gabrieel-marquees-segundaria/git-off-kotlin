package com.g4br3.sitedentrodeapp.components

import android.net.Uri

class UriSave (name: String) {
    public  var uri: Uri? = null
    public var key: String = name

}

class UriSaveList(){
    var html: UriSave = UriSave("**html**")
    var repository: UriSave = UriSave("repository")
    var externalModulejs: UriSave = UriSave("externalModulejs")
    var externalModuleCss: UriSave = UriSave("externalModuleCss")
}