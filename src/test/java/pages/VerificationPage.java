package pages;

import com.codeborne.selenide.SelenideElement;
import data.DataHelper;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Validate;


import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement codeField = $("[data-test-id=code] input");
    private SelenideElement verifyButton = $("[data-test-id=action-verify]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public VerificationPage() {
        codeField.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void codeInput(String verificationCode) {
        codeField.setValue(verificationCode);
        verifyButton.click();
    }

    public DashboardPage validVerify(DataHelper.VerificationCode verificationCode) {
        codeInput(verificationCode.getCode());
        return new DashboardPage();
    }

    public VerificationPage invalidVerify() {
        codeInput(String.valueOf(DataHelper.getRandomVerificationCode()));
        codeErrorNotification();
        return new VerificationPage();
    }

    public void errorNotificationVisible() {
        errorNotification.shouldBe(visible, Duration.ofSeconds(20));
    }
    public void codeErrorNotification() {
        errorNotificationVisible();
        errorNotification.shouldHave(text("Ошибка! Неверно указан код! Попробуйте ещё раз."));
    }
}