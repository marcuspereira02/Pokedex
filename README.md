# Pokedex

Pokedex é um aplicativo Android desenvolvido em kotlin que permite pesquisar Pokémon e visualizar informações detalhadas como imagem, tipos, habilidades, estatísticas e outras características utilizando a PokéAPI.


---
## ✨ Funcionalidades
- Pesquisa de Pokémon por nome em tempo real
- Exibição de informações detalhadas
- Tratamento de estados de carregamento e erro
- Histórico de pesquisa
---
## 🧱 Arquitetura e Padrões
O projeto foi desenvolvido utilizando a arquitetura MVVM para separar responsabilidades e facilitar manutenção e testes.

- **Arquitetura:** MVVM (Model–View–ViewModel)
- **Padrão de Repositório:** *Online First* (consulta a dados locais ou remotos)
---
## 🧪 Testes

O projeto inclui testes unitários utilizando coroutines test, Flow testing e Fake Services para simular comportamentos da API, garantindo previsibilidade e isolamento durante os testes.

Exemplos de cenários testados:

- Quando a resposta da API é um sucesso, o ViewModel retorna o estado da UI corretamente

- Quando ocorre erro no serviço, o estado de erro é emitido




---
## :camera_flash: Screenshots
<!-- You can add more screenshots here if you like -->
<img src="https://github.com/user-attachments/assets/555f0da4-a35c-4141-8f37-f4e8a6e6fe31" width=260/> <img src="https://github.com/user-attachments/assets/5f378fd8-3f62-4084-80c5-f7db95b4e5ca" width=260/> <img src="https://github.com/user-attachments/assets/a07ed8fb-23be-4d99-ab39-6ef4fdf78cf8" width=260/> 

---

## 🛠️ Tecnologias
- **Linguagem:** 100% Kotlin  
- **UI:** Jetpack Compose  
- **Networking:** Retrofit 
- **API:** PokéAPI  
- **Gerenciamento de Estado:** ViewModel

---

## License
```
The MIT License (MIT)

Copyright (c) 2026 Marcus Vinícius de Sá Pereira

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
