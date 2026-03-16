// let text = "tee\\tte\\ff\\'".startsWith("\\")

// console.log(text.replaceAll("\\", '\\')

// texto= "Aqui tem uma barra\\ e \" também";
// var novoTexto = "";
// for (var i = 0; i < texto.length; i++) if (texto[i] !== "\"" && texto[i] !== "\\") novoTexto += texto[i];
// console.log(novoTexto);

// var str = "bonjour le monde vive le javascript";
// var arr = ['bonjour','europe', 'c++'];

// function contains(target, pattern){
//     var value = 0;
//     pattern.forEach(function(word){
//       value = value + target.includes(word);
//     });
//     return (value === 1)
// }

// console.log(contains(str, arr));

// // Importando o marked
// import { marked } from 'marked';

// // Configurando o renderer personalizado
// const renderer = new marked.Renderer();

// // Personalizando o renderer para elementos code
// renderer.code = function(code, language) {
//   // Adiciona classes tanto ao pre quanto ao code
//   return `<pre class="my-pre-class">
//     <code class="my-code-class language-${language}">${code}</code>
//   </pre>`;
// };

// // Personalizando o renderer para elementos code inline
// renderer.codespan = function(code) {
//   return `<code class="my-inline-code-class">${code}</code>`;
// };

// // Configurando o marked para usar o renderer personalizado
// marked.setOptions({
//   renderer: renderer,
//   highlight: function(code, lang) {
//     // Aqui você pode adicionar um highlighter como highlight.js ou prism.js
//     return code;
//   }
// });

// // Exemplo de uso
// const markdown = `
// # Exemplo de código

// \`\`\`javascript
// function exemplo() {
//   console.log("Olá mundo!");
// }
// \`\`\`

// E aqui temos um \`código inline\` como exemplo.
// `;

// // Convertendo markdown para HTML com as classes personalizadas
// const htmlOutput = marked(markdown);
// console.log(htmlOutput);

// function endsWithAll(
//   target,
//   pattern = [".md", ".js", ".cjs", ".py", ".add", ".txt"]
// ) {
//   var value = 0;
//   pattern.forEach(function (word) {
//     value = value + target.endsWith(word);
//   });
//   return value === 1;
// }

// let pdf = "flex.pdf";

// if (endsWithAll(pdf)) {
//   console.log(endsWithAll(pdf));
// } else if (pdf.endsWith(".pdf")) {
//   console.log("is pdf")
// }
// else {
// 	console.log("else");
// }

let path = "path0/path1/path2/ir.pdf";
// console.log(path.replace("/"+path.split("/").pop(), ""));
console.log(path.slice(0, path.lastIndexOf("/")));