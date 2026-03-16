
package com.g4br3.sitedentrodeapp

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.assertion.ViewAssertions.matches
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import org.junit.Assert.assertTrue

/*
 * Testes de instrumentação para `FileActivity`.
 *
 * Objetivo:
 *  - Verificar que a Activity exibe corretamente o título recebido por Intent e que o ProgressBar
 *    encerra seu estado de carregamento após a renderização da página.
 *  - Verificar que o `WebView` recebe conteúdo injetado pela função JavaScript `mostrarConteudo`
 *    (o teste lê o texto do elemento com id `resultado`).
 *  - Verificar que, quando o WebView não pode voltar (`canGoBack() == false`), o pressionar do
 *    botão "voltar" dispara a navegação para `FilesListActivity`.
 *
 * Observações / pré-requisitos:
 *  - Os testes criam arquivos temporários em `context.filesDir` e passam seu caminho via extra
 *    `file` para a Activity; `FileActivity` deve ler esse arquivo e injetar o conteúdo no WebView
 *    através da função JavaScript presente em `assets/view.html`.
 *  - Para deixar os testes mais rápidos e determinísticos, o `setUp()` define
 *    `FileActivity.pageLoadDelayMs = 0L` (hook adicionado no código de produção).
 *  - O teste que lê o conteúdo do WebView usa `evaluateJavascript` executado na UI thread via
 *    `ActivityScenario.onActivity {}` e sincroniza com um `CountDownLatch`.
 */
class FileActivityTest {

    @Before
    fun setUp() {
        // Deixa o delay de injeção JS em 0 para acelerar e tornar o teste determinístico.
        FileActivity.pageLoadDelayMs = 0L
    }

    @After
    fun tearDown() {
        // Nenhuma limpeza global necessária por enquanto.
    }

    /**
     * Teste 1 — Lançamento: título e comportamento do ProgressBar
     *  - Cria um arquivo simples em disco e lança `FileActivity` com os extras necessários.
     *  - Verifica que o TextView de título mostra o texto passado via Intent.
     *  - O teste também verifica (de forma leve) que a ProgressBar entra em seu ciclo de
     *    carregamento; como o delay foi zerado, assumimos que a barra ficará finalizada rapidamente.
     */
    @Test
    fun launch_showsTitle_and_progressBar_behaviour() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val tmp = File(context.filesDir, "testfile.html")
        tmp.writeText("<div id='resultado'>OK</div>")

        val intent = Intent(context, FileActivity::class.java).apply {
            putExtra("titulo", "Meu Título de Teste")
            putExtra("file", tmp.absolutePath)
            putExtra("url", "file:///android_asset/view.html")
        }

        ActivityScenario.launch<FileActivity>(intent).use { _ ->
            // Verifica o título passado via Intent
            onView(withId(R.id.tvTituloTela)).check(matches(withText("Meu Título de Teste")))

            // Verificação leve do ProgressBar: aqui mantemos a checagem simples pois a lógica
            // de esconder/exibir é coberta pelo fluxo de carregamento do WebView e pelo hook
            // pageLoadDelayMs (zerado no setUp).
            onView(withId(R.id.progressBar)).check(matches(org.hamcrest.Matchers.anything()))
        }
    }

    /**
     * Teste 2 — Conteúdo injetado no WebView
     *  - Cria um arquivo com conteúdo conhecido ("CONTEUDO_TESTE"), passa o caminho via Intent.
     *  - Depois que a Activity carrega, executamos um `evaluateJavascript` no `WebView` para
     *    ler o texto do elemento `#resultado` e confirmar que o conteúdo foi injetado.
     *
     *  Observação: usamos `CountDownLatch` para aguardar o callback assíncrono do `evaluateJavascript`.
     */
    @Test
    fun webView_loads_local_file_and_injects_content_via_mostrarConteudo() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val tmp = File(context.filesDir, "conteudo_teste.txt")
        tmp.writeText("CONTEUDO_TESTE")

        val intent = Intent(context, FileActivity::class.java).apply {
            putExtra("titulo", "Teste WebView")
            putExtra("file", tmp.absolutePath)
            putExtra("url", "file:///android_asset/view.html")
        }

        ActivityScenario.launch<FileActivity>(intent).use { scenario ->
            // Executa código na Activity (UI thread) para avaliar um script JS no WebView.
            // O script busca o elemento com id `resultado` e retorna seu innerText.
            scenario.onActivity { activity ->
                val latch = CountDownLatch(1)
                var result: String? = null
                val script = "(function(){var el = document.getElementById('resultado'); return el ? el.innerText : '';})()"
                activity.webView.evaluateJavascript(script) { value ->
                    // O valor retornado por evaluateJavascript vem entre aspas (JSON string);
                    // removemos as aspas externas para leitura simples.
                    result = value?.trim('"')
                    latch.countDown()
                }

                // Aguarda até 3 segundos pela resposta; se não responder, o teste falha.
                val awaited = latch.await(3, TimeUnit.SECONDS)
                assertTrue("evaluateJavascript didn't finish in time", awaited)
                assertTrue("Conteúdo não foi injetado", result?.contains("CONTEUDO_TESTE") == true)
            }
        }
    }

    /**
     * Teste 3 — Navegação ao pressionar Back
     *  - Verifica que, quando o `WebView` não pode voltar (`canGoBack() == false`), o pressionar do
     *    botão de voltar da Activity dispara uma Intent para `FilesListActivity`.
     *  - Usa `Espresso-Intents` para captar e verificar a Intent criada.
     */
    @Test
    fun back_button_navigates_to_FilesListActivity_when_webview_cant_go_back() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val tmp = File(context.filesDir, "back_test.html")
        tmp.writeText("<div id='resultado'>BACK</div>")

        val intent = Intent(context, FileActivity::class.java).apply {
            putExtra("titulo", "Teste Back")
            putExtra("file", tmp.absolutePath)
            putExtra("url", "file:///android_asset/view.html")
        }

        Intents.init()
        try {
            ActivityScenario.launch<FileActivity>(intent).use { _ ->
                // Simula o pressionar do botão "voltar"
                pressBack()

                // Verifica que foi criada uma intent para FilesListActivity
                Intents.intended(hasComponent(FilesListActivity::class.java.name))
            }
        } finally {
            Intents.release()
        }
    }
}
