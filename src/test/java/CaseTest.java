
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.junit.Assert;

import org.testng.annotations.Test;
import services.*;
import userdata.User;


import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;


public class CaseTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(LogInPage.class);


    @Test

    public void allTest() throws InterruptedException {


        // Ana sayfa giriş kontrol
        HomePage homePage = new HomePage(driver);

        try {
            Assert.assertEquals("GittiGidiyor - Türkiye'nin Öncü Alışveriş Sitesi", driver.getTitle());
        } catch (Exception e) {
            logger.error("Doğru web sitesine giriş yapılamadı");
        }

        logger.info("Sayfaya başarılı bir şekilde giriş yapıldı");



        //Kullanıcı giriş bilgileri ile ana sayfaya giriş
        User user = new User();
        user.setEmail("demo.test.otomasyon.2021@gmail.com");
        user.setPassword("demotest2021");
        LogInPage logInPage =  homePage.clickItemPage();
        logger.info("Login sayfasına girildi.");
        logInPage.logIn(user.getEmail(), user.getPassword());
        logger.info("Kullanıcı girişi başarılı");


        //Ana sayfada search box ın bulunması ve "bilgisayar" kelimesinin aratılması

        ResultPage resultPage = homePage.searchItem();
        resultPage.scrollPage(); // sayfa sonuna gelinmesi ve 2. sayfaya geçilmesi
        assertTrue(driver.getCurrentUrl().contains("2"));


        //Ürünün sepete eklenmesi
        ProductPage productPage = resultPage.goToProductDetail();
        String productPrice = productPage.getProductPrice();//ürün say. fiyat
        productPage.addToBasket();
        logger.info("Ürün sepete eklendi.");

        //Sepete gidiyoruz ve ürünün ilk fiyatı ile sepetteki fiyatını karşılaştırma
        BasketPage basketPage = productPage.clickToBasket();
        logger.info("Sepet sayfasına başarıyla giriş yapıldı");

        Assert.assertEquals(productPrice, basketPage.basketProductPrice());
        logger.info("İki fiyat karşılaştırması yapılmıştır.");

        //Ürün miktarı arttırma
        basketPage.setNumberOfProducts();
        Thread.sleep(2000);
        logger.info("Ürün miktarı arttırıldı");
        assertTrue(basketPage.getTotalProduct().contains("2 Adet"));//sipariş özeti kısmındaki adeti kontrol ediyor.


        //Ürün silme
        basketPage.deleteProduct();
        Thread.sleep(2000);
        String basketMessage = "Sepetinizde ürün bulunmamaktadır.";
        assertEquals(basketMessage, basketPage.isEmpty());
        logger.info("Sepet boş");

    }
}

























