package ua.bentleytek.messenger.service.cash;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.bentleytek.messenger.dao.UsersDAO;
import ua.bentleytek.messenger.entity.User;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineUsersCashTest {

    OnlineUsersCash usersCash;
    @Mock
    UsersDAO usersDAO;
    @Mock
    User user1;
    @Mock
    User user2;

    @Before
    public void init(){
        usersCash = new OnlineUsersCash(usersDAO);

        when(user1.getId()).thenReturn(1);
        when(user1.getName()).thenReturn("user1");
        when(user1.isOnline()).thenReturn(true);

        when(user2.getId()).thenReturn(2);
        when(user2.getName()).thenReturn("user2");
        when(user2.isOnline()).thenReturn(false);
    }


    @Test
    public void testAddContainsAndGet() throws Exception {
        usersCash.add(user1);
        assertTrue(usersCash.contains(1));
        assertEquals(user1, usersCash.get("user1"));
        assertEquals(user1, usersCash.get(1));

        assertFalse(usersCash.contains(2));
    }

    @Test
    public void testClean(){
        usersCash.add(user1);
        usersCash.add(user2);
        usersCash.clean();

        assertTrue(usersCash.contains(1));
        assertFalse(usersCash.contains(2));
        verify(usersDAO, times(1)).save(user2);


    }

}