package tests;

import org.easetech.easytest.annotation.DataLoader;
import org.easetech.easytest.annotation.Param;
import org.easetech.easytest.runner.DataDrivenTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import suporte.Generator;
import suporte.Screenshot;
import suporte.Web;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(DataDrivenTestRunner.class)
@DataLoader(filePaths = "InformacoesUsuarioTestData.csv")

public class InformacoesUsuarioTest {
    private WebDriver navegador;

    @Rule
    public TestName test = new TestName();

    @Before
    public void setUp(){
        navegador = Web.createChrome();

        WebElement formularioSignInBox = navegador.findElement(By.id("signinbox"));

        formularioSignInBox.findElement(By.name("login")).sendKeys("julio0001");
        formularioSignInBox.findElement(By.name("password")).sendKeys("123456");

        navegador.findElement(By.linkText("SIGN IN")).click();
        navegador.findElement(By.className("me")).click();

        //Teste que verificar que o usuário que logou é o Julio
        /*String textoNoElementoMe = me.getText();
        assertEquals("Hi, Julio", textoNoElementoMe);*/

        navegador.findElement(By.linkText("MORE DATA ABOUT YOU")).click();
    }

    @Test
    public void testAdicionaUmaInformacaoAdicionalDoUsuario(@Param(name="tipo") String tipo,
                                                            @Param(name="contato") String contato,
                                                            @Param(name="mensagem") String mensagemEsperada){

        navegador.findElement(By.xpath("//button[@data-target=\"addmoredata\"]")).click();

        WebElement popUpAddMoreData = navegador.findElement(By.id("addmoredata"));
        WebElement campoType = popUpAddMoreData.findElement(By.name("type"));
        //Interação com Combobox
        new Select(campoType).selectByVisibleText(tipo);

        popUpAddMoreData.findElement(By.name("contact")).sendKeys(contato);
        popUpAddMoreData.findElement(By.linkText("SAVE")).click();

        WebElement mensagemPop = navegador.findElement(By.id("toast-container"));
        String mensagem = mensagemPop.getText();
        assertEquals(mensagemEsperada,mensagem);

    }

    @Test
    public void removerUmContatoDeUmUsuario(){
        navegador.findElement(By.xpath("//span[text()=\"369963369963\"]/following-sibling::a")).click();

        //confirmação de janela javascript
        navegador.switchTo().alert().accept();

        WebElement mensagemPop = navegador.findElement(By.id("toast-container"));
        String mensagem = mensagemPop.getText();
        assertEquals("Rest in peace, dear phone!",mensagem);

        String screenshotArquivo = "/Ingrid/Nova pasta/" + Generator.dataHoraParaArquivo() + test.getMethodName() + ".png";
        Screenshot.tirar(navegador, screenshotArquivo);

        //Espera explicita
        WebDriverWait aguardar = new WebDriverWait(navegador, 10);
        aguardar.until(ExpectedConditions.stalenessOf(mensagemPop));

        navegador.findElement(By.linkText("Logout")).click();
    }

    @After
    public void tearDown(){
        //navegador.quit();
    }
}


