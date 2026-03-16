 



const _hljs  =new Hljs()



function files_ext_parse(conteudo, fileName){
    if (fileName.endsWith(".md")){
       return marked.parse(conteudo);
    }
    else if (fileName.endsWith(".txt")){
        console.log(".txt")
        return marked.parse(conteudo);
    }
    else {
      return _hljs.parse(conteudo, fileName)
    }
}
 
 
 
 function mostrarConteudo(conteudo, fileName) {
            document.getElementById("resultado").innerHTML = files_ext_parse(conteudo, fileName)
            hljs.highlightAll();     
        }