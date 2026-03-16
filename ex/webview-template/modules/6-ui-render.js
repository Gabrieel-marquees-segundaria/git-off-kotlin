/*

class UIController



    renderizarListaArquivos(arquivos) {
        let listaHtml = '<ul class="file-list">';
        arquivos.forEach(nome => {
            listaHtml += `<li class="file-item" onclick="lerArquivo('${nome}')" data-file="${nome}">
                <span>${nome}</span>
            </li>`;
        });
        listaHtml += "</ul>";

        this.resultadoEl.innerHTML = listaHtml;
    }

    renderizarConteudoArquivo(conteudo, nomeArquivo) {
        const htmlConteudo = `
            <button class="back-button" onclick="voltarParaLista()">
                ← Voltar para lista
            </button>
            <h3>📄 ${nomeArquivo}</h3>
            <div class="file-content">${Utils.escapeHtml(conteudo)}</div>
        `;

        this.resultadoEl.innerHTML = htmlConteudo;
    }

*/
const ui = new UIController();
const RCA = ui.renderizarConteudoArquivo;
const hjls_ = new Hljs();
let uiRendetList = null;

function newRenderizarConteudoArquivo(conteudo, nomeArquivo) {
    // if (nomeArquivo.endsWith(".md")) {
    //     conteudo = marked.parse(conteudo);
    // } else if (nomeArquivo.includes("html")) {
    //     conteudo = Utils.escapeHtml(conteudo);
    // } else if (nomeArquivo.endsWith(".py")) {
    //     conteudo = `<pre><code class="language-python"> ${conteudo}</code></pre>`;
    // } else if (nomeArquivo.endsWith(".js")) {
    //     conteudo = `<pre><code class="language-python"> ${conteudo}</code></pre>`;
    // } else {
    //     // conteudo = hjls_.parse(conteudo, nomeArquivo);
    // }
    const lang = getLangFromFile(nomeArquivo);
    if (nomeArquivo.endsWith(".md")) {
        conteudo = marked.parse(conteudo);
    } else if (nomeArquivo.includes("html")) {
        conteudo = Utils.escapeHtml(conteudo);
    } else if (lang) {
        conteudo = `<pre><code class="language-${lang}"> ${conteudo}</code></pre>`;
    }
    // document.querySelector("file-content").innerHTML = htmlConteudo;

    const modal = document.getElementById("fileModal");
    const modalTitle = document.getElementById("modalTitle");
    const fileContent = document.getElementById("fileContent");

    fileContent.innerHTML = conteudo || "nao a conteudo";
    modalTitle.textContent = nomeArquivo;
    modal.style.display = "block";

    // Add fade in animation
    modal.style.opacity = "0";
    setTimeout(() => {
        modal.style.opacity = "1";
    }, 10);
    setTimeout(() => {
        mostrarStatus("carregando Highlight no html");
        atualizarHighlight();
    }, 500);
}

function renderizarListaArquivos(arquivos) {
    
    try {
        const ulElement = document.querySelector(".file-list");
        console.log(ulElement);
        let html = "";
        
        arquivos.forEach(file => {
            if (file) {
                html += `<div class="file-item" onclick="getUriData('${
                    file.uri
                }', '${file.type}', '${file.name}', ${file.id})">
                    <span class="file-icon">${path_emoji.getEmojiForFile(
                        file.name
                    )}</span>
                    <span>${file.name}</span>
                </div>`;
            }
        });
        //listaHtml.innerHTML += html;
        /*
 ${path_emoji.getEmojiForFile(
                        file.name
                    )}
                    */
        ulElement.innerHTML = html;
    } catch (err) {
        console.error("Error:", err);
    }
}

UIController.prototype.renderizarConteudoArquivo = newRenderizarConteudoArquivo;
UIController.prototype.renderizarListaArquivos = renderizarListaArquivos;
