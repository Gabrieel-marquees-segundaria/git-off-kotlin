 O Vite por padrão gera **apenas ESM** no build.

Mas dá pra mudar isso no `vite.config.js`:

```javascript
export default {
  build: {
    rollupOptions: {
      output: {
        format: "umd" // ou "umd", "cjs"
      }
    }
  }
}
```

**Formatos disponíveis:**

| Formato | Uso |
|---|---|
| `iife` | Script comum, sem módulos, funciona direto no browser |
| `umd` | Universal, funciona no browser e Node |
| `cjs` | CommonJS, só Node |
| `esm` | Padrão do Vite |

O **`iife`** é o que mais se aproxima de um JS comum — gera um arquivo único sem `import/export`, pronto pra usar com `<script src="...">`.