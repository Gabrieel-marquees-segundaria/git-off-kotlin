const socket = io();

import {
  includesAll,
  popup,
  cookies_name,
  aplicarHighlight,
  endsWithAll,
  requestTypePath
} from "./utils.js";
// import {marked} from "./js/marked.esm.js"
socket.on("connect", () => {
  console.log("Connected to server");
});
const mainId = document.getElementById("main");
const ul = document.getElementById("repositorys");
socket.on("disconnect", () => {
  console.log("Disconnected from server");
});
function createList(list) {
  const repo = document.createElement("li");
  const link = document.createElement("a");
  repo.classList.add("repository-li");
  while (ul.firstChild) {
    ul.removeChild(ul.firstChild);
  }
  list.forEach(text => {
    console.log(text);
    const clone = repo.cloneNode(true);
    const linkclone = link.cloneNode(true);
    linkclone.textContent = text;
    clone.appendChild(linkclone);
    clone.addEventListener("click", () => {
      if (text.includes(".")) {
        console.log(text);
        requestTypePath(text)
        let lsg = localStorage.getItem(cookies_name);
        localStorage.setItem(
          cookies_name,
          lsg ? String(lsg) + "/" + text : text
        );
      } else {
        localStorage.setItem("repository", text);

        window.location.href = "repository";
      }
    });
    ul.appendChild(clone);
  });
  ul.appendChild(repo);
}

if (!localStorage.getItem("repository")) {
  socket.on("repositorys", repositorys => {
    console.log("New repositorys", repositorys);
    createList(repositorys["repositorys"]);
  });
} else {
  popup(cookies_name);

  const repository = String(localStorage.getItem("repository"));
  requestTypePath(repository);
}
socket.on("result1", data => {
  //search_input
  console.log(data);
});
socket.on("result2", data => {
  // search_click
  createList(data.result);
  console.log(data);
});

socket.emit("repositorys", { request: "repositorys" });

const search_input = document.getElementById("search-input");
search_input.addEventListener("input", () => {
  socket.emit("search_input", { texto: search_input.value.toLowerCase() });
});
socket.on("result1", data => {
  createList(data.result);
});

// const renderer = new marked.Renderer();

// renderer.paragraph = text => {
//   return `<p class="minha-classe">${text}</p>`;
// };

// const markdownText = "Este é um parágrafo.";
// const html = marked(markdownText, { renderer });

// console.log(html);
