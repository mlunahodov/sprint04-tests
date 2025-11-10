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
 * Teste de cadastro de moto
 */
public class TC03CadastroMoto {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";

    /**
     * Cenário de teste: Cadastro de moto com sucesso
     */
    @Test
    public void cadastroMotoComSucesso() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Dado que o usuário está logado
            LoginAuxiliar.realizarLogin(driver);
            
            // E que ele está na página de cadastro de moto
            driver.get(BASE_URL + "/motorcycle/form");

            // Aguarda os campos ficarem visíveis
            WebElement campoPlaca = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("plate")));
            WebElement campoModelo = driver.findElement(By.id("model"));
            WebElement campoChassi = driver.findElement(By.id("chassis"));
            WebElement campoSituacao = driver.findElement(By.id("situation"));
            WebElement campoDescricao = driver.findElement(By.id("description"));
            WebElement botaoAdicionar = driver.findElement(By.xpath("//button[@type='submit']"));

            // Quando o usuário preenche os campos corretamente
            String placaUnica = "ABC" + System.currentTimeMillis() % 10000;
            campoPlaca.sendKeys(placaUnica);
            
            // Seleciona um modelo
            Select selectModelo = new Select(campoModelo);
            selectModelo.selectByIndex(1); // Seleciona o primeiro modelo disponível
            
            // Preenche o chassi
            String chassiUnico = "CHASSI" + System.currentTimeMillis();
            campoChassi.sendKeys(chassiUnico);
            
            // Seleciona uma situação/problema
            Select selectSituacao = new Select(campoSituacao);
            selectSituacao.selectByIndex(1); // Seleciona o primeiro problema disponível
            
            // Preenche a descrição
            campoDescricao.sendKeys("Moto com problema no motor. Teste automatizado.");

            // E clica em adicionar
            botaoAdicionar.click();

            // Então o sistema deve redirecionar para a lista de motos ou página inicial
            boolean sucesso = false;
            try {
                wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("/motorcycle"),
                    ExpectedConditions.urlToBe(BASE_URL + "/")
                ));
                sucesso = true;
            } catch (Exception e) {
                // Verifica se há mensagem de sucesso na página
                try {
                    WebElement msgSucesso = driver.findElement(
                        By.xpath("//*[contains(text(),'sucesso') or contains(text(),'cadastrada')]")
                    );
                    sucesso = msgSucesso.isDisplayed();
                } catch (Exception ex) {
                    sucesso = false;
                }
            }
            
            Assertions.assertTrue(sucesso, "Moto não foi cadastrada com sucesso.");
            
        } finally {
            driver.quit();
        }
    }
}
