package com.henriquemelissopoulos.dribbbletest;

import com.henriquemelissopoulos.dribbbletest.controller.Utils;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by h on 03/11/15.
 */
public class UtilsTest {

    ArrayList<Integer> array;

    @Before
    public void setUp() {
        array = new ArrayList();
    }


    @Test
    public void utils_PageToRequest_EmptyValidation() {
        assertThat(Utils.pageToRequest(array), is(0));
    }

    @Test
    public void utils_PageToRequest_NullValidation() {
        array = null;
        assertThat(Utils.pageToRequest(array), is(0));
    }

    @Test
    public void utils_PageToRequest_ReturnsValid() {
        for (int i = 0; i < 20; i++) {
            array.add(i);
        }
        assertThat(Utils.pageToRequest(array), is(2));
    }

    @Test
    public void utils_PageToRequest_ReturnsInvalid() {
        for (int i = 0; i < 20; i++) {
            array.add(i);
        }
        assertThat(Utils.pageToRequest(array), not(1));
        assertThat(Utils.pageToRequest(array), not(3));
    }

    @Test
    public void utils_IsEmpty_WithValueValidation() {
        String str = "test";
        assertThat(Utils.isEmpty(str), is(false));
        assertThat(Utils.isEmpty(str), not(true));
    }

    @Test
    public void utils_IsEmpty_EmptyValidation() {
        String str = "";
        assertThat(Utils.isEmpty(str), is(true));
        assertThat(Utils.isEmpty(str), not(false));
    }

    @Test
    public void utils_IsEmpty_NullValidation() {
        String str = null;
        assertThat(Utils.isEmpty(str), is(true));
        assertThat(Utils.isEmpty(str), not(false));
    }
}
