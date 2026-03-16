document.addEventListener(
  "backbutton",
  function (event) {
    event.preventDefault();
    alert("Botão voltar pressionado!");
  },
  false
);

history.pushState(null, null, location.href);
window.addEventListener("popstate", function () {
  history.pushState(null, null, location.href);
  alert("Use o botão dentro do app para sair!");
});

history.pushState(null, null, location.href);
window.onpopstate = function () {
  history.pushState(null, null, location.href);
  alert("Use o botão de dentro do site para sair!");
};



document.addEventListener("DOMContentLoaded", function () {
  document.addEventListener(
    "backbutton",
    function (event) {
      event.preventDefault();
      alert("Botão voltar pressionado!");
    },
    false
  );
});
