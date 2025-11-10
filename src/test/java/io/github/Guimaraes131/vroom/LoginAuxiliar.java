package io.github.Guimaraes131.vroom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Classe auxiliar para realizar login em testes automatizados
 */
public class LoginAuxiliar {
    private static final String BASE_URL = "http://localhost:8080";
    private static final String LOGIN_PADRAO = "operador_teste";
    private static final String SENHA_PADRAO = "123456";

    /**
     * Realiza login com usuário e senha padrão
     */
    public static void realizarLogin(WebDriver driver) {
        realizarLogin(driver, LOGIN_PADRAO, SENHA_PADRAO);
    }

    /**
     * Realiza login com usuário e senha específicos
     */
    public static void realizarLogin(WebDriver driver, String login, String senha) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(BASE_URL + "/login");

        WebElement campoLogin = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        WebElement campoSenha = driver.findElement(By.id("password"));
        WebElement botaoEntrar = driver.findElement(By.xpath("//button[@type='submit']"));

        campoLogin.sendKeys(login);
        campoSenha.sendKeys(senha);
        botaoEntrar.click();

        // Aguarda o redirecionamento após login bem-sucedido
        wait.until(ExpectedConditions.urlContains("/"));
    }
}
