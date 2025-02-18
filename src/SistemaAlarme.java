package src;

import javax.swing.Timer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

public class SistemaAlarme extends JFrame {
    private GerenciadorAlarme gerenciador;
    private DefaultListModel<Alarme> modeloLista;
    private JList<Alarme> listaAlarmes;
    private JToggleButton btnSom;
    private Timer timerRelogio;
    private JLabel labelRelogio;
    private JTabbedPane tabbedPane;
    private JPanel painelGerenciamento;

    public SistemaAlarme() {
        gerenciador = new GerenciadorAlarme();
        configurarJanela();
        iniciarRelogio();
    }

    private void configurarJanela() {
        setTitle("Sistema de Alarme");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Painel Superior com Relógio Digital
        JPanel painelSuperior = criarPainelSuperior();
        add(painelSuperior, BorderLayout.NORTH);

        // Lista de Alarmes
        JPanel painelCentral = criarPainelCentral();
        add(painelCentral, BorderLayout.CENTER);

        // Barra de Ferramentas
        JPanel barraFerramentas = criarBarraFerramentas();
        add(barraFerramentas, BorderLayout.SOUTH);
    }

    private JPanel criarPainelSuperior() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));
        painel.setBackground(new Color(50, 50, 50));

        // Relógio Digital
        labelRelogio = new JLabel();
        labelRelogio.setFont(new Font("Digital-7", Font.BOLD, 48));
        labelRelogio.setForeground(Color.GREEN);
        labelRelogio.setHorizontalAlignment(SwingConstants.CENTER);
        painel.add(labelRelogio, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelCentral() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Título
        JLabel titulo = new JLabel("Alarmes");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        painel.add(titulo, BorderLayout.NORTH);

        // Lista de Alarmes
        modeloLista = new DefaultListModel<>();
        listaAlarmes = new JList<>(modeloLista);
        listaAlarmes.setCellRenderer(new AlarmeListRenderer());
        listaAlarmes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarAlarmeSelecionado();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(listaAlarmes);
        scrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
        painel.add(scrollPane, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarBarraFerramentas() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton btnNovo = new JButton("+ Novo Alarme");
        btnNovo.addActionListener(e -> mostrarDialogoNovoAlarme());

        btnSom = new JToggleButton("Som Ligado");
        btnSom.setSelected(true);
        btnSom.addActionListener(e ->
                btnSom.setText(btnSom.isSelected() ? "Som Ligado" : "Som Mudo"));

        painel.add(btnNovo);
        painel.add(btnSom);

        return painel;
    }

    private void iniciarRelogio() {
        timerRelogio = new Timer(1000, e -> {
            LocalDateTime agora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            labelRelogio.setText(agora.format(formatter));
        });
        timerRelogio.start();
    }

    private void mostrarDialogoNovoAlarme() {
        JDialog dialog = new JDialog(this, "Novo Alarme", true);
        dialog.setSize(350, 500);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Campos do formulário
        JTextField nomeField = new JTextField(20);
        SpinnerNumberModel horasModel = new SpinnerNumberModel(LocalTime.now().getHour(), 0, 23, 1);
        SpinnerNumberModel minutosModel = new SpinnerNumberModel(LocalTime.now().getMinute(), 0, 59, 1);
        JSpinner horasSpinner = new JSpinner(horasModel);
        JSpinner minutosSpinner = new JSpinner(minutosModel);

        // Configurações adicionais
        JComboBox<TipoSom> somCombo = new JComboBox<>(TipoSom.values());
        JSlider volumeSlider = new JSlider(0, 100, 70);
        JCheckBox vibracaoCheck = new JCheckBox("Vibração", true);
        JSpinner sonecaSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 30, 1));

        // Dias da semana
        JPanel diasPanel = new JPanel(new GridLayout(0, 2));
        diasPanel.setBorder(BorderFactory.createTitledBorder("Repetir"));
        Map<DiasSemana, JCheckBox> diasCheckboxes = new EnumMap<>(DiasSemana.class);

        for (DiasSemana dia : DiasSemana.values()) {
            JCheckBox checkbox = new JCheckBox(formatarDiaSemana(dia));
            diasCheckboxes.put(dia, checkbox);
            diasPanel.add(checkbox);
        }

        // Layout dos componentes
        panel.add(criarLabeledComponent("Nome:", nomeField));
        panel.add(Box.createVerticalStrut(10));

        JPanel horarioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        horarioPanel.add(new JLabel("Horário:"));
        horarioPanel.add(horasSpinner);
        horarioPanel.add(new JLabel(":"));
        horarioPanel.add(minutosSpinner);
        panel.add(horarioPanel);
        panel.add(Box.createVerticalStrut(10));

        panel.add(criarLabeledComponent("Som:", somCombo));
        panel.add(Box.createVerticalStrut(10));

        panel.add(new JLabel("Volume:"));
        panel.add(volumeSlider);
        panel.add(Box.createVerticalStrut(10));

        panel.add(vibracaoCheck);
        panel.add(Box.createVerticalStrut(10));

        panel.add(criarLabeledComponent("Tempo de Soneca (min):", sonecaSpinner));
        panel.add(Box.createVerticalStrut(10));

        panel.add(diasPanel);
        panel.add(Box.createVerticalStrut(20));

        // Botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton salvarBtn = new JButton("Salvar");
        JButton cancelarBtn = new JButton("Cancelar");

        salvarBtn.addActionListener(e -> {
            LocalTime horario = LocalTime.of(
                    (int)horasSpinner.getValue(),
                    (int)minutosSpinner.getValue()
            );

            Alarme novoAlarme = new Alarme(nomeField.getText(), horario);
            novoAlarme.setSomAlarme((TipoSom)somCombo.getSelectedItem());
            novoAlarme.setVolumeAlarme(volumeSlider.getValue());
            novoAlarme.setVibracao(vibracaoCheck.isSelected());
            novoAlarme.setDuracaoSoneca((int)sonecaSpinner.getValue());

            for (Map.Entry<DiasSemana, JCheckBox> entry : diasCheckboxes.entrySet()) {
                if (entry.getValue().isSelected()) {
                    novoAlarme.getDiasRepetir().add(entry.getKey());
                }
            }

            gerenciador.adicionarAlarme(novoAlarme);
            atualizarListaAlarmes();
            dialog.dispose();
        });

        cancelarBtn.addActionListener(e -> dialog.dispose());

        botoesPanel.add(salvarBtn);
        botoesPanel.add(cancelarBtn);
        panel.add(botoesPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private JPanel criarLabeledComponent(String label, JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label));
        panel.add(component);
        return panel;
    }

    private void editarAlarmeSelecionado() {
        Alarme alarme = listaAlarmes.getSelectedValue();
        if (alarme != null) {
            // Implementar diálogo de edição similar ao de criação
            mostrarDialogoEditarAlarme(alarme);
        }
    }

    private void mostrarDialogoEditarAlarme(Alarme alarme) {
        // Similar ao diálogo de novo alarme, mas com os campos preenchidos
        // Implementar conforme necessário
    }

    private void atualizarListaAlarmes() {
        modeloLista.clear();
        for (Alarme alarme : gerenciador.listarAlarmes()) {
            modeloLista.addElement(alarme);
        }
    }

    private String formatarDiaSemana(DiasSemana dia) {
        return dia.toString().substring(0, 1) +
                dia.toString().substring(1).toLowerCase();
    }

    // Classes internas
    private class AlarmeListRenderer extends JPanel implements ListCellRenderer<Alarme> {
        private JLabel horarioLabel = new JLabel();
        private JLabel nomeLabel = new JLabel();
        private JLabel detalhesLabel = new JLabel();
        private JToggleButton ativoToggle = new JToggleButton();

        public AlarmeListRenderer() {
            setLayout(new BorderLayout(10, 5));
            setBorder(new EmptyBorder(5, 10, 5, 10));

            JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 2));
            infoPanel.setOpaque(false);
            infoPanel.add(nomeLabel);
            infoPanel.add(detalhesLabel);

            horarioLabel.setFont(new Font("Arial", Font.BOLD, 18));

            add(horarioLabel, BorderLayout.WEST);
            add(infoPanel, BorderLayout.CENTER);
            add(ativoToggle, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends Alarme> list, Alarme alarme, int index,
                boolean isSelected, boolean cellHasFocus) {

            horarioLabel.setText(alarme.getHorario().format(
                    DateTimeFormatter.ofPattern("HH:mm")));
            nomeLabel.setText(alarme.getNome());
            detalhesLabel.setText(formatarDetalhesAlarme(alarme));
            ativoToggle.setSelected(alarme.isAtivo());

            setBackground(isSelected ?
                    list.getSelectionBackground() :
                    index % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));

            return this;
        }

        private String formatarDetalhesAlarme(Alarme alarme) {
            StringBuilder sb = new StringBuilder();
            if (alarme.getDiasRepetir().isEmpty()) {
                sb.append("Uma vez");
            } else {
                for (DiasSemana dia : alarme.getDiasRepetir()) {
                    sb.append(dia.toString().substring(0, 3)).append(" ");
                }
            }
            return sb.toString();
        }
    }

    // Classe principal e método main
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            SistemaAlarme sistema = new SistemaAlarme();
            sistema.setVisible(true);
        });
    }
}

// Classes de modelo
class Alarme {
    private String nome;
    private LocalTime horario;
    private boolean ativo;
    private Set<DiasSemana> diasRepetir;
    private TipoSom somAlarme;
    private int volumeAlarme;
    private int duracaoSoneca;
    private boolean vibracao;

    public Alarme(String nome, LocalTime horario) {
        this.nome = nome;
        this.horario = horario;
        this.ativo = true;
        this.diasRepetir = new HashSet<>();
        this.somAlarme = TipoSom.PADRAO;
        this.volumeAlarme = 70;
        this.duracaoSoneca = 5;
        this.vibracao = true;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public Set<DiasSemana> getDiasRepetir() { return diasRepetir; }
    public void setDiasRepetir(Set<DiasSemana> diasRepetir) { this.diasRepetir = diasRepetir; }
    public TipoSom getSomAlarme() { return somAlarme; }
    public void setSomAlarme(TipoSom somAlarme) { this.somAlarme = somAlarme; }
    public int getVolumeAlarme() { return volumeAlarme; }
    public void setVolumeAlarme(int volumeAlarme) { this.volumeAlarme = volumeAlarme; }
    public int getDuracaoSoneca() { return duracaoSoneca; }
    public void setDuracaoSoneca(int duracaoSoneca) { this.duracaoSoneca = duracaoSoneca; }
    public boolean isVibracao() { return vibracao; }
    public void setVibracao(boolean vibracao) { this.vibracao = vibracao; }
}

enum DiasSemana {
    DOMINGO, SEGUNDA, TERCA, QUARTA, QUINTA, SEXTA, SABADO
}

enum TipoSom {
    PADRAO("Padrão"),
    SUAVE("Suave"),
    ENERGICO("Enérgico"),
    NATUREZA("Natureza"),
    PERSONALIZADO("Personalizado");

    private final String descricao;

    TipoSom(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}

class GerenciadorAlarme {
    private List<Alarme> alarmes;
    private Timer timer;
    private Set<AlarmeListener> listeners;

    public GerenciadorAlarme() {
        this.alarmes = new ArrayList<>();
        this.listeners = new HashSet<>();
        iniciarVerificacaoAlarmes();
    }

    public void adicionarAlarme(Alarme alarme) {
        alarmes.add(alarme);
        notificarListeners();
    }

    public void removerAlarme(Alarme alarme) {
        alarmes.remove(alarme);
        notificarListeners();
    }

    public List<Alarme> listarAlarmes() {
        return new ArrayList<>(alarmes);
    }

    public void adicionarListener(AlarmeListener listener) {
        listeners.add(listener);
    }

    public void removerListener(AlarmeListener listener) {
        listeners.remove(listener);
    }

    private void notificarListeners() {
        for (AlarmeListener listener : listeners) {
            listener.alarmesAtualizados();
        }
    }

    private void iniciarVerificacaoAlarmes() {
        timer = new Timer(60000, e -> verificarAlarmes()); // Verifica a cada minuto
        timer.start();
    }

    private void verificarAlarmes() {
        LocalDateTime agora = LocalDateTime.now();
        DiasSemana diaAtual = DiasSemana.values()[agora.getDayOfWeek().getValue() % 7];

        for (Alarme alarme : alarmes) {
            if (deveDisparar(alarme, agora, diaAtual)) {
                dispararAlarme(alarme);
            }
        }
    }

    private boolean deveDisparar(Alarme alarme, LocalDateTime agora, DiasSemana diaAtual) {
        return alarme.isAtivo() &&
                alarme.getHorario().getHour() == agora.getHour() &&
                alarme.getHorario().getMinute() == agora.getMinute() &&
                (alarme.getDiasRepetir().isEmpty() || alarme.getDiasRepetir().contains(diaAtual));
    }

    private void dispararAlarme(Alarme alarme) {
        // Implementação do disparo do alarme
        try {
            // Simula um som de alarme
            Toolkit.getDefaultToolkit().beep();

            // Mostra notificação
            if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();
                Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
                TrayIcon trayIcon = new TrayIcon(image, "Alarme");
                trayIcon.setImageAutoSize(true);

                try {
                    tray.add(trayIcon);
                    trayIcon.displayMessage(
                            alarme.getNome(),
                            "Alarme disparando!",
                            TrayIcon.MessageType.INFO
                    );
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }

            // Dispara vibração se ativada
            if (alarme.isVibracao()) {
                // Simula vibração (apenas visual neste exemplo)
                System.out.println("*Vibração*");
            }

            // Aguarda confirmação ou soneca
            SwingUtilities.invokeLater(() -> mostrarDialogoAlarme(alarme));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarDialogoAlarme(Alarme alarme) {
        JDialog dialog = new JDialog((Frame)null, alarme.getNome(), true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel timeLabel = new JLabel(
                alarme.getHorario().format(DateTimeFormatter.ofPattern("HH:mm")),
                SwingConstants.CENTER
        );
        timeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        panel.add(timeLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton snoozeButton = new JButton("Soneca (" + alarme.getDuracaoSoneca() + "min)");
        JButton dismissButton = new JButton("Desligar");

        snoozeButton.addActionListener(e -> {
            // Implementa a soneca
            LocalTime novoHorario = LocalTime.now().plusMinutes(alarme.getDuracaoSoneca());
            alarme.setHorario(novoHorario);
            dialog.dispose();
        });

        dismissButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(snoozeButton);
        buttonPanel.add(dismissButton);
        panel.add(buttonPanel);

        dialog.add(panel);
        dialog.setVisible(true);
        System.out.println();
    }



}

interface AlarmeListener {
    void alarmesAtualizados();
}