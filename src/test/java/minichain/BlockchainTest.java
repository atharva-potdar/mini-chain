package minichain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BlockchainTest {

    @Test
    void startsWithGenesisBlock() {
        Blockchain chain = new Blockchain(2);
        assertEquals(1, chain.getChain().size());
        assertEquals("0", chain.getChain().get(0).getPreviousHash());
    }

    @Test
    void minerGetsReward() {
        Blockchain chain = new Blockchain(2);
        chain.minePending("asha");
        assertEquals(Blockchain.MINING_REWARD, chain.balanceOf("asha"));
    }

    @Test
    void paymentsMoveCoins() {
        Blockchain chain = new Blockchain(2);
        chain.minePending("asha");
        chain.addTransaction(new Transaction("asha", "ravi", 20));
        chain.minePending("meera");
        assertEquals(30, chain.balanceOf("asha"));
        assertEquals(20, chain.balanceOf("ravi"));
        assertEquals(50, chain.balanceOf("meera"));
    }

    @Test
    void cannotSpendMoreThanYouHave() {
        Blockchain chain = new Blockchain(2);
        chain.minePending("asha");
        assertThrows(IllegalArgumentException.class, () -> chain.addTransaction(new Transaction("asha", "ravi", 60)));
    }

    @Test
    void cannotPayYourself() {
        Blockchain chain = new Blockchain(2);
        chain.minePending("asha");
        assertThrows(IllegalArgumentException.class, () -> chain.addTransaction(new Transaction("asha", "asha", 5)));
    }

    @Test
    void blocksAreMinedAndLinked() {
        Blockchain chain = new Blockchain(3);
        chain.minePending("asha");
        chain.minePending("ravi");
        for (int i = 1; i < chain.getChain().size(); i++) {
            Block block = chain.getChain().get(i);
            assertTrue(block.getHash().startsWith("000"));
            assertEquals(chain.getChain().get(i - 1).getHash(), block.getPreviousHash());
        }
        assertTrue(chain.isValid());
    }

    @Test
    void brokenLinkIsDetected() {
        Blockchain chain = new Blockchain(2);
        chain.minePending("asha");
        chain.minePending("ravi");
        chain.getChain().get(2).tamperPreviousHash("f".repeat(64));
        assertFalse(chain.isValid());
    }
}
