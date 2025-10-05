# Ludus Latrunculorum

## Descrição

Este projeto é uma implementação digital do antigo jogo de tabuleiro romano "Ludus Latrunculorum", também conhecido como o "Jogo do Soldado". Desenvolvido em Java com a biblioteca JavaFX, o jogo oferece uma interface gráfica para dois jogadores competirem seguindo as regras clássicas de estratégia e captura.

## Funcionalidades

- **Interface Gráfica**: Tabuleiro visual interativo criado com JavaFX.
- **Dois Jogadores**: Modo de jogo local para dois participantes.
- **Fases de Jogo**: Implementação completa das fases de colocação e de movimento.
- **Regras Clássicas**:
    - Captura por custódia (cercar uma peça inimiga).
    - Turno extra após uma captura.
    - Movimento especial de salto para a peça "General".
- **Controles Intuitivos**:
    - **Novo Jogo**: Inicia uma nova partida a qualquer momento.
    - **Desfazer Jogada**: Reverte o último movimento realizado.
    - **Ajuda/Regras**: Exibe uma janela com as regras detalhadas do jogo.
- **Feedback Visual**:
    - Destaque de movimentos válidos para a peça selecionada.
    - "Cemitério" para exibir as peças capturadas de cada jogador.
    - Janela de fim de jogo que declara o vencedor ou empate.

## Como Executar

Este é um projeto Maven, então a forma mais fácil de executá-lo é através de uma IDE que suporte Maven (como IntelliJ IDEA, Eclipse ou NetBeans).

1.  **Clone ou baixe o repositório.**
2.  **Abra o projeto na sua IDE** como um projeto Maven. A IDE irá baixar as dependências do JavaFX automaticamente.
3.  **Localize a classe principal**: `src/main/java/view/ViewFX.java`.
4.  **Execute** o método `main` nesta classe para iniciar o jogo.

Alternativamente, você pode compilar e executar via linha de comando usando o Maven:

```bash
# Compila o projeto e executa o jogo
mvn clean javafx:run