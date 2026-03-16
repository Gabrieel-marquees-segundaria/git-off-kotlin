// Elemento onde o código será inserido
const codeContainer = document.querySelector('#code-container');

// Função para carregar os dados
async function carregarDados() {
    try {
        // Substitua esta parte pela sua chamada real à API
        const response = await fetch('/api/codigo-exemplo');
        const codigo = await response.text();
        
        // Insere o código no container
        codeContainer.innerHTML = `
            <pre>
                <code class="language-javascript">
                    ${codigo}
                </code>
            </pre>
        `;
        
        // Aplica o highlight após inserir o código
        aplicarHighlight();
    } catch (error) {
        console.error('Erro ao carregar código:', error);
        codeContainer.innerHTML = '<p>Erro ao carregar o código</p>';
    }
}

// Função para aplicar o highlight
function aplicarHighlight() {
    // Verifica se o hljs já está disponível
    if (typeof hljs !== 'undefined') {
        hljs.highlightAll();
    } else {
        // Se não estiver disponível, carrega o script
        const script = document.createElement('script');
        script.src = 'https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.8.0/highlight.min.js';
        script.onload = () => hljs.highlightAll();
        document.head.appendChild(script);
        
        // Carrega o CSS do highlight.js
        const style = document.createElement('link');
        style.rel = 'stylesheet';
        style.href = 'https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.8.0/styles/default.min.css';
        document.head.appendChild(style);
    }
}

// Inicia o carregamento quando a página carregar
document.addEventListener('DOMContentLoaded', carregarDados);

// Para recarregar o highlight em caso de mudanças dinâmicas
function atualizarHighlight() {
    if (typeof hljs !== 'undefined') {
    	console.log("atualizado")
        hljs.highlightAll();
    }
}