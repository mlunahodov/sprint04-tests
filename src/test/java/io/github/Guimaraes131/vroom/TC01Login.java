package io.github.Guimaraes131.vroom;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Teste de login de usuário
 */
public class TC01Login {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";

    /**
     * Cenário de teste: Login com sucesso
     */
    @Test
    public void loginComSucesso() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Dado que o usuário está na página de login
            driver.get(BASE_URL + "/login");

            // Aguarda os campos ficarem visíveis
            WebElement campoUsername = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
            WebElement campoPassword = driver.findElement(By.id("password"));
            WebElement botaoEntrar = driver.findElement(By.xpath("//button[@type='submit']"));

            // Quando o usuário preenche as credenciais válidas
            // NOTA: Este teste pressupõe que existe um usuário "operador_teste" com senha "123456"
            // Você pode precisar criar este usuário primeiro ou ajustar as credenciais
            campoUsername.sendKeys("operador_teste");
            campoPassword.sendKeys("123456");

            // E clica em entrar
            botaoEntrar.click();

            // Então o usuário deve ser redirecionado para a página inicial
            wait.until(ExpectedConditions.or(
                ExpectedConditions.urlToBe(BASE_URL + "/"),
                ExpectedConditions.urlContains("/motorcycle")
            ));
            
            String urlAtual = driver.getCurrentUrl();
            Assertions.assertFalse(urlAtual.contains("/login"), 
                "Usuário não foi redirecionado após login bem-sucedido.");
            
        } finally {
            driver.quit();
        }
    }
}
