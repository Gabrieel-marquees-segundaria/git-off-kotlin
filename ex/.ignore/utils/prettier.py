import black
import autopep8
import subprocess

import black.mode


def detect_lang(code: str):
    if 'def ' in code or 'import ' in code:
        return 'python'
    elif 'function' in code or 'var ' in code or 'const' in code:
        return 'javascript'
    elif "SELECT" in code.upper():
        return 'sql'
    elif "class " in code or "public static void main" in code:
        return 'java'
    return 'Desconhecido'


def formatter_code(code: str, lang: str):
    try:
        if lang == 'python':
            return black.format_str(code, mode=black.Mode())
        elif lang == 'javascript':
            result = subprocess.run(
                ['npx', 'prettier', '--parser', 'babel'],
                input=code,
                capture_output=True,
                text=True,
                check=True)
            return result.stdout

        elif linguagem == "sql":
            return sqlparse.format(codigo, reindent=True, keyword_case="upper")
        elif linguagem == "java":
            resultado = subprocess.run(
                ["clang-format"],
                input=codigo,
                text=True,
                capture_output=True,
                check=True
            )
            return resultado.stdout
        else:
            return "Linguagem não suportada!"
    except Exception as e:
        return f"Erro ao formatar código: {e}"


if __name__ == "__main__":
    codigo_exemplo = "function soma(a,b){return a+b}"  # Exemplo de JavaScript
    linguagem = detect_lang(codigo_exemplo)
    codigo_formatado = formatter_code(codigo_exemplo, linguagem)

    print(codigo_formatado)
