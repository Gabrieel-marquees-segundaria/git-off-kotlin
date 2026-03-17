Aqui está a classe `TopBar`. Veja como usá-la:

---

**Instanciar (com opções opcionais)**
```js
const bar = new TopBar({
  bg: '#1a1a2e',   // cor de fundo
  color: '#fff',   // cor do texto
  height: '48px',  // altura
  pushBody: true   // empurra o conteúdo da página para baixo
});
```

**Adicionar elementos**
```js
const logo = document.createElement('span');
logo.textContent = '🚀 Meu App';

const btn = document.createElement('button');
btn.textContent = 'Login';

bar.add(logo, btn);   // aceita múltiplos elementos, é chainable
```

**Outros métodos**
```js
bar.remove(btn);          // remove um elemento específico
bar.clear();              // limpa tudo dentro da barra
bar.setStyle({ bg: '#000', height: '60px' }); // muda estilo em tempo real
bar.getElement();         // acesso direto à <div>
bar.destroy();            // remove a barra e restaura o padding do body
```

---

**Opções disponíveis no construtor:**

| Opção | Padrão | Descrição |
|---|---|---|
| `id` | `'top-bar'` | ID do elemento |
| `bg` | `'#1a1a2e'` | Cor de fundo |
| `color` | `'#fff'` | Cor do texto |
| `height` | `'48px'` | Altura da barra |
| `zIndex` | `'9999'` | Z-index |
| `padding` | `'0 16px'` | Padding interno |
| `gap` | `'12px'` | Espaço entre itens |
| `pushBody` | `true` | Empurra o body |
| `position` | `'fixed'` | `fixed` / `sticky` / `absolute` |