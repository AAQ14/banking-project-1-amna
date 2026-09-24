package bank;

import bank.cards.DebitCard;
import bank.cards.Mastercard;
import bank.cards.MastercardPlatinum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(1, "Checking", 500.00, 0, 0.0, true, "Mastercard");
    }

    @Test
    void getBalance() {
        assertEquals(500.00, account.getBalance(), 0.001);
    }

    @Test
    void setBalance() {
        account.setBalance(1250.50);
        assertEquals(1250.50, account.getBalance(), 0.001);
    }

    @Test
    void getAccountType() {
        assertEquals("Checking", account.getAccountType());
    }

    @Test
    void setAccountType() {
        account.setAccountType("Savings");
        assertEquals("Savings", account.getAccountType());
    }

    @Test
    void getAccountId() {
        assertEquals(1, account.getAccountId());
    }

    @Test
    void setAccountId() {
        account.setAccountId(999);
        assertEquals(999, account.getAccountId());
    }

    @Test
    void testToString() {
        String text = account.toString();
        assertTrue(text.startsWith("ACCOUNT,A-00001,Checking,500.0,0,0.0,true,"));
    }

    @Test
    void newAccountGetsNextId() throws IOException {
        int before = Account.num.get();
        Account newAccount = new Account("Savings");
        assertEquals(before + 1, newAccount.getAccountId());
        assertEquals(0, newAccount.getBalance(), 0.001);
        assertTrue(newAccount.isActive());
    }

    @Test
    @Disabled("Reads the real accounts.txt and changes the id counter")
    void readLastAccountId() throws IOException {
        Account.readLastAccountId();
        assertTrue(Account.num.get() >= 0);
    }

    @Test
    void findAccountNotFoundReturnsNull() throws IOException {
        // needs accounts.txt to exist in the project folder
        assertNull(Account.findAccount("A-99999"));
    }

    @Test
    void getOverdraftCount() {
        assertEquals(0, account.getOverdraftCount());
    }

    @Test
    void setOverdraftCount() {
        account.setOverdraftCount(3);
        assertEquals(3, account.getOverdraftCount());
    }

    @Test
    void getOverdraftFees() {
        assertEquals(0.00, account.getOverdraftFees(), 0.001);
    }

    @Test
    void setOverdraftFees() {
        account.setOverdraftFees(35.00);
        assertEquals(35.00, account.getOverdraftFees(), 0.001);
    }

    @Test
    void isActive() {
        assertTrue(account.isActive());
    }

    @Test
    void setActive() {
        account.setActive(false);
        assertFalse(account.isActive());
    }

    @Test
    void reactivateAccountWithNegativeBalanceStaysInactive() throws IOException {
        account.setActive(false);
        account.setBalance(-50);
        account.reactivateAccount(null);   // customer isn't used in this case
        assertFalse(account.isActive());
    }

    @Test
    void reactivateAccountAlreadyActiveStaysActive() throws IOException {
        account.reactivateAccount(null);
        assertTrue(account.isActive());
    }

    @Test
    void getCard() {
        assertNotNull(account.getCard());
        assertInstanceOf(Mastercard.class, account.getCard());
    }

    @Test
    void platinumCardNameGivesPlatinumCard() {
        Account platinum = new Account(2, "Checking", 0, 0, 0.0, true, "Platinum");
        assertInstanceOf(MastercardPlatinum.class, platinum.getCard());
    }

    @Test
    void setCard() {
        DebitCard platinum = new MastercardPlatinum();
        account.setCard(platinum);
        assertSame(platinum, account.getCard());
    }

}