


const btn = document.getElementById('toggleDark');
let theme = 'light'
const toggleDark =  () => {
  document.body.classList.toggle('dark');
  localStorage.setItem('tema', document.body.classList.contains('dark') ? 'dark' : 'light');

  theme =  document.body.classList.contains('dark') ? 'dark' : 'light'
};

// Ao carregar a página, restaurar preferência
if (localStorage.getItem('tema') === 'dark') {
    console.log("dark")
    theme = 'dark'
  document.body.classList.add('dark');
}
// toggleDark()
