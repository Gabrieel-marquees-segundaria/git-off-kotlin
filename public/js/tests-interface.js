/**
 * TopBar
 * Injeta uma barra fixa no topo do <body> e expõe métodos
 * para adicionar / remover elementos dentro dela.
 * @example
 * Uso básico:
 *   const bar = new TopBar({ bg: '#111', height: '48px' });
 *   bar.add(meuElemento);
 *   bar.remove(meuElemento);
 *   bar.destroy();
 */
class TopBar {
  /**
   * @param {object}  [options]
   * @param {string}  [options.id='top-bar']        – id do container
   * @param {string}  [options.bg='#1a1a2e']         – cor de fundo
   * @param {string}  [options.color='#fff']          – cor do texto padrão
   * @param {string}  [options.height='48px']         – altura da barra
   * @param {string}  [options.zIndex='9999']         – z-index
   * @param {string}  [options.padding='0 16px']      – padding interno
   * @param {string}  [options.gap='12px']            – espaço entre itens
   * @param {boolean} [options.pushBody=true]         – empurra o body para baixo
   * @param {string}  [options.position='fixed']      – 'fixed' | 'sticky' | 'absolute'
   */
  constructor(options = {}) {
    this._cfg = Object.assign(
      {
        id: 'top-bar',
        bg: '#1a1a2e',
        color: '#fff',
        height: '48px',
        zIndex: '9999',
        padding: '0 16px',
        gap: '12px',
        pushBody: true,
        position: 'fixed',
      },
      options
    );

    this._bar = null;
    this._originalBodyPaddingTop = null;
    this._init();
  }

  /* ─────────────────────────── privado ─────────────────────────── */

  _init() {
    // Evita duplicatas
    const existing = document.getElementById(this._cfg.id);
    if (existing) existing.remove();

    const bar = document.createElement('div');
    bar.id = this._cfg.id;

    Object.assign(bar.style, {
      position: this._cfg.position,
      top: '0',
      left: '0',
      width: '100%',
      height: this._cfg.height,
      background: this._cfg.bg,
      color: this._cfg.color,
      zIndex: this._cfg.zIndex,
      padding: this._cfg.padding,
      boxSizing: 'border-box',
      display: 'flex',
      alignItems: 'center',
      gap: this._cfg.gap,
    });

    document.body.prepend(bar);
    this._bar = bar;

    if (this._cfg.pushBody && this._cfg.position === 'fixed') {
      this._originalBodyPaddingTop = document.body.style.paddingTop;
      document.body.style.paddingTop = this._cfg.height;
    }
  }

  /* ─────────────────────────── público ─────────────────────────── */

  /**
   * Adiciona um ou mais elementos à barra.
   * @param {...HTMLElement} elements
   * @returns {TopBar} this (chainable)
   */
  add(...elements) {
    elements.forEach(el => this._bar.appendChild(el));
    return this;
  }

  /**
   * Remove um elemento da barra (sem destruí-lo no DOM externo).
   * @param {HTMLElement} element
   * @returns {TopBar} this
   */
  remove(element) {
    if (this._bar.contains(element)) this._bar.removeChild(element);
    return this;
  }

  /**
   * Limpa todos os filhos da barra.
   * @returns {TopBar} this
   */
  clear() {
    this._bar.innerHTML = '';
    return this;
  }

  /**
   * Retorna o elemento DIV da barra para manipulação direta.
   * @returns {HTMLElement}
   */
  getElement() {
    return this._bar;
  }

  /**
   * Atualiza propriedades visuais em tempo real.
   * @param {object} props – subconjunto das opções do construtor
   * @returns {TopBar} this
   */
  setStyle(props = {}) {
    const map = {
      bg: 'background',
      color: 'color',
      height: 'height',
      zIndex: 'zIndex',
      padding: 'padding',
      gap: 'gap',
    };
    Object.entries(props).forEach(([key, val]) => {
      const cssProp = map[key] || key;
      this._bar.style[cssProp] = val;
    });
    return this;
  }

  /**
   * Remove a barra do DOM e restaura o padding original do body.
   */
  destroy() {
    if (this._bar) {
      this._bar.remove();
      this._bar = null;
    }
    if (this._cfg.pushBody && this._originalBodyPaddingTop !== null) {
      document.body.style.paddingTop = this._originalBodyPaddingTop;
    }
  }
}

export const topBar = new TopBar()