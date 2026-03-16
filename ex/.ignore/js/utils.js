export const socket = io();

export const cookies_name = "repository";

export function repoInstance() {
  const $repo = document.getElementById("repo")
    ? document.getElementById("repo")
    : document.getElementById("files");
  while ($repo.firstChild) {
    $repo.removeChild($repo.firstChild);
  }
  return $repo;
}

export function includesAll(
  target,
  pattern = [".md", ".js", ".cjs", ".py", ".add", ".txt", '.html']
) {
  var value = 0;
  pattern.forEach(function (word) {
    value = value + target.includes(word);
  });
  return value === 1;
}
export function endsWithAll(
  target,
  pattern = [".md", ".js", ".cjs", ".py", ".add", ".txt",  '.html']
) {
  var value = 0;
  pattern.forEach(function (word) {
    value = value + target.endsWith(word);
  });
  return value === 1;
}
export function popup(name, repository = null) {
  const $popup = document.createElement("div");
  $popup.classList.add("popup");
  const p = document.createElement("p");
  p.textContent = "Deseja continuar de onde parou?";
  const nao = document.createElement("button");
  nao.classList.add("nao");
  const sim = document.createElement("button");
  sim.classList.add("sim");
  nao.addEventListener("click", () => {
    localStorage.removeItem(name);
    $popup.style.display = "none";
    location.href = "/";
  });
  sim.textContent = "Yes";
  nao.textContent = "No";
  sim.addEventListener("click", () => {
    $popup.style.display = "none";
  });

  $popup.appendChild(p);
  $popup.appendChild(sim);
  $popup.appendChild(nao);

  document.body.appendChild($popup);
  console.log("popup");
}

document.getElementById("home-link").addEventListener("click", () => {
  localStorage.removeItem("repository");
});

// Função para aplicar o highlight
export function aplicarHighlight() {
  // Verifica se o hljs já está disponível
  if (typeof hljs !== "undefined") {
    hljs.highlightAll();
  } else {
    // Se não estiver disponível, carrega o script
    const script = document.createElement("script");
    script.src = "../js/highlight.min.js";
    script.onload = () => hljs.highlightAll();
    document.head.appendChild(script);

    // Carrega o CSS do highlight.js
    const style = document.createElement("link");
    style.rel = "stylesheet";
    style.href = "./.ignore/css/highlightjs-default.min.css";
    document.head.appendChild(style);
  }
}

socket.on("connect", () => {
  console.log("Connected to server");
});

socket.on("disconnect", () => {
  console.log("Disconnected from server");
});

socket.on("read_file", (data) => {
  console.log(data);
  const repo = repoInstance();
  const fileRead = document.createElement("div");
  const fullscreen = document.createElement("button");
  const $return = document.createElement("button");
  fullscreen.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
  <!-- Background (transparent) -->
  <rect width="24" height="24" fill="none"/>
  
  <!-- Corners representing fullscreen -->
  <!-- Top Left -->
  <path d="M3 3v6h2V5h4V3H3z" fill="currentColor"/>
  
  <!-- Top Right -->
  <path d="M19 3h-6v2h4v4h2V3z" fill="currentColor"/>
  
  <!-- Bottom Left -->
  <path d="M5 19v-4H3v6h6v-2H5z" fill="currentColor"/>
  
  <!-- Bottom Right -->
  <path d="M19 15h-2v4h-4v2h6v-6z" fill="currentColor"/>
</svg>`;
  let is_fullscreen = false;
  fullscreen.classList.add("fullscreenbutton");
  $return.classList.add("returnbutton");
  $return.innerHTML = `<?xml version="1.0" encoding="utf-8"?>

<!-- Uploaded to: SVG Repo, www.svgrepo.com, Generator: SVG Repo Mixer Tools -->
<svg width="30px" height="30px" viewBox="0 0 48 48" xmlns="http://www.w3.org/2000/svg" >

<path d="M0 0h48v48H0z" fill="none"/>
<g id="Shopicon">
	<path d="M10,22v2c0,7.72,6.28,14,14,14s14-6.28,14-14s-6.28-14-14-14h-6.662l3.474-4.298l-3.11-2.515L10.577,12l7.125,8.813
		l3.11-2.515L17.338,14H24c5.514,0,10,4.486,10,10s-4.486,10-10,10s-10-4.486-10-10v-2H10z"/>
</g>
</svg>`;
  fullscreen.addEventListener("click", () => {
    if (!is_fullscreen) {
      repo.classList.add("fullscreen");
      $return.style.top = "70px";
      is_fullscreen = true;
    } else {
      is_fullscreen = false;
      repo.classList.remove("fullscreen");
      $return.style.top = "150px";
    }
  });
  $return.addEventListener("click", () => {
    let path = localStorage.getItem("repository");
    localStorage.setItem("repository", path.slice(0, path.lastIndexOf("/")));
    location.reload();
  });
  repo.appendChild(fullscreen);
  repo.appendChild($return);
  if (includesAll(data.filename)) {
    console.log("marked reading", data);
    fileRead.innerHTML = marked.parse(data.text);
  } else if (data.filename.endsWith(".pdf")) {
    console.log("pdf reading: ", data);
  } else {
    fileRead.textContent = data.text;
    console.log("not marked reading");
  }
  if (data.filename.includes("txt")) {
    console.log("txt reading");
  }

  fileRead.classList.add("readFile");

  repo.appendChild(fileRead);
  setTimeout(() => {
    aplicarHighlight();
  }, 1000);
});

export function requestTypePath(repository) {
  console.log("requestTypePath", repository);
  if (endsWithAll(repository)) {
    console.log("read_file");
    socket.emit("read_file", { name: repository });
  } else if (repository.endsWith(".pdf")) {
    location.href = `/PDF`;
    //     fetch("/pdf", {
    //       method: "POST",
    //       headers: {
    //         "Content-Type": "application/pdf",
    //         path: repository
    //       }
    //     })
    //       .then(responce => responce.blob())
    //       .then(blob => {
    //         console.log(blob);
    //         const $pdf = document.createElement("embed");
    //         const urlimage = URL.createObjectURL(blob);
    //         $pdf.setAttribute("type", "application/pdf");
    //         $pdf.setAttribute("src", urlimage);
    //         const $repo = repoInstance();
    //         $pdf.classList.add("pdf");
    //         $repo.appendChild($pdf);
    //         const $exit = document.createElement("button");
    //         $exit.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
    //   <!-- Simple arrow pointing left -->
    //   <path d="M19 12H5" stroke="currentColor" fill="none" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
    //   <path d="M12 19L5 12L12 5" stroke="currentColor" fill="none" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
    // </svg>`;
    //         $exit.classList.add("exit");
    //         $exit.addEventListener("click", () => {
    //           localStorage.setItem(
    //             "repository",
    //             repository.replace("/" + repository.split("/").pop(), "")

    //           );
    //           location.href = "/repository"
    //         });
    //         $repo.appendChild($exit);
    //       });
  } else {
    socket.emit("search_click", { repository: String(repository) });
  }
}

// socket.on("read_file", data => {
//   const ul = document.getElementById("files");
//   const main = document.getElementById("main");
//   main.removeChild(ul);
//   const fileRead = document.createElement("div");
//   if (includesAll(data.filename)) {
//   fileRead.innerHTML = marked.parse(data.text);
//   } else {
//   fileRead.textContent = data.text;
//   }
//   fileRead.classList.add("readFile");

//   main.appendChild(fileRead);
//   setTimeout(aplicarHighlight, 1000)
//   })

// if (localStorage.getItem(cookies_name) == null || localStorage.getItem(cookies_name) == '') {
// 	window.location.href = '/'
// }
// else {
//  // popup(cookies_name);

//   const repository = String(localStorage.getItem("repository"));
//   if (includesAll(repository)) {
//     socket.emit("read_file", { name: repository });
//   }else if (repository.endsWith(".pdf")) {
//     fetch('/pdf/'+repository).then(responce => responce.blob()).then(blob => {
//       const $pdf = document.createElement("embed");
//       const urlimage = URL.createObjectURL(blob);
//       $pdf.setAttribute("type", "application/pdf");
//       $pdf.setAttribute("src", urlimage);
//     })
//   }

//   else {
//     socket.emit("search_click", { repository: String(repository) });
//   }
// }
