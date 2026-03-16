# git-off-kotlin

## Funcionalidades Adicionadas

### 🔐 Geração de Chave SSH
- Gera pares de chaves SSH ED25519 para autenticação segura
- Permite copiar a chave pública para adicionar ao GitHub
- Armazena a chave privada de forma segura no dispositivo

### 📦 Clone de Repositório
- **Clone via SSH**: Clona repositórios diretamente do GitHub usando autenticação SSH
  - Requer informar o proprietário (owner) e nome do repositório
  - Utiliza a chave SSH gerada para autenticação segura
  - Armazena o repositório localmente no dispositivo

### 🔄 Pull
- Atualiza repositórios já clonados
- Obtém as últimas mudanças do repositório remoto
- Disponível apenas após clonar um repositório

### 📄 Listar Arquivos
- Lista os arquivos e pastas do repositório clonado
- Permite visualizar a estrutura do projeto localmente

## Como Usar

1. **Gerar Chave SSH**
   - Clique em "Gerar Nova Chave SSH ED25519"
   - A chave pública será exibida

2. **Configurar no GitHub**
   - Clique em "Abrir GitHub" para acessar as configurações
   - Cole a chave pública em Settings > SSH and GPG keys

3. **Clonar Repositório**
   - Preench o campo "Owner" com o proprietário do repositório (ex: torvalds)
   - Preencha o campo "Repositório" com o nome (ex: linux)
   - Clique em "⬇️ Clonar Repositório via SSH"

4. **Gerenciar Repositório**
   - Use "🔄 Pull" para atualizar o repositório
   - Use "📄 Listar Arquivos" para ver o conteúdo

## Arquitetura

- **MainActivity.kt**: Activity principal com a interface do usuário
- **GitOperations.kt**: Classe que gerencia operações Git (clone, pull, listar arquivos)
- **SSHKeygenGithub.kt**: Geração de chaves SSH

## Armazenamento

- Chaves SSH são armazenadas em `context.filesDir/.ssh/id_rsa`
- Repositórios clonados são armazenados em `context.filesDir/repos/`
