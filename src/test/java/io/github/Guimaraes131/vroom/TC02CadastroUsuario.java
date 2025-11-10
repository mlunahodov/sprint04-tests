package io.github.Guimaraes131.vroom;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Teste de cadastro de usuário
 */
public class TC02CadastroUsuario {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";

    /**
     * Cenário de teste: Cadastro de usuário com sucesso
     */
    @Test
    public void cadastroUsuarioComSucesso() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Dado que o usuário está na página de registro
            driver.get(BASE_URL + "/register");

            // Aguarda os campos ficarem visíveis
            WebElement campoLogin = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login")));
            WebElement campoSenha = driver.findElement(By.id("password"));
            WebElement campoRole = driver.findElement(By.id("role"));
            WebElement botaoRegistrar = driver.findElement(By.xpath("//button[@type='submit']"));

            // Quando o usuário preenche os campos corretamente
            String loginUnico = "usuario_teste_" + System.currentTimeMillis();
            campoLogin.sendKeys(loginUnico);
            campoSenha.sendKeys("senha123");
            
            // Seleciona o tipo de usuário (OPERATOR)
            Select selectRole = new Select(campoRole);
            selectRole.selectByValue("OPERATOR");

            // E clica em registrar
            botaoRegistrar.click();

            // Então o usuário deve ser redirecionado para a página de login
            wait.until(ExpectedConditions.urlContains("/login"));
            
            String urlAtual = driver.getCurrentUrl();
            Assertions.assertTrue(urlAtual.contains("/login"), 
                "Usuário não foi redirecionado para a página de login após cadastro bem-sucedido.");
            
        } finally {
            driver.quit();
        }
    }
}
