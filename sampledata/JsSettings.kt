
package com.g4br3.sitedentrodeapp.components

class JsSettings {


var script: String = """
            // Sistema de Menu de Configurações Flutuante
            class FloatingSettingsMenu {
                constructor() {
                    this.isMenuOpen = false;
                    this.settings = {
                        theme: 'light',
                        fontSize: 'medium',
                        notifications: true,
                        autoSave: true,
                        userName: ''
                    };

                    this.init();
                }

                init() {
                    this.createStyles();
                    this.createMenuButton();
                    this.createMenu();
                    this.attachEventListeners();
                }

                createStyles() {
                    const style = document.createElement('style');
                    style.textContent = `
                        /* Estilos do menu flutuante */
                        .floating-settings-container {
                            position: fixed;
                            top: 20px;
                            right: 20px;
                            z-index: 10000;
                            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                        }

                        .settings-toggle-btn {
                            width: 50px;
                            height: 50px;
                            border-radius: 50%;
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            border: none;
                            cursor: pointer;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            box-shadow: 0 4px 20px rgba(0,0,0,0.15);
                            transition: all 0.3s ease;
                            color: white;
                            font-size: 18px;
                        }

                        .settings-toggle-btn:hover {
                            transform: scale(1.1);
                            box-shadow: 0 6px 25px rgba(0,0,0,0.2);
                        }

                        .settings-toggle-btn.active {
                            background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
                            transform: rotate(180deg);
                        }

                        .settings-menu {
                            position: absolute;
                            top: 60px;
                            right: 0;
                            width: 280px;
                            background: white;
                            border-radius: 12px;
                            box-shadow: 0 10px 40px rgba(0,0,0,0.15);
                            opacity: 0;
                            visibility: hidden;
                            transform: translateY(-10px);
                            transition: all 0.3s ease;
                            border: 1px solid #e0e0e0;
                        }

                        .settings-menu.open {
                            opacity: 1;
                            visibility: visible;
                            transform: translateY(0);
                        }

                        .settings-header {
                            padding: 20px;
                            border-bottom: 1px solid #f0f0f0;
                            background: linear-gradient(135deg, #f8f9ff 0%, #f0f2ff 100%);
                            border-radius: 12px 12px 0 0;
                        }

                        .settings-title {
                            margin: 0;
                            font-size: 16px;
                            font-weight: 600;
                            color: #333;
                            display: flex;
                            align-items: center;
                            gap: 8px;
                        }

                        .settings-content {
                            padding: 20px;
                            max-height: 400px;
                            overflow-y: auto;
                        }

                        .setting-item {
                            display: flex;
                            align-items: center;
                            justify-content: space-between;
                            padding: 12px 0;
                            border-bottom: 1px solid #f5f5f5;
                        }

                        .setting-item:last-child {
                            border-bottom: none;
                        }

                        .setting-label {
                            font-size: 14px;
                            color: #333;
                            font-weight: 500;
                        }

                        .setting-description {
                            font-size: 12px;
                            color: #666;
                            margin-top: 2px;
                        }

                        .setting-control {
                            display: flex;
                            align-items: center;
                            gap: 8px;
                        }

                        .toggle-switch {
                            position: relative;
                            width: 44px;
                            height: 24px;
                            background: #ddd;
                            border-radius: 12px;
                            cursor: pointer;
                            transition: background 0.3s ease;
                        }

                        .toggle-switch.active {
                            background: #4CAF50;
                        }

                        .toggle-switch::before {
                            content: '';
                            position: absolute;
                            width: 20px;
                            height: 20px;
                            background: white;
                            border-radius: 50%;
                            top: 2px;
                            left: 2px;
                            transition: transform 0.3s ease;
                            box-shadow: 0 2px 4px rgba(0,0,0,0.2);
                        }

                        .toggle-switch.active::before {
                            transform: translateX(20px);
                        }

                        .setting-select {
                            padding: 6px 12px;
                            border: 1px solid #ddd;
                            border-radius: 6px;
                            font-size: 14px;
                            background: white;
                            cursor: pointer;
                            min-width: 80px;
                        }

                        .setting-select:focus {
                            outline: none;
                            border-color: #667eea;
                        }

                        .setting-input {
                            padding: 8px 12px;
                            border: 1px solid #ddd;
                            border-radius: 6px;
                            font-size: 14px;
                            background: white;
                            min-width: 120px;
                            transition: border-color 0.2s ease;
                        }

                        .setting-input:focus {
                            outline: none;
                            border-color: #667eea;
                            box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
                        }

                        .setting-input::placeholder {
                            color: #999;
                        }

                        .settings-footer {
                            padding: 15px 20px;
                            border-top: 1px solid #f0f0f0;
                            background: #fafafa;
                            border-radius: 0 0 12px 12px;
                            display: flex;
                            gap: 10px;
                        }

                        .settings-btn {
                            flex: 1;
                            padding: 8px 16px;
                            border: none;
                            border-radius: 6px;
                            font-size: 14px;
                            font-weight: 500;
                            cursor: pointer;
                            transition: all 0.2s ease;
                        }

                        .settings-btn.primary {
                            background: #667eea;
                            color: white;
                        }

                        .settings-btn.primary:hover {
                            background: #5a6fd8;
                        }

                        .settings-btn.secondary {
                            background: #f5f5f5;
                            color: #666;
                        }

                        .settings-btn.secondary:hover {
                            background: #e0e0e0;
                        }

                        /* Responsivo */
                        @media (max-width: 320px) {
                            .settings-menu {
                                width: 260px;
                                right: -10px;
                            }
                        }
                    `;

                    document.head.appendChild(style);
                }

                createMenuButton() {
                    this.container = document.createElement('div');
                    this.container.className = 'floating-settings-container';

                    this.toggleBtn = document.createElement('button');
                    this.toggleBtn.className = 'settings-toggle-btn';
                    this.toggleBtn.innerHTML = '⚙️';
                    this.toggleBtn.title = 'Configurações';

                    this.container.appendChild(this.toggleBtn);
                    document.body.appendChild(this.container);
                }

                createMenu() {
                    this.menu = document.createElement('div');
                    this.menu.className = 'settings-menu';

                    this.menu.innerHTML = `
                        <div class="settings-header">
                            <h3 class="settings-title">
                                ⚙️ Configurações
                            </h3>
                        </div>

                        <div class="settings-content">
                            <div class="setting-item">
                                <div>
                                    <div class="setting-label">pagina Home</div>
                                    <div class="setting-description">Digite a sua pagina Home para personalização</div>
                                </div>
                                <div class="setting-control">
                                    <input type="text" class="setting-input" data-setting="userName" placeholder="Sea url..." maxlength="50">
                                </div>
                            </div>

                            <div class="setting-item">
                                <div>
                                    <div class="setting-label">Tema</div>
                                    <div class="setting-description">Escolha o tema da interface</div>
                                </div>
                                <div class="setting-control">
                                    <select class="setting-select" data-setting="theme">
                                        <option value="light">Claro</option>
                                        <option value="dark">Escuro</option>
                                        <option value="auto">Automático</option>
                                    </select>
                                </div>
                            </div>

                            <div class="setting-item">
                                <div>
                                    <div class="setting-label">Tamanho da Fonte</div>
                                    <div class="setting-description">Ajuste o tamanho do texto</div>
                                </div>
                                <div class="setting-control">
                                    <select class="setting-select" data-setting="fontSize">
                                        <option value="small">Pequeno</option>
                                        <option value="medium">Médio</option>
                                        <option value="large">Grande</option>
                                    </select>
                                </div>
                            </div>

                            <div class="setting-item">
                                <div>
                                    <div class="setting-label">Notificações</div>
                                    <div class="setting-description">Receber notificações do sistema</div>
                                </div>
                                <div class="setting-control">
                                    <div class="toggle-switch" data-setting="notifications"></div>
                                </div>
                            </div>

                            <div class="setting-item">
                                <div>
                                    <div class="setting-label">Salvamento Automático</div>
                                    <div class="setting-description">Salvar alterações automaticamente</div>
                                </div>
                                <div class="setting-control">
                                    <div class="toggle-switch" data-setting="autoSave"></div>
                                </div>
                            </div>
                        </div>

                        <div class="settings-footer">
                            <button class="settings-btn secondary" data-action="reset">Resetar</button>
                            <button class="settings-btn primary" data-action="save">Salvar</button>
                        </div>
                    `;

                    this.container.appendChild(this.menu);
                    this.loadSettings();
                }

                attachEventListeners() {
                    // Toggle do menu
                    this.toggleBtn.addEventListener('click', (e) => {
                        e.stopPropagation();
                        this.toggleMenu();
                    });

                    // Fechar menu ao clicar fora
                    document.addEventListener('click', (e) => {
                        if (!this.container.contains(e.target) && this.isMenuOpen) {
                            this.closeMenu();
                        }
                    });

                    // Prevenir fechamento ao clicar no menu
                    this.menu.addEventListener('click', (e) => {
                        e.stopPropagation();
                    });

                    // Controles do menu
                    this.menu.addEventListener('change', (e) => {
                        const setting = e.target.dataset.setting;
                        if (setting) {
                            this.settings[setting] = e.target.value;
                            this.applySettings();
                        }
                    });

                    this.menu.addEventListener('input', (e) => {
                        const setting = e.target.dataset.setting;
                        if (setting) {
                            this.settings[setting] = e.target.value;
                            this.applySettings();
                        }
                    });

                    this.menu.addEventListener('click', (e) => {
                        const setting = e.target.dataset.setting;
                        const action = e.target.dataset.action;

                        if (setting && e.target.classList.contains('toggle-switch')) {
                            this.settings[setting] = !this.settings[setting];
                            this.updateToggle(e.target, this.settings[setting]);
                            this.applySettings();
                        }

                        if (action === 'save') {
                            this.saveSettings();
                            this.showFeedback('Configurações salvas!');
                        }

                        if (action === 'reset') {
                            this.resetSettings();
                            this.showFeedback('Configurações resetadas!');
                        }
                    });
                }

                toggleMenu() {
                    this.isMenuOpen = !this.isMenuOpen;
                    this.menu.classList.toggle('open', this.isMenuOpen);
                    this.toggleBtn.classList.toggle('active', this.isMenuOpen);
                }

                closeMenu() {
                    this.isMenuOpen = false;
                    this.menu.classList.remove('open');
                    this.toggleBtn.classList.remove('active');
                }

                updateToggle(toggle, active) {
                    toggle.classList.toggle('active', active);
                }

                loadSettings() {
                    // Carregar configurações do localStorage (se disponível)
                    try {
                        const saved = localStorage.getItem('floatingMenuSettings');
                        if (saved) {
                            this.settings = { ...this.settings, ...JSON.parse(saved) };
                        }
                    } catch (e) {
                        console.log('Configurações não puderam ser carregadas');
                    }

                    // Aplicar configurações aos controles
                    const selects = this.menu.querySelectorAll('.setting-select');
                    selects.forEach(select => {
                        const setting = select.dataset.setting;
                        if (this.settings[setting]) {
                            select.value = this.settings[setting];
                        }
                    });

                    const inputs = this.menu.querySelectorAll('.setting-input');
                    inputs.forEach(input => {
                        const setting = input.dataset.setting;
                        if (this.settings[setting] !== undefined) {
                            input.value = this.settings[setting];
                        }
                    });

                    const toggles = this.menu.querySelectorAll('.toggle-switch');
                    toggles.forEach(toggle => {
                        const setting = toggle.dataset.setting;
                        if (this.settings[setting] !== undefined) {
                            this.updateToggle(toggle, this.settings[setting]);
                        }
                    });

                    this.applySettings();
                }

                saveSettings() {
                    try {
                        localStorage.setItem('floatingMenuSettings', JSON.stringify(this.settings));
                    } catch (e) {
                        console.log('Configurações não puderam ser salvas');
                    }
                }

                resetSettings() {
                    this.settings = {
                        theme: 'light',
                        fontSize: 'medium',
                        notifications: true,
                        autoSave: true,
                        userName: ''
                    };

                    this.loadSettings();
                }

                applySettings() {
                    // Aplicar tema
                    document.body.classList.remove('theme-light', 'theme-dark');
                    document.body.classList.add(`theme-${'$'}{this.settings.theme}`);

                    // Aplicar tamanho da fonte
                    document.body.classList.remove('font-small', 'font-medium', 'font-large');
                    document.body.classList.add(`font-${'$'}{this.settings.fontSize}`);

                    // Aplicar nome do usuário (exemplo: pode ser usado para personalizar a interface)
                    if (this.settings.userName) {
                        document.body.setAttribute('data-user', this.settings.userName);
                        // Exemplo: atualizar título da página com o nome
                        const originalTitle = document.title.replace(/ - .+${'$'}/, '');
                        document.title = `${'$'}{originalTitle} - ${'$'}{this.settings.userName}`;
                    } else {
                        document.body.removeAttribute('data-user');
                        // Restaurar título original
                        document.title = document.title.replace(/ - .+${'$'}/, '');
                    }

                    // Aplicar outras configurações conforme necessário
                    console.log('Configurações aplicadas:', this.settings);
                }

                showFeedback(message) {
                    const feedback = document.createElement('div');
                    feedback.style.cssText = `
                        position: fixed;
                        top: 80px;
                        right: 20px;
                        background: #4CAF50;
                        color: white;
                        padding: 12px 20px;
                        border-radius: 6px;
                        font-size: 14px;
                        z-index: 10001;
                        box-shadow: 0 4px 20px rgba(0,0,0,0.15);
                        animation: slideIn 0.3s ease;
                    `;

                    feedback.textContent = message;
                    document.body.appendChild(feedback);

                    setTimeout(() => {
                        feedback.style.animation = 'slideOut 0.3s ease';
                        setTimeout(() => {
                            document.body.removeChild(feedback);
                        }, 300);
                    }, 2000);
                }
            }

            // Adicionar animações CSS
            const animationStyle = document.createElement('style');
            animationStyle.textContent = `
                @keyframes slideIn {
                    from {
                        transform: translateX(100%);
                        opacity: 0;
                    }
                    to {
                        transform: translateX(0);
                        opacity: 1;
                    }
                }

                @keyframes slideOut {
                    from {
                        transform: translateX(0);
                        opacity: 1;
                    }
                    to {
                        transform: translateX(100%);
                        opacity: 0;
                    }
                }

                /* Estilos para temas e fontes */
                .theme-dark {
                    background-color: #1a1a1a !important;
                    color: #ffffff !important;
                }

                .theme-dark .content {
                    background-color: #2d2d2d !important;
                    color: #ffffff !important;
                }

                .font-small { font-size: 14px !important; }
                .font-medium { font-size: 16px !important; }
                .font-large { font-size: 18px !important; }
            `;

            document.head.appendChild(animationStyle);

            // Inicializar o menu quando a página carregar
            document.addEventListener('DOMContentLoaded', () => {
                new FloatingSettingsMenu();
            });

            // Se a página já carregou, inicializar imediatamente
            if (document.readyState === 'loading') {
                document.addEventListener('DOMContentLoaded', () => {
                    new FloatingSettingsMenu();
                });
            } else {
                new FloatingSettingsMenu();
            }
    """.trimIndent()
}