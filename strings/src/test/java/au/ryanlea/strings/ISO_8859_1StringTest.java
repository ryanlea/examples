package au.ryanlea.strings;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;

public class ISO_8859_1StringTest {

    @Test
    public void basic() {
        final ISO_8859_1String string = new ISO_8859_1String("This is a sample string.");
        assertThat(string.length(), equalTo(24));
        assertThat(string, equalTo("This is a sample string."));
    }

}