import data.DataHelper;

import data.SQLHelper;

import org.junit.jupiter.api.*;
import pages.DashboardPage;
import pages.LoginPage;
import pages.VerificationPage;

import static com.codeborne.selenide.Selenide.*;
import static data.DataHelper.*;
import static data.SQLHelper.cleanDatabase;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginTest {

    DashboardPage dashboardPage;

    @AfterAll
    static void teardown() {
        cleanDatabase();
    }

    @Test
    @DisplayName("Should login registered user")
    void shouldLoginRegisteredUser() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfoFromTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage = new VerificationPage();
        var verificationCode = SQLHelper.getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    @DisplayName("Should be error if unregistered user logs in")
    void shouldBeErrorIfUnregisteredUser() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.generateUser();
        loginPage.invalidLogin();
    }

    @Test
    @DisplayName("Should be error if registered user logs in with different password")
    void shouldBeErrorIfRegisteredUserUsesDifferentPassword() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var differentPassword = loginPage.getUserWithDifferentPassword();
    }

    @Test
    @DisplayName("Should be error if registered user uses verification code not from database")
    void shouldBeErrorIfRegisteredUserUsesVerificationCodeNotFromDatabase() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfoFromTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage = new VerificationPage();
        var verificationCode = DataHelper.getRandomVerificationCode();
        verificationCode.getCode();
        verificationPage.invalidVerify();
    }

    @Test
    @DisplayName("Should block registered user after 3 attempts of use different password")
    void shouldBlockRegisteredUserAfter3TimesDifferentPassword() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var threeTimesLogin = loginPage.threeTimesLoginDifferentPassword();
        var status = SQLHelper.getUserBlockedStatus();
        Assertions.assertEquals("blocked", status);
    }
}
