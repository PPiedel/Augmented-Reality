import com.example.pawel_piedel.thesis.data.model.AccessToken;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Pawel_Piedel on 30.08.2017.
 */

@Test
public class AccessTokenTest {
    private AccessToken accessToken;

    @Before
    public void setUpAccessToken(){
        accessToken = new AccessToken();
    }

    @Test
    public void firstLetterIsReturnedAsUppercase() throws Exception {
        //with lowercase token type
        accessToken.setTokenType("bearer");

        //first letter is always returned upper cased
        assertEquals("Bearer",accessToken.getTokenType());
    }

    @Test
    public void firstLetterAsUppercase() throws Exception{
        //with ok token type
        accessToken.setTokenType("Bearer");

        //first letter is always returned upper cased
        assertEquals("Bearer",accessToken.getTokenType());
    }

}