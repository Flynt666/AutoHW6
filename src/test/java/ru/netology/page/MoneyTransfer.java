package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MoneyTransfer {

    private SelenideElement heading = $("h1").shouldHave(text("Пополнение карты"));
    private SelenideElement amountField = $("[data-test-id='amount'] input");
    private SelenideElement whereFromField = $("[data-test-id='from'] input");
    private SelenideElement whereToField = $("[data-test-id='to'] input");
    private SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private SelenideElement cancelButton = $("[data-test-id='action-cancel']");
    private SelenideElement errorNotification = $("[data-test-id='error-notification']");

    public MoneyTransfer() {
        heading.shouldBe(visible);
    }

    public DashBoardPage validTransfer(String amount, String from) {
        amountField.val(amount);
        whereFromField.val(from);
        transferButton.click();
        return new DashBoardPage();
    }

    public MoneyTransfer invalidTransfer(String amount, String from) {
        amountField.val(amount);
        whereFromField.val(from);
        transferButton.click();
        errorNotification.shouldHave(text("Ошибка"));
        return new MoneyTransfer();
    }

    public DashBoardPage cancelTransfer(String amount, String from) {
        amountField.val(amount);
        whereFromField.val(from);
        cancelButton.click();
        return new DashBoardPage();
    }

    public SelenideElement getToField() {
        return whereToField;
    }
}