package ua.bentleytek.messenger.service.cash;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.bentleytek.messenger.dao.MessageDAO;
import ua.bentleytek.messenger.entity.Message;
import ua.bentleytek.messenger.entity.User;

import java.sql.Timestamp;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineMessagesCashTest {

    OnlineMessagesCash messagesCash;
    @Mock
    MessageDAO messageDAO;
    @Mock
    OnlineUsersCash usersCash;
    @Mock
    Message message1;
    @Mock
    Message message2;
    @Mock
    User user1;
    @Mock
    User user2;

    @Before
    public void setUp() throws Exception {
        messagesCash = new OnlineMessagesCash(usersCash, messageDAO);

        when(user1.getId()).thenReturn(1);
        when(user1.getName()).thenReturn("user1");
        when(user1.isOnline()).thenReturn(true);

        when(user2.getId()).thenReturn(2);
        when(user2.getName()).thenReturn("user2");
        when(user2.isOnline()).thenReturn(false);

        when(message1.getDate()).thenReturn(new Timestamp(1000));
        when(message1.getFrom()).thenReturn(user1);
        when(message1.getTo()).thenReturn(user2);

        when(message2.getDate()).thenReturn(new Timestamp(2000));
        when(message2.getFrom()).thenReturn(user2);
        when(message2.getTo()).thenReturn(user1);
    }

    @Test
    public void testRegister() throws Exception {
        messagesCash.register(1);
        assertTrue(messagesCash.registered(1));
        assertFalse(messagesCash.registered(2));
    }

    @Test
    public void testPut() throws Exception {
        messagesCash.register(1);
        assertTrue(messagesCash.put(message2));
        assertFalse(messagesCash.put(message1));
    }

    @Test
    public void testGetForUnregistered() throws Exception {
        assertTrue(messagesCash.get(1).isEmpty());
    }

    @Test
    public void testGetOne() throws Exception {
        messagesCash.register(1);
        messagesCash.put(message2);
        assertTrue(messagesCash.get(1).contains(message2));
    }

    @Test
    public void testGetFromUserAndToUser(){
        messagesCash.register(1);
        messagesCash.register(2);
        messagesCash.put(message1);
        messagesCash.put(message2);

        assertEquals(2, messagesCash.get(1,2,2).size());
        assertEquals(1, messagesCash.get(1,2,1).size());
        assertEquals(2, messagesCash.get(2,1,2).size());
        assertEquals(1, messagesCash.get(2,1,1).size());
    }

    @Test
    public void testRightOrder(){
        messagesCash.register(1);
        messagesCash.register(2);
        messagesCash.put(message1);
        messagesCash.put(message2);

        assertTrue(messagesCash.get(1, 2, 1).contains(message2));
        assertFalse(messagesCash.get(1, 2, 1).contains(message1));
    }

    @Test
    public void testHasNew() throws Exception {
        assertFalse(messagesCash.hasNew(1));

        messagesCash.register(1);
        messagesCash.put(message2);

        assertTrue(messagesCash.hasNew(1));
        assertFalse(messagesCash.hasNew(1));
    }

    @Test
    public void testResetNew() throws Exception {
        messagesCash.register(1);
        messagesCash.put(message2);

        messagesCash.resetNew(1);
        assertFalse(messagesCash.hasNew(1));
    }

    @Test
    public void testClean() throws Exception {

    }

    @Test
    public void testMarkRead() throws Exception {

    }
}