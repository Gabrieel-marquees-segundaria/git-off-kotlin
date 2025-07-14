package com.g4br3.sitedentrodeapp.components

import com.g4br3.sitedentrodeapp.components.StorageHelper
class FileName(var name: String){
    var value: String = name
}
class WebFiles(var storageHelper: StorageHelper?) {
     var webListName = FileName("WebList")

     var webHtmlViewName = FileName("WebView")

    var listDefalt ="file:///android_asset/list.html"
    var HtmlViewDefalt =  "file:///android_asset/view.html"
     var webList: String = listDefalt
     var webHtmlView: String = HtmlViewDefalt
     var dict = mutableMapOf<String, Any>()
    init {
        this.webList = listDefalt
        this.webHtmlView = HtmlViewDefalt // Keep the existing webList
        this.dict[this.webListName.value] = listDefalt
        this.dict[this.webHtmlViewName.value] = HtmlViewDefalt

    }


    fun loadData(): WebFiles {
        println("WebFiles.loadData")
        println("storageHelper?.recuperarString(webListName.value): ${storageHelper}")
        storageHelper?.recuperarString(webListName.value)?.let { it -> // 'it' is the string from storage
            (if (it.isNotEmpty()) {
                this.webList = listDefalt // If storage is NOT empty, you use listDefalt
                // This seems counter-intuitive. Usually, if storage has a value, you use that.
            } else {
                // If storage IS empty, you try to retrieve it AGAIN.
                // This 'else' branch's value is what 'also' will receive.
                storageHelper?.recuperarString(webListName.value)
            }).also { retrievedAgain -> // 'retrievedAgain' could be null if the second call also returns null
                if (retrievedAgain != null) {
                    println(retrievedAgain)
                    // You're casting 'retrievedAgain' to String, which is fine if it's not null.
                    this.webList = retrievedAgain as String
                    // **** CRITICAL ****:
                    // You're assigning to this.webList, but you're using
                    // this.webListName.value as the key for the 'dict'.
                    // If 'retrievedAgain' was from the 'else' branch (meaning original 'it' was empty),
                    // AND if the second call to 'recuperarString' returned null,
                    // 'retrievedAgain' would be null, and this 'if' block wouldn't execute,
                    // so dict[this.webListName.value] might not be set with a new value.
                    // If 'it' was NOT empty, 'retrievedAgain' would be Unit (because the if branch
                    // 'this.webList = listDefalt' doesn't return a value to the 'also' block),
                    // which is not null, but the cast to 'String' would fail.
                    this.dict[this.webListName.value] = retrievedAgain // <-- Potential problem here if retrievedAgain is Unit
                }
            }
        }

        storageHelper?.recuperarString(webHtmlViewName.value)?.let { // Similar logic for webHtmlViewName
            (if (it.isNotEmpty()) {
                this.webHtmlView = HtmlViewDefalt
                this.dict[this.webHtmlViewName.value] = HtmlViewDefalt // You set dict here
            } else {
                // Again, retrieving from storage if it was empty.
                // Note the '!!' here. If 'storageHelper' is null, this will crash.
                // If 'recuperarString' returns null, this will be null.
                storageHelper!!.recuperarString(webHtmlViewName.value)
            }).also { retrievedAgain ->
                if (retrievedAgain != null) {
                    println(retrievedAgain)
                    // **** CRITICAL ****:
                    // You assign 'retrievedAgain' to 'this.webList' NOT 'this.webHtmlView'. This is a BUG.
                    this.webList = retrievedAgain as String  // <-- BUG: Should be this.webHtmlView
                    this.dict[this.webHtmlViewName.value] = retrievedAgain // <-- Potential problem if retrievedAgain is Unit
                }
            }
        }
        return this
    }


fun getName(name: FileName): String{
    return dict[name.value] as String
}
    fun reset(){
        this.webHtmlView = HtmlViewDefalt
        this.dict[this.webHtmlViewName.value] = HtmlViewDefalt
        this.webList = listDefalt
        this.dict[this.webListName.value] = listDefalt
    }
}

//   fun setWebList(value: String){
//       this.webList = value
//   }
//    fun setWebHtmlView(value: String){
//        this.webHtmlView = value
//    }