class IText {
  parse(conteudo) {
    throw new Error("method 'parse' is not defined");
  }
}

class Markdown extends IText {
  parse(md) {
    return marked.parse(md);
  }
}

class Text  extends IText{
  parse(txt) {
    return txt;
  }
}


class FileManger