class gitDir {
  constructor(dir){
    this.dir = dir
  }
}
class gitFile {
  constructor(file){
    this.file = file
  }
}
class PathItem {
    constructor(path) {
        this.path = path;
    }
    path_type(path) {}
}

class UiRendetList {
    constructor(list) {
        this.list = list;
        this.repositorys = this.repo();
    }
    repo() {
        let langs = [];
        this.list.forEach(item => {
            let path = item.includes("/") ? item.slice(0, item.indexOf("/")) : item;
            if (langs.includes(path)) return;
            langs.push(path);
        });
        return langs;
    }
}
