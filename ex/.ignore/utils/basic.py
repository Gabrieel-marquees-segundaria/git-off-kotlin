import os
import time
import re


class Search:
    def __init__(self, listdirname: list, dir):
        self.dir: str = os.path.dirname(dir)  # caminho dos repositorios
        self.listdir: list = listdirname  # lista de repositorios

        # items ignorados nos repositorios
        self.fileiginore: list = ['gitignore' 'item-fake', '.venv']

# ".js", ".cjs",'requeriments.txt']
        temp = []
        for i, v in enumerate(self.listdir):
            if not v.startswith("."):
                temp.append(v)
        self.listdir = temp
        
    def sorted_dirs(self, dirs: list):
        dirs = sorted(dirs)
        dirs2 = []
        files = []
        for i in dirs:
            if "." in i:
                files.append(i)
            else:
                dirs2.append(i)
        return dirs2 + sorted(files)
        
    def ignore(self, path=True, file=True) -> None:
        """ responcavel por remover item indesejsdos das pesquisas e resultados como arquivos refente ao servidor ou o site ou arquivo secreto """
        temp = []
        for ind, dirr in enumerate(self.listdir):
            for ext in self.fileiginore:
                if ext in dirr or ext == dirr:
                    temp.append(dirr)
        for i in temp:
            self.listdir.remove(i)

        self.listdir = self.sorted_dirs(self.listdir)


