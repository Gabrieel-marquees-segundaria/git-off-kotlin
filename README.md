
# 📁 App de Visualização de Exemplos de Código

Este é um aplicativo Android que permite **ler arquivos HTML de exemplos de código** salvos localmente e exibi-los de forma interativa. Ideal para estudos, revisões e aprendizado prático de diversas linguagens de programação.

## 🚀 Funcionalidades

- 📂 Acesso a arquivos `.html` salvos em uma pasta de exemplos de código.
- 🌐 Exibição dos arquivos em uma WebView com suporte completo a HTML, CSS e JavaScript.
- 🔁 Comunicação bidirecional entre o código Kotlin (nativo Android) e JavaScript.
- 📚 Organização de exemplos por linguagem de programação.

## 💻 Linguagens Suportadas

O app foi criado com foco nas seguintes linguagens, mas funciona com qualquer conteúdo HTML:

- ✅ JavaScript
- ✅ Kotlin
- ✅ Python
- ✅ Linguagem C

## 📁 Estrutura esperada dos arquivos

O app permite selecionar uma pasta com os arquivos de exemplo, que devem estar organizados assim:

```

📂 exemplos/
├── javascript/
│    └── eventos.html
├── kotlin/
│    └── webview-interface.html
├── python/
│    └── ordenar-lista.html
└── c/
└── ponteiros.html

````

Cada arquivo `.html` pode conter:
- Código-fonte destacado com `<pre><code>`
- Explicações com HTML puro
- Estilos personalizados com CSS
- Scripts de demonstração com JavaScript

## 🛠️ Tecnologias Utilizadas

- **Kotlin** (Android)
- **Jetpack Compose** (layouts modernos)
- **WebView Android** com suporte a JavaScript
- **SAF (Storage Access Framework)** para acessar pastas locais
- **HTML/CSS/JS** para os arquivos de conteúdo

## 🔧 Como usar

1. Instale o app em um dispositivo Android.
2. Ao iniciar, selecione uma pasta com arquivos `.html`.
3. Os arquivos serão exibidos dentro do app, com visualização interativa.

## 📦 Como compilar

Clone este repositório e abra no Android Studio:

```bash
git clone https://github.com/seu-usuario/nome-do-repo.git
cd nome-do-repo
````

Depois disso:

* Execute o projeto em um dispositivo físico ou emulador.
* Certifique-se de ativar permissões de armazenamento, se necessário.

## 📸 Capturas de Tela

*(Adicione aqui prints do app em funcionamento, se quiser deixar mais visual)*

## 🙋‍♂️ Sobre o Projeto

Este app foi criado como uma ferramenta pessoal para organizar e visualizar exemplos de código que aprendi ao longo do tempo. Também serve como estudo prático de integração entre interfaces nativas Android e conteúdo web.

## 📝 Licença

Distribuído sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.


