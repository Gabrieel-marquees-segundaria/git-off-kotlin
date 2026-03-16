import os
import time
import re
from basic import  Search as Basic


class Search(Basic):


    def read_file(self, file: str):
        """ metodo que retorna o conteudo de um arquivo """
        file = os.path.join(self.dir, file)

        """
        <pre><code class="hljs bash">   $ python script.py --name João --age 25
   Nome: João
   Idade: 25
</code>
</pre>
        """
        if os.path.exists(file):
            with open(file, "r", encoding='utf-8') as f:
                texte = f.read()
            if file.endswith(".py"):
                return f'<pre><code class="hljs python">{texte}</code></pre>'
            if file.endswith(".js"):
                return f'<pre><code class="hljs javascript">{texte}</code></pre>'

            if file.endswith(".cjs"):
                return f'<pre><code class="hljs javascript">{texte}</code></pre>'
            if file.endswith(".html"):
                return f'<pre><code class="hljs javascript">{texte}</code></pre>'
            return texte

    def search_click(self, text: str) -> str:
        """ method que inicia a busca, utlizado quando o usuario clickar em uma pasta ou arquivo"""
        text = text.replace('\\', '')

        def _is(endpoint: list):
            """  algoritimo de busca"""
            path = self.dir
            dirs = []
            temp = ""
            for ind, item in enumerate(endpoint):
                temp = os.path.join(path, item)
                if os.path.isfile(temp):
                    path = temp
                    if ind == len(endpoint) - 1:
                        return [item]
                    continue
                elif os.path.isdir(temp):
                    path = temp
                    if ind == len(endpoint) - 1:
                        return os.listdir(temp)
                else:
                    sim = []
                    for i in os.listdir(path):
                        if item in i:
                            sim.append(i)
                    return sim
        endpoint = text.split("/")
        # print(endpoint)
        return _is(endpoint=endpoint)

    def search_file(self, txt: str, paths: str) -> str:
        txt = " " + txt.strip() + " "
        with open(paths, "r", encoding="utf-8") as f:
            text = f.read()
        return text

    def search_input(self, text: str) -> str:
        """ utlizado pela barra de pesquisa 
        args:
                text: definicao de  asunto arquivo ou codigo que o usuario procura
        """
        result = []
        listdirs = self.listdir

        def recusive_search(dirs: list, temp: str):
            """ algorimo de busca baseado em recursividade 
            args:
                    dirs: lista com os repositorios ou pastas e arquivos disponiveis.
                    temp: caminho temporario da pata atual

            """
            if len(text) == 0:
                return self.listdir
            for item in dirs:
                temp_item = os.path.join(temp, item)
                if os.path.isdir(temp_item):
                    if text.lower() in item.lower():
                        result.append(temp_item[len(self.dir)+1:])

                    recusive_search(
                        os.listdir(temp_item),
                        temp_item
                    )
                if os.path.isfile(temp_item):
                    if text.lower() in item.lower():
                        result.append(temp_item[len(self.dir) + 1:])

        recusive_search(listdirs, self.dir)
        return result


class SearchInArchive(Basic):
	""" >>> { "path": "path/path/file", "line": int, "result": "line: exemplo de pesquisa <strong> pesquisa </strong>"" }"""
	pass