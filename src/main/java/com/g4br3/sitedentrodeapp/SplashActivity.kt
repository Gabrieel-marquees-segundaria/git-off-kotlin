package com.g4br3.sitedentrodeapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper

import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.g4br3.sitedentrodeapp.components.Loader
import com.g4br3.sitedentrodeapp.components.UriList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import com.g4br3.sitedentrodeapp.components.FileManager
import com.g4br3.sitedentrodeapp.components.JSLoader
import com.g4br3.sitedentrodeapp.components.LoadExternalModel
import com.g4br3.sitedentrodeapp.components.modulos
import kotlinx.coroutines.withContext


var uriListS = UriList()
var loader = Loader()





@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var jsLoader: JSLoader
    private lateinit var fileManager: FileManager
    private lateinit var externalModel: LoadExternalModel
    private val TAG = "SplashActivity"
    private var dataBasekey: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate() - Starting SplashActivity")
        super.onCreate(savedInstanceState)
        installSplashScreen()
       jsLoader = JSLoader(this)
        fileManager = FileManager(this)

        Log.d(TAG, "Launching coroutine to load saved URI")
        CoroutineScope(Dispatchers.Default).launch {
            Log.d(TAG, "Coroutine started on ${Thread.currentThread().name}")

            try {
                val sharedPrefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
                val getStringDefalt: String = "uri not found"
                Log.d(TAG, "SharedPreferences retrieved successfully")
               // val uriPathSalva = sharedPrefs.getString(uriListS.repository.key, getStringDefalt)

                val uriExternalModel: String? = sharedPrefs.getString(uriListS.externalModulejs.key, getStringDefalt)

                val intModulos: String= jsLoaderSetup()
                var dataString = ""
                if (uriExternalModel != getStringDefalt){
                    externalModel = LoadExternalModel(fileManager, Uri.parse(uriExternalModel))
                    val externalModelString: String = externalModel.stringBuilder.toString()
                     dataString  = "$intModulos\n\n\n$externalModelString"
                }
                else {
                    dataString = intModulos

                }
                sharedPrefs.edit().putString(dataBasekey, dataString).apply()

//                Log.d(
//                    TAG,
//                    "URI loaded from SharedPreferences - Key: '${uriListS.repository.key}', Value: '$uriPathSalva'"
//                )

//                if (uriPathSalva.isNullOrEmpty()) {
//                    Log.w(TAG, "No saved URI found or URI is empty")
//                } else {
//                    Log.i(TAG, "Successfully loaded saved URI: $uriPathSalva")
//                }

            } catch (e: Exception) {
                Log.e(TAG, "Error loading URI from SharedPreferences", e)
            }
            // apos terminar de carregar vai para main
            withContext(Dispatchers.Main){
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                try {

                    Log.d(TAG, "Intent created for MainActivity")
                    Log.d(TAG, "dados enviadospara MainActivity")
                    //intent.putExtra("FILE_DATA", AppData("h", ))
                    intent.putExtra(modulos, dataBasekey)
                    startActivity(intent)
                    Log.d(TAG, "MainActivity started successfully")
                    finish()
                    Log.d(TAG, "SplashActivity finished")
                } catch (e: Exception) {
                    Log.e(TAG, "Error starting MainActivity", e)
                }

            }
        }

        //Log.d(TAG, "URI carregada: $uriPathSalva")
        Log.d(TAG, "Enabling edge-to-edge display")
        enableEdgeToEdge()

        Log.d(TAG, "Setting content view to activity_splash layout")
        setContentView(R.layout.activity_splash)

        Log.d(TAG, "Setting up window insets listener")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splashLogo)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            Log.d(
                TAG,
                "Applying window insets - Left: ${systemBars.left}, Top: ${systemBars.top}, Right: ${systemBars.right}, Bottom: ${systemBars.bottom}"
            )
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d(TAG, "Setting up handler to start MainActivity after 300ms delay")
        Handler(Looper.getMainLooper()).postDelayed({
            Log.d(TAG, "Handler delay completed - Starting MainActivity")

        }, 300)

        Log.d(TAG, "onCreate() completed")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
    fun jsLoaderSetup(): String {
       return jsLoader.JsJuncao()
    }

}