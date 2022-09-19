package ru.netology.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashBoardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.MoneyTransfer;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {

    @BeforeEach
    void login() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @AfterEach
    void returnCardBalancesToDefault() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        var cardPage = new DashBoardPage();
        var firstCardBalance = cardPage.getCardBalance(DataHelper.getFirstCard().getDataTestId());
        var secondCardBalance = cardPage.getCardBalance(DataHelper.getSecondCard().getDataTestId());
        if (firstCardBalance < secondCardBalance) {
            cardPage.firstCardButtonClick();
            var transferCardValue = new MoneyTransfer();
            transferCardValue.validTransfer(String.valueOf((secondCardBalance - firstCardBalance) / 2),
                    DataHelper.getSecondCard().getNumber());
        } else if (firstCardBalance > secondCardBalance) {
            cardPage.secondCardButtonClick();
            var transferCardValue = new MoneyTransfer();
            transferCardValue.validTransfer(String.valueOf((firstCardBalance - secondCardBalance) / 2),
                    DataHelper.getFirstCard().getNumber());
        }
    }

    @AfterEach
    void closeWebBrowser() {
        closeWebDriver();
    }

    @Test
    void shouldTransferFromFirstToSecondCardHappyPath() {
        var cardPage = new DashBoardPage();
        cardPage.secondCardButtonClick();
        var transferCardValue = new MoneyTransfer();
        transferCardValue.getToField().shouldHave(attribute("value", "**** **** **** 0002"));
        var amount = 3500;
        transferCardValue.validTransfer(String.valueOf(amount), DataHelper.getFirstCard().getNumber());
        var firstCardBalance = cardPage.getCardBalance(DataHelper.getFirstCard().getDataTestId());
        var secondCardBalance = cardPage.getCardBalance(DataHelper.getSecondCard().getDataTestId());
        assertEquals(10000 - amount, firstCardBalance);
        assertEquals(10000 + amount, secondCardBalance);
    }

    @Test
    void shouldTransferFromSecondToFirstCardHappyPath() {
        var cardPage = new DashBoardPage();
        cardPage.firstCardButtonClick();
        var transferCardValue = new MoneyTransfer();
        transferCardValue.getToField().shouldHave(attribute("value", "**** **** **** 0001"));
        var amount = 1500;
        transferCardValue.validTransfer(String.valueOf(amount), DataHelper.getSecondCard().getNumber());
        var firstCardBalance = cardPage.getCardBalance(DataHelper.getFirstCard().getDataTestId());
        var secondCardBalance = cardPage.getCardBalance(DataHelper.getSecondCard().getDataTestId());
        assertEquals(10000 + amount, firstCardBalance);
        assertEquals(10000 - amount, secondCardBalance);
    }


    @Test
    void shouldNotTransferOverCardBalance() {
        var cardPage = new DashBoardPage();
        cardPage.firstCardButtonClick();
        var transferCardValue = new MoneyTransfer();
        transferCardValue.getToField().shouldHave(attribute("value", "**** **** **** 0001"));
        var amount = 135000;
        transferCardValue.invalidTransfer(String.valueOf(amount), DataHelper.getSecondCard().getNumber());
    }

    @Test
    void shouldCancelTransfer() {
        var cardPage = new DashBoardPage();
        cardPage.firstCardButtonClick();
        var transferCardValue = new MoneyTransfer();
        transferCardValue.getToField().shouldHave(attribute("value", "**** **** **** 0001"));
        var amount = 1500;
        transferCardValue.cancelTransfer(String.valueOf(amount), DataHelper.getSecondCard().getNumber());
        var firstCardBalance = cardPage.getCardBalance(DataHelper.getFirstCard().getDataTestId());
        var secondCardBalance = cardPage.getCardBalance(DataHelper.getSecondCard().getDataTestId());
        assertEquals(10000, firstCardBalance);
        assertEquals(10000, secondCardBalance);
    }

}