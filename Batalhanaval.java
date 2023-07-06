
package exemplo.batalhanaval;

import java.util.Random;
import java.util.Scanner;

public class Batalhanaval {

    private static  int TAMANHO_MAPA = 10;
    private static  int QUANTIDADE_BARCOS = 3;
    private static  int TAMANHO_MAXIMO_BARCO = 4;

    private char[][] mapaJogador;
    private char[][] mapaOponente;
    private Random random;
    private Scanner scanner;
    private String nome;
    private String nomeOponente;

    public Batalhanaval() {
        mapaJogador = new char[TAMANHO_MAPA][TAMANHO_MAPA];
        mapaOponente = new char[TAMANHO_MAPA][TAMANHO_MAPA];
        random = new Random();
        scanner = new Scanner(System.in);
    }

    public void iniciarJogo() {
        System.out.println("Bem-vindo ao jogo Batalha Naval!");
        System.out.println("Como devo te chamar?");
        String nome = scanner.next();

        System.out.println("Escolha uma opção:");
        System.out.println("1 - Jogar contra jogador");
        System.out.println("2 - Jogar contra o computador");
        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1:
                System.out.println(nome+", aloque seus barcos:");
                alocarBarcos(mapaJogador, false);
                
                System.out.println("segundo jogador, como é seu nome?");
                String nomeOponente = scanner.next();

                System.out.println(nomeOponente+", aloque seus barcos:");
                alocarBarcos(mapaOponente, false);
                break;
            case 2:
                System.out.println(nome+", aloque seus barcos:");
                alocarBarcos(mapaJogador, false);

                System.out.println("Computador, alocando os barcos...");
                alocarBarcos(mapaOponente, true);
                break;
            default:
                System.out.println("Opção inválida. O jogo será encerrado.");
                return;
        }

        while (true) {
           switch (opcao) {
           case 1:
            System.out.println(nome+", é sua vez:");
            realizarAtaque(mapaOponente, false);

            boolean jogoAcabou = verificarFimDoJogo(mapaOponente);
            if (jogoAcabou) {
                System.out.println("Você venceu! Todos os barcos do oponente foram afundados.");
                break;
            }

            System.out.println("Oponente, é sua vez:");
            realizarAtaque(mapaJogador, false);

            jogoAcabou = verificarFimDoJogo(mapaJogador);
            if (jogoAcabou) {
                System.out.println("O oponente venceu! Todos os seus barcos foram afundados.");
                break;
            }
           }
        }
    }

    private void alocarBarcos(char[][] mapa, boolean alocacaoAutomatica) {
        int barcosAlocados = 0;

        while (barcosAlocados < QUANTIDADE_BARCOS) {
            int tamanhoBarco = barcosAlocados % TAMANHO_MAXIMO_BARCO + 1;

            if (alocacaoAutomatica) {
                int linha = random.nextInt(TAMANHO_MAPA);
                int coluna = random.nextInt(TAMANHO_MAPA);

                if (verificarEspacoDisponivel(linha, coluna, tamanhoBarco, true, mapa)) {
                    barcosAlocados++;
                }
            } else {
                exibirMapa(mapa);
                System.out.println("Aloque um barco de tamanho " + tamanhoBarco + ":");

                int linha = scanner.nextInt();
                int coluna = scanner.nextInt();

                System.out.println("Digite 'h' para alocar na horizontal ou 'v' para alocar na vertical:");
                char direcao = scanner.next().charAt(0);

                if (verificarEspacoDisponivel(linha, coluna, tamanhoBarco, direcao == 'h', mapa)) {
                    alocarBarco(linha, coluna, tamanhoBarco, direcao == 'h', mapa);
                    barcosAlocados++;
                } else {
                    System.out.println("Posição inválida. Tente novamente.");
                }
            }
        }
    }

    private boolean verificarEspacoDisponivel(int linha, int coluna, int tamanhoBarco, boolean horizontal, char[][] mapa) {
        if (horizontal) {
            if (coluna + tamanhoBarco > TAMANHO_MAPA) {
                return false;
            }

            for (int i = 0; i < tamanhoBarco; i++) {
                if (mapa[linha][coluna + i] != 0) {
                    return false;
                }
            }
        } else {
            if (linha + tamanhoBarco > TAMANHO_MAPA) {
                return false;
            }

            for (int i = 0; i < tamanhoBarco; i++) {
                if (mapa[linha + i][coluna] != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    private void alocarBarco(int linha, int coluna, int tamanhoBarco, boolean horizontal, char[][] mapa) {
        if (horizontal) {
            for (int i = 0; i < tamanhoBarco; i++) {
                mapa[linha][coluna + i] = 'B';
            }
        } else {
            for (int i = 0; i < tamanhoBarco; i++) {
                mapa[linha + i][coluna] = 'B';
            }
        }
    }

    private void realizarAtaque(char[][] mapa, boolean exibirMapa) {
        int linha, coluna;
        boolean repetirJogada;

        do {
            repetirJogada = false;

            if (exibirMapa) {
                exibirMapa(mapa);
            }

            System.out.print("Digite a posição (linha coluna) para atacar: ");
            linha = scanner.nextInt();
            coluna = scanner.nextInt();

            if (linha < 0 || linha >= TAMANHO_MAPA || coluna < 0 || coluna >= TAMANHO_MAPA) {
                System.out.println("Posição inválida. Tente novamente.");
                repetirJogada = true;
            } else if (mapa[linha][coluna] == 'X' || mapa[linha][coluna] == 'O') {
                System.out.println("Você já atirou nessa posição. Tente novamente.");
                repetirJogada = true;
            } else if (mapa[linha][coluna] == 'B') {
                System.out.println("Você acertou um barco!");
                mapa[linha][coluna] = 'X';
            } else {
                System.out.println("Você acertou na água.");
                mapa[linha][coluna] = 'O';
            }

        } while (repetirJogada);
    }

    private boolean verificarFimDoJogo(char[][] mapa) {
        for (int i = 0; i < TAMANHO_MAPA; i++) {
            for (int j = 0; j < TAMANHO_MAPA; j++) {
                if (mapa[i][j] == 'B') {
                    return false;
                }
            }
        }
        return true;
    }

    private void exibirMapa(char[][] mapa) {
        System.out.println("   0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < TAMANHO_MAPA; i++) {
            System.out.print(i + "  ");
            for (int j = 0; j < TAMANHO_MAPA; j++) {
                if (mapa[i][j] == 'B') {
                    System.out.print(" ~ ");
                } else {
                    System.out.print(mapa[i][j] + "  ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Batalhanaval jogo = new Batalhanaval();
        jogo.iniciarJogo();
    }
}

