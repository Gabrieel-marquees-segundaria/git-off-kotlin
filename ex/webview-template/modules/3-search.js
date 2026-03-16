            // // Funcionalidade do botão de pesquisa
            // const searchBtn = document.getElementById("searchBtn");
            // const searchInput = document.getElementById("searchInput");

            // searchBtn.addEventListener("click", function () {
            //     if (searchInput.classList.contains("show")) {
            //         searchInput.classList.remove("show");
            //         searchBtn.innerHTML = "🔍";
            //     } else {
            //         searchInput.classList.add("show");
            //         searchInput.focus();
            //         searchBtn.innerHTML = "✖️";
            //     }
            // });

            // // Fechar pesquisa ao pressionar Escape
            // searchInput.addEventListener("keydown", function (e) {
            //     if (e.key === "Escape") {
            //         searchInput.classList.remove("show");
            //         searchBtn.innerHTML = "🔍";
            //     }
            // });

            // // Funcionalidade de pesquisa
            // searchInput.addEventListener("input", function () {
            //     const searchTerm = this.value.toLowerCase();
            //     const resultado = document.getElementById("resultado");
            //     const content = resultado.textContent;

            //     if (searchTerm && content) {
            //         // Destacar texto encontrado
            //         const regex = new RegExp(`(${searchTerm})`, "gi");
            //         const highlightedContent = content.replace(
            //             regex,
            //             "<mark>$1</mark>"
            //         );
            //         resultado.innerHTML = highlightedContent;
            //     } else {
            //         // Restaurar conteúdo original
            //         resultado.innerHTML = content;
            //     }
            // });

            // // Função para mostrar status
            // function showStatus(message, type = "info") {
            //     const status = document.getElementById("status");
            //     status.textContent = message;
            //     status.className = `status ${type}`;
            //     status.style.display = "block";

            //     // Auto-hide após 3 segundos
            //     setTimeout(() => {
            //         status.style.display = "none";
            //     }, 3000);
            // }

            // // Exemplo de uso
            // showStatus("Sistema inicializado com sucesso!", "success");