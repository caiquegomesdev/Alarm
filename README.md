# 🔔 Sistema de Alarme  

## 📋 Descrição
O Sistema de Alarme é uma aplicação Java desktop que permite gerenciar múltiplos alarmes com interface gráfica intuitiva, desenvolvida utilizando Java Swing. O sistema possui funcionalidades similares aos aplicativos de alarme modernos para smartphones.

## ✨ Funcionalidades Principais

### ⏰ Gerenciamento de Alarmes
- Adicionar novos alarmes
- Editar alarmes existentes
- Ativar/desativar alarmes
- Personalizar configurações (som, volume, vibração)

### 🔄 Opções de Repetição
- Configurar alarmes para dias específicos da semana
- Alarmes únicos ou recorrentes

### 💤 Função Soneca
- Adiar alarmes por tempo personalizado
- Configuração de duração da soneca

## 🏗️ Estrutura do Projeto

### 📱 Interface de Usuário
O sistema possui uma interface gráfica composta por:
- Relógio digital na parte superior
- Lista de alarmes na área central
- Barra de ferramentas na parte inferior

### 🧩 Classes Principais

#### `SistemaAlarme`
Classe principal que gerencia a interface gráfica e integra os componentes.

#### `GerenciadorAlarme`
Responsável pelo gerenciamento de alarmes, incluindo:
- Armazenamento da lista de alarmes
- Verificação periódica para disparo
- Execução das ações quando um alarme dispara

#### `Alarme`
Modelo de dados que representa um alarme, contendo:
- Nome
- Horário
- Status (ativo/inativo)
- Dias de repetição
- Configurações de som e volume
- Configurações de vibração e soneca

#### Enums
- `DiasSemana`: representa os dias da semana
- `TipoSom`: tipos de sons disponíveis para os alarmes

## 🚀 Como Usar

### 📥 Requisitos
- Java 8 ou superior
- Sistema operacional compatível com Java Swing

### 🛠️ Compilação e Execução
```bash
# Compilar
javac src/SistemaAlarme.java

# Executar
java src.SistemaAlarme
```

### 📱 Interface Principal
1. **Relógio Digital**: Exibe a hora atual
2. **Lista de Alarmes**: Mostra todos os alarmes configurados
   - Clique duplo em um alarme para editá-lo
   - Use o botão de toggle para ativar/desativar
3. **Barra de Ferramentas**:
   - Botão "Novo Alarme": Abre o diálogo para criar alarmes
   - Botão "Som": Controla o estado do som (ligado/mudo)

### ➕ Criando um Novo Alarme
1. Clique no botão "Novo Alarme"
2. Preencha o formulário:
   - Nome do alarme
   - Horário (horas e minutos)
   - Tipo de som
   - Volume
   - Vibração (ligada/desligada)
   - Tempo de soneca
   - Dias para repetição

### ⏲️ Quando um Alarme Dispara
1. Uma notificação é exibida
2. O som selecionado é reproduzido
3. Se configurado, o dispositivo vibra
4. Um diálogo é apresentado com opções:
   - Desligar: Desativa o alarme atual
   - Soneca: Adia o alarme pelo tempo configurado

## 🧠 Lógica de Funcionamento

### ⏱️ Verificação de Alarmes
O sistema verifica a cada minuto se algum alarme deve ser disparado, baseado em:
- Horário atual
- Dia da semana atual
- Status do alarme (ativo/inativo)
- Configurações de repetição

### 📢 Notificações
Quando um alarme dispara:
- Um som é reproduzido (com volume configurado)
- Uma notificação na bandeja do sistema é exibida (se suportado)
- O diálogo de alarme é mostrado

## 🤝 Contribuição
Sinta-se à vontade para contribuir com o projeto:
1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## 📄 Licença
Este projeto está licenciado sob a licença MIT - veja o arquivo LICENSE para detalhes.

## 🙏 Agradecimentos
- Interface inspirada em aplicativos modernos de alarme
- Utiliza a biblioteca Java Swing para a interface gráfica
