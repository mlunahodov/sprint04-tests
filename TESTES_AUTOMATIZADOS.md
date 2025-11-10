# Testes Automatizados - Vroom

Este documento descreve os testes automatizados criados para a aplicação Vroom usando Selenium WebDriver e JUnit 5.

## 📋 Testes Implementados

### 1. TC01Login - Teste de Login
**Arquivo:** `src/test/java/io/github/Guimaraes131/vroom/TC01Login.java`

**Cenário:** Login com sucesso
- Acessa a página de login
- Preenche credenciais válidas
- Clica no botão "Entrar"
- Verifica o redirecionamento após login bem-sucedido

**Credenciais padrão:** 
- Login: `operador_teste`
- Senha: `123456`

⚠️ **Importante:** Você precisa criar este usuário no sistema antes de executar o teste, ou ajustar as credenciais no código.

---

### 2. TC02CadastroUsuario - Teste de Cadastro de Usuário
**Arquivo:** `src/test/java/io/github/Guimaraes131/vroom/TC02CadastroUsuario.java`

**Cenário:** Cadastro de usuário com sucesso
- Acessa a página de registro
- Preenche login único (com timestamp)
- Preenche senha válida
- Seleciona o tipo de usuário (OPERATOR)
- Clica no botão "Registrar"
- Verifica o redirecionamento para a página de login

---

### 3. TC03CadastroMoto - Teste de Cadastro de Moto
**Arquivo:** `src/test/java/io/github/Guimaraes131/vroom/TC03CadastroMoto.java`

**Cenário:** Cadastro de moto com sucesso
- Faz login usando a classe auxiliar
- Acessa a página de cadastro de moto
- Preenche placa única
- Seleciona modelo da moto
- Preenche chassi único
- Seleciona situação/problema
- Preenche descrição do problema
- Clica no botão "Adicionar moto"
- Verifica o redirecionamento após cadastro bem-sucedido

---

### 4. TC04EditarMoto - Teste de Edição de Moto
**Arquivo:** `src/test/java/io/github/Guimaraes131/vroom/TC04EditarMoto.java`

**Cenário:** Editar moto com sucesso
- Faz login usando a classe auxiliar
- Acessa a página inicial (lista de motos)
- Clica em um link de edição de moto
- Modifica os campos (placa e descrição)
- Clica no botão "Atualizar moto"
- Verifica o redirecionamento após edição bem-sucedida

**Observação:** Se não houver motos cadastradas, o teste cria uma moto automaticamente antes de tentar editá-la.

---

## 🔧 Classe Auxiliar

### LoginAuxiliar
**Arquivo:** `src/test/java/io/github/Guimaraes131/vroom/LoginAuxiliar.java`

Classe utilitária para realizar login nos testes. Possui dois métodos:
- `realizarLogin(WebDriver driver)` - Login com credenciais padrão
- `realizarLogin(WebDriver driver, String login, String senha)` - Login com credenciais customizadas

---

## 🚀 Como Executar os Testes

### Pré-requisitos

1. **Aplicação em execução:**
   ```powershell
   # Certifique-se de que a aplicação está rodando em http://localhost:8080
   ./gradlew bootRun
   ```

2. **Banco de dados configurado:**
   - PostgreSQL deve estar rodando (via Docker Compose ou instalação local)
   - Execute: `docker-compose up -d` (se estiver usando Docker)

3. **Google Chrome instalado:**
   - O WebDriverManager fará o download automático do ChromeDriver compatível

4. **Criar usuário de teste (para TC01Login):**
   - Acesse `http://localhost:8080/register`
   - Crie um usuário com:
     - Login: `operador_teste`
     - Senha: `123456`
     - Tipo: OPERATOR

### Executar Todos os Testes

```powershell
# Via Gradle
./gradlew test

# Ou apenas os testes de uma classe específica
./gradlew test --tests TC01Login
./gradlew test --tests TC02CadastroUsuario
./gradlew test --tests TC03CadastroMoto
./gradlew test --tests TC04EditarMoto
```

### Executar pela IDE (IntelliJ IDEA / Eclipse / VS Code)

1. Abra a classe de teste desejada
2. Clique com botão direito no método de teste
3. Selecione "Run Test" ou "Debug Test"

---

## 📦 Dependências

As dependências do Selenium e JUnit já estão configuradas no `build.gradle`:

```gradle
// Selenium WebDriver
testImplementation 'org.seleniumhq.selenium:selenium-java:4.21.0'

// JUnit 5
testImplementation 'org.junit.jupiter:junit-jupiter:5.10.2'

// WebDriverManager (gerencia o ChromeDriver automaticamente)
testImplementation 'io.github.bonigarcia:webdrivermanager:5.8.0'
```

---

## 🎯 Estrutura dos Testes

Todos os testes seguem o padrão **Given-When-Then** (Dado-Quando-Então):

```java
@Test
public void testeExemplo() {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    try {
        // DADO (Given) - Preparação
        // Navega para a página, faz login, etc.
        
        // QUANDO (When) - Ação
        // Preenche formulário, clica em botão, etc.
        
        // ENTÃO (Then) - Verificação
        // Assertions para verificar o resultado esperado
        
    } finally {
        driver.quit(); // Sempre fecha o navegador
    }
}
```

---

## 🔍 Solução de Problemas

### Teste falha com "Element not found"
- **Causa:** Elemento não carregou a tempo
- **Solução:** Aumente o timeout em `Duration.ofSeconds(10)` para `Duration.ofSeconds(20)`

### Teste falha com "Connection refused"
- **Causa:** Aplicação não está rodando
- **Solução:** Certifique-se de que a aplicação está em `http://localhost:8080`

### ChromeDriver incompatível
- **Causa:** Versão do Chrome desatualizada
- **Solução:** Atualize o Google Chrome ou use outro navegador (Firefox, Edge)

### Erro de login no TC01Login
- **Causa:** Usuário `operador_teste` não existe
- **Solução:** Crie o usuário manualmente ou ajuste as credenciais no código

---

## 📝 Notas Importantes

1. **Os testes criam dados únicos** usando `System.currentTimeMillis()` para evitar conflitos
2. **Os testes são independentes** - cada um abre e fecha seu próprio navegador
3. **Os testes assumem URLs padrão** - se sua aplicação roda em outra porta, ajuste a constante `BASE_URL`
4. **Os seletores podem precisar de ajuste** - se a estrutura HTML mudar, atualize os seletores (IDs, classes, XPaths)

---

## 🎨 Melhorias Futuras

- [ ] Adicionar testes de validação de campos obrigatórios
- [ ] Adicionar testes de casos negativos (login inválido, campos vazios)
- [ ] Implementar Page Object Model (POM) para melhor organização
- [ ] Adicionar screenshots em caso de falha
- [ ] Configurar execução paralela de testes
- [ ] Adicionar relatórios HTML com Allure ou ExtentReports

---

## 👥 Autor

Testes criados usando Selenium WebDriver 4.21.0, JUnit 5.10.2 e WebDriverManager 5.8.0.
