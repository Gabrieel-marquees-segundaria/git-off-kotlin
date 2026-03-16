def format_code_samples(code_data):
    """
    Formata amostras de código de diferentes linguagens.

    Args:
        code_data (dict): Dicionário com linguagem como chave e código como valor

    Returns:
        str: String formatada com todos os códigos
    """
    # Dicionário com extensões comuns de arquivo por linguagem
    language_extensions = {
        'python': 'py',
        'javascript': 'js',
        'java': 'java',
        'html': 'html',
        'css': 'css',
        'typescript': 'ts',
        'php': 'php',
        'ruby': 'rb',
        'go': 'go',
        'rust': 'rs'
    }

    formatted_output = []

    for language, code in code_data.items():
        # Normaliza o nome da linguagem
        language = language.lower()

        # Obtém a extensão do arquivo (usa 'txt' se a linguagem não estiver mapeada)
        extension = language_extensions.get(language, 'txt')

        # Adiciona o cabeçalho da linguagem
        formatted_output.append(f"\n### Código em {language.capitalize()}\n")

        # Adiciona o código formatado com markdown
        formatted_output.append(f"```{extension}")
        formatted_output.append(code.strip())
        formatted_output.append("```\n")

    return "\n".join(formatted_output)


# Exemplo de uso
exemplo_codigos = {
    'python': """
def hello_world():
    print("Olá, mundo!")
    """,

    'javascript': """
function helloWorld() {
    console.log("Olá, mundo!");
}
    """,

    'java': """
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Olá, mundo!");
    }
}
    """
}

# Formata os códigos
resultado = format_code_samples(exemplo_codigos)
print(resultado)
