import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Map<String, Time> tabela = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("jogos.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",");
                String nomeA = partes[1];
                String nomeB = partes[2];
                String[] placar = partes[3].split("x");
                int golsA = Integer.parseInt(placar[0]);
                int golsB = Integer.parseInt(placar[1]);

                tabela.putIfAbsent(nomeA, new Time(nomeA));
                tabela.putIfAbsent(nomeB, new Time(nomeB));

                tabela.get(nomeA).adicionarJogo(golsA, golsB);
                tabela.get(nomeB).adicionarJogo(golsB, golsA);
            }
        } catch (IOException e) {
            System.out.println("jogos.txt: " + e.getMessage());
        }

        List<Time> ranking = new ArrayList<>(tabela.values());
        Collections.sort(ranking);

        System.out.println("+---+----------+--------+-------+");
        System.out.println("| # | Time     | Pontos | Saldo |");
        System.out.println("+---+----------+--------+-------+");
        for (int i = 0; i < ranking.size(); i++) {
            Time t = ranking.get(i);
            System.out.printf("| %dº | %-8s | %d      | %d     |\n", i + 1, t.getNome(), t.getPontos(), t.getSaldo());
        }
        System.out.println("+---+----------+--------+-------+");
    }
}

class Time implements Comparable<Time> {
    private String nome;
    private int pontos = 0;
    private int golsFeitos = 0;
    private int golsSofridos = 0;

    public Time(String nome) { this.nome = nome; }

    public void adicionarJogo(int golsFeitos, int golsSofridos) {
        this.golsFeitos += golsFeitos;
        this.golsSofridos += golsSofridos;
        if (golsFeitos > golsSofridos) pontos += 3;
        else if (golsFeitos == golsSofridos) pontos += 1;
    }

    public int getSaldo() { return golsFeitos - golsSofridos; }
    public String getNome() { return nome; }
    public int getPontos() { return pontos; }

    @Override
    public int compareTo(Time outro) {
        if (this.pontos != outro.pontos) return Integer.compare(outro.pontos, this.pontos);
        return Integer.compare(outro.getSaldo(), this.getSaldo());
    }
}