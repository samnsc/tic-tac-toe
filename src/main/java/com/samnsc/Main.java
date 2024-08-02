package com.samnsc;

import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

// Tic-Tac-Toe
// this project was made for class DCC025 - "Orientação à Objetos"
// by Samuel Nascimento on 2024-07-29

public class Main {
    public static void main(String[] args) {
        System.out.println("=============================================");
        System.out.println("=== Seja bem-vindo(a) ao Jogo da Velha!!! ===");
        System.out.println("=============================================");

        Scanner scanner = new Scanner(System.in);
        boolean playerTwoBot;
        boolean playerOneFirst;
        char playerOneSymbol;
        char playerTwoSymbol;

        System.out.println("Deseja jogar contra um humano ou contra um bot? (1 para humano e qualquer outro valor para bot)");
        playerTwoBot = !(scanner.nextLine().equals("1"));

        System.out.print("Jogador 1 digite o símbolo que irá representar as suas peças: ");
        String aux = scanner.nextLine();

        // if the input is valid the first time it won't enter this while loop
        while (!Objects.equals(aux, "X") && !Objects.equals(aux, "O")) {
            System.out.println("Input inválido! Por favor digite apenas X ou O!");
            aux = scanner.nextLine();
        }
        playerOneSymbol = aux.charAt(0);

        if (playerTwoBot) {
            if (playerOneSymbol == 'X') {
                playerTwoSymbol = 'O';
            } else {
                playerTwoSymbol = 'X';
            }
        } else {
            System.out.print("Jogador 2 digite o símbolo que irá representar as suas peças: ");
            aux = scanner.nextLine();

            // same as the previous while loop
            while ((!Objects.equals(aux, "X") && !Objects.equals(aux, "O")) || aux.charAt(0) == playerOneSymbol) {
                System.out.println("Input inválido! Por favor digite apenas " + (playerOneSymbol == 'X' ? 'O' : 'X') + "!");
                aux = scanner.nextLine();
            }
            playerTwoSymbol = aux.charAt(0);
        }

        System.out.println("Decidindo aleatoriamente qual jogador irá primeiro!");
        Random randomGenerator = new Random();
        playerOneFirst = randomGenerator.nextBoolean();

        System.out.println("O jogador " + (playerOneFirst ? playerOneSymbol : playerTwoSymbol) + " irá jogar primeiro!");

        Game game = new Game(playerTwoBot, playerOneFirst, playerOneSymbol, playerTwoSymbol);
        game.startGame();
    }
}