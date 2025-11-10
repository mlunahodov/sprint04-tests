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
import java.util.List;

/**
 * Teste de edição de moto
 */
public class TC04EditarMoto {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";

    /**
     * Cenário de teste: Editar moto com sucesso
     */
    @Test
    public void editarMotoComSucesso() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Dado que o usuário está logado
            LoginAuxiliar.realizarLogin(driver);
            
            // E que existe pelo menos uma moto cadastrada
            // Navega para a página inicial/lista de motos
            driver.get(BASE_URL + "/");
            
            // Aguarda a página carregar e procura por um link de edição
            // O link de edição pode estar em botões ou links com texto "Editar" ou ícones
            WebElement linkEditar = null;
            try {
                // Tenta encontrar um link de editar (ajuste o seletor conforme sua aplicação)
                linkEditar = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//a[contains(@href, '/motorcycle/') and contains(@href, '/edit')] | " +
                             "//a[contains(text(), 'Editar')] | " +
                             "//button[contains(text(), 'Editar')]")
                ));
            } catch (Exception e) {
                // Se não encontrar link de editar, tenta criar uma moto primeiro
                driver.get(BASE_URL + "/motorcycle/form");
                cadastrarMotoTeste();
                driver.get(BASE_URL + "/");
                linkEditar = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//a[contains(@href, '/motorcycle/') and contains(@href, '/edit')] | " +
                             "//a[contains(text(), 'Editar')] | " +
                             "//button[contains(text(), 'Editar')]")
                ));
            }

            // Quando o usuário clica em editar
            linkEditar.click();
            
            // Aguarda a página de edição carregar
            wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/edit"),
                ExpectedConditions.presenceOfElementLocated(By.id("plate"))
            ));

            // Aguarda os campos ficarem visíveis
            WebElement campoPlaca = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("plate")));
            WebElement campoDescricao = driver.findElement(By.id("description"));
            WebElement botaoAtualizar = driver.findElement(
                By.xpath("//button[@type='submit' and contains(text(), 'Atualizar')]")
            );

            // E modifica alguns campos
            String novaPlaca = "XYZ" + System.currentTimeMillis() % 10000;
            campoPlaca.clear();
            campoPlaca.sendKeys(novaPlaca);
            
            campoDescricao.clear();
            campoDescricao.sendKeys("Descrição atualizada pelo teste automatizado - " + System.currentTimeMillis());

            // E clica em atualizar
            botaoAtualizar.click();

            // Então o sistema deve processar a atualização com sucesso
            boolean sucesso = false;
            try {
                wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("/motorcycle"),
                    ExpectedConditions.urlToBe(BASE_URL + "/")
                ));
                sucesso = true;
            } catch (Exception e) {
                // Verifica se há mensagem de sucesso
                try {
                    WebElement msgSucesso = driver.findElement(
                        By.xpath("//*[contains(text(),'sucesso') or contains(text(),'atualizada')]")
                    );
                    sucesso = msgSucesso.isDisplayed();
                } catch (Exception ex) {
                    sucesso = false;
                }
            }
            
            Assertions.assertTrue(sucesso, "Moto não foi atualizada com sucesso.");
            
        } finally {
            driver.quit();
        }
    }

    /**
     * Método auxiliar para cadastrar uma moto de teste
     */
    private void cadastrarMotoTeste() {
        WebElement campoPlaca = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("plate")));
        WebElement campoModelo = driver.findElement(By.id("model"));
        WebElement campoChassi = driver.findElement(By.id("chassis"));
        WebElement campoSituacao = driver.findElement(By.id("situation"));
        WebElement campoDescricao = driver.findElement(By.id("description"));
        WebElement botaoAdicionar = driver.findElement(By.xpath("//button[@type='submit']"));

        String placaUnica = "TST" + System.currentTimeMillis() % 10000;
        campoPlaca.sendKeys(placaUnica);
        
        Select selectModelo = new Select(campoModelo);
        selectModelo.selectByIndex(1);
        
        String chassiUnico = "CHASSI" + System.currentTimeMillis();
        campoChassi.sendKeys(chassiUnico);
        
        Select selectSituacao = new Select(campoSituacao);
        selectSituacao.selectByIndex(1);
        
        campoDescricao.sendKeys("Moto de teste para edição");
        botaoAdicionar.click();
        
        wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("/motorcycle"),
            ExpectedConditions.urlToBe(BASE_URL + "/")
        ));
    }
}
