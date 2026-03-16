## Como executar os testes localmente

### Build + montar APK de teste (este comando também valida a compilação dos testes):

./gradlew :app:assembleDebugAndroidTest
### todos os testes de instrumentação em um dispositivo/emulador conectado:
./gradlew :app:connectedDebugAndroidTest
### Executar apenas a classe de teste criada:
./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.g4br3.sitedentrodeapp.FileActivityTest
### Executar apenas um método específico (ex.: o teste do WebView):
./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.g4br3.sitedentrodeapp.FileActivityTest#webView_loads_local_file_and_injects_content_via_mostrarConteudo
